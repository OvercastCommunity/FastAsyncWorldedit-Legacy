plugins {
    id("fawe.java-conventions")
}

dependencies {
    testImplementation("junit:junit:4.13.1")
    implementation("com.google.code.gson:gson:2.8.9")
    implementation("org.yaml:snakeyaml:1.33")
    compileOnly("net.fabiozumbi12:redprotect:1.9.6")
    compileOnly("com.plotsquared:PlotSquared-Bukkit:3.823")
    compileOnly("org.primesoft:BlocksHub:2.0")
    implementation("com.github.luben:zstd-jni:1.4.8-1") { isTransitive = false }
    implementation("at.yawk.lz4:lz4-java:1.11.0") { isTransitive = false }
    // Your maintained legacy Paper fork of choice should use fastutil already
    compileOnly("it.unimi.dsi:fastutil:8.5.15")
    api("com.sk89q.worldedit:worldedit-core:6.1.4-SNAPSHOT") {
        exclude(module = "bukkit-classloader-check")
    }
    api("org.jspecify:jspecify:1.0.0")
}

tasks.processResources {
    from("src/main/resources") {
        include("fawe.properties")
        expand(
            mapOf(
                "version" to "${project.parent?.version}",
                "name" to project.parent?.name
            )
        )
    }
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}
