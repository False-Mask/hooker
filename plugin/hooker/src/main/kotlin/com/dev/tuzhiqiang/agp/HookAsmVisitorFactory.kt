package com.dev.tuzhiqiang.agp

import com.android.build.api.instrumentation.AsmClassVisitorFactory
import com.android.build.api.instrumentation.ClassContext
import com.android.build.api.instrumentation.ClassData
import com.dev.tuzhiqiang.aop.HookClassVisitor
import org.objectweb.asm.ClassVisitor

abstract class HookAsmVisitorFactory: AsmClassVisitorFactory<HookParams> {

    override fun createClassVisitor(
        classContext: ClassContext,
        nextClassVisitor: ClassVisitor
    ): ClassVisitor {
        return HookClassVisitor(classContext,parameters.get().extension)
    }

    override fun isInstrumentable(classData: ClassData): Boolean {
        return true
    }

}