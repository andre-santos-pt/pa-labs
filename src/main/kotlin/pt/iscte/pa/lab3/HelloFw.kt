package pt.iscte.pa.lab3

import java.io.File
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

interface Hello {
    fun sayHello(): String
}

class HelloFw(val confFile: String = "hello.conf") {
    fun allHellos(): List<String> =
        (if(File(confFile).exists()) File(confFile).readLines() else emptyList())
        .mapNotNull { className ->
            try {
                val clazz: KClass<*> = Class.forName(className).kotlin
                val hello = clazz.createInstance() as Hello
                hello.sayHello()
            } catch (_ : Exception) {
                System.err.println("could not instantiate $className")
                null
            }
        }
}

fun main() {
    listOf("HelloPt","HelloIt").forEach { className ->
        val clazz: KClass<*> = Class.forName("pt.iscte.pa.lab3.$className").kotlin
        val hello = clazz.createInstance() as Hello
        println(hello.sayHello() + "!")
    }
}
