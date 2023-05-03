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
	var resolution = 75

	@Export
	@RegisterProperty
	var radius = 1.4f

	@Export
	@RegisterProperty
	var amplitude = 1.75f

	@Export
	@RegisterProperty
	var roughness = 0.75f

	@Export
	@RegisterProperty
	var noiseCenter = Vector3(1, 1, 1)

	@Export
	@RegisterProperty
	@IntRange(1, 9)
	var noiseLayers = 8

	@Export
	@RegisterProperty
	var noiseLayerContribution = 0.67f

	@Export
	@RegisterProperty
	var noiseLayerRoughnessMultiplier = 1.67f

	@Export
	@RegisterProperty
	var minValue = 0.15f

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
			)
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
