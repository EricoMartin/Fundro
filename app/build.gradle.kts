import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
    id("kotlin-parcelize")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("com.google.devtools.ksp") version "1.9.22-1.0.17"
}

android {
    namespace = "com.basebox.fundro"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.basebox.fundro"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables {
            useSupportLibrary = true
        }
        // Backend API URL
//        buildConfigField("String", "BASE_URL", "\"http://10.0.2.2:8088/api/\"")  // Android Emulator localhost
        // For physical device, use your computer's IP:
         buildConfigField("String", "BASE_URL", "\"http://192.168.1.4:8088/api/\"")

        buildConfigField("String", "PAYSTACK_PUBLIC_KEY", "\"pk_test_4965f30d0188da36b2ee4b1fb569a129b2c03082\"")
    }

    buildTypes {
        debug {
            isDebuggable = true
            isMinifyEnabled = false
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            // For production
            buildConfigField("String", "BASE_URL", "\"https://api.fundro.com/api/\"")

            signingConfig = signingConfigs.getByName("debug")  // Change to release signing
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17

        // Enable desugaring for Java 8+ APIs on older Android versions
        isCoreLibraryDesugaringEnabled = true
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/DEPENDENCIES"
            excludes += "META-INF/LICENSE"
            excludes += "META-INF/LICENSE.txt"
            excludes += "META-INF/license.txt"
            excludes += "META-INF/NOTICE"
            excludes += "META-INF/NOTICE.txt"
            excludes += "META-INF/notice.txt"
            excludes += "META-INF/*.kotlin_module"
        }
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)

        // Enable experimental APIs
        freeCompilerArgs.addAll(
            "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
            "-opt-in=androidx.compose.foundation.ExperimentalFoundationApi",
            "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi"
        )
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // Java 8+ API desugaring (for LocalDateTime, etc.)
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")

    // ========================================
    // JETPACK COMPOSE (UI)
    // ========================================
    val composeBom = platform("androidx.compose:compose-bom:2024.02.00")
    implementation(composeBom)
    androidTestImplementation(composeBom)

    // Material 3 Design
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material3:material3-window-size-class")

    // Compose UI
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // Material Icons Extended (financial icons)
    implementation("androidx.compose.material:material-icons-extended")

    // Compose Foundation (LazyColumn, animations, etc.)
    implementation("androidx.compose.foundation:foundation")

    // Compose Runtime
    implementation("androidx.compose.runtime:runtime")
    implementation("androidx.compose.runtime:runtime-livedata")

    // ========================================
    // NAVIGATION
    // ========================================
    implementation("androidx.navigation:navigation-compose:2.7.6")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")

    // ========================================
    // DEPENDENCY INJECTION (HILT)
    // ========================================
    implementation("com.google.dagger:hilt-android:2.51.1")
    ksp("com.google.dagger:hilt-android-compiler:2.51.1")
    ksp("androidx.hilt:hilt-compiler:1.1.0")

    // ========================================
    // NETWORKING (RETROFIT + OKHTTP)
    // ========================================
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // Moshi (JSON parsing - better than Gson for Kotlin)
    implementation("com.squareup.moshi:moshi:1.15.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.15.0")
    ksp("com.squareup.moshi:moshi-kotlin-codegen:1.15.0")

    // Google Accompanist for FlowRow
    implementation("com.google.accompanist:accompanist-flowlayout:0.34.0")

    // ========================================
    // LOCAL STORAGE (ROOM DATABASE)
    // ========================================
    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")
    implementation("androidx.room:room-paging:${roomVersion}")

    // ========================================
    // DATASTORE (SECURE PREFERENCES)
    // ========================================
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // Encrypted SharedPreferences (for JWT tokens)
    implementation("androidx.security:security-crypto:1.1.0-alpha06")

    // ========================================
    // COROUTINES (ASYNC)
    // ========================================
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")

    // ========================================
    // LIFECYCLE (VIEWMODEL, LIVEDATA, FLOW)
    // ========================================
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")

    // ========================================
    // IMAGE LOADING (COIL)
    // ========================================
    implementation("io.coil-kt:coil-compose:2.5.0")

    // ========================================
    // ACCOMPANIST (COMPOSE UTILITIES)
    // ========================================
    implementation("com.google.accompanist:accompanist-permissions:0.34.0")
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.34.0")
    implementation("com.google.accompanist:accompanist-swiperefresh:0.34.0")

    // ========================================
    // BIOMETRIC AUTHENTICATION
    // ========================================
    implementation("androidx.biometric:biometric:1.1.0")

    // ========================================
    // FIREBASE (PUSH NOTIFICATIONS)
    // ========================================
    implementation(platform("com.google.firebase:firebase-bom:32.7.1"))
    implementation("com.google.firebase:firebase-messaging-ktx")
    implementation("com.google.firebase:firebase-analytics-ktx")

    // ========================================
    // PAYMENT INTEGRATIONS
    // ========================================
    // Paystack Android SDK
    implementation("co.paystack.android:paystack:3.1.3")

    // WebView for Paystack checkout
    implementation("androidx.webkit:webkit:1.9.0")

    // ========================================
    // QR CODE GENERATION/SCANNING
    // ========================================
    implementation("com.google.zxing:core:3.5.2")
    implementation("com.journeyapps:zxing-android-embedded:4.3.0")

    // ========================================
    // DATE/TIME HANDLING
    // ========================================
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.5.0")

    // ========================================
    // LOGGING (TIMBER)
    // ========================================
    implementation("com.jakewharton.timber:timber:5.0.1")

    // ========================================
    // LEAK DETECTION (DEBUG ONLY)
    // ========================================
    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.13")

    // ========================================
    // SPLASH SCREEN
    // ========================================
    implementation("androidx.core:core-splashscreen:1.0.1")

    // ========================================
    // LOTTIE ANIMATIONS
    // ========================================
    implementation("com.airbnb.android:lottie-compose:6.3.0")

    // ========================================
    // FINANCIAL/CURRENCY FORMATTING
    // ========================================
    // BigDecimal safe math
    implementation("com.ionspin.kotlin:bignum:0.3.9")

    // ========================================
    // TESTING
    // ========================================
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.mockito:mockito-core:5.8.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.2.1")
    testImplementation("app.cash.turbine:turbine:1.0.0")
    testImplementation("com.google.truth:truth:1.4.0")

    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.50")
    kspAndroidTest("com.google.dagger:hilt-android-compiler:2.50")
}