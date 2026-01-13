package pt.iscte.pa.lab3.microeditorfw.plugins

import pt.iscte.pa.lab3.microeditorfw.skeleton.Command
import pt.iscte.pa.lab3.microeditorfw.skeleton.Editor

class ItalicsCommand : Command {
    override val name: String
        get() = "Italics"

    override fun execute(editor: Editor) {
        if(!editor.selection.isEmpty()) {
            val text = editor.get(editor.selection)
            editor.replace("<i>$text</i>", editor.selection)
        }
    }
}