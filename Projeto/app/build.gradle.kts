plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)

    // Firebase
    id("com.google.gms.google-services")



}

android {
    namespace = "engsoft.matfit"
    compileSdk = 34

    defaultConfig {
        applicationId = "engsoft.matfit"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
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
        viewBinding = true
    }
    testOptions {
        unitTests.isReturnDefaultValues = true
    }
}

dependencies {

    // Apache POI -> arquivos excel
    implementation ("org.apache.poi:poi-ooxml:5.2.3")
    //Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.9.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth")
   // implementation("com.google.firebase:firebase-firestore")

    // Retrofit
    implementation(libs.retrofit)
    // Gson
    implementation(libs.converter.gson)
    // OKHTTP3
    implementation("com.squareup.okhttp3:okhttp:4.9.3")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.activity)

    // Mockito
    testImplementation ("org.mockito:mockito-core:5.16.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:4.1.0")
    // Truth
    testImplementation ("com.google.truth:truth:1.4.4")
    // runTest
    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    // LiveDataTest
    testImplementation ("androidx.arch.core:core-testing:2.2.0")

    testImplementation(libs.junit)
    testImplementation(libs.junit.junit)
    testImplementation(libs.junit.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}