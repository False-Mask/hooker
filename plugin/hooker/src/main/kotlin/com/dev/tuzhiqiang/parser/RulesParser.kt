package com.dev.tuzhiqiang.parser

import com.dev.tuzhiqiang.aop.MethodInfo
import com.dev.tuzhiqiang.plugin.Hooks
import com.dev.tuzhiqiang.utils.paramsToString
import com.dev.tuzhiqiang.utils.typeToDescriptor
import java.lang.reflect.Modifier

object RulesParser {

  private val parsedData: MutableMap<Hooks,List<Pair<MethodInfo, MethodInfo>>> = mutableMapOf()

  fun parse(params: Hooks): List<Pair<MethodInfo, MethodInfo>> {
    if(!parsedData.contains(params)) {
      parsedData[params] = parseInternal(params)
    }
    return parsedData[params]!!
  }

  private fun parseInternal(params: Hooks): List<Pair<MethodInfo, MethodInfo>> = params.l.map {
    parseString(it.hook) to parseString(it.target)
  }


  private fun parseString(template: String): MethodInfo {
    // 去除前后空格
    val res = MethodInfo()
    val parser = TokenParser().also {
      it.init(template.trim())
    }
    var token = parser.consume()
    var state = 0

    var flag = 0
    var returnType = ""
    var ownerName = ""
    var methodName = ""
    var params = listOf<String>()

    while (token != null) {
      when(state) {
        // 权限修饰符
        0 -> {
          flag = when(token) {
            "public" -> {
              parser.consume()
              flag.or(Modifier.PUBLIC)
            }
            "private" -> {
              parser.consume()
              flag.or(Modifier.PRIVATE)
            }
            "protected" -> {
              parser.consume()
              flag.or(Modifier.PROTECTED)
            }
            else -> 0
          }
          state++
        }
        1 -> {
          when(token) {
            "static" -> {
              parser.consume()
              flag = flag.or(Modifier.STATIC)
            }
          }
          state++
        }
        2 -> {
          when(token) {
            "final" -> {
              parser.consume()
              flag = flag.or(Modifier.FINAL)
            }
          }
          state++
        }
        // returnType
        3 -> {
          parser.consume()
          returnType = token
          state++
        }
        // class类名
        4 -> {
          parser.consume()
          ownerName = token
          state++
        }
        // 函数名称
        5 -> {
          parser.consume()
          methodName = token
          state++
        }
        // 参数类型
        6 -> {
          parser.consume()
          val paramsStr = token.replace("[\\s()]".toRegex(), "")
          params = paramsStr
            .split(",")
            .filter { it != "" }
          state++
        }
        else -> {
          throw IllegalStateException("已经解析完毕")
        }

      }
      token = parser.peek()
    }
    return res.apply {
      access = flag
      owner = ownerName
      name = methodName
      descriptor = buildDescriptor(returnType, params)
    }
  }

  private fun buildDescriptor(
    returnType: String,
    params: List<String>
  ): String {
    return paramsToString(params) + typeToDescriptor(returnType)

  }

}