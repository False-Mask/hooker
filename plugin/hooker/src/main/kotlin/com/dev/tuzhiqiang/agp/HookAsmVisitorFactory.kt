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
        Logger.error(classContext.currentClassData.className)
        return HookClassVisitor(
            classContext,
            nextClassVisitor,
            parameters.get().extension
        )
    }

    override fun isInstrumentable(classData: ClassData): Boolean {
        return true
    }

}