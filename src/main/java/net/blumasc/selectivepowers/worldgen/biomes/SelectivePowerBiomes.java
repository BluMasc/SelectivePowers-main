package net.blumasc.selectivepowers.worldgen.biomes;

import net.blumasc.selectivepowers.SelectivePowers;
import net.blumasc.selectivepowers.entity.SelectivepowersEntities;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;

public class SelectivePowerBiomes {

    public static final ResourceKey<Biome> SOLAR_BIOME =
            ResourceKey.create(
                    Registries.BIOME,
                    ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "solar_biome")
            );

    public static final ResourceKey<Biome> LUNAR_BIOME =
            ResourceKey.create(
                    Registries.BIOME,
                    ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "lunar_biome")
            );

    public static void bootstrap(BootstrapContext<Biome> ctx) {
        ctx.register(
                SOLAR_BIOME,
                new Biome.BiomeBuilder()
                        .hasPrecipitation(false)
                        .temperature(1.0f)
                        .downfall(0.0f)
                        .specialEffects(
                                new BiomeSpecialEffects.Builder()
                                        .skyColor(0xff8800)
                                        .fogColor(0xff8800)
                                        .waterColor(0xff8800)
                                        .waterFogColor(0xff8800)
                                        .build()
                        )
                        .mobSpawnSettings(MobSpawnSettings.EMPTY)
                        .generationSettings(BiomeGenerationSettings.EMPTY)
                        .build()
        );
        ctx.register(
                LUNAR_BIOME,
                new Biome.BiomeBuilder()
                        .hasPrecipitation(false)
                        .temperature(0.6f)
                        .downfall(0.0f)
                        .specialEffects(
                                new BiomeSpecialEffects.Builder()
                                        .skyColor(0x000000)
                                        .fogColor(0xC0C0C0)
                                        .waterColor(0xC0C0C0)
                                        .waterFogColor(0xC0C0C0)
                                        .build()
                        )
                        .mobSpawnSettings(getMobSpawnSettings())
                        .generationSettings(BiomeGenerationSettings.EMPTY)
                        .build()
        );
    }

    private static MobSpawnSettings getMobSpawnSettings() {
        MobSpawnSettings.Builder builder = new MobSpawnSettings.Builder();
        builder.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(SelectivepowersEntities.MOON_SQUID.get(), 20, 10, 20));
        return builder.build();
    }
}
