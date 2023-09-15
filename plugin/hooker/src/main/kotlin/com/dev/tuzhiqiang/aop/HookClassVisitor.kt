package com.dev.tuzhiqiang.aop

import com.android.build.api.instrumentation.ClassContext
import com.dev.tuzhiqiang.conf.apiVersion
import com.dev.tuzhiqiang.plugin.Hooks
import com.dev.tuzhiqiang.utils.RulesParser
import com.dev.tuzhiqiang.utils.paramsToString
import com.dev.tuzhiqiang.utils.typeToDescriptor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import java.lang.reflect.Modifier

class HookClassVisitor(
    private val ctx: ClassContext,
    nextVisitor: ClassVisitor,
    params: Hooks,
    version: Int = apiVersion,
): ClassVisitor(version, nextVisitor) {

    private val filterParam = parse(params)

    private fun parse(params: Hooks): List<Pair<MethodInfo, MethodInfo>> = params.l.map {
        parseString(it.hook) to parseString(it.target)
    }


    private fun parseString(template: String): MethodInfo {
        // 去除前后空格
        val res = MethodInfo()
        val parser = RulesParser().also {
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
                    params = paramsStr.split(" ")
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

    @Suppress("unused", "UNUSED_PARAMETER")
    private fun syntaxError(
        s: String,
        cur: String,
        all: String
    ) {


    }

    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor? {
        // 对符合规则的方法进行transform
        if (filterMethod(
                filterParam,
                MethodInfo(
                    ctx.currentClassData.className,
                    access,
                    name,
                    descriptor,
                    signature,
                    exceptions
                )
            )
        ) {
            return HookMethodVisitor()
        }
        return super.visitMethod(access, name, descriptor, signature, exceptions)
    }

    private fun filterMethod(
        filterParam: List<Pair<MethodInfo, MethodInfo>>,
        access: MethodInfo
    ): Boolean {
        return filterParam.any {
            it.first == access
        }
    }


}