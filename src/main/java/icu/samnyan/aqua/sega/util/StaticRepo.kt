package icu.samnyan.aqua.sega.util

import java.lang.reflect.Field
import java.util.*

open class StaticRepo<T : Any, ID>(val data: List<T>, val idGetter: (T) -> ID) {
    fun findAll(): List<T> = data
    private val idMap by lazy { data.associateBy { idGetter(it) } }
    fun findById(id: ID): Optional<T> = Optional.ofNullable(idMap[id])
}
