package pt.iscte.pa.lab4

import kotlin.reflect.*
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor


@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class PrimaryKey

@Target(AnnotationTarget.CLASS, AnnotationTarget.VALUE_PARAMETER)
annotation class DbName(val name: String)

@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class DbIgnore


fun KClass<*>.matchProperty(parameter: KParameter): KProperty<*> {
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
        }

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

fun KAnnotatedElement.dbName(default: String) =
    if (hasAnnotation<DbName>())
        findAnnotation<DbName>()?.name ?: ""
    else
        default

class ORM(val dbSupport: DBSupport) {

    private fun KClass<*>.tableFields() =
        primaryConstructor?.parameters
            ?.filter { !it.hasAnnotation<DbIgnore>() }
            ?: emptyList()

    fun createTable(clazz: KClass<*>): String {
        require(clazz.isData)
        return "CREATE TABLE ${clazz.dbName(clazz.simpleName!!)} (${
            clazz.tableFields().joinToString { p ->
                "${p.dbName(p.name ?: "")} ${dbSupport.mapType(p.type)}" +
                        if (p.hasAnnotation<PrimaryKey>()) " PRIMARY KEY"
                        else if (!p.type.isMarkedNullable) " NOT NULL"
                        else ""
            }
        });"
    }

    fun insertInto(obj: Any): String {
        require(obj::class.isData)
        val clazz = obj::class
        val fields = clazz.tableFields()
        return "INSERT INTO ${clazz.dbName(clazz.simpleName!!)} (${
            fields.joinToString {
                it.dbName(it.name ?: "")
            }
        }) VALUES (${
            fields.joinToString {
                val prop = clazz.matchProperty(it)
                dbSupport.mapValue(prop.call(obj))
            }
        });"
    }
}