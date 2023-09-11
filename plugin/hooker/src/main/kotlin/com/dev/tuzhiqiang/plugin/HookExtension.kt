package com.dev.tuzhiqiang.plugin

import org.gradle.api.Action
import org.gradle.api.tasks.Nested

@Suppress("unused")
abstract class HookExtension {

    val elements = mutableListOf<Element>()
    @Nested
    fun getElement() = Element()

    fun element(action: Action<Element>) {
        val ele = getElement()
        action.execute(ele)
        elements.add(ele)
    }
}