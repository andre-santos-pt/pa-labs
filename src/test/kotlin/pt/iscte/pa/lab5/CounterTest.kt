package pt.iscte.pa.lab5

import kotlin.test.Test
import kotlin.test.assertEquals

class CounterTest {

    @Test
    fun testCountModification() {
        var modifications = 0
        val counter = Counter {
            modifications++
        }
        counter.inc()
        counter.inc()
        counter.dec()
        counter.inc()
        assertEquals(4, modifications)
    }
}