tool
extends Node

export(int, 2, 128) var resolution = 30 setget set_resolution
export(float) var radius = 1.0 setget set_radius
export(float) var amplitude = 1.75 setget set_amplitude
export(float) var roughness = 0.75 setget set_roughness
export(Vector3) var noise_center = Vector3(1, 1, 1) setget set_noise_center
export(int, 1, 9) var noise_layers = 1 setget set_noise_layers
export(float) var noise_layer_contribution = 0.5 setget set_noise_layer_contribution
export(float) var noise_layer_roughness_multiplier = 2.0 setget set_noise_layer_roughness_multiplier
export var color: Color = Color.white setget set_color

var root
var planet

func set_resolution(new_resolution):
	if (resolution != new_resolution):
		resolution = new_resolution
		updatePlanet()

func set_radius(new_radius):
	if (radius != new_radius):
		radius = new_radius
		updatePlanet()

func set_amplitude(new_amplitude):
	if (amplitude != new_amplitude):
		amplitude = new_amplitude
		updatePlanet()

func set_roughness(new_roughness):
	if (roughness != new_roughness):
		roughness = new_roughness
		updatePlanet()

func set_noise_center(new_noise_center):
	if (noise_center != new_noise_center):
		noise_center = new_noise_center
		updatePlanet()

func set_noise_layers(new_noise_layers):
	if (noise_layers != new_noise_layers):
		noise_layers = new_noise_layers
		updatePlanet()

func set_noise_layer_contribution(new_noise_layer_contribution):
	if (noise_layer_contribution != new_noise_layer_contribution):
		noise_layer_contribution = new_noise_layer_contribution
		updatePlanet()

func set_noise_layer_roughness_multiplier(new_noise_layer_roughness_multiplier):
	if (noise_layer_roughness_multiplier != new_noise_layer_roughness_multiplier):
		noise_layer_roughness_multiplier = new_noise_layer_roughness_multiplier
		updatePlanet()

func set_color(new_color):
	if (color != new_color):
		color = new_color
		updatePlanet()

func updatePlanet():
	planet = dev_gluton_planets_Planet.new()
	planet.name = "Planet"
	planet.resolution = resolution
	planet.radius = radius
	planet.amplitude = amplitude
	planet.roughness = roughness
	planet.noise_center = noise_center
	planet.noise_layers = noise_layers
	planet.noise_layer_contribution = noise_layer_contribution
	planet.noise_layer_roughness_multiplier = noise_layer_roughness_multiplier
	planet.color = color

	if (root):
		if (root.has_node("Planet")):
			root.remove_child(root.get_node("Planet"))

		root.add_child(planet)
		planet.set_owner(root)

func _ready():
	root = get_tree().edited_scene_root

	updatePlanet()
