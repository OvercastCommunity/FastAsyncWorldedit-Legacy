import org.gradle.api.tasks.javadoc.Javadoc
import org.gradle.external.javadoc.StandardJavadocDocletOptions

plugins {
    base
}

tasks.named<Delete>("clean") {
    delete("target")
}

group = "com.boydti.fawe"

version = if (providers.gradleProperty("lzNoVersion").isPresent) {
    "unknown"
} else {
    val commitDate = latestCommitDate()
    val commitHash = latestCommitHash()
    val commitCount = firstParentCommitCount()
    if ("unknown" in setOf(commitDate, commitHash, commitCount)) {
        "unknown"
    } else {
        "$commitDate-$commitHash-$commitCount"
    }
}
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
