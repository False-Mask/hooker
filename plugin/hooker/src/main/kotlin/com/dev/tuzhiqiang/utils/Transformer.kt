package com.dev.tuzhiqiang.utils

import com.android.build.api.transform.Context
import com.android.build.api.transform.Format
import com.android.build.api.transform.TransformInput
import com.android.build.api.transform.TransformOutputProvider
import org.gradle.kotlin.dsl.support.unzipTo
import org.gradle.kotlin.dsl.support.zipTo
import java.io.File

class Transformer {

  private lateinit var trans: (ByteArray) -> ByteArray
  private var incremental: Boolean = false
  private var outputProvider: TransformOutputProvider? = null
  private var refInputs: MutableCollection<TransformInput>? = null
  private var ctx: Context? = null
  private var inputs: MutableCollection<TransformInput>? = null
  fun addInputs(
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
    inputs?.forEach {input->
      input.directoryInputs.forEach {
        val file = outputProvider!!.getContentLocation(
          it.file.absolutePath,
          it.contentTypes,
          it.scopes,
          Format.DIRECTORY
        )!!
        it.file.copyRecursively(file, overwrite = true)
        transformDirectory(file)
      }

      input.jarInputs.forEach {
        val file = outputProvider!!.getContentLocation(
          it.file.absolutePath,
          it.contentTypes,
          it.scopes,
          Format.JAR
        )!!
        // 解压
        val tmpDir = File(it.file.absolutePath.replace(".jar",""))
        unzipTo(tmpDir,it.file)
        // 转换
        transformDirectory(tmpDir)
        // 压缩
        zipTo(file, tmpDir)
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
        val transBytes = trans(it.readBytes())
        it.writeBytes(transBytes)
      }
  }

  fun actions(trans: (ByteArray) -> ByteArray): Transformer {
    this.trans = trans
    return this
  }

}