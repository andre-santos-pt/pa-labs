package pt.iscte.pa.lab3.junitplugin

import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext

class TimeWatcher : BeforeEachCallback, AfterEachCallback {
    var time: Long? = null

    override fun beforeEach(e: ExtensionContext) {
        time = System.currentTimeMillis()
    }

    override fun afterEach(e: ExtensionContext) {
        val duration = System.currentTimeMillis() - (time ?: 0)
        println("${e.requiredTestMethod.name}: $duration ms")
    }
}