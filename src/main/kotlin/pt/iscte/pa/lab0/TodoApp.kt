package pt.iscte.pa.lab0

data class TodoItem(
    val description: String,
    var done: Boolean = false) {
    override fun toString(): String = "[${if (done) "X" else " "}] $description"
}

class TodoList {
    private val items = mutableListOf<TodoItem>()

    val isEmpty get() = items.isEmpty()

    fun all(): List<TodoItem> = items.sortedBy { !it.done }

    fun add(task: String) {
        items.add(TodoItem(task))
    }

    fun check(index: Int) {
        check(index in 0 until items.size)
        all()[index].done = true
    }

    fun uncheck (index: Int) {
        check(index in 0 until items.size)
        all()[index].done = false
    }

    fun clearDone() {
        items.removeIf { it.done }
    }
}

fun main() {
    val todo = TodoList()
    println("""
        TODO App
        [no tasks]
    """.trimIndent())
    do {
        val cmd = readln()
        if (cmd.startsWith("+"))
            todo.add(cmd.substring(1))
        else if (cmd == "#")
            todo.clearDone()
        else runCatching {
                if (cmd.startsWith("*"))
                    todo.check(cmd.substring(1).toInt())
                else if (cmd.startsWith("-"))
                    todo.uncheck(cmd.substring(1).toInt())
            }.onFailure {
                println("invalid command")
            }
        val list = if (todo.isEmpty)
            listOf("[no tasks]")
        else
            todo.all()
        println(joinIterable(list, System.lineSeparator()))
    } while (cmd != "exit")
}