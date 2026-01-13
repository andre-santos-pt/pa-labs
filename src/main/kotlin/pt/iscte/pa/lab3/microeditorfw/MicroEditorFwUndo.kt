package pt.iscte.pa.lab3.microeditorfw

import pt.iscte.pa.lab3.microeditorfw.skeleton.CloseCommand
import pt.iscte.pa.lab3.microeditorfw.skeleton.Command
import pt.iscte.pa.lab3.microeditorfw.skeleton.Editor
import java.awt.BorderLayout
import java.awt.Dimension
import java.io.File
import javax.swing.*
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance


interface CommandWithUndo : Command {
    fun undo(editor: Editor)
}

class Clear : CommandWithUndo {
    var text: String? = null

    override val name: String
        get() = "Clear"

    override fun execute(editor: Editor) {
        text = editor.text
        editor.replace("", 0..< editor.text.length)
    }

    override fun undo(editor: Editor) {
        editor.insert(text ?: "", 0)
    }
}

class Trim : CommandWithUndo {
    var text: String? = null

    override val name: String
        get() = "Trim"

    override fun execute(editor: Editor) {
        text = editor.text
        editor.replace(editor.text.trim(), 0..< editor.text.length)
    }

    override fun undo(editor: Editor) {
        editor.replace(text ?: "", 0..<editor.text.length)
    }
}

internal class UndoManager {
    val undoStack = mutableListOf<CommandWithUndo>()
    var next = 0

    fun store(cmd: CommandWithUndo) {
        undoStack.add(next, cmd)
        next++
        // clear remaining
        repeat(undoStack.size - next) {
            undoStack.removeLast()
        }
    }

    fun undo(editor: Editor) {
        if(next > 0) {
            next--
            undoStack[next].undo(editor)
        }
    }

    fun redo(editor: Editor) {
        if(next < undoStack.size) {
            undoStack[next].execute(editor)
            next++
        }
    }
}

internal class MicroEditorFwUndo : Editor {
    private val frame: JFrame
    private val buttons: JPanel
    private val textArea: JTextArea
    private val undoManager = UndoManager()

    init {
        frame = JFrame("Micro Text Editor Framework")
        frame.size = Dimension(600, 300)
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        textArea = JTextArea()
        buttons = JPanel()
        frame.add(buttons, BorderLayout.NORTH)
        frame.add(textArea)
        val commands = mutableListOf<KClass<out Command>>(CloseCommand::class, Clear::class, Trim::class)
        commands.addAll(loadCommands("commands.conf"))
        commands.forEach {
            addButton(it)
        }
        buttons.add(JButton("Undo").apply {
            addActionListener {
                undoManager.undo(this@MicroEditorFwUndo)
            }
        }, BorderLayout.NORTH)
        buttons.add(JButton("Redo").apply {
            addActionListener {
                undoManager.redo(this@MicroEditorFwUndo)
            }
        }, BorderLayout.NORTH)
    }


    private fun loadCommands(configurationFile: String): List<KClass<out Command>> {
        return File(configurationFile).readLines().map {
            Class.forName(it).kotlin as KClass<out Command>
        }
    }

    private fun addButton(commandClass: KClass<out Command>) {
        val command = commandClass.createInstance()
        buttons.add(JButton(command.name).apply {
            addActionListener {
                val cmd = commandClass.createInstance()
                cmd.execute(this@MicroEditorFwUndo)
                if(cmd is CommandWithUndo)
                    undoManager.store(cmd)
            }
        }, BorderLayout.NORTH)
    }

    fun open() {
        frame.isVisible = true
    }

    override val text: String
        get() = textArea.text
    override val selection: IntRange
        get() = textArea.selectionStart..<textArea.selectionEnd

    override fun get(selection: IntRange): String =
        textArea.text.substring(selection)

    override fun insert(text: String, offset: Int) {
        textArea.insert(text, offset)
    }

    override fun replace(text: String, selection: IntRange) {
        textArea.replaceRange(text, selection.first, selection.last + 1)
        textArea.selectionStart = selection.first
        textArea.selectionEnd = selection.first + text.length
    }

    override fun setSelection(selection: IntRange) {
        textArea.selectionStart = selection.first
        textArea.selectionEnd = selection.last
    }

    override fun prompt(text: String): String {
        return JOptionPane.showInputDialog(frame, text)
    }
}

fun main() {
    val window = MicroEditorFwUndo()
    window.open()
}