package com.dev.tuzhiqiang.aop

import com.android.build.api.instrumentation.ClassContext
import com.dev.tuzhiqiang.conf.apiVersion
import com.dev.tuzhiqiang.parser.RulesParser
import com.dev.tuzhiqiang.plugin.Hooks
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