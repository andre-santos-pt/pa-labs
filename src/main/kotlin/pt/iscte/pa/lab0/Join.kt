package pt.iscte.pa.lab0


fun joinStringList(list: List<String>): String {
    if(list.isEmpty()) return ""
    var result = list.first()
    for (i in 1 until list.size)
        result += ", ${list[i]}"
    return result
}

fun joinIterable(list: Iterable<Any>, separator: String = ", "): String {
    val iterator = list.iterator()
    if(!iterator.hasNext()) return ""
    var result = iterator.next().toString()
    while (iterator.hasNext())
        result += "$separator${iterator.next()}"
    return result
}

fun joinIterableSkipNulls(list: Iterable<Any?>, separator: String = ", ") =
    joinIterable(list.filterNotNull(), separator)


