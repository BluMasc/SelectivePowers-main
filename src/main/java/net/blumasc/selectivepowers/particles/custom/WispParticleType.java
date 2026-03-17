package net.blumasc.selectivepowers.particles.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class WispParticleType extends ParticleType<WispParticleOption> {
    public WispParticleType() {
        super(false);
    }

    @Override
    public MapCodec<WispParticleOption> codec() {
        return WispParticleOption.CODEC;
    }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, WispParticleOption> streamCodec() {
        return null;
    }
}