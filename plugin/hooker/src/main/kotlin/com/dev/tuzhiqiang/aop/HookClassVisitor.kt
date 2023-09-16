package com.dev.tuzhiqiang.aop

import com.android.build.api.instrumentation.ClassContext
import com.dev.tuzhiqiang.conf.apiVersion
import com.dev.tuzhiqiang.parser.RulesParser
import com.dev.tuzhiqiang.plugin.Hooks
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor

class HookClassVisitor(
    @Suppress("unused") private val ctx: ClassContext,
    nextVisitor: ClassVisitor,
    private val params: Hooks,
    version: Int = apiVersion,
): ClassVisitor(version, nextVisitor) {

    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
        return HookMethodVisitor(RulesParser.parse(params),super.visitMethod(access, name, descriptor, signature, exceptions))
    }


}