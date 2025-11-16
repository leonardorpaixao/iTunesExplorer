import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.ksp)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    jvm("desktop")
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    
    sourceSets {
        val desktopMain by getting
        
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.coil.network.okhttp)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)

            implementation(projects.core.network)
            implementation(projects.core.error)
            implementation(projects.core.common)
            implementation(projects.core.settings)
            implementation(projects.designSystem)
            implementation(projects.features.home)
            implementation(projects.features.catalog)
            implementation(projects.features.preferences)

            implementation(libs.voyager.navigator)
            implementation(libs.voyager.screenModel)
            implementation(libs.voyager.kodein)
            implementation(libs.voyager.transitions)
            implementation(libs.voyager.bottom.sheet.navigator)
            implementation(libs.kodein.di)
            implementation(libs.kodein.di.framework.compose)
            implementation(libs.coil.compose)
            implementation(libs.lyricist)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.coroutines.swing)
        }
        iosMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
        }
    }
}

android {
    namespace = "com.itunesexplorer"
    compileSdk = 35

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = "com.itunesexplorer"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
    dependencies {
        debugImplementation(compose.uiTooling)
    }
}

compose.desktop {
    application {
        mainClass = "com.itunesexplorer.MainKt"
    }
}

dependencies {
    add("kspCommonMainMetadata", libs.lyricist.processor)
}

tasks.withType<org.jetbrains.kotlin.gradle.dsl.KotlinCompile<*>>().all {
    if(name != "kspCommonMainKotlinMetadata") {
        dependsOn("kspCommonMainKotlinMetadata")
    }
}

kotlin.sourceSets.commonMain {
    kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
}

// Custom tasks to run each target
tasks.register("runDesktop") {
    group = "application"
    description = "Run the Desktop (JVM) application"
    dependsOn("run")
}

tasks.register("runAndroid") {
    group = "application"
    description = "Install and run the Android application on connected device/emulator"
    dependsOn("installDebug")
    doLast {
        exec {
            commandLine("adb", "shell", "am", "start", "-n", "com.itunesexplorer/.MainActivity")
        }
    }
}

tasks.register("buildIosSimulator") {
    group = "build"
    description = "Build iOS framework for simulator"
    dependsOn("linkDebugFrameworkIosSimulatorArm64")
}

tasks.register("buildIosDevice") {
    group = "build"
    description = "Build iOS framework for device"
    dependsOn("linkDebugFrameworkIosArm64")
}
