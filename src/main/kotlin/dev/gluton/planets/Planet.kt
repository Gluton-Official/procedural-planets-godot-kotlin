package dev.gluton.planets

import godot.MeshInstance
import godot.Spatial
import godot.SpatialMaterial
import godot.annotation.Export
import godot.annotation.IntRange
import godot.annotation.RegisterClass
import godot.annotation.RegisterFunction
import godot.annotation.RegisterProperty
import godot.core.Color
import godot.core.Vector3

val directions = arrayOf(Vector3.UP, Vector3.DOWN, Vector3.LEFT, Vector3.RIGHT, Vector3.FORWARD, Vector3.BACK)

@RegisterClass
class Planet : Spatial() {
	private var meshInstances = arrayOf<MeshInstance>()
	private var terrainFaces = arrayOf<TerrainFace>()

	private lateinit var shapeGenerator: ShapeGenerator

	@Export
	@RegisterProperty
	@IntRange(2, 128)
	var resolution = 30

	@Export
	@RegisterProperty
	var radius = 3f

	@Export
	@RegisterProperty
	var amplitude = 1.25f

	@Export
	@RegisterProperty
	var roughness = 0.5f

	@Export
	@RegisterProperty
	var noiseCenter = Vector3(1, 1, 1)

	@Export
	@RegisterProperty
	@IntRange(1, 9)
	var noiseLayers = 6

	@Export
	@RegisterProperty
	var noiseLayerContribution = 0.42f

	@Export
	@RegisterProperty
	var noiseLayerRoughnessMultiplier = 2.3f

	@Export
	@RegisterProperty
	var minValue = 0.67f

	@Export
	@RegisterProperty
	var strength = 0.275f

	@Export
	@RegisterProperty
	var enableNoiseFilterOverlay = true

	@Export
	@RegisterProperty
	var overlayAmplitude = 0.5f

	@Export
	@RegisterProperty
	var overlayRoughness = 0.075f

	@Export
	@RegisterProperty
	var overlayNoiseCenter = Vector3(1, 1, 1)

	@Export
	@RegisterProperty
	@IntRange(1, 9)
	var overlayNoiseLayers = 6

	@Export
	@RegisterProperty
	var overlayNoiseLayerContribution = 5.15f

	@Export
	@RegisterProperty
	var overlayNoiseLayerRoughnessMultiplier = 1f

	@Export
	@RegisterProperty
	var overlayMinValue = 0.1f

	@Export
	@RegisterProperty
	var overlayStrength = 3.8f

	@Export
	@RegisterProperty
	var color = Color.white

	@RegisterFunction
	override fun _ready() {
		shapeGenerator = ShapeGenerator(
			radius,
			NoiseFilter(
				amplitude,
				roughness.toDouble(),
				noiseCenter,
				noiseLayers.toLong(),
				noiseLayerContribution.toDouble(),
				noiseLayerRoughnessMultiplier.toDouble(),
				minValue.toDouble(),
				strength,
			),
			enableNoiseFilterOverlay,
			noiseFilterOverlay = NoiseFilter(
				overlayAmplitude,
				overlayRoughness.toDouble(),
				overlayNoiseCenter,
				overlayNoiseLayers.toLong(),
				overlayNoiseLayerContribution.toDouble(),
				overlayNoiseLayerRoughnessMultiplier.toDouble(),
				overlayMinValue.toDouble(),
				overlayStrength,
			),
		)

		initialize()
		generateMesh()
	}

	private fun initialize() {
		meshInstances = Array(6) {
			val meshInstance = MeshInstance().apply {
				materialOverride = SpatialMaterial().apply {
					flagsUnshaded = false
					albedoColor = color
					roughness = 0.5
					metallic = 0.5
				}
				visible
			}
			addChild(meshInstance)
			meshInstance
		}
		terrainFaces = Array(6) {
			TerrainFace(shapeGenerator, meshInstances[it], resolution, directions[it])
		}
	}

	private fun generateMesh() {
		terrainFaces.forEach(TerrainFace::constructMesh)
	}
}
