import org.gradle.api.tasks.javadoc.Javadoc

plugins {
    `java-library`
    eclipse
    idea
}

group = rootProject.group
version = rootProject.version

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.add("-parameters")
}

rootProject.tasks.named<Javadoc>("aggregatedJavadocs").configure {
    val javadocs = tasks.named<Javadoc>("javadoc")
    source(javadocs.map { it.source })
    classpath += files(javadocs.map { it.classpath })
}
