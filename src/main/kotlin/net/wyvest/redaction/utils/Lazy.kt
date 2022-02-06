package net.wyvest.redaction.utils

import kotlin.reflect.KProperty

fun <T> invalidateableLazy(initializer: () -> T): InvalidatableLazyImpl<T> = InvalidatableLazyImpl(initializer)

private object UNINITIALIZED_VALUE

/**
 * Taken from Levelhead under GPLv3
 * https://github.com/Sk1erLLC/Levelhead/blob/master/LICENSE
 */
class InvalidatableLazyImpl<T>(private val initializer: () -> T, lock: Any? = null) : Lazy<T> {
    @Volatile private var _value: Any? = UNINITIALIZED_VALUE
    private val lock = lock ?: this

    fun invalidate() {
        _value = UNINITIALIZED_VALUE
    }

    override val value: T
        get() {
            val _v1 = _value
            if (_v1 !== UNINITIALIZED_VALUE) {
                return _v1 as T
            }

            return synchronized(lock) {
                val _v2 = _value
                if (_v2 !== UNINITIALIZED_VALUE) {
                    _v2 as T
                }
                else {
                    val typedValue = initializer()
                    _value = typedValue
                    typedValue
                }
            }
        }


    override fun isInitialized(): Boolean = _value !== UNINITIALIZED_VALUE

    override fun toString(): String = if (isInitialized()) value.toString() else "Lazy value not initialized yet."

    operator fun setValue(any: Any, property: KProperty<*>, t: T) {
        _value = t
    }
}