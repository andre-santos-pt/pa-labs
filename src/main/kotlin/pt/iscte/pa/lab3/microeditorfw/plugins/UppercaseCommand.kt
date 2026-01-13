package pt.iscte.pa.lab3.microeditorfw.plugins

import pt.iscte.pa.lab3.microeditorfw.skeleton.Command
import pt.iscte.pa.lab3.microeditorfw.skeleton.Editor

class UppercaseCommand : Command {
    override val name: String
        get() = "Upper Case"

    override fun execute(editor: Editor) {
        val sel = editor.get(editor.selection)
        val cap = sel.uppercase()
        editor.replace(cap, editor.selection)
    }
}