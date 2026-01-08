package pt.iscte.pa.lab0

import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.assertFalse
import kotlin.test.assertEquals
import kotlin.test.assertNull

class CollectionTests {

    @Test
    fun `list concatenation`() {
        val listA = listOf("A", "B", "C")
        val listB = listOf("D", "E")
        val listAB =  listA + listB // listA.plus(listB)
        assertEquals(listOf("A", "B", "C"), listA) // listA does not change
        assertEquals(listOf("D", "E"), listB) // listB does not change
        assertEquals(listOf("A","B","C","D","E"), listAB)
    }

    @Test
    fun `list filtering`() {
        val list = listOf("A", "BB", "CCC", "D")
        val listSingleChar = list.filter { it.length == 1 }
        assertEquals(listOf("A","D"), listSingleChar)
    }

    @Test
    fun `list removeIf`() {
        val list = mutableListOf("", "B", "", "D")
        list.removeIf { it.isEmpty() }
        assertEquals(listOf("B","D"), list)
    }

    @Test
    fun `map to doubled`() {
        val list = mutableListOf(1, 2, 3)
        val doubled = list.map { it * 2 }
        assertEquals(listOf(2, 4, 6), doubled)
    }

    @Test
    fun `set has unique elements`() {
        val set = mutableSetOf(1, 2, 3)
        set.add(1)
        set.add(4)
        assertEquals(setOf(4, 3, 2, 1), set) // order is not considered in equality
    }

    @Test
    fun `join with semi-colon`() {
        val list = mutableListOf(1, 2, 3)
        val listAsString = list.joinToString(";")
        assertEquals("1;2;3", listAsString)
    }

    @Test
    fun `map has unique keys`() {
        val map = mutableMapOf(1 to "A", 2 to "B", 3 to "C")
        map[1] = "D"
        assertEquals(3, map.size)
        assertTrue(map.containsKey(1))
        assertEquals(map[1], "D")
        assertFalse(map.containsKey(4))
        assertNull(map[4])
    }
}