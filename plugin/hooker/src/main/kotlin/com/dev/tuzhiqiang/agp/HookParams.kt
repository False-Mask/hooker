package com.dev.tuzhiqiang.agp

import com.android.build.api.instrumentation.InstrumentationParameters
import com.dev.tuzhiqiang.plugin.Hooks
import org.gradle.api.tasks.Input

interface HookParams: InstrumentationParameters {

    @get:Input
    var extension: Hooks

}