package dev.gluton.planets.filegen

import godot.Node
import godot.annotation.RegisterClass
import godot.annotation.RegisterFunction
import godot.global.GD
import java.io.File

@RegisterClass
class Generators : Node() {

    @RegisterFunction
    fun generateScripts(scriptsDirectoryPath: String) {
        ScriptGenerator.scriptsDirectory = File(scriptsDirectoryPath)
        GeneratePlanetViewer()
    }
}
