import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("fawe.java-conventions")
    id("com.gradleup.shadow")
}

dependencies {
    implementation(project(":core"))
    compileOnly("com.sk89q:worldguard:6.0.0-SNAPSHOT")
    compileOnly("com.destroystokyo.paper:paper-api:1.12-R0.1-SNAPSHOT") {
        exclude(group = "net.md-5")
    }
    compileOnly("org.bukkit.craftbukkit:Craftbukkit_1_12:1.12.1")
    compileOnly("org.bukkit.craftbukkit:Craftbukkit_1_11:1.11")
    compileOnly("org.bukkit.craftbukkit:Craftbukkit_1_10:1.10")
    compileOnly("org.bukkit.craftbukkit:Craftbukkit_1_9:1.9.4")
    compileOnly("app.ashcon:sportpaper:1.8.8-R0.1-SNAPSHOT")
    compileOnly("org.bukkit.craftbukkit:Craftbukkit_1_7:1.7.10")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
    compileOnly("com.plotsquared:PlotSquared-Bukkit:3.823")
    compileOnly("org.primesoft:BlocksHub:2.0")
    compileOnly("com.sk89q.worldedit:worldedit-bukkit:6.1.5")
    compileOnly("com.sk89q.worldedit:worldedit-core:6.1.4-SNAPSHOT")
    compileOnly("net.dmulloy2:ProtocolLib:5.1.0")
    compileOnly("org.inventivetalent:mapmanager:1.7.2-SNAPSHOT") { isTransitive = false }
}

tasks.processResources {
    from("src/main/resources") {
        include("plugin.yml")
        expand(
            mapOf(
                "name" to project.parent?.name,
                "version" to project.parent?.version
            )
        )
    }
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}

val shadowJar = tasks.named<ShadowJar>("shadowJar") {
    archiveFileName = "${rootProject.name}-${project.name}-${rootProject.version}.jar"
    archiveClassifier.set("")
    destinationDirectory = rootProject.projectDir.resolve("target")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    dependencies {
        include(dependency("com.github.luben:zstd-jni:1.4.8-1"))
        include(dependency("at.yawk.lz4:lz4-java:1.11.0"))
        include(dependency("org.yaml:snakeyaml:1.33"))
        include(dependency("com.google.code.gson:gson:2.8.9"))
        include(dependency(":core"))
    }

    relocate("com.google.gson", "com.sk89q.worldedit.internal.gson")
}

tasks.named("build") {
    dependsOn(shadowJar)
}
