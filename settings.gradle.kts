pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
        maven(url = "https://kotlin.bintray.com/kotlinx/")
    }
    
}
rootProject.name = "OMCDecoder"


include(":android")
include(":desktop")
include(":common")

