package net.blumasc.selectivepowers.particles;

import net.blumasc.selectivepowers.SelectivePowers;
import net.blumasc.selectivepowers.particles.custom.WispParticleType;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class SelectivePowersParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, SelectivePowers.MODID);

    public static final DeferredHolder<ParticleType<?>, WispParticleType> WISP =
            PARTICLE_TYPES.register("wisp", WispParticleType::new);

    public static void register(IEventBus bus){
        PARTICLE_TYPES.register(bus);
    }
}
