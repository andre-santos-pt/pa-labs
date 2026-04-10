package pt.iscte.pa.lab5

class Counter(val observer: (Int) -> Unit = {}) {
   private var value = 0

   fun value() = value

   fun inc() {
       value++
       observer(value)
   }

   fun dec() {
       value--
       observer(value)
   }
}
