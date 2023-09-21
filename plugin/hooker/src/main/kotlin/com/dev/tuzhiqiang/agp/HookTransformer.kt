package com.dev.tuzhiqiang.agp

import com.android.build.api.transform.Context
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.QualifiedContent.DefaultContentType
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformInput
import com.android.build.api.transform.TransformOutputProvider
import com.dev.tuzhiqiang.aop.HookClassVisitor
import com.dev.tuzhiqiang.plugin.HookExtension
import com.dev.tuzhiqiang.plugin.Hooks
import com.dev.tuzhiqiang.utils.Transformer
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import kotlin.math.exp

class HookTransformer(
    private val ext: HookExtension
): Transform() {
    override fun getName(): String {
        return HookTransformer::class.java.name
    }

    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> {
        return mutableSetOf(DefaultContentType.CLASSES)
    }

    override fun getScopes(): MutableSet<in QualifiedContent.Scope> {
        return mutableSetOf(QualifiedContent.Scope.PROJECT, QualifiedContent.Scope.EXTERNAL_LIBRARIES)
    }

    override fun isIncremental(): Boolean {
        return true
    }

    override fun transform(
        context: Context?,
        inputs: MutableCollection<TransformInput>?,
        referencedInputs: MutableCollection<TransformInput>?,
        outputProvider: TransformOutputProvider?,
        isIncremental: Boolean
    ) {
        Transformer()
            .addInputs(
                context,
                inputs,
                referencedInputs,
                outputProvider,
                isIncremental
            )
            .actions {
                val writer = ClassWriter(0)
                val reader = ClassReader(it)
                reader.accept(HookClassVisitor(writer, ext.hooks), 0)
                writer.toByteArray()
            }
            .transform()


    }
}