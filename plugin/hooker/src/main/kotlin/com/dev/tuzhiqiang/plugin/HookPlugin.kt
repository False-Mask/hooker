@file:Suppress("unused")

package com.dev.tuzhiqiang.plugin

import com.android.build.api.instrumentation.InstrumentationScope
import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.gradle.BaseExtension
import com.dev.tuzhiqiang.agp.HookAsmVisitorFactory
import com.dev.tuzhiqiang.agp.HookTransformer
import com.dev.tuzhiqiang.utils.Logger
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.get

@Suppress("DEPRECATION", "REDUNDANT_ELSE_IN_WHEN")
class HookPlugin: Plugin<Project> {

    private val plugins = listOf(
        "com.android.library",
        "com.android.application"
    )

    private val extName = "hook"

    private lateinit var target: Project

    private lateinit var pluginType: PluginType

    override fun apply(target: Project) {
        this.target = target
        Logger.init(target)
        init()
        if(hasPlugins(target, plugins)) {
            Logger.info("hook插件添加成功!!")
            // 添加扩展
            target.extensions.add(extName, HookExtension::class.java)
            // 添加extension
            applyTransform()
        } else {
            Logger.error("====== 没apply android插件请不要打开hook插件!! ======")
        }
    }

    private fun applyTransform() {
        when(pluginType) {
            PluginType.Transformer -> {
                val android = target.extensions["android"] as BaseExtension
                android.registerTransform(HookTransformer(target.extensions[extName] as HookExtension))
            }
            PluginType.TransformAction -> {
                val ext = target.extensions.getByType(AndroidComponentsExtension::class.java)
                ext.onVariants {
                    it.instrumentation.transformClassesWith(
                        HookAsmVisitorFactory::class.java,
                        InstrumentationScope.ALL
                    ) { params ->
                        params.extension = target
                            .extensions
                            .getByType(HookExtension::class.java)
                            .hooks
                    }
                }
            }
            else -> {
                throw IllegalStateException("AGP版本暂时不兼容")
            }
        }
    }

    private fun init() {
        target.extensions.getByType(AndroidComponentsExtension::class.java)?.apply {
            pluginType = PluginType.TransformAction
            if(target.properties["forceTransform"] as String? == "true") {
                if(pluginVersion.major >= 8) {
                    Logger.error("agpVersion >= 8.0.0, forceTransform=true失效")
                } else {
                    pluginType = PluginType.Transformer
                    Logger.warn("hook强制采用transform")
                }
            }
        } ?: {
            pluginType = PluginType.Transformer
        }
    }

    private fun hasPlugins(target: Project, plugins: List<String>): Boolean {
        return plugins.any {
            target.plugins.hasPlugin(it)
        }
    }
}