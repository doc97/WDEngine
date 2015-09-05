#version 400

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 texcoords;

out vec2 v_texcoords;

uniform mat4 MVP;

void main() {
	gl_Position = vec4(position, 1.0);
	v_texcoords = texcoords;
}

//@ // Shader split

#version 400

in vec2 v_texcoords;

out vec4 frag_colour;

uniform sampler2D tex;

void main() {
	frag_colour = texture(tex, v_texcoords);
}