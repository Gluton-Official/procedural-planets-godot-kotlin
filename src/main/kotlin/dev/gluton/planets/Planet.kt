package dev.gluton.planets

import godot.GDScript
import godot.Gradient
import godot.GradientTexture2D
import godot.MeshInstance
import godot.MeshTexture
import godot.ResourceLoader
import godot.SceneTree
import godot.Shader
import godot.ShaderMaterial
import godot.Spatial
import godot.SpatialMaterial
import godot.annotation.Export
import godot.annotation.IntRange
import godot.annotation.RegisterClass
import godot.annotation.RegisterFunction
import godot.annotation.RegisterProperty
import godot.core.Color
import godot.core.PoolColorArray
import godot.core.Vector3
import godot.global.GD

val directions = arrayOf(Vector3.UP, Vector3.DOWN, Vector3.LEFT, Vector3.RIGHT, Vector3.FORWARD, Vector3.BACK)

@RegisterClass
class Planet : Spatial() {
	private var meshInstances = arrayOf<MeshInstance>()
	private var terrainFaces = arrayOf<TerrainFace>()

	private lateinit var shapeGenerator: ShapeGenerator
	private lateinit var shaderMaterial: ShaderMaterial
	private lateinit var gradient: Gradient

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
		shaderMaterial = ShaderMaterial().apply {
			setShaderParam("roughness", 0.75)
			setShaderParam("albedo", color)
			setShaderParam("metallic", 0.1)
			setShaderParam("specular", 0.5)
			shader = GD.load("res://shaders/PlanetShader.shader", "Shader")!!
		}
		gradient = Gradient().apply {
//			colors = PoolColorArray().apply {
//				append(Color(84 / 255f, 134 / 255f, 219 / 255f)) // water
//				append(Color(164 / 255f, 174 / 255f, 91 / 255f)) // sand
//				append(Color(112 / 255f, 183 / 255f, 19 / 255f)) // plains
//				append(Color(76 / 255f, 102 / 255f, 26 / 255f)) // hills
//				append(Color(145 / 255f, 90 / 255f, 42 / 255f)) // rocky mountain bottoms
//				append(Color(125 / 255f, 81 / 255f, 45 / 255f)) // rocky mountain tops
//				append(Color.white) // snow peaks
//			}
			addPoint(0.0, Color(84 / 255f, 134 / 255f, 219 / 255f)) // water
			addPoint(0.05, Color(164 / 255f, 174 / 255f, 91 / 255f)) // sand
			addPoint(0.1, Color(112 / 255f, 183 / 255f, 19 / 255f)) // plains
			addPoint(0.3, Color(76 / 255f, 102 / 255f, 26 / 255f)) // hills
			addPoint(0.6, Color(145 / 255f, 90 / 255f, 42 / 255f)) // rocky mountain bottoms
			addPoint(0.9, Color(125 / 255f, 81 / 255f, 45 / 255f)) // rocky mountain tops
			addPoint(1.0, Color.white) // snow peaks
		}

		initialize()
		generateMesh()
	}

	private fun initialize() {
		meshInstances = Array(6) {
			val meshInstance = MeshInstance().apply {
//				materialOverride = SpatialMaterial().apply {
//					flagsUnshaded = false
//					albedoColor = color
//					roughness = 0.75
//					metallic = 0.1
//				}
				materialOverride = shaderMaterial
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

		shaderMaterial.setShaderParam("min_elevation", shapeGenerator.minElevation)
		shaderMaterial.setShaderParam("max_elevation", shapeGenerator.maxElevation)
		shaderMaterial.setShaderParam("elevation_texture", GradientTexture2D().apply {
			gradient = this@Planet.gradient
			height = 1
			width = 50
			fill = GradientTexture2D.FILL_LINEAR
		})
	}
}
