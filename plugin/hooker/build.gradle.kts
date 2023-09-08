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

    implementation("com.android.tools.build:gradle:8.1.1")
}