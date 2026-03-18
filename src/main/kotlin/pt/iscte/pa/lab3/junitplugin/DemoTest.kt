package pt.iscte.pa.lab3.junitplugin

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.Assertions.assertEquals

@ExtendWith(TimeWatcher::class)
class DemoTest {
    @Test
    fun shortTest() {
        assertEquals(2, 1 + 1)
    }

    @Test
    fun longTest() {
        Thread.sleep(1000)
    }
}
