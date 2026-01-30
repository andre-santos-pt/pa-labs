package pt.iscte.pa.lab4

import kotlin.test.Test
import kotlin.test.assertEquals


class Tests {
    @Test
    fun testCreateTable() {
        val orm = ORM(MySQLSupport())
        val sql = orm.createTable(Student::class)
        val expected =
            "CREATE TABLE Student (number INT NOT NULL, name CHAR NOT NULL, worker BOOL, nickname CHAR);"
        assertEquals(expected, sql)
    }

    @Test
    fun testCreateTableWithAnnotations() {
        val orm = ORM(MySQLSupport())
        val sql = orm.createTable(StudentAnnotated::class)
        val expected =
            "CREATE TABLE Estudante (numero INT PRIMARY KEY, nome CHAR NOT NULL, worker BOOL);"
        assertEquals(expected, sql)
    }

    @Test
    fun testInsertInto() {
        val orm = ORM(MySQLSupport())
        val s = Student(26503, "André")
        val sql = orm.insertInto(s)
        val expected =
            "INSERT INTO Student (number, name, worker, nickname) VALUES (26503, 'André', NULL, NULL);"
        assertEquals(expected, sql)
    }

    @Test
    fun testInsertIntoWithAnnotations() {
        val orm = ORM(MySQLSupport())
        val s = StudentAnnotated(26503, "André")
        val sql = orm.insertInto(s)
        val expected =
            "INSERT INTO Estudante (numero, nome, worker) VALUES (26503, 'André', NULL);"
        assertEquals(expected, sql)
    }
}

data class Student(
    val number: Int,
    val name: String,
    val worker: Boolean? = null,
    val nickname: String? = null
)

@DbName("Estudante")
data class StudentAnnotated(
    @PrimaryKey
    @DbName("numero")
    val number: Int,
    @DbName("nome")
    val name: String,
    val worker: Boolean? = null,
    @DbIgnore
    val nickname: String? = null
)
