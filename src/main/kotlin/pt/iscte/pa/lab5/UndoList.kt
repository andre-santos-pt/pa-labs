package pt.iscte.pa.lab5

class UndoList<T>(
    private val list: MutableList<T> = mutableListOf()
) : MutableList<T> by list {

    private var undoAction: (() -> (Unit)) = {}

    fun undoLast() {
        undoAction()
        undoAction = {}
    }

    override fun add(element: T): Boolean {
        list.add(element)
        undoAction = {
            list.removeLast()
        }
        return true
    }

    override fun set(index: Int, element: T): T {
        val existing = list.set(index, element)
        undoAction = {
            list.set(index, existing)
        }
        return existing
    }

    override fun removeAt(index: Int): T {
        val existing = list.removeAt(index)
        undoAction = {
            list.add(index, existing)
        }
        return existing
    }
}