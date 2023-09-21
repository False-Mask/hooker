plugins {
    id("com.android.application") version "7.4.2"
    id("org.jetbrains.kotlin.android") version "1.7.10"
    id("com.plugin.hook")
}

androidComponents {
    println(pluginVersion)
}

android {
    namespace = "com.dev.demo_agp_7"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.dev.demo_agp_7"
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
        viewBinding.enable = true
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
            hook = "public static void com.dev.tuzhiqiang#test()"
            target = "public static void com.dev.tuzhiqiag#a(...)"
        }

        element {
            hook = "public static void com.dev.tuzhiqiang.Main#test()"
            target = "public static void com.dev.tuzhiqiag.Test#a(...)"
        }

        element {
            hook = "public static void com.dev.tuzhiqiang.Main#test(int,int,double)"
            target = "public static void com.dev.tuzhiqiang.Main#test(...)"
        }

        element {
            hook = "public static void com.dev.tuzhiqiang.aop.Tester#testStaticHook()"
            target = "void com.dev.tuzhiqiang.aop.TesterHooker#testStaticHook(...)"
        }

        element {
            hook = "public static void com.dev.tuzhiqiang.aop.Tester#testMemberHook()"
            target = "void com.dev.tuzhiqiang.aop.TesterHooker#testMemberHook(...)"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}