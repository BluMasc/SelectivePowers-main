package net.blumasc.selectivepowers.worldgen;

import com.mojang.serialization.MapCodec;
import net.blumasc.selectivepowers.SelectivePowers;
import net.blumasc.selectivepowers.entity.SelectivepowersEntities;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.common.world.ModifiableBiomeInfo;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.List;

public class ModBiomeModifiers {

    public static final ResourceKey<BiomeModifier> SPAWN_CROW = registerKey("spawn_crow");
    public static final ResourceKey<BiomeModifier> SPAWN_PACKWING = registerKey("spawn_packwing");
    public static final ResourceKey<BiomeModifier> SPAWN_SOLAR_BEETLE = registerKey("spawn_solar_beetle");
    public static final ResourceKey<BiomeModifier> SPAWN_SALAMANDER = registerKey("spawn_salamander");
    public static final ResourceKey<BiomeModifier> SPAWN_QUETZAL = registerKey("spawn_quetzal");

    public static final ResourceKey<BiomeModifier> ADD_MOONCAP_MUSHROOM = registerKey("add_mooncap_mushroom");
    public static final ResourceKey<BiomeModifier> ADD_DORMANT_CRAB = registerKey("add_dormant_crab");
    public static final ResourceKey<BiomeModifier> ADD_CROWS_NEST = registerKey("add_crows_nest");

    public static void bootstrap(BootstrapContext<BiomeModifier> context) {
        var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        var biomes = context.lookup(Registries.BIOME);

        context.register(SPAWN_CROW, new BiomeModifiers.AddSpawnsBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_FOREST),
                List.of(new MobSpawnSettings.SpawnerData(SelectivepowersEntities.CROW.get(), 20, 2, 4))
        ));

        context.register(SPAWN_SALAMANDER, new BiomeModifiers.AddSpawnsBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_MOUNTAIN),
                List.of(new MobSpawnSettings.SpawnerData(SelectivepowersEntities.SALAMANDER.get(), 20, 1, 1))
        ));

        context.register(SPAWN_SOLAR_BEETLE, new BiomeModifiers.AddSpawnsBiomeModifier(
                biomes.getOrThrow(BiomeTags.HAS_PILLAGER_OUTPOST),
                List.of(new MobSpawnSettings.SpawnerData(SelectivepowersEntities.SOLAR_BEETLE.get(), 20, 2, 5))
        ));

        context.register(SPAWN_PACKWING, new BiomeModifiers.AddSpawnsBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(Biomes.FOREST), biomes.getOrThrow(Biomes.END_HIGHLANDS), biomes.getOrThrow(Biomes.LUSH_CAVES), biomes.getOrThrow(Biomes.SWAMP), biomes.getOrThrow(Biomes.JUNGLE), biomes.getOrThrow(Biomes.WARPED_FOREST), biomes.getOrThrow(Biomes.CRIMSON_FOREST)),
                List.of(new MobSpawnSettings.SpawnerData(SelectivepowersEntities.PACKWING.get(), 20, 1, 1))
        ));

        context.register(SPAWN_QUETZAL, new BiomeModifiers.AddSpawnsBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_NETHER),
                List.of(new MobSpawnSettings.SpawnerData(SelectivepowersEntities.QUETZAL.get(), 20, 1, 3))
        ));

        context.register(ADD_MOONCAP_MUSHROOM, new BiomeModifiers.AddFeaturesBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(Biomes.JUNGLE),biomes.getOrThrow(Biomes.WARPED_FOREST), biomes.getOrThrow(Biomes.MUSHROOM_FIELDS), biomes.getOrThrow(Biomes.LUSH_CAVES), biomes.getOrThrow(Biomes.SWAMP), biomes.getOrThrow(Biomes.BIRCH_FOREST), biomes.getOrThrow(Biomes.WINDSWEPT_GRAVELLY_HILLS), biomes.getOrThrow(Biomes.DARK_FOREST)),
            HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.MOONCAP_MUSHROOM_PLACE_KEY)),
                GenerationStep.Decoration.VEGETAL_DECORATION
        ));

        context.register(ADD_DORMANT_CRAB, new BiomeModifiers.AddFeaturesBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(Biomes.DEEP_DARK)),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.DORMANT_CRAB_PLACE_KEY)),
                GenerationStep.Decoration.UNDERGROUND_DECORATION
        ));

        context.register(ADD_CROWS_NEST, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_FOREST),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.CROW_NEST_PLACE_KEY)),
                GenerationStep.Decoration.TOP_LAYER_MODIFICATION
        ));

    }

    private static ResourceKey<BiomeModifier> registerKey(String name) {
        return ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, name));
    }
}
