plugins {
    `kotlin-dsl`
}

gradlePlugin {
    plugins {
        val hook by creating {
            implementationClass = "com.dev.tuzhiqiang.plugin.HookPlugin"
            id = "com.plugin.hook"
        }
    }
}

dependencies {

    // https://mvnrepository.com/artifact/com.google.code.gson/gson
    implementation("com.google.code.gson:gson:2.10.1")

    compileOnly("com.android.tools.build:gradle:8.1.1")
}