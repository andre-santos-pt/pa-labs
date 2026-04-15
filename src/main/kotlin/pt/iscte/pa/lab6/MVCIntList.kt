package pt.iscte.pa.lab6

import pt.iscte.pa.lab5.ListEvent
import pt.iscte.pa.lab5.ListObserver
import java.awt.*
import java.awt.event.FocusAdapter
import java.awt.event.FocusEvent
import javax.swing.*


class BarChartPanel(
    private val model: ListObserver<Int>
) : JPanel() {

    // TODO a) register observer on the model to repaint() on changes
    init {
        model.addObserver { _, _, _ ->
            repaint()
        }
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        if (model.isEmpty())
            return

        val chartHeight = height - 50
        val barWidth = 50

        val maxValue = model.max()
        for (i in model.indices) {
            val barHeight =
                ((model[i] / maxValue.toDouble()) * chartHeight).toInt()
            val x = 20 + i * barWidth
            val y = chartHeight + 30 - barHeight
            g.color = Color(100, 150, 255)
            g.fillRect(x, y, barWidth - 10, barHeight)
            g.color = Color.WHITE
            g.drawString(model[i].toString(), x + 5, y + 15)
        }
    }
}

class ListPanel(
    private val model: ListObserver<Int>
) : JPanel() {

    init {
        layout = FlowLayout()
        model.forEach { e ->
            addCell(e)
        }
        model.addObserver { event, index, element ->

            // TODO b) handle the observer event
            when(event) {
                ListEvent.ADD -> addCell(element, index)
                ListEvent.REMOVE -> remove(index)
                ListEvent.SET -> (components[index] as JTextField).text = element.toString()
            }
            revalidate()
            repaint()
        }
    }

    // TODO c) add observation feature when a field is edited (on focus lost)
    private val observers = mutableListOf<(Int, String) -> Unit>()
    fun addObserver(observer: (Int, String) -> Unit) {
        observers.add(observer)
    }

    private fun addCell(value: Any?, index: Int? = null) {
        val textField = JTextField(value.toString())
        textField.addFocusListener(object : FocusAdapter() {
            override fun focusLost(e: FocusEvent) {
                val index = components.indexOfFirst { it == textField }
                val text = (components[index] as JTextField).text
                // TODO c) notify observers
                observers.forEach {
                    it(index, text)
                }
            }
        })
        textField.preferredSize = Dimension(50, 30)
        if(index == null)
            add(textField)
        else
            add(textField, index)
    }
}

// TODO d) sum panel
class SumPanel(
    private val model: ListObserver<Int>
) : JPanel() {

    init {
        val label = JLabel("total: " + model.sum())
        add(label)
        model.addObserver { _,_,_ ->
            label.text = "total: " + model.sum()
        }
    }
}

interface Command {
    fun run(model: ListObserver<Int>)
    fun undo(model: ListObserver<Int>)
}

class AddCommand(val value: Int) : Command {
    override fun run(model: ListObserver<Int>) {
        model.add(value)
    }

    override fun undo(model: ListObserver<Int>) {
        model.removeLast()
    }
}

class RemoveCommand(val index: Int) : Command {
    private var previous: Int? = null

    override fun run(model: ListObserver<Int>) {
        previous = model.removeAt(index)
    }

    override fun undo(model: ListObserver<Int>) {
        model.add(index, previous ?: throw RuntimeException("undo called before run()"))
    }
}

class SetCommand(val index: Int, val value: Int) : Command {
    private var previous: Int? = null

    override fun run(model: ListObserver<Int>) {
        previous = model.set(index, value)
    }

    override fun undo(model: ListObserver<Int>) {
        model.set(index, previous ?: throw RuntimeException("undo called before run()"))
    }
}


fun main() {
    val list = mutableListOf(40, 60, 80, 100, 30, 50, 90, 70)
    val model = ListObserver(list)

    val undoStack = mutableListOf<Command>()
    fun runCommand(cmd: Command) {
        cmd.run(model)
        undoStack.add(cmd)
    }

    val frame = JFrame("MVC Example")
    frame.add(BarChartPanel(model), BorderLayout.CENTER)
    frame.add(ListPanel(model).apply {

        // TODO c) register observer to update the model when view changes
        addObserver { index, string ->
            runCommand(SetCommand(index, string.toIntOrNull() ?: 0))
        }

    }, BorderLayout.NORTH)

    frame.add(JPanel().apply {
        layout = FlowLayout()
        add(JButton("Add").apply {
            addActionListener {
                JOptionPane.showInputDialog("Value?").toIntOrNull()?.let {
                    runCommand(AddCommand(it))
                }
            }
        })
        add(JButton("Remove").apply {
            addActionListener {
                JOptionPane.showInputDialog("Index?").toIntOrNull()?.let {
                    if (it in 0..model.lastIndex)
                        runCommand(RemoveCommand(it))
                }
            }
        })
        add(JButton("Undo").apply {
            addActionListener {
                if(undoStack.isNotEmpty())
                    undoStack.removeLast().undo(model)
            }
        })
        add(SumPanel(model)) // goal d)
    }, BorderLayout.SOUTH)

    frame.setSize(800, 600)
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    frame.setLocationRelativeTo(null)
    frame.isVisible = true
}