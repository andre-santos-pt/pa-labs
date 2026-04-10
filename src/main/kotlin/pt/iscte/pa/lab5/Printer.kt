package pt.iscte.pa.lab5

interface Printer {
    fun print(text: String)
}

class ConsolePrinter : Printer {
    override fun print(text: String) {
        println(text)
    }
}

class BracketDecorator(val printer: Printer) : Printer {
    override fun print(text: String) {
        printer.print("[$text]")
    }
}

class SkipEmptyDecorator(val printer: Printer) : Printer {
    override fun print(text: String) {
        if(text.isNotBlank())
            printer.print(text)
    }
}

fun main() {
    val console = ConsolePrinter()
    console.print("hello")

    val bracket = BracketDecorator(console)
    bracket.print("hello!")

    val skipEmpty = SkipEmptyDecorator(bracket)
    skipEmpty.print("")
    skipEmpty.print("")
    skipEmpty.print("hello!!")
}