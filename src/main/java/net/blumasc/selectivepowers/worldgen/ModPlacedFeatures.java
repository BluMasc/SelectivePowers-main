package net.blumasc.selectivepowers.worldgen;

import net.blumasc.selectivepowers.SelectivePowers;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class ModPlacedFeatures {

    public static final ResourceKey<PlacedFeature> MOONCAP_MUSHROOM_PLACE_KEY = registerKey("mooncap_mushroom_placed");
    public static final ResourceKey<PlacedFeature> DORMANT_CRAB_PLACE_KEY = registerKey("dormant_crab_placed");
    public static final ResourceKey<PlacedFeature> CROW_NEST_PLACE_KEY = registerKey("crow_nest_placed");

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        var configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);
        register(context, MOONCAP_MUSHROOM_PLACE_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.MOONCAP_MUSHROOM_KEY),
                List.of(RarityFilter.onAverageOnceEvery(4), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome()));

        register(context, DORMANT_CRAB_PLACE_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.DORMANT_CRAB_KEY),
                List.of(RarityFilter.onAverageOnceEvery(3),
                        InSquarePlacement.spread(),
                        HeightRangePlacement.uniform(
                                VerticalAnchor.absolute(-64),
                                VerticalAnchor.absolute(-20)
                        ),
                        BiomeFilter.biome()));

        register(context, CROW_NEST_PLACE_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.CROWS_NEST_KEY),
                List.of(RarityFilter.onAverageOnceEvery(6),
                        InSquarePlacement.spread(),
                        PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                        BiomeFilter.biome()));
    }

    private static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, name));
    }

    private static void register(BootstrapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> configuration,
                                 List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }
}
