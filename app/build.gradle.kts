plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("com.google.gms.google-services")
    id("androidx.navigation.safeargs.kotlin")
    id("kotlin-parcelize")

  //  id("com.google.dagger.hilt.android")
   // id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.pjiii"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.pjiii"
        minSdk = 24
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
viewBinding{
    enable= true
}
}

dependencies {
    implementation(kotlin("stdlib"))
    //for date
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.2.1")
    implementation ("com.jakewharton.threetenabp:threetenabp:1.3.1")


    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Room

//location
    implementation ("com.google.android.gms:play-services-location:21.0.1")


    //hilt
   // implementation("com.google.dagger:hilt-android:2.48.1")
  //  ksp("com.google.dagger:hilt-android-compiler:2.48.1")
  //  implementation("androidx.room:room-ktx:2.48.1")


    //BD
    implementation(platform("com.google.firebase:firebase-bom:32.7.4"))
    implementation("com.google.firebase:firebase-database")
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-storage")


    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

