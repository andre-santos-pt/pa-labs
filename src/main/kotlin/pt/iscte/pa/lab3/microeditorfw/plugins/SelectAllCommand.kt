package pt.iscte.pa.lab3.microeditorfw.plugins

import pt.iscte.pa.lab3.microeditorfw.skeleton.Command
import pt.iscte.pa.lab3.microeditorfw.skeleton.Editor

class SelectAllCommand : Command {
    override val name = "Select All"

    override fun execute(editor: Editor) {
        editor.setSelection(0.. editor.text.length)
    }

}