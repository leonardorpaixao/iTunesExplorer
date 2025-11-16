plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "11"
            }
        }
    }
    jvm()
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(libs.coil.compose)
        }

        val androidMain by getting {
            dependencies {
                implementation(libs.compose.material.icons.extended)
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(libs.compose.material.icons.extended)
            }
        }

        val iosMain by creating {
            dependencies {
                implementation(libs.compose.material.icons.extended)
            }
        }
    }
}

android {
    namespace = "com.itunesexplorer.design"
    compileSdk = 35
    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}
