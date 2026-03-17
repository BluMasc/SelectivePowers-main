package net.blumasc.selectivepowers.particles.custom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.blumasc.selectivepowers.particles.SelectivePowersParticles;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;

public class WispParticleOption implements ParticleOptions {
    public static final MapCodec<WispParticleOption> CODEC = RecordCodecBuilder.mapCodec(instance ->
        instance.group(
            Codec.FLOAT.fieldOf("rStart").forGetter(o -> o.rStart),
            Codec.FLOAT.fieldOf("gStart").forGetter(o -> o.gStart),
            Codec.FLOAT.fieldOf("bStart").forGetter(o -> o.bStart),
            Codec.FLOAT.fieldOf("rEnd").forGetter(o -> o.rEnd),
            Codec.FLOAT.fieldOf("gEnd").forGetter(o -> o.gEnd),
            Codec.FLOAT.fieldOf("bEnd").forGetter(o -> o.bEnd)
        ).apply(instance, WispParticleOption::new)
    );

    public final float rStart, gStart, bStart, rEnd, gEnd, bEnd;

    public WispParticleOption(float rStart, float gStart, float bStart,
                               float rEnd, float gEnd, float bEnd) {
        this.rStart = rStart; this.gStart = gStart; this.bStart = bStart;
        this.rEnd = rEnd;     this.gEnd = gEnd;     this.bEnd = bEnd;
    }

    @Override
    public ParticleType<?> getType() {
        return SelectivePowersParticles.WISP.get(); // point to your registered type
    }
}