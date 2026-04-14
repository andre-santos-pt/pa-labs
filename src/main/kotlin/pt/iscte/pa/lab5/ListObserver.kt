package pt.iscte.pa.lab5

enum class ListEvent {
    ADD, REMOVE, SET
}

class ListObserver<T>(
    private val list: MutableList<T> = mutableListOf()
) : MutableList<T> by list {

    private val observers = mutableListOf<(ListEvent, Int, T) -> Unit>()

    fun addObserver(observer: (ListEvent, Int, T) -> Unit) =
        observers.add(observer)

    fun removeObserver(observer: (ListEvent, Int, T) -> Unit) =
        observers.remove(observer)

    override fun add(element: T): Boolean {
        val ret = list.add(element)
        observers.forEach { obs ->
            obs(ListEvent.ADD, list.size - 1, element)
        }
        return ret
    }

    override fun add(index: Int, element: T): Unit {
        list.add(index, element)
        observers.forEach { obs ->
            obs(ListEvent.ADD, index, element)
        }
    }

    override fun removeAt(index: Int): T {
        val ret = list.removeAt(index)
        observers.forEach { obs ->
            obs(ListEvent.REMOVE, index, ret)
        }
        return ret
    }

    override fun set(index: Int, element: T): T {
        val ret = list.set(index, element)
        observers.forEach { obs ->
            obs(ListEvent.SET, index, element)
        }
        return ret
    }
}
