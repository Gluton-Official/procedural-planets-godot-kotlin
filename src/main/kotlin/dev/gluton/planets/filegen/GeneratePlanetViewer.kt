package dev.gluton.planets.filegen

import dev.gluton.planets.Planet
import godot.Node
import godot.annotation.Export
import godot.annotation.RegisterProperty
import godot.core.Color
import godot.core.Vector3
import godot.global.GD
import kotlin.reflect.KVisibility
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation

object GeneratePlanetViewer : ScriptGenerator("PlanetViewer.gd") {
    override fun generateData() {
        val planetReference = Planet()

        tool()
        extends<Node>()
        line()
        val exportedProperties = Planet::class.declaredMemberProperties.filter {
            it.hasAnnotation<Export>() && it.hasAnnotation<RegisterProperty>()
        }
        exportedProperties.forEach { property ->
            export(
                property.name,
                property.get(planetReference),
                onUpdateValue = "updatePlanet()",
                godotAnnotations = property.annotations.filter {
                    it.annotationClass.qualifiedName?.startsWith("godot.annotation") ?: false
                }
            )
        }
        declaration("root")
        declaration("planet")
        line()
        function("updatePlanet") {
            appendLine("""
               |planet = dev_gluton_planets_Planet.new()
               |planet.name = "Planet"
            ${exportedProperties.joinToString("\n") { 
                val name = camelCaseToSnakeCase(it.name)
                "planet.$name = $name"
            }.prependIndent("|")}
               |
               |if (root):
               |${"\t"}if (root.has_node("Planet")):
               |${"\t\t"}root.remove_child(root.get_node("Planet"))
               |
               |${"\t"}root.add_child(planet)
               |${"\t"}planet.set_owner(root)
            """.trimMargin())
        }
        function("_ready") {
            appendLine("""
               |root = get_tree().edited_scene_root
               |
               |updatePlanet()
            """.trimMargin())
        }
    }
}