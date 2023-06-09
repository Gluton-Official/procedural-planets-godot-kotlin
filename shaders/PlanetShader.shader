shader_type spatial;
render_mode async_visible, blend_mix, depth_draw_opaque, cull_back, diffuse_burley, specular_schlick_ggx;
uniform vec4 albedo : hint_color;
uniform sampler2D texture_albedo : hint_albedo;
uniform float specular;
uniform float metallic;
uniform float roughness : hint_range(0,1);
uniform float point_size : hint_range(0,128);
uniform sampler2D texture_normal : hint_normal;
uniform float normal_scale : hint_range(-16,16);
uniform vec3 uv1_scale;
uniform vec3 uv1_offset;
uniform vec3 uv2_scale;
uniform vec3 uv2_offset;

uniform float min_elevation;
uniform float max_elevation;
uniform sampler2D elevation_texture;

varying vec3 position;

void vertex() {
	position = VERTEX;
	UV = UV * uv1_scale.xy + uv1_offset.xy;
}

void fragment() {
	vec2 base_uv = UV;
	vec4 albedo_tex = texture(texture_albedo, base_uv);
// 	ALBEDO = albedo.rgb * albedo_tex.rgb;
	ALBEDO = texture(elevation_texture, vec2(1.0 - (max_elevation - length(position)) / (max_elevation - min_elevation), 0.5)).rgb;
	METALLIC = metallic;
	ROUGHNESS = roughness;
	SPECULAR = specular;
	NORMALMAP = texture(texture_normal, base_uv).rgb;
	NORMALMAP_DEPTH = normal_scale;
}
