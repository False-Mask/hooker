package com.dev.tuzhiqiang.utils

fun typeToDescriptor(str: String): String {
    return when(str) {
        "boolean" -> "Z"
        "byte" -> "B"
        "short" -> "S"
        "int" -> "I"
        "long" -> "J"
        "float" -> "F"
        "double" -> "D"
        "char" -> "C"
        "void" -> "V"
        else -> {
            return if(str.contains("[]")) {
                parseArray(str)
            } else {
                "L${str.replace(".","/")};"
            }
        }

    }
}

fun parseArray(str: String): String {
    val arrCharIdx = str.indexOf('[')
    val obj = typeToDescriptor(str.substring(0,arrCharIdx))
    val arrChars = str.substring(arrCharIdx,str.length).replace("]","")
    return "$arrChars$obj"
}

fun paramsToString(parser: List<String>): String {
    val builder = StringBuilder("(")
    for(e in parser) {
        builder.append(typeToDescriptor(e))
    }
    builder.append(")")
    return builder.toString()
}