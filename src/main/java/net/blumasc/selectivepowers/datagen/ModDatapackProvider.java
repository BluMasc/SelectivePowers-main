package net.blumasc.selectivepowers.datagen;

import net.blumasc.selectivepowers.SelectivePowers;
import net.blumasc.selectivepowers.enchantment.ModEnchantments;
import net.blumasc.selectivepowers.worldgen.*;
import net.blumasc.selectivepowers.worldgen.biomes.SelectivePowerBiomes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModDatapackProvider extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.ENCHANTMENT, ModEnchantments::bootstrap)
            .add(Registries.CONFIGURED_FEATURE, ModConfiguredFeatures::bootstrap)
            .add(Registries.PLACED_FEATURE, ModPlacedFeatures::bootstrap)
            .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ModBiomeModifiers::bootstrap)

            .add(Registries.BIOME, SelectivePowerBiomes::bootstrap)

            .add(Registries.NOISE_SETTINGS, ModNoiseSettings::bootstrap)

            .add(Registries.DIMENSION_TYPE, ModDimensions::bootstrapDimensionType)
            .add(Registries.LEVEL_STEM, ModDimensions::bootstrapDimension);
    public ModDatapackProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(SelectivePowers.MODID));
    }
}
