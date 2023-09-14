@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.dev.tuzhiqiang.utils

import org.gradle.api.Project
import org.gradle.api.logging.LogLevel

object Logger {

    private lateinit var target: Project

    fun init(target: Project) {
        Logger.target = target
    }

    fun log(level: LogLevel = LogLevel.INFO , msg: String) {
        val log = when(level) {
            LogLevel.INFO -> "INFO:\t$msg"
            LogLevel.ERROR -> "ERROR:\t$msg"
            LogLevel.DEBUG -> "DEBUG:\t$msg"
            LogLevel.QUIET -> "QUIET:\t$msg"
            LogLevel.LIFECYCLE -> "LIFECYCLE:\t$msg"
            LogLevel.WARN -> "WARN:\t$msg"
        }
        target.logger.log(level, log)
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