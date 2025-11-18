rootProject.name = "iTunesExplorer"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

include(":composeApp")

// Core modules
include(":core:error")
include(":core:common")
include(":core:settings")
include(":core:currency")
include(":core:logger")
include(":core:network")

// Design System
include(":design-system")

// Features
include(":features:home")
include(":features:catalog")
include(":features:preferences")
