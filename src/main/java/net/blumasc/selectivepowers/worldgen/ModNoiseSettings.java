package net.blumasc.selectivepowers.worldgen;

import net.blumasc.selectivepowers.SelectivePowers;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.NoiseData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.placement.CaveSurface;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

import java.util.List;

public class ModNoiseSettings {
    public static final ResourceKey<NoiseGeneratorSettings> LUNAR_DIM_NOISE =
            ResourceKey.create(
                    Registries.NOISE_SETTINGS,
                    ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "lunar_dimension")
            );
    public static void bootstrap(BootstrapContext<NoiseGeneratorSettings> ctx) {

    }
}
