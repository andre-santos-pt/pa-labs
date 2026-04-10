package pt.iscte.pa.lab5

class UndoListMulti<T>(
    private val list: MutableList<T> = mutableListOf()
) : MutableList<T> by list {
    private var undoStack = mutableListOf<() -> Unit>()

    fun undo() {
        if(undoStack.isNotEmpty()) {
            val lastUndo = undoStack.removeLast()
            lastUndo()
        }
    }
    override fun add(element: T): Boolean {
        list.add(element)
        undoStack.add {
            list.removeLast()
        }
        return true
    }

    override fun set(index: Int, element: T): T {
        val existing = list.set(index, element)
        undoStack.add {
            list.set(index, existing)
        }
        return existing
    }

    override fun removeAt(index: Int): T {
        val existing = list.removeAt(index)
        undoStack.add {
            list.add(index, existing)
        }
        return existing
    }

}