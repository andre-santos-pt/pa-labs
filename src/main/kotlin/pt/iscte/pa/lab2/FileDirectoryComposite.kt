package pt.iscte.pa.lab2

sealed class Element(
    val name: String,
    val parent: DirectoryElement?
) {
    init {
        parent?.childrenList?.add(this)
    }

    override fun toString(): String = name

    val depth: Int
        get() = if (parent == null) 0
        else
            1 + parent.depth

    val path: String
        get() = if (parent == null)
            "/$name"
        else
            parent.path + "/$name"
}

class FileElement(
    name: String,
    parent: DirectoryElement? = null
) : Element(name, parent)


class DirectoryElement(
    name: String,
    parent: DirectoryElement? = null
) : Element(name, parent) {

    internal val childrenList: MutableList<Element> = mutableListOf()

    val children: List<Element> get() = childrenList.toList()
}

fun Element.print() {
    accept {
        println("\t".repeat(it.depth) +
                (if(it is FileElement) "- " else "+ ") + it.name)
    }
}

fun DirectoryElement.allFiles(): List<FileElement> {
    val list = mutableListOf<FileElement>()
    childrenList.forEach {
        when(it) {
            is FileElement -> list.add(it)
            is DirectoryElement -> list.addAll(it.allFiles())
        }
    }
    return list
}

fun DirectoryElement.allDirectories(): List<DirectoryElement> {
    val list = mutableListOf<DirectoryElement>()
    childrenList.forEach {
        if(it is DirectoryElement) {
            list.add(it)
            list.addAll(it.allDirectories())
        }
    }
    return list
}

fun Element.accept(visitor: (Element) -> Unit) {
    when (this) {
        is FileElement -> visitor(this)
        is DirectoryElement -> {
            visitor(this)
            this.childrenList.forEach {
                it.accept(visitor)
            }
        }
    }
}

fun DirectoryElement.allFiles2(): List<FileElement> {
    val list = mutableListOf<FileElement>()
    accept {
        if(it is FileElement)
            list.add(it)
    }
    return list
}

fun DirectoryElement.allDirectories2(): List<DirectoryElement> {
    val list = mutableListOf<DirectoryElement>()
    accept {
        if(it is DirectoryElement)
            list.add(it)
    }
    return list
}


fun Element.count(predicate: (Element) -> Boolean = { true }): Int {
    var count = 0
    accept {
        if(predicate(it))
            count++
    }
    return count
}

fun Element.filter(predicate: (Element) -> Boolean): List<Element> {
    val filter = mutableListOf<Element>()
    accept { e ->
        if (predicate(e))
            filter.add(e)
    }
    return filter
}

fun <M> Element.map(transform: (Element) -> M): List<M> {
    val list = mutableListOf<M>()
    accept {
        list.add(transform(it))
    }
    return list
}