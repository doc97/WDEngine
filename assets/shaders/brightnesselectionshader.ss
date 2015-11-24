#version 400

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 texcoords;
layout(location = 2) in vec4 color;

out vec2 v_texcoords;
 
void main()
{
    gl_Position = vec4(position, 1.0);
	v_texcoords = texcoords;
}

//@ // Shader split

#version 400

in vec2 v_texcoords;

out vec4 frag_color;

uniform sampler2D tex;
uniform float threshold;

void main()
{
	vec4 color = texture(tex, v_texcoords);
	
	if((color.r + color.g + color.b) * color.a / 3.0 < threshold) {
		color = vec4(0, 0, 0, 1.0);
	}
			
	frag_color = color;
}