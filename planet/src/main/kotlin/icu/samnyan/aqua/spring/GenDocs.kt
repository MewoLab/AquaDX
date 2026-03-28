package icu.samnyan.aqua.spring

import ext.API
import ext.Doc
import ext.RP
import java.io.File
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.javaType

const val PACKAGE_NAME = "icu.samnyan.aqua.net"

@OptIn(ExperimentalStdlibApi::class)
fun main() {
    val path = PACKAGE_NAME.replace('.', '/')
    val resources = Thread.currentThread().contextClassLoader.getResources(path)
    val classes = mutableListOf<Class<*>>()

    while (resources.hasMoreElements()) {
        val resource = resources.nextElement()
        File(resource.file).listFiles { _, name -> name.endsWith(".class") }?.forEach {
            val className = it.name.substring(0, it.name.length - 6)
            val fullClassName = "$PACKAGE_NAME.$className"
            val clazz = Class.forName(fullClassName)
            classes.add(clazz)
        }
    }
    val apiCls = API::class.java

    var buf = ""

    fun write(s: String) {
        buf += s + "\n"
        println(s)
    }

    println(classes)

    // Loop through all classes
    classes.filter { it.isAnnotationPresent(apiCls) }.forEach { cls ->
        val base = cls.getAnnotation(API::class.java).value.joinToString(", ")
        write("\n\n### ${cls.simpleName} : $base\n")
        write("Located at: [${cls.name}](${cls.name.replace('.', '/')}.kt)")

        // Loop through all functions
        cls.kotlin.declaredFunctions.filter { it.hasAnnotation<API>() }.forEach { fn ->
            val endpoint = fn.findAnnotation<API>()!!.value.joinToString(", ")
            val doc = fn.findAnnotation<Doc>()

            write("\n**${base.replace("/api/v2", "")}$endpoint** : ${doc?.desc ?: "No description"}\n")

            // Parameters
            fn.parameters.filter { it.hasAnnotation<RP>() }.forEach { param ->
                val paramName = param.name
                val paramType = param.type.javaType.typeName.split('.').last()
                write("* $paramName: $paramType")
            }

            doc?.ret?.let { write("* **Returns**: $it") }
        }
    }

    // Write to docs/api-v2.md
    File("docs/api-v2.md").writeText(buf)
}