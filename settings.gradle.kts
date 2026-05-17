pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        flatDir {
            dirs("libs")
        }
        maven { url = uri("https://repo.pgm.fyi/snapshots") }
        maven { url = uri("https://hub.spigotmc.org/nexus/content/groups/public/") }
        maven { url = uri("https://maven.enginehub.org/repo/") }
        maven { url = uri("https://repo.maven.apache.org/maven2") }
        maven { url = uri("https://ci.frostcast.net/plugin/repository/everything") }
        maven { url = uri("https://repo.spongepowered.org/maven") }
        maven { url = uri("https://repo.inventivetalent.org/content/groups/public/") }
        maven { url = uri("https://store.ttyh.ru/libraries/") }
        maven { url = uri("https://repo.dmulloy2.net/nexus/repository/public/") }
        maven { url = uri("https://maven.elmakers.com/repository/") }
        maven { url = uri("https://ci.ender.zone/plugin/repository/everything/") }
        maven { url = uri("https://repo.papermc.io/repository/maven-public/") }
        maven { url = uri("https://jitpack.io") }
        maven { url = uri("https://repo.codemc.org/repository/maven-public") }
        maven { url = uri("https://repo.minebench.de") }
    }
}

rootProject.name = "FastAsyncWorldEdit"

include("core", "bukkit")
