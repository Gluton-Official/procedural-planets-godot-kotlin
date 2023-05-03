package dev.gluton.planets

import godot.core.Vector3
import godot.global.GD
import kotlin.math.max

class ShapeGenerator(
    private val radius: Float,
    private val noiseFilter: NoiseFilter,
    private val enableNoiseFilterOverlay: Boolean,
    private val noiseFilterOverlay: NoiseFilter,
) {
    var minElevation: Float = Float.MAX_VALUE
        private set(value) { if (value < field) field = value }
    var maxElevation: Float = Float.MIN_VALUE
        private set(value) { if (value > field) field = value }

    fun calculatePointOnPlanet(pointOnUnitSphere: Vector3): Vector3 {
        var elevation = noiseFilter.evaluate(pointOnUnitSphere)
        if (enableNoiseFilterOverlay) {
            elevation += noiseFilterOverlay.evaluate(pointOnUnitSphere) * elevation
        }
        elevation = radius * (1 + elevation)
        minElevation = elevation
        maxElevation = elevation
        return pointOnUnitSphere * elevation
    }
}