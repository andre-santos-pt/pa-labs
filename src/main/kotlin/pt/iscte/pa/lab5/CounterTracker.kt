package pt.iscte.pa.lab5

class CounterTracker(counter: CounterObservable) {
    var modifications = 0
        private set
    var maxValue = 0
        private set
    //...
    init {
        counter.addObserver(object : CounterObserver {
            override fun incEvent(value: Int) {
                if(value > maxValue)
                    maxValue = value
                modifications++
            }
            override fun decEvent(value: Int) {
                modifications++
            }
        })
    }
}