package com.dev.tuzhiqiang.plugin

import org.gradle.api.Action
import org.gradle.api.tasks.Nested
import java.io.Serializable

@Suppress("unused")
open class HookExtension: Serializable {
    val hooks = Hooks()
    // 适配gradle dsl
    @Nested
    fun getElement() = Element()

    // 适配kotlin dsl
    fun element(action: Action<Element>) {
        val ele = getElement()
        action.execute(ele)
        hooks.l.add(ele)
    }
}