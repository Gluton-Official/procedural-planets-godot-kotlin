@file:Suppress("ReplaceWithOperatorAssignment")

package dev.gluton.planets

import godot.Input
import godot.Camera
import godot.GlobalConstants.BUTTON_MASK_MIDDLE
import godot.GlobalConstants.BUTTON_MASK_RIGHT
import godot.GlobalConstants.BUTTON_WHEEL_DOWN
import godot.GlobalConstants.BUTTON_WHEEL_UP
import godot.InputEvent
import godot.InputEventMouseButton
import godot.InputEventMouseMotion
import godot.Spatial
import godot.annotation.Export
import godot.annotation.RegisterClass
import godot.annotation.RegisterFunction
import godot.annotation.RegisterProperty
import godot.extensions.getNodeAs

@RegisterClass
class OrbitalCamera : Spatial() {
    @Export
    @RegisterProperty
    var panSensitivity = 0.1f

    @Export
    @RegisterProperty
    var zoomSensitivity = 0.25f

    @Export
    @RegisterProperty
    var distance = 5.0

    private var pitch = 0.0
    private var yaw = 0.0

    private var camera: Camera? = null

    @RegisterFunction
    override fun _ready() {
        Input.mouseMode = Input.MOUSE_MODE_CAPTURED

        camera = getNodeAs("%Camera")
    }

    @RegisterFunction
    override fun _input(event: InputEvent) {
        if (event is InputEventMouseMotion) {
            when (event.buttonMask) {
                BUTTON_MASK_MIDDLE, BUTTON_MASK_RIGHT -> {
                    pitch += event.relative.x * panSensitivity
                    yaw = (yaw - event.relative.y * panSensitivity).coerceIn(-90.0, 90.0)
                }
            }
        }
        if (event is InputEventMouseButton && event.pressed) {
            when (event.buttonIndex) {
                BUTTON_WHEEL_UP -> distance = (distance - zoomSensitivity).coerceAtLeast(0.0)
                BUTTON_WHEEL_DOWN -> distance += zoomSensitivity
            }
        }
    }

    @RegisterFunction
    override fun _physicsProcess(delta: Double) {
        camera?.translation {
            z = distance
        }

        rotationDegrees {
            x = yaw
            y = pitch
        }
    }
}