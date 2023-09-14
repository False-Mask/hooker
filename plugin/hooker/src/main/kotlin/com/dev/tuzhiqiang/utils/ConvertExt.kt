package com.dev.tuzhiqiang.utils

@Suppress("UNUSED_EXPRESSION")
fun typeToDescriptor(str: String): String {
    when(str) {
        "boolean" -> "Z"
        "byte" -> "B"
        "short" -> "S"
        "int" -> 'I'
        "long" -> "J"
        "float" -> "F"
        "double" -> "D"
        "char" -> "C"
        "void" -> "V"
        else -> {

        }

    }
    return str

}

fun paramsToString(@Suppress("UNUSED_PARAMETER") parser: List<String>): String {
    TODO()
}