plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
    id("com.google.dagger.hilt.android")
    id("org.jlleitschuh.gradle.ktlint") version "12.1.1"
    id("io.gitlab.arturbosch.detekt")
}

detekt {
    // Detekt 기본 설정 위에 내 설정을 덮어씀
    buildUponDefaultConfig = true
    allRules = false        // 불필요한 실험/구식 룰은 끔
    parallel = true

    // 커스텀 룰 구성 파일(바로 아래 3단계에서 생성)
    config = files("$rootDir/detekt.yml")

    source = files(
        "src/main/java",
        "src/test/java",
        "src/androidTest/java"
    )

    // 리포트 (원하는 형식만 true)
    reports {
        html.required.set(true)
        xml.required.set(false)
        txt.required.set(false)
        sarif.required.set(false)
        md.required.set(false)
    }
}

// ⚠️ ktlint와 중복되는 formatting 룰을 detekt로까지 검사하려면 아래를 추가.
// (우리는 포맷은 ktlint로만 하기로 했으니 "굳이" 필요 없음)
// dependencies {
//     detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.6")
// }

android {
    namespace = "com.bettor.medlinkduo"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.bettor.medlinkduo"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        ktlint {
            version.set("1.2.1")
            android.set(true)
            filter {
                exclude("**/generated/**", "**/build/**")
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    lint {
        checkOnly += setOf("UnusedDeclaration", "UnusedResources")
        abortOnError = false    // 리포트만 보고 싶으면
    }
}

dependencies {

    // AndroidX 기본
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")

    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")

    // Compose (BOM은 libs 사용 그대로)
    implementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(platform(libs.androidx.compose.bom))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")

    // Navigation / Hilt (호환 버전)
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    implementation("com.google.dagger:hilt-android:2.50")
    kapt("com.google.dagger:hilt-android-compiler:2.50")

    // Room (2.6.1은 AGP 8.3/Kotlin 1.9와 호환)
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")

    // WorkManager
    implementation("androidx.work:work-runtime-ktx:2.9.0")

    // Coroutines (Kotlin 1.9 라인 호환)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

    // DataStore / Startup / Timber
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    implementation("androidx.startup:startup-runtime:1.2.0")
    implementation("com.jakewharton.timber:timber:5.0.1")

    // 테스트
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
