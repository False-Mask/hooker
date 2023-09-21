package com.dev.tuzhiqiang.utils

import com.android.build.api.transform.Context
import com.android.build.api.transform.TransformInput
import com.android.build.api.transform.TransformOutputProvider
import java.io.File

class Transformer {

  private var incremental: Boolean = false
  private var outputProvider: TransformOutputProvider? = null
  private var refInputs: MutableCollection<TransformInput>? = null
  private var ctx: Context? = null
  private var inputs: MutableCollection<TransformInput>? = null
  fun addInputes(
    context: Context?,
    inputs: MutableCollection<TransformInput>?,
    referencedInputs: MutableCollection<TransformInput>?,
    outputProvider: TransformOutputProvider?,
    incremental: Boolean
  ): Transformer {
    this.ctx = context
    this.inputs = inputs
    this.refInputs = referencedInputs
    this.outputProvider = outputProvider
    this.incremental = incremental
    return this
  }

  fun transform() {
    inputs?.forEach {
      it.directoryInputs.forEach {
        transformDirectory(it.file)
      }

      it.jarInputs.forEach {

      }
    }
  }

  private fun transformDirectory(directory: File) {
    if(!directory.isDirectory) {
      throw IllegalArgumentException("directory.isDirectory false")
    }
    directory.walk()
      .filter {
        it.name.endsWith(".class")
      }.forEach {
        Logger.error(it.name)
      }

  }

}