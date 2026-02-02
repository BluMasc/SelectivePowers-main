#version 150

uniform sampler2D DiffuseSampler;

in vec2 texCoord;
uniform vec2 InSize;

uniform vec3 Gray;
uniform vec3 RedMatrix;
uniform vec3 GreenMatrix;
uniform vec3 BlueMatrix;
uniform vec3 Offset;
uniform vec3 ColorScale;
uniform float Saturation;
uniform float Darkness;

out vec4 fragColor;

vec3 rgb2hsv(vec3 c) {
    vec4 K = vec4(0.0, -1.0 / 3.0, 2.0 / 3.0, -1.0);
    vec4 p = c.g < c.b ? vec4(c.bg, K.wz) : vec4(c.gb, K.xy);
    vec4 q = c.r < p.x ? vec4(p.xyw, c.r) : vec4(c.r, p.yzx);
    float d = q.x - min(q.w, q.y);
    float e = 1e-10;
    return vec3(abs(q.z + (q.w - q.y) / (6.0*d + e)), d / (q.x + e), q.x);
}

float isYellow(vec3 rgb) {
    vec3 hsv = rgb2hsv(rgb);
    return step(0.12, hsv.x) * step(hsv.x, 0.20)
         * step(0.35, hsv.y) * step(0.35, hsv.z);
}

vec3 gaussianBlur(vec2 uv, float strength) {
    if (strength <= 0.001)
        return texture(DiffuseSampler, uv).rgb;

    vec2 texel = 1.0 / InSize;
    float radius = mix(0.0, 6.0, strength);

    vec3 color = vec3(0.0);
    float total = 0.0;

    for (int x = -3; x <= 3; x++) {
        for (int y = -3; y <= 3; y++) {
            float weight = exp(-(x*x + y*y) / (2.0 * radius * radius + 0.001));
            vec2 offset = vec2(x, y) * texel * strength;
            color += texture(DiffuseSampler, uv + offset).rgb * weight;
            total += weight;
        }
    }

    return color / total;
}

void main() {


    float dist = distance(texCoord, vec2(0.5));
    float blurStrength = smoothstep(0.02, 0.5, dist);
    blurStrength *= 3;

    vec3 InColor = gaussianBlur(texCoord, blurStrength);


    float RedValue   = dot(InColor, RedMatrix);
    float GreenValue = dot(InColor, GreenMatrix);
    float BlueValue  = dot(InColor, BlueMatrix);
    vec3 OutColor = vec3(RedValue, GreenValue, BlueValue);

    OutColor = (OutColor * ColorScale) + Offset;

    float yellowMask = isYellow(OutColor);
    float Luma = dot(OutColor, Gray);

    OutColor = mix(vec3(Luma), OutColor, yellowMask);
    OutColor *= 1.0 - Darkness;

    fragColor = vec4(OutColor, 1.0);
}
