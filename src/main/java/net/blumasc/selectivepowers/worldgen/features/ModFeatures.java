package net.blumasc.selectivepowers.worldgen.features;

import net.blumasc.selectivepowers.SelectivePowers;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES =
            DeferredRegister.create(Registries.FEATURE, SelectivePowers.MODID);

    public static final Supplier<Feature<NoneFeatureConfiguration>> CROW_NEST =
            FEATURES.register("crows_nest", NestFeature::new);

    public static void register(IEventBus bus){
        FEATURES.register(bus);
    }
}

