package net.blumasc.selectivepowers.datagen;

import net.blumasc.selectivepowers.item.SelectivepowersItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.Compostable;
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;

import java.util.concurrent.CompletableFuture;

public class ModDataMapProvider extends DataMapProvider {
    protected ModDataMapProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather(HolderLookup.Provider provider) {
        this.builder(NeoForgeDataMaps.FURNACE_FUELS)
                .add(SelectivepowersItems.SUN_HORN, new FurnaceFuel(2400), false)
                .add(SelectivepowersItems.FLAMING_EGG, new FurnaceFuel(3400), false);

        this.builder(NeoForgeDataMaps.COMPOSTABLES)
                .add(SelectivepowersItems.MOONCAP_MUSHROOM, new Compostable(0.10f), false)
                .add(SelectivepowersItems.MOONCAP_SEEDS, new Compostable(0.10f), false);
    }
}
