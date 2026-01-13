package pt.iscte.pa.lab1

import java.io.File
import java.nio.file.Path
import kotlin.test.Test
import kotlin.test.assertEquals

class FileUtilTests {
    @Test
    fun testFileExtension() {
        assertEquals("", File("random").extension)
        assertEquals("kt", File("DynamicClassLoading.kt").extension)
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
        File("DynamicClassLoading.kt"),
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
        val expected = listOf(File("DynamicClassLoading.kt"), File("Example.kt"))
        assertEquals(expected, fileList.withExtension("kt"))
        assertEquals(expected + File("Script.kts"), fileList.withExtension("kt", "kts"))
        assertEquals(emptyList<File>(), fileList.withExtension("txt"))
    }

    @Test
    fun testListFilesRecursively() {
        // /src/test/testDir
        val path = Path.of(System.getProperty("user.dir"), "src", "test", "testDir").toFile()
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
        // /src/test/testDir
        val path = Path.of(System.getProperty("user.dir"), "src", "test", "testDir").toFile()
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