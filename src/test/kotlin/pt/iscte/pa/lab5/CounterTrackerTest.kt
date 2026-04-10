package pt.iscte.pa.lab5

import kotlin.test.Test
import kotlin.test.assertEquals

class CounterTrackerTest {
    @Test
    fun test() {
        val counter = CounterObservable()
        val tracker = CounterTracker(counter)
        repeat(5) {
            counter.inc()
        }
        repeat(2) {
            counter.dec()
        }
        assertEquals(5, tracker.maxValue)
        assertEquals(7, tracker.modifications)
    }
}