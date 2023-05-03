tool
extends Node

func _ready():
	var generators = dev_gluton_planets_filegen_Generators.new()
	generators.generate_scripts(ProjectSettings.globalize_path("res://scripts"))
