package dev.gluton.planets

import godot.OpenSimplexNoise
import godot.core.Vector3

class NoiseFilter(
    private val amplitude: Float,
    private val roughness: Double,
    private val center: Vector3,
    private val layers: Long,
    private val layerContribution: Double,
    private val layerRoughnessMultiplier: Double,
) {
    private val noise = OpenSimplexNoise().apply {
        octaves = layers
        period = this@NoiseFilter.roughness
        persistence = layerContribution
        lacunarity = layerRoughnessMultiplier
    }

    fun evaluate(point: Vector3): Float {
        val noiseValue = noise.getNoise3dv(point + center) * amplitude
        return (noiseValue.toFloat() + 1) / 2
    }
}