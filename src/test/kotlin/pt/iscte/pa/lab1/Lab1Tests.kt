package pt.iscte.pa.lab1

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals

class Lab1Tests {
    @Test
    fun testFileExtension() {
        assertEquals("", File("random").extension)
        assertEquals("kt", File("Test.kt").extension)
        assertEquals("kts", File("build.gradle.kts").extension)
    }

    @Test
    fun testFileDepth() {
        assertEquals(0, File("").depth)
        assertEquals(1, File("dir").depth)
        assertEquals(2, File("dir/subdir").depth)
    }

    val fileList = listOf(
        File("README"),
        File("Test.kt"),
        File("Example.kt"),
        File("Script.kts")
    )

    @Test
    fun testDistinctExtensions() {
        assertEquals(setOf("kt", "kts"), fileList.distinctExtensions)
    }

    @Test
    fun testCountExtensions() {
        assertEquals(2, fileList.countExtension("kt"))
        assertEquals(0, fileList.countExtension("txt"))
    }

    @Test
    fun testWithExtension() {
        val expected = listOf(File("Test.kt"), File("Example.kt"))
        assertEquals(expected, fileList.withExtension("kt"))
        assertEquals(expected + File("Script.kts"), fileList.withExtension("kt", "kts"))
        assertEquals(emptyList<File>(), fileList.withExtension("txt"))
    }

    @Test
    fun testListFilesRecursively() {
        // testDir folder in intelliJ project
        val path = File(File(System.getProperty("user.dir")), "testDir")
        val files = path.listFilesRec { true }
        val expected = listOf(
            File(path,"f0.kt"),
            File(path,"a/f1.txt"),
            File(path,"b/f2.txt"),
            File(path,"b/f3.kt"),
            File(path,"b/c/f4.txt")
        )
        assertEquals(expected, files)
    }

    @Test
    fun testListFilesRecursivelyAccept() {
        // testDir in execution directory
        val path = File(File(System.getProperty("user.dir")), "testDir")
        val files = path.listFilesRec {
            it.extension == "kt"
        }
        val expected = listOf(
            File(path,"f0.kt"),
            File(path,"b/f3.kt"),
        )
        assertEquals(expected, files)
    }
}