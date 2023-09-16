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
        //noinspection OldTargetApi
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
    buildFeatures {
        viewBinding = true
    }

    hook {
        element {
            isStatic = true
            hook = "public static void com.dev.tuzhiqiang#test()"
            target = "public static void com.dev.tuzhiqiag#a()"
        }

        element {
            isStatic = false
            hook = "public static void com.dev.tuzhiqiang.Main#test()"
            target = "public static void com.dev.tuzhiqiag.Test#a()"
        }

        element {
            isStatic = false
            hook = "public static void com.dev.tuzhiqiang.Main#test(int,int,double)"
            target = "public static void com.dev.tuzhiqiang.Main#test(int,int,double)"
        }

        element {
            hook = "public static void com.dev.tuzhiqiang.aop.Tester#testStaticHook()"
            target = "void com.dev.tuzhiqiang.aop.TesterHooker#testStaticHook()"
        }

        element {
            hook = "public static void com.dev.tuzhiqiang.aop.Tester#testMemberHook()"
            target = "void com.dev.tuzhiqiang.aop.TesterHooker#testMemberHook(com.dev.tuzhiqiang.aop.Tester)"
        }
    }
}

dependencies {

    //noinspection GradleDependency
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    //noinspection GradleDependency
    implementation("androidx.navigation:navigation-fragment-ktx:2.5.3")
    //noinspection GradleDependency
    implementation("androidx.navigation:navigation-ui-ktx:2.5.3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}