#version 400

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 texcoords;
layout(location = 2) in vec4 color;
layout(location = 3) in int colAppMeth;

out vec2 v_texcoords;
out vec4 v_color;
out vec2 v_colAppMeth;

uniform mat4 MVP;

void main() {
	gl_Position = vec4(position, 1.0);
	v_texcoords = texcoords;
	v_color = color;
	v_colAppMeth = vec2(colAppMeth, 0);
}

//@ // Shader split

#version 400

in vec2 v_texcoords;
in vec4 v_color;
in vec2 v_colAppMeth;


out vec4 frag_color;

uniform sampler2D tex;
uniform int textures;

void main() {
	vec4 texCol = texture(tex, v_texcoords);
	
	vec4 color = vec4(0);
	
	if(textures == 1) {
		if (v_colAppMeth.x == 0){
			color = texCol * v_color * v_color * v_color;
		}
		else if (v_colAppMeth.x == 1){
			color = vec4(texCol.rgb + v_color.rgb, texCol.a);
		}
		else if (v_colAppMeth.x == 2){
			color = vec4(v_color.rgb, texCol.a);
		}
	} else if(textures == 0) {
		color = v_color;
	}
	
	
	
	frag_color = color;
}