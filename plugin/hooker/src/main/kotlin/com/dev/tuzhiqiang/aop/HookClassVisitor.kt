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
    params: Hooks,
    version: Int = apiVersion,
) : ClassVisitor(version) {

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
                        "public" -> flag.or(Modifier.PUBLIC)
                        "private" -> flag.or(Modifier.PRIVATE)
                        "protected" -> flag.or(Modifier.PROTECTED)
                        else -> throw IllegalStateException("非法token state")
                    }
                    state++
                }
                1 -> {
                   when(token) {
                       "static" -> flag = flag.or(Modifier.STATIC)
                       else -> throw IllegalStateException("非法token state")
                   }
                    state++
                }
                2 -> {
                    when(token) {
                        "final" -> flag = flag.or(Modifier.FINAL)
                        else -> throw IllegalStateException("非法token state")
                    }
                    state++
                }
                // returnType
                3 -> {
                    returnType = token
                    state++
                }
                // class类名
                4 -> {
                    ownerName = token
                    state++
                }
                // 函数名称
                5 -> {
                    methodName = token
                    state++
                }
                // 参数类型
                6 -> {
                    val paramsStr = token.replace("[\\s()]".toRegex(), "")
                    params = paramsStr.split(" ")

                }
                else -> {
                    throw IllegalStateException("已经解析完毕")
                }

            }
            token = parser.consume()
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