@file:Suppress("unused")

package com.dev.tuzhiqiang.plugin

import com.android.build.gradle.BaseExtension
import com.dev.tuzhiqiang.agp.HookTransformer
import com.dev.tuzhiqiang.log.Logger
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.get

class HookPlugin: Plugin<Project> {

    private val plugins = listOf(
        "com.android.library",
        "com.android.application"
    )

    private val extName = "hook"

    override fun apply(target: Project) {
        if(hasPlugins(target, plugins)) {
            // 添加扩展
            target.extensions.add(extName, HookExtension::class.java)
            // 添加extension
            target.afterEvaluate {
                (target.extensions["android"] as BaseExtension)
                    .registerTransform(HookTransformer(target.extensions[extName] as HookExtension))
            }
        } else {
            Logger.error("====== 没apply android插件请不要打开hook插件!! ======")
        }
    }

    private fun hasPlugins(target: Project, plugins: List<String>): Boolean {
        return plugins.any {
            target.plugins.hasPlugin(it)
        }
    }
}