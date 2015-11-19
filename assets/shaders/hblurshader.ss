#version 400

attribute vec4 a_position;
attribute vec2 a_texCoord;
 
varying vec2 v_texCoord;
varying vec2 v_blurTexCoords[14];
 
uniform float blurFactor;
 
void main()
{
    gl_Position = a_position;
    v_texCoord = a_texCoord;
    v_blurTexCoords[ 0] = v_texCoord + vec2(-0.028 * blurFactor, 0.0);
    v_blurTexCoords[ 1] = v_texCoord + vec2(-0.024 * blurFactor, 0.0);
    v_blurTexCoords[ 2] = v_texCoord + vec2(-0.020 * blurFactor, 0.0);
    v_blurTexCoords[ 3] = v_texCoord + vec2(-0.016 * blurFactor, 0.0);
    v_blurTexCoords[ 4] = v_texCoord + vec2(-0.012 * blurFactor, 0.0);
    v_blurTexCoords[ 5] = v_texCoord + vec2(-0.008 * blurFactor, 0.0);
    v_blurTexCoords[ 6] = v_texCoord + vec2(-0.004 * blurFactor, 0.0);
    v_blurTexCoords[ 7] = v_texCoord + vec2( 0.004 * blurFactor, 0.0);
    v_blurTexCoords[ 8] = v_texCoord + vec2( 0.008 * blurFactor, 0.0);
    v_blurTexCoords[ 9] = v_texCoord + vec2( 0.012 * blurFactor, 0.0);
    v_blurTexCoords[10] = v_texCoord + vec2( 0.016 * blurFactor, 0.0);
    v_blurTexCoords[11] = v_texCoord + vec2( 0.020 * blurFactor, 0.0);
    v_blurTexCoords[12] = v_texCoord + vec2( 0.024 * blurFactor, 0.0);
    v_blurTexCoords[13] = v_texCoord + vec2( 0.028 * blurFactor, 0.0);
}

//@ // Shader split

#version 400

precision mediump float;
 
uniform sampler2D s_texture;
 
varying vec2 v_texCoord;
varying vec2 v_blurTexCoords[14];

void main()
{
    gl_FragColor = vec4(0.0);
    gl_FragColor += texture2D(s_texture, v_blurTexCoords[ 0])*0.0044299121055113265;
    gl_FragColor += texture2D(s_texture, v_blurTexCoords[ 1])*0.00895781211794;
    gl_FragColor += texture2D(s_texture, v_blurTexCoords[ 2])*0.0215963866053;
    gl_FragColor += texture2D(s_texture, v_blurTexCoords[ 3])*0.0443683338718;
    gl_FragColor += texture2D(s_texture, v_blurTexCoords[ 4])*0.0776744219933;
    gl_FragColor += texture2D(s_texture, v_blurTexCoords[ 5])*0.115876621105;
    gl_FragColor += texture2D(s_texture, v_blurTexCoords[ 6])*0.147308056121;
    gl_FragColor += texture2D(s_texture, v_texCoord         )*0.159576912161;
    gl_FragColor += texture2D(s_texture, v_blurTexCoords[ 7])*0.147308056121;
    gl_FragColor += texture2D(s_texture, v_blurTexCoords[ 8])*0.115876621105;
    gl_FragColor += texture2D(s_texture, v_blurTexCoords[ 9])*0.0776744219933;
    gl_FragColor += texture2D(s_texture, v_blurTexCoords[10])*0.0443683338718;
    gl_FragColor += texture2D(s_texture, v_blurTexCoords[11])*0.0215963866053;
    gl_FragColor += texture2D(s_texture, v_blurTexCoords[12])*0.00895781211794;
    gl_FragColor += texture2D(s_texture, v_blurTexCoords[13])*0.0044299121055113265;
}