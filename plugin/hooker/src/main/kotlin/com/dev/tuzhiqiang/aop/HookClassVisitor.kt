package com.dev.tuzhiqiang.aop

import com.android.build.api.instrumentation.ClassContext
import com.dev.tuzhiqiang.conf.apiVersion
import com.dev.tuzhiqiang.parser.RulesParser
import com.dev.tuzhiqiang.plugin.Hooks
import com.dev.tuzhiqiang.utils.Logger
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor

class HookClassVisitor(
    private val ctx: ClassContext,
    nextVisitor: ClassVisitor,
    params: Hooks,
    version: Int = apiVersion,
): ClassVisitor(version, nextVisitor) {

    private val filterParam = RulesParser.parse(params)

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
            Logger.error("Trigger Method Hook $name")
            return HookMethodVisitor()
        }
        return super.visitMethod(access, name, descriptor, signature, exceptions)
    }

    // 目前会依据
    // 1.方法指定类名
    // 2.方法描述符
    // 3.方法访问标识符(可选)
    private fun filterMethod(
        filterParam: List<Pair<MethodInfo, MethodInfo>>,
        targetMethod: MethodInfo
    ): Boolean {
        return filterParam.any {
            val rules = it.first
            rules.owner == targetMethod.owner &&
            rules.name == targetMethod.owner &&
            rules.descriptor == targetMethod.descriptor &&
            (rules.access != 0 && rules.access.and(targetMethod.access) == rules.access)
        }
    }


}