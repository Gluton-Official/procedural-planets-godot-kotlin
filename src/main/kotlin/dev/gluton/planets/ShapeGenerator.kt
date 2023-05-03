package dev.gluton.planets

import godot.core.Vector3

class ShapeGenerator(
    private val radius: Float,
    private val noiseFilter: NoiseFilter,
) {

    fun calculatePointOnPlanet(pointOnUnitSphere: Vector3): Vector3 {
        val elevation = noiseFilter.evaluate(pointOnUnitSphere)
        return pointOnUnitSphere * radius * (1 + elevation)
    }
}