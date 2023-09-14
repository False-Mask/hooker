package com.dev.tuzhiqiang.aop

data class MethodInfo(
    var owner: String? = null,
    var access: Int = 0,
    var name: String? = "",
    var descriptor: String? = null,
    var signature: String? = null,
    var exceptions: Array<out String>? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MethodInfo

        if (owner != other.owner) return false
        if (access != other.access) return false
        if (name != other.name) return false
        if (descriptor != other.descriptor) return false
        if (signature != other.signature) return false
        if (exceptions != null) {
            if (other.exceptions == null) return false
            if (!exceptions.contentEquals(other.exceptions)) return false
        } else if (other.exceptions != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = owner.hashCode()
        result = 31 * result + access
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (descriptor?.hashCode() ?: 0)
        result = 31 * result + (signature?.hashCode() ?: 0)
        result = 31 * result + (exceptions?.contentHashCode() ?: 0)
        return result
    }
}