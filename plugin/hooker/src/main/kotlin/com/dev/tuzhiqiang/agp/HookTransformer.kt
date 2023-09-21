package com.dev.tuzhiqiang.agp

import com.android.build.api.transform.Context
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.QualifiedContent.DefaultContentType
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformInput
import com.android.build.api.transform.TransformInvocation
import com.android.build.api.transform.TransformOutputProvider
import com.dev.tuzhiqiang.plugin.HookExtension
import com.dev.tuzhiqiang.utils.Transformer

class HookTransformer(
    ext: HookExtension
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
            .addInputes(
                context,
                inputs,
                referencedInputs,
                outputProvider,
                isIncremental
            )
            .transform()


    }
}