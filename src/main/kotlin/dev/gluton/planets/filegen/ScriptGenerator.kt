package dev.gluton.planets.filegen

import dev.gluton.planets.filegen.GeneratePlanetViewer.stringBuilder
import godot.Object
import godot.annotation.DoubleRange
import godot.annotation.FloatRange
import godot.annotation.IntRange
import godot.annotation.LongRange
import godot.core.Color
import godot.core.Vector3
import godot.global.GD
import java.io.File

abstract class ScriptGenerator(fileName: String) {
    private val scriptFile = File(scriptsDirectory, fileName)
    protected val stringBuilder = StringBuilder()

    protected abstract fun generateData()

    operator fun invoke() {
        GD.print("Generating ${scriptFile.absolutePath}...")
        if (scriptFile.exists()) {
            scriptFile.delete()
        }
        generateData()
        scriptFile.writeText(stringBuilder.toString())
        GD.print("Generated ${scriptFile.absolutePath}!")
    }

    protected fun line() {
        stringBuilder.appendLine()
    }

    protected fun tool() {
        stringBuilder.appendLine("tool")
    }

    protected inline fun <reified T : Object> extends() {
        stringBuilder.appendLine("extends ${T::class.simpleName}")
    }

    protected inline fun <reified T> export(
        name: String,
        defaultValue: T,
        onUpdateValue: String? = null,
        godotAnnotations: List<Annotation> = emptyList(),
    ) {
        val godotTypeHint = when (defaultValue) {
            is Int -> {
                "int" + (godotAnnotations.filterIsInstance<IntRange>().takeIf { it.isNotEmpty() }?.first()?.run {
                    ", $start, $end" + if (step != -1) ", $step" else ""
                } ?: "")
            }
            is Long -> {
                "int" + (godotAnnotations.filterIsInstance<LongRange>().takeIf { it.isNotEmpty() }?.first()?.run {
                    ", $start, $end" + if (step != -1L) ", $step" else ""
                } ?: "")
            }
            is Float -> {
                "float" + (godotAnnotations.filterIsInstance<FloatRange>().takeIf { it.isNotEmpty() }?.first()?.run {
                    ", $start, $end" + if (step != -1F) ", $step" else ""
                } ?: "")
            }
            is Double -> {
                "float" + (godotAnnotations.filterIsInstance<DoubleRange>().takeIf { it.isNotEmpty() }?.first()?.run {
                    ", $start, $end" + if (step != -1.0) ", $step" else ""
                } ?: "")
            }
            is Color -> ""
            else -> {
                defaultValue?.let {
                    it::class.simpleName!!
                } ?: ""
            }
        }
        val godotName = camelCaseToSnakeCase(name)
        val defaultValueString = when (defaultValue) {
            is Int, is Long, is Float, is Double -> defaultValue.toString()
            is Vector3 -> "Vector3(${defaultValue.x}, ${defaultValue.y}, ${defaultValue.z})"
            is Color -> "Color(${defaultValue.r}, ${defaultValue.g}, ${defaultValue.b})"
            else -> null
        }
        stringBuilder.apply {
            append("export${if (godotTypeHint.isNotBlank()) "($godotTypeHint)" else ""} var $godotName")
            defaultValueString?.let {
                append(" = $it")
            }
            onUpdateValue?.let {
                append(" setget set_$godotName")
            }
            appendLine()
        }
        line()
        onUpdateValue?.let {
            function("set_$godotName", "new_$godotName") {
                appendLine("if ($godotName != new_$godotName):")
                appendLine("\t$godotName = new_$godotName")
                appendLine(onUpdateValue.prependIndent("\t"))
            }
            line()
        }
    }

    protected fun declaration(name: String, value: String? = null) {
        stringBuilder.apply {
            append("var $name")
            value?.let {
                append(" = $value")
            }
            appendLine()
        }
    }

    protected fun assignment(left: String, right: String) {
        stringBuilder.appendLine("$left = $right")
    }

    protected fun ifCondition(condition: String, body: StringBuilder.() -> Unit) {
        stringBuilder.apply {
            appendLine("if ($condition):")
            StringBuilder().apply(body).toString().prependIndent("\t").lines().forEach(::appendLine)
        }
    }

    protected fun statement(statement: String) {
        stringBuilder.appendLine(statement)
    }

    protected fun function(name: String, vararg parameterNames: String, body: StringBuilder.() -> Unit) {
        stringBuilder.apply {
            appendLine("func $name(${parameterNames.joinToString(", ")}):")
            StringBuilder().apply(body).toString().prependIndent("\t").lines().forEach(::appendLine)
        }
    }

    companion object {
        lateinit var scriptsDirectory: File

        private val camelCaseSplitterRegex = Regex("([A-Z]+(?=[A-Z][a-z]))|([A-Z]*[a-z\\d\$]*)")

        fun camelCaseToSnakeCase(camelCaseString: String) = camelCaseSplitterRegex
            .findAll(camelCaseString)
            .takeWhile { it.value.isNotBlank() }
            .joinToString(separator = "_") { it.value.lowercase() }
    }
}