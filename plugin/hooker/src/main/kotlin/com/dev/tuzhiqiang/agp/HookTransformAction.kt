package com.dev.tuzhiqiang.agp

import com.android.build.api.instrumentation.AsmClassVisitorFactory
import com.android.build.api.instrumentation.ClassContext
import com.android.build.api.instrumentation.ClassData
import com.android.build.api.instrumentation.InstrumentationContext
import com.android.build.api.instrumentation.InstrumentationParameters
import org.gradle.api.provider.Property
import org.objectweb.asm.ClassVisitor

open class HookTransformAction: AsmClassVisitorFactory<InstrumentationParameters.None> {
    override val instrumentationContext: InstrumentationContext
        get() = TODO("Not yet implemented")
    override val parameters: Property<InstrumentationParameters.None>
        get() = TODO("Not yet implemented")

    override fun createClassVisitor(
        classContext: ClassContext,
        nextClassVisitor: ClassVisitor
    ): ClassVisitor {
        TODO("Not yet implemented")
    }

    override fun isInstrumentable(classData: ClassData): Boolean {
        TODO("Not yet implemented")
    }

}