@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.dev.tuzhiqiang.log

import org.gradle.api.Project
import org.gradle.api.logging.LogLevel

object Logger {

    private lateinit var target: Project

    fun init(target: Project) {
        this.target = target
    }

    fun log(level: LogLevel = LogLevel.INFO , msg: String) {
        target.logger.log(level,msg)
    }

    fun error(msg: String) {
        log(LogLevel.ERROR, msg)
    }

    fun info(msg: String) {
        log(LogLevel.INFO,msg)
    }

    fun debug(msg: String) {
        log(LogLevel.DEBUG,msg)
    }

}