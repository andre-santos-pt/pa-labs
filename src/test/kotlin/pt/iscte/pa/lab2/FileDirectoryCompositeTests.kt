package pt.iscte.pa.lab2

import kotlin.test.Test
import kotlin.test.assertEquals


class FileDirectoryCompositeTests {

    val artists = DirectoryElement("artists")
    val beatles = DirectoryElement("beatles", artists)
    val help = DirectoryElement("help", beatles)
    val iNeedYou = FileElement("i need you", help)
    val letItBe = DirectoryElement("let it be", beatles)
    val getDown = FileElement("get down", letItBe)
    val twoOfUs = FileElement("two of us", letItBe)

    @Test
    fun testParents() {
        assertEquals(null, artists.parent)
        assertEquals(artists, beatles.parent)
        assertEquals(beatles, letItBe.parent)
        assertEquals(letItBe, twoOfUs.parent)
        assertEquals(help, iNeedYou.parent)
    }

    @Test
    fun testDepth() {
        assertEquals(0, artists.depth)
        assertEquals(1, beatles.depth)
        assertEquals(2, letItBe.depth)
        assertEquals(3, getDown.depth)
    }

    @Test
    fun testPath() {
        assertEquals("/artists", artists.path)
        assertEquals("/artists/beatles", beatles.path)
        assertEquals("/artists/beatles/let it be", letItBe.path)
        assertEquals("/artists/beatles/let it be/get down", getDown.path)
    }

    @Test
    fun testAcceptFileNames() {
        val list = mutableListOf<String>()
        artists.accept {
            if (it is FileElement)
                list.add(it.name)
        }
        assertEquals(listOf("i need you", "get down", "two of us"), list)
    }

    @Test
    fun testSimpleAllFiles() {
        assertEquals(listOf(iNeedYou, getDown, twoOfUs), beatles.allFiles())
    }

    @Test
    fun testSimpleAllDirectories() {
        assertEquals(listOf(beatles, help, letItBe), artists.allDirectories())
    }

    @Test
    fun testCount() {
        assertEquals(7, artists.count())
        assertEquals(3, artists.count { it is FileElement})
    }

    @Test
    fun testFilter() {
        val files = beatles.filter { it is FileElement }
        assertEquals(listOf(iNeedYou, getDown, twoOfUs), files)
    }

    @Test
    fun testMap() {
        val list = letItBe.map { it.name }
        assertEquals(listOf("let it be", "get down", "two of us"), list)
    }
}