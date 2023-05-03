package addons.planet_plugin

import godot.EditorPlugin
import godot.annotation.RegisterClass
import godot.annotation.RegisterFunction
import godot.annotation.Tool
import godot.global.GD

@Tool
@RegisterClass
class PlanetPlugin : EditorPlugin() {

	private lateinit var plugin: PlanetInspectorPlugin

	@RegisterFunction
	override fun _enterTree() {
		GD.print("plugin!")
		plugin = PlanetInspectorPlugin()
		addInspectorPlugin(plugin)
	}

	@RegisterFunction
	override fun _exitTree() {
		removeInspectorPlugin(plugin)
	}
}
