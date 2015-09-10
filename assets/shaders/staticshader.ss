#version 400

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 texcoords;
layout(location = 2) in vec4 color;

out vec2 v_texcoords;
out vec4 v_color;

uniform mat4 MVP;

void main() {
	gl_Position = vec4(position, 1.0);
	v_texcoords = texcoords;
	v_color = color;
}

//@ // Shader split

#version 400

in vec2 v_texcoords;
in vec4 v_color;

out vec4 frag_colour;

uniform sampler2D tex;

void main() {
	vec4 texCol = texture(tex, v_texcoords);
	frag_colour = texCol * v_color;
}