package addons.planet_plugin

import dev.gluton.planets.Planet
import godot.EditorInspectorPlugin
import godot.Object
import godot.annotation.RegisterFunction

class PlanetInspectorPlugin : EditorInspectorPlugin() {

    @RegisterFunction
    override fun _canHandle(@Suppress("LocalVariableName") _object: Object): Boolean {
        return _object is Planet
    }

    override fun _parseProperty(
        _object: Object,
        type: Long,
        path: String,
        hint: Long,
        hintText: String,
        usage: Long
    ): Boolean {
        return super._parseProperty(_object, type, path, hint, hintText, usage)
    }
}
