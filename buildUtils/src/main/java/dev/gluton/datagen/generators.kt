package dev.gluton.planets.datagen

import java.io.File

fun generateScripts(scriptsDirectory: File) {
    ScriptGenerator.scriptsDirectory = scriptsDirectory
    GeneratePlanetViewer()
}

abstract class ScriptGenerator(fileName: String) {
    private val scriptFile = File(scriptsDirectory, fileName)

    protected abstract fun generateData(): String

    operator fun invoke() {
        println("Generating ${scriptFile.name} at $scriptsDirectory")
//        scriptFile.writeText(generateData())
//        scriptFile.createNewFile()
    }

    companion object {
        lateinit var scriptsDirectory: File
    }
}
