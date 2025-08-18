#version 150
#define HUNGER_PIXELATE

uniform sampler2D DiffuseSampler;
uniform float RenderTime;
uniform float Percent;

in vec2 texCoord;

out vec4 fragColor;

const int ditherMatrix[16] = int[](
1, 9, 3, 11,
13, 5, 15, 7,
4, 12, 2, 10,
16, 8, 14, 6
);

// http://alex-charlton.com/posts/Dithering_on_the_GPU/
float index() {
    int x = int(mod(gl_FragCoord.x, 4));
    int y = int(mod(gl_FragCoord.y, 4));
    return ditherMatrix[(x + y * 4)] / 16.0;
}

float dither(float color) {
    float closestColor = (color < 0.5) ? 0 : 1;
    float secondClosest = 1 - closestColor;
    float index = index();
    float dist = abs(closestColor - color);
    return (dist < index) ? closestColor : secondClosest;
}

#ifdef HUNGER_PIXELATE
vec2 pixelate(vec2 coords, float size) {
    return floor(coords / size) * size;
}
#endif

void main() {
    #ifdef HUNGER_PIXELATE
    vec2 pos = pixelate(texCoord, 0.01) - 0.5;
    #else
    vec2 pos = texCoord - 0.5;
    #endif
    float scroll = RenderTime / 8.0;
    // add wobble
    float xModifier = sin((scroll + pos.y) * 32.0) / 16.0;
    float yModifier = cos((scroll + pos.x) * 16.0) / 16.0;
    pos.x = pos.x + ((texCoord.x < 0.5) ? xModifier : -xModifier);
    pos.y = pos.y + ((texCoord.y < 0.5) ? yModifier : -yModifier);
    // generate gradient with wobble
    float gradient = smoothstep(0.2, Percent, clamp(length(pos), 0.0, 1.0));
    float ditherGradient = dither(gradient);
    vec4 color = mix(texture2D(DiffuseSampler, texCoord), vec4(0.0), ditherGradient);
    fragColor = vec4(color.rgb, 1.0);
}