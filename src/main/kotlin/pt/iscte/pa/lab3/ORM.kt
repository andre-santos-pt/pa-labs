package pt.iscte.pa.lab3

import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty
import kotlin.reflect.KType
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.primaryConstructor

class ORM(val dbSupport: DBSupport) {
    fun createTable(clazz: KClass<*>): String {
        require(clazz.isData)
        return "CREATE TABLE ${clazz.simpleName} (${clazz.primaryConstructor?.parameters?.joinToString { "${it.name} ${dbSupport.mapType(it.type)}" }});"
    }

    fun insertInto(obj: Any) : String {
        require(obj::class.isData)
        val c = obj::class
        return "INSERT INTO ${c.simpleName} (${c.primaryConstructor?.parameters?.joinToString { "${it.name}" }}) " +
                "VALUES (${c.primaryConstructor?.parameters?.joinToString { dbSupport.mapValue(c.matchProperty(it).call(
                    obj
                ))}});"
    }
}

fun KClass<*>.matchProperty(parameter: KParameter) : KProperty<*> {
    require(isData)
    return declaredMemberProperties.first { it.name == parameter.name }
}

interface DBSupport {
    fun mapType(type: KType): String
    fun mapValue(value: Any?): String
}

class MySQLSupport : DBSupport {
    override fun mapType(type: KType): String =
        when (type.classifier) {
            Int::class -> "INT"
            Double::class -> "DECIMAL"
            String::class -> "CHAR"
            Boolean::class -> "BOOL"
            else -> TODO("unsupported: " + type.classifier.toString())
        } + if (!type.isMarkedNullable) " NOT NULL" else ""


    override fun mapValue(value: Any?): String =
        if (value == null)
            "NULL"
        else
            when (value::class) {
                String::class -> "'$value'"
                Int::class, Double::class, Boolean::class -> "$value"
                else -> TODO("unsupported: " + value::class.simpleName)
            }
}