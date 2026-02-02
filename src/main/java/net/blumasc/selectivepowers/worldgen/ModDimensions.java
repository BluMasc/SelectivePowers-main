package net.blumasc.selectivepowers.worldgen;

import net.blumasc.selectivepowers.SelectivePowers;
import net.blumasc.selectivepowers.block.SelectivepowersBlocks;
import net.blumasc.selectivepowers.worldgen.biomes.SelectivePowerBiomes;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.FixedBiomeSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseSettings;
import net.minecraft.world.level.levelgen.flat.FlatLayerInfo;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModDimensions{
    public static final ResourceKey<DimensionType> SOLAR_DIM_TYPE =
            ResourceKey.create(Registries.DIMENSION_TYPE, ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "solar_dimension"));

    public static final ResourceKey<LevelStem> SOLAR_DIM =
            ResourceKey.create(Registries.LEVEL_STEM, ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "solar_dimension"));

    public static final ResourceKey<Level> SOLAR_DIM_LEVEL =
            ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "solar_dimension"));

    public static final ResourceKey<DimensionType> LUNAR_DIM_TYPE =
            ResourceKey.create(Registries.DIMENSION_TYPE, ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "lunar_dimension"));

    public static final ResourceKey<LevelStem> LUNAR_DIM =
            ResourceKey.create(Registries.LEVEL_STEM, ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "lunar_dimension"));

    public static final ResourceKey<Level> LUNAR_DIM_LEVEL =
            ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "lunar_dimension"));


    public static void bootstrapDimensionType(BootstrapContext<DimensionType> ctx) {
        ctx.register(SOLAR_DIM_TYPE,
                new DimensionType(
                        OptionalLong.of(12000),
                        true,
                        false,
                        false,
                        true,
                        1.0,
                        false,
                        false,
                        -64,
                        384,
                        384,
                        BlockTags.INFINIBURN_NETHER,
                        BuiltinDimensionTypes.OVERWORLD_EFFECTS,
                        0.0f,
                        new DimensionType.MonsterSettings(
                                false, false, ConstantInt.of(0), 0
                        )
                )
        );
        ctx.register(LUNAR_DIM_TYPE,
                new DimensionType(
                        OptionalLong.of(18000),
                        true,
                        false,
                        false,
                        true,
                        1.0,
                        false,
                        false,
                        -64,
                        384,
                        384,
                        BlockTags.INFINIBURN_OVERWORLD,
                        ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "lunar"),
                        0.0f,
                        new DimensionType.MonsterSettings(
                                false, false, ConstantInt.of(0), 0
                        )
                )
        );
    }
    public static void bootstrapDimension(BootstrapContext<LevelStem> ctx) {
        var dimTypes = ctx.lookup(Registries.DIMENSION_TYPE);
        var noiseSettings = ctx.lookup(Registries.NOISE_SETTINGS);
        var biomes = ctx.lookup(Registries.BIOME);

        Holder<Biome> solar_biome = biomes.getOrThrow(SelectivePowerBiomes.SOLAR_BIOME);
        Holder<Biome> lunar_biome = biomes.getOrThrow(SelectivePowerBiomes.LUNAR_BIOME);

        FlatLevelGeneratorSettings settings =
                new FlatLevelGeneratorSettings(
                        Optional.empty(),
                        solar_biome,
                        List.of()

                );

        ctx.register(
                SOLAR_DIM,
                new LevelStem(
                        dimTypes.getOrThrow(SOLAR_DIM_TYPE),
                        new FlatLevelSource(settings.withBiomeAndLayers(
                                List.of(),
                                Optional.empty(),
                                solar_biome
                        ))
                )
        );

        Holder<NoiseGeneratorSettings> lunarNoise =
                noiseSettings.getOrThrow(ModNoiseSettings.LUNAR_DIM_NOISE);

        ChunkGenerator generator =
                new NoiseBasedChunkGenerator(
                        new FixedBiomeSource(lunar_biome),
                       lunarNoise
                );

        ctx.register(
                LUNAR_DIM,
                new LevelStem(
                        dimTypes.getOrThrow(LUNAR_DIM_TYPE),
                        generator
                )
        );


    }

}
