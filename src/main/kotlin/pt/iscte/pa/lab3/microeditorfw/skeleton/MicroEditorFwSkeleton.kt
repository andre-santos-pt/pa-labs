package pt.iscte.pa.lab3.microeditorfw.skeleton

import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.*
import kotlin.system.exitProcess

interface Editor {
    val text: String
    val selection: IntRange
    fun get(range: IntRange): String
    fun insert(text: String, offset: Int)
    fun replace(text: String, selection: IntRange)
    fun setSelection(selection: IntRange)
    fun prompt(text: String): String?
}

interface Command {
    val name: String
    fun execute(editor: Editor)
}

internal class CloseCommand : Command {
    override val name = "Close"

    override fun execute(editor: Editor) {
        exitProcess(0)
    }
}

internal class MicroEditorFw : Editor {
    private val frame: JFrame
    private val buttons: JPanel
    private val textArea: JTextArea

    init {
        frame = JFrame("Micro Text Editor Framework")
        frame.size = Dimension(400, 200)
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        textArea = JTextArea()
        buttons = JPanel()
        frame.add(buttons, BorderLayout.NORTH)
        frame.add(textArea)
        val commands = mutableListOf<Command>(CloseCommand())
        commands.addAll(loadCommands("commands.conf"))
        commands.forEach {
            addButton(it)
        }
    }

    private fun loadCommands(configurationFile: String): List<Command> {
        return emptyList() // TODO
    }

    private fun addButton(command: Command) {
        buttons.add(JButton(command.name).apply {
            addActionListener {
                command.execute(this@MicroEditorFw)
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

    override fun get(selection: IntRange): String = textArea.text.substring(selection)

    override fun insert(text: String, offset: Int) {
        textArea.insert(text, offset)
    }

    override fun replace(text: String, selection: IntRange) {
        textArea.replaceRange(text, selection.first, selection.last+1)
        textArea.selectionStart = selection.first
        textArea.selectionEnd = selection.first + text.length
    }

    override fun setSelection(selection: IntRange) {
        textArea.selectionStart = selection.first
        textArea.selectionEnd = selection.last
    }

    override fun prompt(text: String): String? {
        return JOptionPane.showInputDialog(frame, text)
    }
}

fun main() {
    val window = MicroEditorFw()
    window.open()
}