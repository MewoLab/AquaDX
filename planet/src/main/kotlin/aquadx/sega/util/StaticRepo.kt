package aquadx.sega.util

import java.lang.reflect.Field
import java.util.*

open class StaticRepo<T : Any, ID>(val data: List<T>, val idGetter: (T) -> ID) {
    fun findAll(): List<T> = data
    private val idMap by lazy { data.associateBy { idGetter(it) } }
    fun findById(id: ID): Optional<T> = Optional.ofNullable(idMap[id])

    private val enableField by lazy {
        data.firstOrNull()?.let { obj ->
            var cls: Class<*>? = obj::class.java
            var f: Field? = null
            while (cls != null && f == null) {
                f = cls.declaredFields.find { it.name == "enable" || it.name == "isEnabled" }
                cls = cls.superclass
            }
            f?.apply { isAccessible = true }
        }
    }

    private val enableMap by lazy { data.groupBy { enableField?.get(it) as? Boolean } }

    fun findByEnable(enable: Boolean): List<T> = enableMap[enable] ?: emptyList()
}
