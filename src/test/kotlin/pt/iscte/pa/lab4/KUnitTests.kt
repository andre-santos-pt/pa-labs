package pt.iscte.pa.lab4

import org.junit.jupiter.api.Assertions.assertFalse
import kotlin.reflect.KClass
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@Target(AnnotationTarget.FUNCTION)
annotation class ExpectedException(val exceptionClass: KClass<out Exception>)

@Target(AnnotationTarget.FUNCTION)
annotation class TestCase

class KUnitTests {

    @Test
    fun basicTest() {
        val kunit = KUnit(MyTests::class)
        val results = kunit.testResults()
        assertEquals(setOf("testSize", "testFirstOnEmpty"), results.map { it.id }.toSet())
        assertTrue(results.find { it.id == "testSize" }?.pass ?: false)
        assertFalse(results.find { it.id == "testFirstOnEmpty" }?.pass ?: true)
    }
}



// example class with tests
class MyTests {
    @TestCase
    fun testSize() {
        val list = listOf(1,2,3)
        assertEquals(3, list.size)
    }

    @TestCase
    @ExpectedException(ArrayIndexOutOfBoundsException::class)
    fun testFirstOnEmpty() {
        val list = listOf<Int>()
        val first = list.first()
    }

    fun otherMethod() {

    }
}

