package org.polyfrost.redaction.client.features.particles

class FastIntArrayList {
    private var internalArray = IntArray(64)

    var size = 0
        private set

    fun addAll(src: IntArray) {
        val needed = size + src.size
        if (needed > internalArray.size) {
            internalArray = internalArray.copyOf(needed.coerceAtLeast(internalArray.size * 2))
        }

        System.arraycopy(src, 0, internalArray, size, src.size)
        size += src.size
    }

    fun clear() {
        size = 0
    }

    fun toIntArray(): IntArray {
        return internalArray.copyOf(size)
    }
}
