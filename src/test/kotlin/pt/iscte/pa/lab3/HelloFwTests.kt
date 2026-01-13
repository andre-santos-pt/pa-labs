package pt.iscte.pa.lab3

import kotlin.test.Test
import kotlin.test.assertEquals

class HelloPt : Hello {
    override fun sayHello() = "olá"
}

class HelloIt : Hello {
    override fun sayHello() = "ciao"
}

class HelloFwTests {

    @Test
    fun `say hello`() {
        val fw = HelloFw("src/test/kotlin/pt/iscte/pa/lab3/hellotest.conf")
        assertEquals(listOf("olá", "ciao"), fw.allHellos())
    }
}