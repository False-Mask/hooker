package com.dev.tuzhiqiang.aop

import com.dev.tuzhiqiang.conf.apiVersion
import com.dev.tuzhiqiang.utils.Logger
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

class HookMethodVisitor(
    private val transform: List<Pair<MethodInfo, MethodInfo>>,
    delegate: MethodVisitor,
    version: Int = apiVersion
): MethodVisitor(version,delegate) {

    // hookMethod
    override fun visitMethodInsn(
        opcode: Int,
        owner: String?,
        name: String?,
        descriptor: String?,
        isInterface: Boolean
    ) {
        when(opcode) {
            Opcodes.INVOKESTATIC -> {
                val hookOrNot = transform.firstOrNull {
                    val m = it.first
                    m.owner == owner && m.name == name && descriptor == m.descriptor
                }
                if(hookOrNot != null) {
                    Logger.error("${owner}#${name} => ${hookOrNot.second.owner}#${hookOrNot.second.name}")
                    super.visitMethodInsn(opcode, hookOrNot.second.owner, hookOrNot.second.name, descriptor, isInterface)
                    return
                }
            }
            Opcodes.INVOKEVIRTUAL -> {
                val hookOrNot = transform.firstOrNull {
                    val m = it.first
                    m.owner == owner && m.name == name && descriptor == m.descriptor
                }
                hookOrNot?.let {
                    Logger.error("${owner}#${name} => ${hookOrNot.second.owner}#${hookOrNot.second.name}")
                    super.visitMethodInsn(
                        Opcodes.INVOKESTATIC,
                        it.second.owner,
                        it.second.name,
                        descriptor?.replace("(","(L$owner;"),
                        isInterface
                    )
                    return
                }
            }
        }
        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface)
    }

}