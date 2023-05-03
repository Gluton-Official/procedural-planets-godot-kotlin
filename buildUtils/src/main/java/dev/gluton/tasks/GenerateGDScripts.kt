package dev.gluton.planets.gradle.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import dev.gluton.planets.datagen.generateScripts
import org.gradle.api.provider.Property
import org.gradle.api.tasks.InputDirectory
import java.io.File

abstract class GenerateGDScripts : DefaultTask() {

    init {
        group = "dev.gluton"
        description = "Generates .gd script files"
    }

    @get:InputDirectory
    abstract val scriptsDirectory: Property<File>

    @TaskAction
    fun run() {
        generateScripts(scriptsDirectory.get())
    }
}