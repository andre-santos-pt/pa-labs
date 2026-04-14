package pt.iscte.pa.lab6.skeleton

import pt.iscte.pa.lab5.ListObserver
import java.awt.*
import java.awt.event.FocusAdapter
import java.awt.event.FocusEvent
import javax.swing.*


class BarChartPanel(
    private val model: ListObserver<Int>
) : JPanel() {

    // TODO a) register observer on the model to repaint on changes
    init {

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
            // ADD: addCell
            // REMOVE: JPanel.remove
            // SET: update field text (components[index] as JTextField)

            revalidate()
            repaint()
        }
    }

    // TODO c) add observation feature when a field is edited (on focus lost)

    private fun addCell(value: Any?) {
        val textField = JTextField(value.toString())
        textField.addFocusListener(object : FocusAdapter() {
            override fun focusLost(e: FocusEvent) {
                val index = components.indexOfFirst { it == textField }
                val text = (components[index] as JTextField).text
                // focus lost at $index (new value is $text)
                // TODO c) notify observers
            }
        })
        textField.preferredSize = Dimension(50, 30)
        add(textField)
    }
}

// TODO d) sum panel
class SumPanel(
    private val model: ListObserver<Int>
) : JPanel() {

}

fun main() {
    val list = mutableListOf(40, 60, 80, 100, 30, 50, 90, 70)
    val model = ListObserver(list)

    val frame = JFrame("MVC Example")
    frame.add(BarChartPanel(model), BorderLayout.CENTER)
    frame.add(ListPanel(model).apply {

        // TODO c) register observer to update the model when view changes

    }, BorderLayout.NORTH)

    frame.add(JPanel().apply {
        layout = FlowLayout()
        add(JButton("Add").apply {
            addActionListener {
                JOptionPane.showInputDialog("Value?").toIntOrNull()?.let {
                    model.add(it)
                }
            }
        })
        add(JButton("Remove").apply {
            addActionListener {
                JOptionPane.showInputDialog("Index?").toIntOrNull()?.let {
                    if (it in 0..model.lastIndex)
                        model.removeAt(it)
                }
            }
        })
        add(SumPanel(model)) // goal d)
    }, BorderLayout.SOUTH)

    frame.setSize(800, 600)
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    frame.setLocationRelativeTo(null)
    frame.isVisible = true
}