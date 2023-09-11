plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.plugin.hook")
}

android {
    namespace = "com.dev.tuzhiqiang"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.dev.tuzhiqiang"
        minSdk = 24
        targetSdk = 33
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

    hook {
        element {
            isStatic = true
            hook = "hello"
            target = "1"
        }

        element {
            isStatic = false
            hook = "ele"
            target = "2"
        }
    }
}

dependencies {

    //noinspection GradleDependency
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}