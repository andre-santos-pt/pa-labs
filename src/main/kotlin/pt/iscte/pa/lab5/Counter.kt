package pt.iscte.pa.lab5

class Counter(val observer: (Int) -> Unit = {}) {
   var value = 0
        private set

   fun inc() {
       value++
       observer(value)
   }

   fun dec() {
       value--
       observer(value)
   }
}
