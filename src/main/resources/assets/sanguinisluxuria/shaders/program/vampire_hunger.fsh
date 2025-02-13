#version 150

uniform sampler2D DiffuseSampler;
uniform float Time;
uniform float Percent;

in vec2 texCoord;

out vec4 fragColor;

const float PI = radians(180.0);

void main() {
    float time = 2.0 * PI * Time;
    vec2 pos = vec2(texCoord.rg);
    float gradient = clamp(length(pos - 0.5), 0.0, 1.0);
    vec4 color = mix(texture2D(DiffuseSampler, texCoord), vec4(0.0), smoothstep(0.0, Percent, gradient));
    fragColor = vec4(color.rgb, 1.0);
}