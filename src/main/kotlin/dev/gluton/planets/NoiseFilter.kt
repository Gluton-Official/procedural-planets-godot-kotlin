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
    private val minValue: Double,
    private val strength: Float,
) {
    private val noise = OpenSimplexNoise().apply {
        octaves = layers
        period = roughness
        persistence = layerContribution
        lacunarity = layerRoughnessMultiplier
    }

    fun evaluate(point: Vector3): Float {
        val noiseAtPoint = noise.getNoise3dv(point + center)
        val scaledNoise = (noiseAtPoint + 1) / 2 * amplitude
        val noiseValue = (scaledNoise - minValue).coerceAtLeast(0.0)
        return noiseValue.toFloat() * strength
    }
}