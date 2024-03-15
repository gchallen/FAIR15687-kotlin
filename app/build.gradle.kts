@file:Suppress("SpellCheckingInspection", "MagicNumber", "GradleDependency", "UnstableApiUsage")

import java.util.UUID

/*
 * This file configures the build system that creates your Android app.
 * The syntax is Kotlin, but it may use idioms unfamiliar to you.
 * You do not need to understand the contents of this file, nor should you modify it.
 * ALL CHANGES TO THIS FILE WILL BE OVERWRITTEN DURING OFFICIAL GRADING.
 */

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.cs124.gradlegrader") version "2024.3.0"
    id("io.gitlab.arturbosch.detekt") version "1.23.5"
}
android {
    namespace = "edu.illinois.cs.cs124.ay2023.mp"
    compileSdk = 34
    buildToolsVersion = "34.0.0"
    defaultConfig {
        applicationId = "edu.illinois.cs.cs124.ay2023.courseable"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }
    }
    sourceSets {
        map { it.java.srcDir("src/${it.name}/kotlin") }
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}
/*
 * Do not add dependencies here, since they will be overwritten during official grading.
 * If you have a package that you think would be broadly useful for completing the MP, please start a discussion
 * on the forum.
 */
dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:mockwebserver:4.12.0")
    implementation("com.android.volley:volley:1.2.1")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.17.0")

    testImplementation("org.cs124.gradlegrader:lib:2024.3.0")
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.robolectric:robolectric:4.11.1")
    testImplementation("androidx.test:core:1.5.0")
    testImplementation("androidx.test.ext:junit:1.1.5")
    testImplementation("androidx.test.ext:truth:1.5.0")
    testImplementation("androidx.test.espresso:espresso-core:3.5.1")
    testImplementation("androidx.test.espresso:espresso-intents:3.5.1")
    testImplementation("androidx.test.espresso:espresso-contrib:3.5.1")

    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.5")
}
detekt {
    toolVersion = "1.23.5"
    config.from(files("config/detekt/detekt.yml"))
    buildUponDefaultConfig = true
}
gradlegrader {
    assignment = "AY2023.MP"
    points {
        total = 100
    }
    checkpoint {
        yamlFile = rootProject.file("grade.yaml")
        configureTests { checkpoint, test ->
            require(checkpoint in setOf("0", "1", "2", "3")) { "Cannot grade unknown checkpoint MP$checkpoint" }
            test.setTestNameIncludePatterns(listOf("MP${checkpoint}Test"))
            test.filter.isFailOnNoMatchingTests = true
        }
    }
    detekt {
        points = 10
    }
    earlyDeadline {
        points = { checkpoint ->
            when (checkpoint) {
                in setOf("2", "3") -> 10
                else -> 0
            }
        }
        noteForPoints = { checkpoint, points ->
            "Checkpoint $checkpoint has an early deadline, so the maximum local score is ${100 - points}/100.\n" +
                "$points points will be provided during official grading if you submit code " +
                "that meets the early deadline threshold before the early deadline."
        }
    }
    forceClean = false
    identification {
        txtFile = rootProject.file("ID.txt")
        @Suppress("SwallowedException")
        validate =
            Spec {
                val uuid = it.trim()
                check(uuid.length == 36 && UUID.fromString(uuid).toString() == uuid) {
                    "Invalid UUID string: $uuid"
                }
                true
            }
    }
    reporting {
        post {
            endpoint = "https://cloud.cs124.org/gradlegrader"
        }
        printPretty {
            title = "Grade Summary"
        }
    }
    vcs {
        git = true
        requireCommit = true
    }
}
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}
