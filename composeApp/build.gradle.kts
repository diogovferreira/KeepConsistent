import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.sqldelight)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

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
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.koin.android)
            implementation(libs.koin.androidx.compose)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.appcompat)
            implementation(libs.sqldelight.driver.android)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.ktor.client.android)
            implementation(libs.androidx.splashscreen)
        }

        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)


            implementation(libs.compose.ui)
            implementation(libs.compose.runtime)

            implementation(libs.kotlinx.coroutines.core)

            //COIL FOR IMAGE LOADING
            implementation(libs.coil.compose)
            implementation(libs.coil.svg)
            implementation(libs.coil.network)

            //DEPENDENCY INJECTION
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.koin.core.viewmodel)
            implementation(libs.koin.voyager)


            //LOGS CROSS PLATFORM
            implementation(libs.napier.logs)

            //MULTIPLATFORM SETTINGS
            implementation(libs.settings.multiplatform)

            //VOYAGER NAVIGATION
            implementation(libs.voyager.navigator)
            implementation(libs.voyager.bottom.sheet.navigator)


            //DATE TIME KMP
            implementation(libs.date.time.kmp)

            //SQL DELIGHT
            implementation(libs.sqldelight.coroutines)

            //Icons
            implementation(compose.materialIconsExtended)

            //HTTP CALLS - KTOR
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.client.logging)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.1")
            implementation("app.cash.turbine:turbine:1.2.0")
            implementation("io.ktor:ktor-client-mock:2.3.12")

        }

        iosMain.dependencies{
            implementation(libs.ktor.client.darwin)

            implementation(libs.sqldelight.driver.native)
        }
    }
}

sqldelight {
    databases {
        create("TcgDatabase") {
            packageName.set("com.dfcoding.optcg.database")  // matches your folder
        }
    }
}


android {
    namespace = "org.dfcoding.modelrepocompose"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "org.dfcoding.modelrepocompose"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
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
}

dependencies {
    debugImplementation(compose.uiTooling)
}

