package pt.iscte.pa.apifix

interface DummyListFix<T> {
    val isEmpty: Boolean
    val size: Int
    fun add(e: T)
    fun getFirst(): T
    fun getLast(): T
    fun getElement(index: Int): T
}

class DummyListFixImpl<T> : DummyListFix<T> {
    private var elements: Array<Any?>
    private var next: Int

    init {
        elements = Array(10) { null }
        next = 0
    }

    override val isEmpty: Boolean get() = next == 0

    override val size: Int get() = next

    val isNotEmpty: Boolean get() = next != 0

    override fun add(e : T) {
        if(next == elements.size)
            grow()

        elements[next++] = e
    }

    private fun grow() {
        elements = elements.copyOf(elements.size * 2)
    }

    fun addAll(list: Iterable<T>) {
        list.forEach {
            if(it != null)
                add(it)
        }
    }

    override fun getFirst(): T = elements[0] as T

    override fun getLast(): T = elements[next-1] as T

    override fun getElement(index: Int): T {
        check(index in 0..<size) {
            "invalid index: $index"
        }
        return elements[index] as T
    }

    fun filter(predicate: (T) -> Boolean): DummyList<T> {
        val filter = DummyListImpl<T>()
        for(i in 0 ..< next)
            if(elements[i] != null && predicate(elements[i] as T))
                filter.add(elements[i] as T)
        return filter
    }
}

fun main() {
    val list = DummyListFixImpl<Int>()
    (1..5).forEach {
        list.add(it)
    }
    list.addAll(ArrayList(listOf(6,7,8,9)))
    for(i in 0..< list.size)
        println(list.getElement(i))
}