package com.dev.tuzhiqiang.agp

import com.android.build.api.instrumentation.InstrumentationParameters
import com.dev.tuzhiqiang.plugin.HookExtension

interface HookParams: InstrumentationParameters {

    var extension: HookExtension

}