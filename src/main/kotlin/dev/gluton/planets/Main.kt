package dev.gluton.planets

import godot.GlobalConstants.KEY_TAB
import godot.InputEvent
import godot.InputEventKey
import godot.Spatial
import godot.Viewport.DebugDraw
import godot.VisualServer
import godot.annotation.RegisterClass
import godot.annotation.RegisterFunction
import godot.global.GD

@RegisterClass
class Main : Spatial() {

	@RegisterFunction
	override fun _init() {
		VisualServer.setDebugGenerateWireframes(true)
	}

	@RegisterFunction
	override fun _input(event: InputEvent) {
		if (event is InputEventKey && event.scancode == KEY_TAB && event.pressed) {
			getViewport()?.apply {
				debugDraw = (debugDraw + 1) % DebugDraw.values().size
				GD.print(DebugDraw.from(debugDraw))
			}
		}
	}
}
