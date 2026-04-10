package pt.iscte.pa.lab5

import kotlin.test.Test
import kotlin.test.assertEquals

class UndoListTests {

    @Test
    fun testAdd() {
        val list = UndoList<Int>()
        list.add(1)
        list.add(2)
        list.add(3)
        list.undoLast()
        list.undoLast() // no effect
        assertEquals(listOf(1,2), list)
    }

    @Test
    fun testSet() {
        val list = UndoList(mutableListOf(1,2,3))
        list.set(1, 4)
        assertEquals(listOf(1,4,3), list)
        list.undoLast()
        assertEquals(listOf(1,2,3), list)
    }

    @Test
    fun testRemoveAt() {
        val list = UndoList(mutableListOf(1,2,3))
        list.removeAt(0)
        assertEquals(listOf(2,3), list)
        list.undoLast()
        assertEquals(listOf(1,2,3), list)
        list.removeAt(1)
        assertEquals(listOf(1,3), list)
        list.undoLast()
        assertEquals(listOf(1,2,3), list)
    }

    @Test
    fun testMultiUndo() {
        val list = UndoListMulti<Int>()
        list.add(1)
        list.add(2)
        list.removeAt(0)
        list.add(3)
        list.undo()
        assertEquals(listOf(2), list)
        list.undo()
        assertEquals(listOf(1,2), list)
        list.undo()
        assertEquals(listOf(1), list)
        list.undo()
        assertEquals(listOf(), list)
        list.undo()
        assertEquals(listOf(), list)
    }
}