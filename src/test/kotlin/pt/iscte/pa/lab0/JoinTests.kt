package pt.iscte.pa.lab0

import kotlin.test.Test
import kotlin.test.assertEquals

class JoinTests {
    @Test
    fun testJoinIterable() {
        val set = setOf("A", "B", "C")
        val result = joinIterable(set, "|")
        assertEquals("A|B|C", result)
    }

    @Test
    fun testJoinIterableSkipNulls() {
        val list = listOf(null, "A", "B", null, "C")
        val result = joinIterableSkipNulls(list)
        assertEquals("A, B, C", result)
    }
}
