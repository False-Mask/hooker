package com.dev.tuzhiqiang.aop

import com.dev.tuzhiqiang.conf.apiVersion
import org.objectweb.asm.MethodVisitor

class HookMethodVisitor(
    version: Int = apiVersion
): MethodVisitor(version) {



}