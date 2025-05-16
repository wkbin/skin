pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("${rootProject.projectDir}/maven-repo")
        }
    }
}

rootProject.name = "Skin"
include(":app")
include(":skin")
include(":skins:skin_blue")
include(":skins:skin_red")
include(":skins:skin_green")
