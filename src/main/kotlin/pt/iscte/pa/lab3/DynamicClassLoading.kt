package pt.iscte.pa.lab3

import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

class Test {
   override fun toString(): String = "test object"
}

fun main() {
   val testClass: KClass<*> = Class.forName("pt.iscte.pa.lab3.Test").kotlin
   println(testClass.simpleName)
   val testObject = testClass.createInstance()
   println(testObject)
}
