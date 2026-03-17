package net.blumasc.selectivepowers.particles.custom;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;

public class WispParticle extends TextureSheetParticle {
    private final float rStart, gStart, bStart;
    private final float rEnd, gEnd, bEnd;

    protected WispParticle(ClientLevel level, double x, double y, double z,
                           double moveX, double moveY, double moveZ,
                           float rStart, float gStart, float bStart,
                           float rEnd, float gEnd, float bEnd) {
        super(level, x, y, z);
        this.rStart = rStart; this.gStart = gStart; this.bStart = bStart;
        this.rEnd = rEnd;   this.gEnd = gEnd;   this.bEnd = bEnd;

        this.xd = moveX;
        this.yd = moveY;
        this.zd = moveZ;

        this.lifetime = 40;
        this.hasPhysics = false; // enableNoClip equivalent

        // Initial color
        this.rCol = rStart;
        this.gCol = gStart;
        this.bCol = bStart;
        this.alpha = 0.75f;
    }

    @Override
    public void tick() {
        super.tick();
        float t = (float) age / (float) lifetime;

        // Lerp color from start to end over lifetime
        this.rCol = lerp(t, rStart, rEnd);
        this.gCol = lerp(t, gStart, gEnd);
        this.bCol = lerp(t, bStart, bEnd);

        // Fade alpha from 0.75 to 0.25
        this.alpha = lerp(t, 0.75f, 0.25f);
    }

    private float lerp(float t, float a, float b) {
        return a + t * (b - a);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }
}