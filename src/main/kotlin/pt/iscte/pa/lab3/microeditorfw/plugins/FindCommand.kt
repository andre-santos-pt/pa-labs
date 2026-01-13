package pt.iscte.pa.lab3.microeditorfw.plugins

import pt.iscte.pa.lab3.microeditorfw.skeleton.Command
import pt.iscte.pa.lab3.microeditorfw.skeleton.Editor

class FindCommand : Command {
    override val name: String
        get() = "Find"

    override fun execute(editor: Editor) {
        editor.prompt("Find")?.let { word ->
            val index = editor.text.indexOf(word)
            if (index != -1)
                editor.setSelection(index..index + word.length)
        }
    }
}