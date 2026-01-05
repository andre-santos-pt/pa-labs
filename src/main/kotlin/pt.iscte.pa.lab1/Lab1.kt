package pt.iscte.pa.lab1

import java.io.File

val List<File>.distinctExtensions: Set<String>
    get() = map { it.extension }
        .filter { it != "" }
        .toSet()

fun List<File>.countExtension(extension: String): Int =
    count { it.extension == extension }

fun List<File>.withExtension(vararg extensions: String): List<File> =
    filter { it.extension in extensions}


val File.depth: Int get() =
    if(name == "")
        0
    else if(parentFile == null)
        1
    else
        1 + parentFile.depth

fun File.listFilesRec(accept: (File) -> Boolean = { true }): List<File> {
    require(isDirectory)
    val children = listFiles() ?: return emptyList()
    return children
        .filter { it.isFile && accept(it) }
        .sortedBy { it.nameWithoutExtension } +
            children
                .filter { it.isDirectory }
                .flatMap { it.listFilesRec(accept) }
}