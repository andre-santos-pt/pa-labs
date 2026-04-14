package pt.iscte.pa.lab5.skeleton

enum class ListEvent {
    ADD
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
}
