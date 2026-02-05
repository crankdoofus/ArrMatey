import org.gradle.accessors.dm.LibrariesForLibs
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

buildscript {
    dependencies {
        classpath(libs.resources.generator)
    }
}

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.skie)
    alias(libs.plugins.ksp)
    alias(libs.plugins.androidx.room)
    kotlin("plugin.serialization") version libs.versions.kotlin
}

apply(plugin = "dev.icerock.mobile.multiplatform-resources")

//tasks.register<Exec>("generateLocalization") {
//    group = "build setup"
//    description = "Generation localization files"
//
//    commandLine("/Users/owen.lejeune/.nvm/versions/node/v22.3.0/bin/node", "$rootDir/strings/generate-strings.js")
//}

skie {
    features {
        enableSwiftUIObservingPreview = true
    }
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true
            export(libs.moko.resources)
        }
    }

//    targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget>().configureEach {
//        binaries.all {
//            linkTaskProvider.get()
//                .dependsOn(tasks.named("generateLocalization"))
//        }
//    }

    sourceSets {
        all {
            languageSettings.optIn("kotlin.time.ExperimentalTime")
            languageSettings.optIn("kotlinx.cinterop.ExperimentalForeignApi")
            languageSettings.optIn("kotlin.experimental.ExperimentalObjCName")
        }

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.koin.android)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
        commonMain.dependencies {
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(compose.components.resources)
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.ui)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.datetime)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.koin.compose.viewmodel.navigation)
            implementation(libs.androidx.room.runtime)
            implementation(libs.androidx.sqlite.bundled)
            implementation(libs.coil.compose)
            implementation(libs.coil.network)
            implementation(libs.androidx.datastore)
            implementation(libs.androidx.datastore.preferences)
            implementation(libs.cloudy)

            api(libs.moko.resources)
            api(libs.moko.resources.compose)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "com.dnfapps.arrmatey.shared"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    buildFeatures {
        buildConfig = true
    }

    sourceSets {
        named("main") {
            // MOKO handles the generated res path automatically usually,
            // but if you manually add it, ensure it points to the moko-resources folder
            res.srcDirs("build/generated/moko/androidMain/res")
        }
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
    add("kspAndroid", libs.androidx.room.compiler)
    add("kspIosSimulatorArm64", libs.androidx.room.compiler)
    add("kspIosArm64", libs.androidx.room.compiler)
}

room {
    schemaDirectory("$projectDir/schemas")
}

//tasks.matching { it.name.startsWith("compile") && it.name.contains("Kotlin") }.configureEach {
//    dependsOn("generateMRcommonMain")
//}

//compose.resources {
//    packageOfResClass = "com.dnfapps.arrmatey.resources"
//    nameOfResClass = "MR"
//}
//compose.resources {
//    packageOfResClass = "com.dnfapps.arrmatey.resources"
//    nameOfResClass = "SharedRes"
//}

//afterEvaluate {
//    tasks.matching { (it.name.startsWith("compile") && it.name.contains("Kotlin")) || it.name.contains("build") }
//        .configureEach {
//            dependsOn(tasks.named("generateLocalization"))
//        }
//}
