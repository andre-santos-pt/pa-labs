package pt.iscte.pa.lab4

import java.lang.reflect.InvocationTargetException
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation

@Target(AnnotationTarget.FUNCTION)
annotation class ExpectedException(val exceptionClass: KClass<out Exception>)

@Target(AnnotationTarget.FUNCTION)
annotation class TestCase


data class TestCaseResult(
    val id: String,
    val fail: String? = null
) {
    val pass: Boolean get() = fail == null

    override fun toString(): String {
        return "$id: ${if(pass) "SUCCESS" else "FAIL: $fail"}"
    }
}

class KUnit(val tests: KClass<*>) {

    fun testResults() : List<TestCaseResult> {
        val results = mutableListOf<TestCaseResult>()
        val instance = tests.createInstance()
        tests.declaredMemberFunctions
            .filter { it.hasAnnotation<TestCase>() }
            .forEach { t->
                try {
                    t.call(instance)
                    results.add(TestCaseResult(t.name))
                }
                catch (e: InvocationTargetException) {
                    if(t.hasAnnotation<ExpectedException>() && e.cause != null) {
                        val ann = t.findAnnotation<ExpectedException>()
                        if(ann?.exceptionClass == e.cause!!::class)
                            results.add(TestCaseResult(t.name))
                        else
                            results.add(TestCaseResult(t.name, "Expected ${ann?.exceptionClass?.simpleName}"))
                    }
                    else
                        results.add(TestCaseResult(t.name, e.cause?.message))
                }
            }
        return results
    }
}

