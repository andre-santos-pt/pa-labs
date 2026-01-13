package pt.iscte.pa.lab3

import kotlin.test.Test
import kotlin.test.assertEquals

data class Student(
    val number: Int,
    val name: String,
    val worker: Boolean? = null
)

class Tests {
    @Test
    fun testCreateTable() {
        val orm = ORM(MySQLSupport())
        val sql = orm.createTable(Student::class)
        val expected = "CREATE TABLE Student (number INT NOT NULL, name CHAR NOT NULL, worker BOOL);"
        assertEquals(expected, sql)
    }


    @Test
    fun testInsertInto() {
        val orm = ORM(MySQLSupport())
        val s = Student(26503, "André")
        val sql = orm.insertInto(s)
        val expected = "INSERT INTO Student (number, name, worker) VALUES (26503, 'André', NULL);"
        assertEquals(expected, sql)
    }
}