package com.dev.tuzhiqiang.plugin

import java.io.Serializable

data class Hooks(
    val l: MutableList<Element> = mutableListOf()
): Serializable

data class Element(
    var isStatic: Boolean = false,
    var hook: String = "",
    var target: String = ""
): Serializable