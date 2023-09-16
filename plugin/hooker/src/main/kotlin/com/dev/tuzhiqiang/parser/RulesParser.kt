package com.dev.tuzhiqiang.parser

import com.dev.tuzhiqiang.aop.MethodInfo
import com.dev.tuzhiqiang.plugin.Hooks
import com.dev.tuzhiqiang.utils.paramsToString
import com.dev.tuzhiqiang.utils.typeToDescriptor
import org.objectweb.asm.Opcodes

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
    var token = parser.peek()
    var state = 0

    var flag = 0
    var returnType = ""
    var ownerName = ""
    var methodName = ""
    var params = listOf<String>()

    while (token != null) {
      when(state) {
        // 权限修饰符
        ParserState.ACCESS.ordinal -> {
          flag = when(token) {
            "public" -> {
              parser.consume()
              flag.or(Opcodes.ACC_PUBLIC)
            }
            "private" -> {
              parser.consume()
              flag.or(Opcodes.ACC_PRIVATE)
            }
            "protected" -> {
              parser.consume()
              flag.or(Opcodes.ACC_PROTECTED)
            }
            else -> 0
          }
          state++
        }
        // 是否为static
        ParserState.STATIC.ordinal -> {
          when(token) {
            "static" -> {
              parser.consume()
              flag = flag.or(Opcodes.ACC_STATIC)
            }
          }
          state++
        }
        // 是否为final
        ParserState.FINAL.ordinal -> {
          when(token) {
            "final" -> {
              parser.consume()
              flag = flag.or(Opcodes.ACC_FINAL)
            }
          }
          state++
        }
        // synchronized
        ParserState.SYNCHRONIZED.ordinal -> {
          when(token){
            "synchronized" -> {
              parser.consume()
              flag = flag.or(Opcodes.ACC_SYNCHRONIZED)
            }
          }
          state++
        }
        // returnType
        ParserState.RETURN_TYPE.ordinal -> {
          parser.consume()
          returnType = token
          state++
        }
        // class类名
        ParserState.CLASS_NAME.ordinal -> {
          parser.consume()
          ownerName = token
          state++
        }
        // 函数名称
        ParserState.FUNC_NAME.ordinal -> {
          parser.consume()
          methodName = token
          state++
        }
        // 参数类型
        ParserState.PARAMS.ordinal -> {
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
      owner = ownerName.replace(".","/")
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