package dev.gluton.planets

import godot.core.Vector3
import godot.global.GD

class ShapeGenerator(
    private val radius: Float,
    private val noiseFilter: NoiseFilter,
    private val enableNoiseFilterOverlay: Boolean,
    private val noiseFilterOverlay: NoiseFilter,
) {
    fun calculatePointOnPlanet(pointOnUnitSphere: Vector3): Vector3 {
        var elevation = noiseFilter.evaluate(pointOnUnitSphere)
        if (enableNoiseFilterOverlay) {
            elevation += noiseFilterOverlay.evaluate(pointOnUnitSphere) * elevation
        }
        return pointOnUnitSphere * radius * (1 + elevation)
    }
}