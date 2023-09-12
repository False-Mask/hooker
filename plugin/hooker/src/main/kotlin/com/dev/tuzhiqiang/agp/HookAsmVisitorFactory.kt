package com.dev.tuzhiqiang.agp

import com.android.build.api.instrumentation.AsmClassVisitorFactory
import com.android.build.api.instrumentation.ClassContext
import com.android.build.api.instrumentation.ClassData
import com.dev.tuzhiqiang.aop.HookClassVisitor
import com.dev.tuzhiqiang.utils.Logger
import org.objectweb.asm.ClassVisitor

abstract class HookAsmVisitorFactory: AsmClassVisitorFactory<HookParams> {

    override fun createClassVisitor(
        classContext: ClassContext,
        nextClassVisitor: ClassVisitor
    ): ClassVisitor {
        return HookClassVisitor()
    }

    override fun isInstrumentable(classData: ClassData): Boolean {
        val hooks = parameters.get().extension
        return hooks.l.map {
            it.hook
        }.any {
            Logger.error(classData.className)
            it == classData.className
        }
    }

}