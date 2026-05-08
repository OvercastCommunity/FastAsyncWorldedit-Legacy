import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.ValueSource
import org.gradle.api.provider.ValueSourceParameters
import org.gradle.api.tasks.javadoc.Javadoc
import org.gradle.external.javadoc.StandardJavadocDocletOptions
import org.gradle.process.ExecOperations
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

plugins {
    base
}

abstract class GitVersionValueSource : ValueSource<String, GitVersionValueSource.Parameters> {
    interface Parameters : ValueSourceParameters {
        val projectDir: DirectoryProperty
        val noVersion: Property<Boolean>
    }

    @get:Inject
    abstract val execOperations: ExecOperations

    override fun obtain(): String {
        if (parameters.noVersion.getOrElse(false)) {
            return "unknown"
        }

        return try {
            val dir = parameters.projectDir.get().asFile
            val head = git(dir, "log", "-1", "--format=%ct%x00%h%x00%P")
            if (head.isEmpty()) {
                return "unknown"
            }

            val headParts = head.split("\u0000")
            val date = SimpleDateFormat("yy.MM.dd").format(Date(headParts[0].toLong() * 1000L))
            val revision = "-${headParts[1]}"
            var parents = if (headParts.size > 2 && headParts[2].isNotEmpty()) {
                headParts[2].split(" ")
            } else {
                emptyList()
            }

            var index = -67 // Offset to match CI
            var major = 0
            var minor = 0
            var patch = 0

            while (parents.isNotEmpty()) {
                var majorCount = 0
                var minorCount = 0
                var patchCount = if (minor == 0 && major == 0) 1 else 0

                val commit = git(dir, "show", "-s", "--format=%B%x00%P", parents[0])
                val commitParts = commit.split("\u0000")
                for (line in commitParts[0].split("\n")) {
                    when (line.replace("- ", "").split(" ")[0].lowercase()) {
                        "minor", "added", "add", "change", "changed", "changes" -> {
                            if (major == 0) {
                                minorCount = 1
                                patchCount = 0
                            }
                        }
                        "refactor", "remove", "major" -> {
                            patchCount = 0
                            minorCount = 0
                            majorCount = 1
                        }
                    }
                }

                major += majorCount
                minor += minorCount
                patch += patchCount
                parents = if (commitParts.size > 1 && commitParts[1].isNotEmpty()) {
                    commitParts[1].split(" ")
                } else {
                    emptyList()
                }
                index++
            }

            "$date$revision-$index-$major.$minor.$patch"
        } catch (_: Throwable) {
            "unknown"
        }
    }

    private fun git(dir: File, vararg args: String): String {
        val output = ByteArrayOutputStream()
        val error = ByteArrayOutputStream()
        execOperations.exec {
            workingDir = dir
            commandLine = listOf("git") + args.toList()
            standardOutput = output
            errorOutput = error
        }
        return output.toString("UTF-8").trim()
    }
}

tasks.named<Delete>("clean") {
    delete("target")
}

group = "com.boydti.fawe"

version = providers.of(GitVersionValueSource::class) {
    parameters.projectDir.set(layout.projectDirectory)
    parameters.noVersion.set(providers.gradleProperty("lzNoVersion").map { true }.orElse(false))
}.get()
description = "FastAsyncWorldEdit"

tasks.register<Javadoc>("aggregatedJavadocs") {
    description = "Generate javadocs from all child projects as if it was a single project"
    group = "Documentation"
    destinationDir = file("./docs/javadoc")
    title = "${project.name} $version API"

    (options as StandardJavadocDocletOptions).apply {
        author(true)
        links(
            "https://docs.spring.io/spring/docs/4.3.x/javadoc-api/",
            "https://docs.oracle.com/javase/8/docs/api/",
            "https://docs.spring.io/spring-ws/docs/2.3.0.RELEASE/api/",
            "https://docs.spring.io/spring-security/site/docs/4.0.4.RELEASE/apidocs/"
        )
        addStringOption("Xdoclint:none", "-quiet")
    }

    doFirst {
        delete("./docs")
    }
}
