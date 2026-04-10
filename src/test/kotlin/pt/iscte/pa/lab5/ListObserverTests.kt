package pt.iscte.pa.lab5

import org.junit.jupiter.api.Assertions.assertTrue
import kotlin.test.Test
import kotlin.test.assertEquals

class ListObserverTests {
    @Test
    fun testRemoveAtObservation() {
        val listObs = ListObserver(mutableListOf(1,2,3))
        val rem = mutableListOf<Int>()
        listObs.addObserver { event, index, element ->
            assertEquals(ListEvent.REMOVE, event)
            rem.add(element)
        }
        repeat(3) {
            listObs.removeAt(0)
        }
        assertEquals(listOf(1,2,3), rem)
        assertTrue(listObs.isEmpty())
    }

    @Test
    fun testSetObservation() {
        val listObs = ListObserver(mutableListOf(-1, 2, -3, 4, 5))
        val modifiedIndexes = mutableListOf<Int>()
        listObs.addObserver { event, index, element ->
            assertEquals(ListEvent.SET, event)
            modifiedIndexes.add(index)
        }
        for(i in 0 until listObs.size)
            if(listObs[i] < 0)
                listObs[i] = 0

        assertEquals(listOf(0, 2, 0, 4, 5), listObs)
        assertEquals(listOf(0, 2), modifiedIndexes)
    }

}