#version 400

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 texcoords;
layout(location = 2) in vec4 color;

out vec2 v_texcoords;
out vec4 v_color;
 
void main()
{
    gl_Position = vec4(position, 1.0);
	v_texcoords = texcoords;
	v_color = color;
}

//@ // Shader split

#version 400

in vec2 v_texcoords;
in vec4 v_color;

out vec4 frag_color;

uniform sampler2D tex;
uniform float threshold;

void main()
{
	vec4 color = texture(tex, v_texcoords) * v_color;
	
	if((color.r + color.g + color.b + color.a) / 4.0 < threshold) {
		color = vec4(0, 0, 0, 0.5f);
	}
			
	frag_color = color;
}