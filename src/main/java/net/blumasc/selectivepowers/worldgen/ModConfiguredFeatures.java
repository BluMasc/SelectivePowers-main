package net.blumasc.selectivepowers.worldgen;

import net.blumasc.selectivepowers.SelectivePowers;
import net.blumasc.selectivepowers.block.SelectivepowersBlocks;
import net.blumasc.selectivepowers.block.custom.MooncapCropBlock;
import net.blumasc.selectivepowers.util.ModTags;
import net.blumasc.selectivepowers.worldgen.features.ModFeatures;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.BonusChestFeature;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
import net.blumasc.selectivepowers.util.ModTags;

import java.util.List;

public class ModConfiguredFeatures {

    public static final ResourceKey<ConfiguredFeature<?,?>> MOONCAP_MUSHROOM_KEY = registerKey("mooncap_mushroom");
    public static final ResourceKey<ConfiguredFeature<?,?>> DORMANT_CRAB_KEY = registerKey("dormant_crab");
    public static final ResourceKey<ConfiguredFeature<?,?>> CROWS_NEST_KEY = registerKey("crows_nest");

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        register(context, MOONCAP_MUSHROOM_KEY, Feature.RANDOM_PATCH,
                FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(BlockStateProvider.simple(SelectivepowersBlocks.MOONCAP_CROP.get()
                                .defaultBlockState().setValue(MooncapCropBlock.AGE, 5))
                        ), List.of(Blocks.GRASS_BLOCK, Blocks.MYCELIUM, Blocks.WARPED_NYLIUM, Blocks.PODZOL, Blocks.GRAVEL, Blocks.STONE)));

        register(context, DORMANT_CRAB_KEY, Feature.RANDOM_PATCH,
                new RandomPatchConfiguration(
                        32,
                        10,
                        5,
                        PlacementUtils.inlinePlaced(
                                Feature.SIMPLE_BLOCK,
                                new SimpleBlockConfiguration(
                                        BlockStateProvider.simple(
                                                SelectivepowersBlocks.DORMANT_ECHO_CRAB
                                                        .get()
                                                        .defaultBlockState()
                                        )
                                ),
                                BlockPredicateFilter.forPredicate(
                                        BlockPredicate.allOf(
                                                BlockPredicate.matchesBlocks(Blocks.AIR),
                                                BlockPredicate.matchesTag(
                                                        BlockPos.ZERO.below(),
                                                        ModTags.Blocks.ECHO_CRAB_SPAWNABLE
                                                )
                                        )
                                )
                        )
                )
        );

        register(context, CROWS_NEST_KEY, ModFeatures.CROW_NEST.get(),
                NoneFeatureConfiguration.INSTANCE);


    }

    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, name));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstrapContext<ConfiguredFeature<?, ?>> context,
                                                                                          ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}
