package net.blumasc.selectivepowers.datagen;

import net.blumasc.selectivepowers.block.SelectivepowersBlocks;
import net.blumasc.selectivepowers.block.custom.MooncapCropBlock;
import net.blumasc.selectivepowers.item.SelectivepowersItems;
import net.blumasc.selectivepowers.util.ModTags;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.Set;

public class ModBlockLootTableProvider extends BlockLootSubProvider {
    protected ModBlockLootTableProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        dropSelf(SelectivepowersBlocks.BLUBOTT_PLUSH.get());
        dropSelf(SelectivepowersBlocks.BLUMASC_PLUSH.get());
        dropSelf(SelectivepowersBlocks.RIKARASHI_PLUSH.get());
        dropSelf(SelectivepowersBlocks.ALTAR.get());
        dropSelf(SelectivepowersBlocks.MOON_SACRIFICE_BLOCK.get());
        dropSelf(SelectivepowersBlocks.SUN_SACRIFICE_BLOCK.get());
        dropSelf(SelectivepowersBlocks.INLET_MOON_SACRIFICE_BLOCK.get());
        dropSelf(SelectivepowersBlocks.INLET_SUN_SACRIFICE_BLOCK.get());
        dropSelf(SelectivepowersBlocks.CROWS_NEST.get());
        dropSelf(SelectivepowersBlocks.SUN_LANTERN.get());
        dropSelf(SelectivepowersBlocks.MOON_LANTERN.get());
        dropSelf(SelectivepowersBlocks.DORMANT_ECHO_CRAB.get());
        dropSelf(SelectivepowersBlocks.MOON_CAP_BLOCK.get());
        dropSelf(SelectivepowersBlocks.MOON_DUST.get());
        dropSelf(SelectivepowersBlocks.MOON_ROCK.get());
        dropSelf(SelectivepowersBlocks.SMOOTH_MOON_ROCK.get());
        dropSelf(SelectivepowersBlocks.MOON_ROCK_TILES.get());
        dropSelf(SelectivepowersBlocks.MOON_ROCK_BRICKS.get());
        dropSelf(SelectivepowersBlocks.SMOOTH_MOON_ROCK_STAIRS.get());
        dropSelf(SelectivepowersBlocks.MOON_ROCK_TILES_STAIRS.get());
        dropSelf(SelectivepowersBlocks.MOON_ROCK_BRICKS_STAIRS.get());
        dropSelf(SelectivepowersBlocks.SMOOTH_MOON_ROCK_WALL.get());
        dropSelf(SelectivepowersBlocks.MOON_ROCK_TILES_WALL.get());
        dropSelf(SelectivepowersBlocks.MOON_ROCK_BRICKS_WALL.get());
        dropSelf(SelectivepowersBlocks.SOLAR_BLOCK.get());
        dropSelf(SelectivepowersBlocks.SOLAR_BRICKS.get());
        dropSelf(SelectivepowersBlocks.SOLAR_BRICK_FENCE.get());
        dropSelf(SelectivepowersBlocks.SOLAR_BRICK_WALL.get());
        dropSelf(SelectivepowersBlocks.SOLAR_BRICK_STAIRS.get());
        dropSelf(SelectivepowersBlocks.CHISLED_SOLAR_BRICKS.get());
        dropSelf(SelectivepowersBlocks.CRACKED_SOLAR_BRICKS.get());
        dropSelf(SelectivepowersBlocks.OBSIDIAN_DUST.get());
        dropSelf(SelectivepowersBlocks.DRACONIC_BEACON.get());
        dropSelf(SelectivepowersBlocks.PROTECTION_EFFIGY_BLOCK.get());
        dropSelf(SelectivepowersBlocks.GOLDEN_GOAT.get());
        dropSelf(SelectivepowersBlocks.TESSLA_COIL.get());
        dropSelf(SelectivepowersBlocks.SACRIFICIAL_ALTAR_BLOCK.get());
        dropOther(SelectivepowersBlocks.VOID_BLOCK.get(), ItemStack.EMPTY.getItem());
        dropOther(SelectivepowersBlocks.PITFALL_TRAP.get(), ItemStack.EMPTY.getItem());
        add(SelectivepowersBlocks.ROSE_VINES.get(),
         block -> createSilkTouchOnlyTable(SelectivepowersBlocks.ROSE_VINES.get()));
        dropOther(SelectivepowersBlocks.M_SOLAR_BLOCK.get(), SelectivepowersBlocks.SOLAR_BLOCK);

        add(SelectivepowersBlocks.MOON_GLASS.get(),
                block -> createSilkTouchOnlyTable(SelectivepowersBlocks.MOON_GLASS.get()));


        add(SelectivepowersBlocks.SPORE_MUSHROOM_BLOCK.get(),
                block -> createSilkTouchOnlyTable(SelectivepowersBlocks.SPORE_MUSHROOM_BLOCK.get()));


        add(SelectivepowersBlocks.EARTH_GLASS.get(),
                block -> createSilkTouchOnlyTable(SelectivepowersBlocks.EARTH_GLASS.get()));

        add(SelectivepowersBlocks.SMOOTH_MOON_ROCK_SLAB.get(),
                block -> createSlabItemTable(SelectivepowersBlocks.SMOOTH_MOON_ROCK_SLAB.get()));
        add(SelectivepowersBlocks.MOON_ROCK_TILES_SLAB.get(),
                block -> createSlabItemTable(SelectivepowersBlocks.SMOOTH_MOON_ROCK_SLAB.get()));
        add(SelectivepowersBlocks.MOON_ROCK_BRICKS_SLAB.get(),
                block -> createSlabItemTable(SelectivepowersBlocks.SMOOTH_MOON_ROCK_SLAB.get()));
        add(SelectivepowersBlocks.SOLAR_BRICK_SLAB.get(),
                block -> createSlabItemTable(SelectivepowersBlocks.SOLAR_BRICK_SLAB.get()));
        dropSelf(SelectivepowersBlocks.JUMP_MUSHROOM.get());


        LootItemCondition.Builder lootItemConditionBuilder = LootItemBlockStatePropertyCondition.hasBlockStateProperties(SelectivepowersBlocks.MOONCAP_CROP.get())
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(MooncapCropBlock.AGE, 5));

        LootItemCondition.Builder lootItemAlmostConditionBuilder = LootItemBlockStatePropertyCondition.hasBlockStateProperties(SelectivepowersBlocks.MOONCAP_CROP.get())
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(MooncapCropBlock.AGE, 4));

        this.add(SelectivepowersBlocks.MOONCAP_CROP.get(), this.createMultiCropDrops(SelectivepowersBlocks.MOONCAP_CROP.get(),
                SelectivepowersItems.MOONCAP_MUSHROOM.get(), Items.LAPIS_LAZULI, SelectivepowersItems.MOONCAP_SEEDS.get(), lootItemConditionBuilder, lootItemAlmostConditionBuilder));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return SelectivepowersBlocks.BLOCKS.getEntries().stream().map(Holder::value)::iterator;
    }

    protected LootTable.Builder createMultiCropDrops(
            Block cropBlock,
            Item grownCropItem,
            Item almostGrownCropItem,
            Item seedsItem,
            LootItemCondition.Builder dropGrownCropCondition,
            LootItemCondition.Builder dropAlmostGrownCropCondition
    ) {
        HolderLookup.RegistryLookup<Enchantment> registrylookup =
                this.registries.lookupOrThrow(Registries.ENCHANTMENT);

        return (LootTable.Builder) this.applyExplosionDecay(
                cropBlock,
                LootTable.lootTable()
                        .withPool(
                                LootPool.lootPool()
                                        .add(
                                                ((LootPoolSingletonContainer.Builder<?>)
                                                        LootItem.lootTableItem(grownCropItem)
                                                                .when(dropGrownCropCondition)
                                                ).otherwise(
                                                        LootItem.lootTableItem(almostGrownCropItem)
                                                                .when(dropAlmostGrownCropCondition)
                                                )
                                        )
                        )
                        .withPool(
                                LootPool.lootPool()
                                        .add(
                                                LootItem.lootTableItem(seedsItem)
                                                        .apply(
                                                                ApplyBonusCount.addBonusBinomialDistributionCount(
                                                                        registrylookup.getOrThrow(Enchantments.FORTUNE),
                                                                        0.5714286F,
                                                                        3
                                                                )
                                                        )
                                        )
                        )
        );
    }
}
