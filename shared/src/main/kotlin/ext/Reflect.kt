package ext

import java.lang.reflect.Field
import kotlin.reflect.KCallable
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.jvm.javaField
import kotlin.reflect.jvm.jvmErasure

typealias Var<T, V> = KMutableProperty1<T, V>

// Reflection
@Suppress("UNCHECKED_CAST")
fun <T : Any> KClass<T>.ownVars() = declaredMemberProperties.sortedBy { it.javaField?.declaringClass?.declaredFields?.indexOf(it.javaField) ?: Int.MAX_VALUE }.mapNotNull { it as? Var<T, Any> }
@Suppress("UNCHECKED_CAST")
fun <T : Any> KClass<T>.vars(): List<Var<T, Any>> = supertypes.mapNotNull { it.classifier as? KClass<*> }.filter { !it.java.isInterface }.flatMap{ it.vars() as List<Var<T, Any>> } + ownVars()
fun <T : Any> KClass<T>.varsMap() = vars().associateBy { it.name }
fun <T : Any> KClass<T>.getters() = java.methods.filter { it.name.startsWith("get") }
fun <T : Any> KClass<T>.gettersMap() = getters().associateBy { it.name.removePrefix("get").firstCharLower() }
infix fun KCallable<*>.returns(type: KClass<*>) = returnType.jvmErasure.isSubclassOf(type)
@Suppress("UNCHECKED_CAST")
fun <C, T: Any> Var<C, T>.setCast(obj: C, value: String) = set(obj, when (returnType.classifier) {
    String::class -> value
    Int::class -> value.toInt()
    Boolean::class -> value.toBoolean()
    else -> 400 - "Invalid field type $returnType"
} as T)
inline fun <reified T: Any> Field.gets(obj: Any): T? = get(obj)?.let { it as T }
