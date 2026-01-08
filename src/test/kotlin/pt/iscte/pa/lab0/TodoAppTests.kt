package pt.iscte.pa.lab0

import kotlin.test.Test
import kotlin.test.assertEquals

class TodoAppTests {

    @Test
    fun `items ordered by date with completed tasks first`() {
        val todo = TodoList()
        todo.add("A")
        todo.add("B")
        todo.add("C")
        todo.check(2)
        assertEquals(
            listOf(
                TodoItem("C", true),
                TodoItem("A"),
                TodoItem("B")
            ),
            todo.all()
        )
    }

    @Test
    fun `clear completed items`() {
        val todo = TodoList()
        todo.add("A")
        todo.add("B")
        todo.add("C")
        todo.add("D")
        todo.check(1)
        todo.check(2)
        todo.clearDone()
        assertEquals(
            listOf(
                TodoItem("A"),
                TodoItem("D")
            ),
            todo.all()
        )
    }

    @Test
    fun `check on sorted index`() {
        val todo = TodoList()
        todo.add("A")
        todo.add("B")
        todo.add("C")
        todo.check(2)
        todo.uncheck(0) // C is at top of sorted
        assertEquals(
            listOf(
                TodoItem("A"),
                TodoItem("B"),
                TodoItem("C")
            ),
            todo.all()
        )
    }


}