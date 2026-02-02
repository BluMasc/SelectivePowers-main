package net.blumasc.selectivepowers.datagen;

import net.blumasc.selectivepowers.SelectivePowers;
import net.blumasc.selectivepowers.block.SelectivepowersBlocks;
import net.blumasc.selectivepowers.item.SelectivepowersItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.storage.loot.entries.TagEntry;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.function.BiConsumer;

public class ModChestLoot implements LootTableSubProvider {

    public static final ResourceKey<LootTable> CROWS_NEST_LOOT_TABLE = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "crows_nest_loot"));
    public static final ResourceKey<LootTable> OLD_ALTAR_LOOT_TABLE = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "old_altar_loot"));
    public static final ResourceKey<LootTable> FRESH_ALTAR_LOOT_TABLE = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "fresh_altar_loot"));
    public static final ResourceKey<LootTable> DARK_POWER_LOOT_TABLE = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "dark_power_altar_loot"));
    public static final ResourceKey<LootTable> LIGHT_POWER_LOOT_TABLE = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "light_power_altar_loot"));
    public static final ResourceKey<LootTable> ANIMAL_POWER_LOOT_TABLE = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "animal_power_altar_loot"));
    public static final ResourceKey<LootTable> PLANT_POWER_LOOT_TABLE = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "plant_power_altar_loot"));
    public static final ResourceKey<LootTable> MUSHROOM_POWER_LOOT_TABLE = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "mushroom_power_altar_loot"));
    public static final ResourceKey<LootTable> STORM_POWER_LOOT_TABLE = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "storm_power_altar_loot"));
    public static final ResourceKey<LootTable> ELEMENTAL_POWER_LOOT_TABLE = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "elemental_power_altar_loot"));
    public static final ResourceKey<LootTable> MINING_POWER_LOOT_TABLE = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "mining_power_altar_loot"));
    public static final ResourceKey<LootTable> DRAGON_POWER_LOOT_TABLE = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "dragon_power_altar_loot"));
    public static final ResourceKey<LootTable> RAGE_POWER_LOOT_TABLE = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "rage_power_altar_loot"));
    public static final ResourceKey<LootTable> TRUTH_POWER_LOOT_TABLE = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "truth_power_altar_loot"));
    public static final ResourceKey<LootTable> MOON_POWER_LOOT_TABLE = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "moon_power_altar_loot"));
    public static final ResourceKey<LootTable> YELLOW_POWER_LOOT_TABLE = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "yellow_power_altar_loot"));




    public ModChestLoot(HolderLookup.Provider provider) {

    }

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> consumer) {
        consumer.accept(
                CROWS_NEST_LOOT_TABLE,
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(UniformGenerator.between(1.0F, 3.0F))
                                .add(LootItem.lootTableItem(Items.FEATHER).setWeight(6))
                                .add(LootItem.lootTableItem(SelectivepowersItems.SUN_SACRIFICE).setWeight(2))
                                .add(LootItem.lootTableItem(SelectivepowersItems.MOON_SACRIFICE).setWeight(2))
                                .add(LootItem.lootTableItem(Items.GOLD_NUGGET).setWeight(6))
                                .add(LootItem.lootTableItem(Items.STICK).setWeight(4))
                                .add(LootItem.lootTableItem(Items.BONE).setWeight(4))
                                .add(LootItem.lootTableItem(Items.GLASS_BOTTLE).setWeight(2))
                                .add(LootItem.lootTableItem(Items.RABBIT_HIDE).setWeight(3))
                                .add(LootItem.lootTableItem(Items.RABBIT_FOOT).setWeight(1))
                                .add(LootItem.lootTableItem(Items.ENDER_PEARL).setWeight(2))
                                .add(LootItem.lootTableItem(Items.EGG).setWeight(3))
                                .add(LootItem.lootTableItem(Items.OAK_SAPLING).setWeight(1))
                                .add(LootItem.lootTableItem(Items.CHERRY_SAPLING).setWeight(1))
                                .add(LootItem.lootTableItem(Items.FLINT).setWeight(2))
                                .add(LootItem.lootTableItem(Items.AMETHYST_SHARD).setWeight(1))
                                .add(LootItem.lootTableItem(Items.STRING).setWeight(4))
                        )
        );

        consumer.accept(
                OLD_ALTAR_LOOT_TABLE,
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(UniformGenerator.between(1.0F, 1.0F))
                                .add(LootItem.lootTableItem(Items.BREAD).setWeight(4))
                                .add(LootItem.lootTableItem(Items.IRON_INGOT).setWeight(3))
                                .add(LootItem.lootTableItem(Items.CAKE).setWeight(1))
                                .add(LootItem.lootTableItem(Items.ENDER_PEARL).setWeight(2))
                                .add(LootItem.lootTableItem(Items.WHEAT_SEEDS).setWeight(1))
                                .add(LootItem.lootTableItem(Items.AMETHYST_SHARD).setWeight(1))
                                .add(LootItem.lootTableItem(Items.GOLD_NUGGET).setWeight(2))
                                .add(LootItem.lootTableItem(Items.CLAY).setWeight(2))
                                .add(LootItem.lootTableItem(Items.ARROW).setWeight(4))
                                .add(TagEntry.expandTag(ItemTags.SAPLINGS).setWeight(1))
                        )
        );

        consumer.accept(
                FRESH_ALTAR_LOOT_TABLE,
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(UniformGenerator.between(1.0F, 1.0F))
                                .add(LootItem.lootTableItem(Items.ENCHANTED_GOLDEN_APPLE).setWeight(1))
                                .add(LootItem.lootTableItem(Items.IRON_INGOT).setWeight(12))
                                .add(LootItem.lootTableItem(Items.AMETHYST_SHARD).setWeight(16))
                                .add(LootItem.lootTableItem(Items.GOLD_INGOT).setWeight(18))
                                .add(LootItem.lootTableItem(Items.ARROW).setWeight(10))
                                .add(LootItem.lootTableItem(SelectivepowersItems.FLAMING_EGG).setWeight(2))
                                .add(LootItem.lootTableItem(SelectivepowersItems.SUN_SACRIFICE).setWeight(6))
                                .add(LootItem.lootTableItem(SelectivepowersItems.MOON_SACRIFICE).setWeight(6))
                        )
        );

        consumer.accept(
                DARK_POWER_LOOT_TABLE,
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(UniformGenerator.between(1.0F, 1.0F))
                                .add(LootItem.lootTableItem(Items.DISC_FRAGMENT_5).setWeight(4))
                                .add(LootItem.lootTableItem(Items.INK_SAC).setWeight(2))
                                .add(LootItem.lootTableItem(Items.SCULK).setWeight(2))
                                .add(LootItem.lootTableItem(SelectivepowersItems.WARDEN_HORNS).setWeight(5))
                                .add(LootItem.lootTableItem(SelectivepowersItems.SCULK_MOSS).setWeight(5))
                                .add(LootItem.lootTableItem(SelectivepowersItems.SCULK_TENDRIL).setWeight(5))
                                .add(LootItem.lootTableItem(SelectivepowersItems.SOLID_VOID).setWeight(7))
                                .add(LootItem.lootTableItem(SelectivepowersBlocks.DORMANT_ECHO_CRAB).setWeight(6))
                        )
        );

        consumer.accept(
                LIGHT_POWER_LOOT_TABLE,
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(UniformGenerator.between(1.0F, 1.0F))
                                .add(LootItem.lootTableItem(Items.GILDED_BLACKSTONE).setWeight(4))
                                .add(LootItem.lootTableItem(Items.CARROT).setWeight(2))
                                .add(LootItem.lootTableItem(Items.MOSS_BLOCK).setWeight(2))
                                .add(LootItem.lootTableItem(SelectivepowersItems.FLOWER_CROWN).setWeight(5))
                                .add(LootItem.lootTableItem(SelectivepowersItems.MOSS_LAYER).setWeight(5))
                                .add(LootItem.lootTableItem(SelectivepowersItems.SPINE_TREE).setWeight(5))
                                .add(LootItem.lootTableItem(SelectivepowersItems.LEAFWALKER_CURIO).setWeight(7))
                                .add(LootItem.lootTableItem(SelectivepowersBlocks.ROSE_VINES).setWeight(6))
                        )
        );

        consumer.accept(
                ANIMAL_POWER_LOOT_TABLE,
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(UniformGenerator.between(1.0F, 1.0F))
                                .add(LootItem.lootTableItem(Items.NAME_TAG).setWeight(4))
                                .add(LootItem.lootTableItem(Items.SADDLE).setWeight(2))
                                .add(LootItem.lootTableItem(Items.FEATHER).setWeight(2))
                                .add(LootItem.lootTableItem(SelectivepowersItems.WOLF_EARS).setWeight(5))
                                .add(LootItem.lootTableItem(SelectivepowersItems.WOLF_TAIL).setWeight(5))
                                .add(LootItem.lootTableItem(SelectivepowersItems.SHOULDER_CROW).setWeight(5))
                                .add(LootItem.lootTableItem(SelectivepowersItems.CHIMERA_CORE).setWeight(7))
                                .add(LootItem.lootTableItem(SelectivepowersItems.HORNED_HELMET).setWeight(6))
                        )
        );

        consumer.accept(
                PLANT_POWER_LOOT_TABLE,
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(UniformGenerator.between(1.0F, 1.0F))
                                .add(LootItem.lootTableItem(Items.SPORE_BLOSSOM).setWeight(4))
                                .add(LootItem.lootTableItem(Items.SADDLE).setWeight(2))
                                .add(LootItem.lootTableItem(Items.FEATHER).setWeight(2))
                                .add(LootItem.lootTableItem(SelectivepowersItems.WOLF_EARS).setWeight(5))
                                .add(LootItem.lootTableItem(SelectivepowersItems.WOLF_TAIL).setWeight(5))
                                .add(LootItem.lootTableItem(SelectivepowersItems.SHOULDER_CROW).setWeight(5))
                                .add(LootItem.lootTableItem(SelectivepowersItems.CHIMERA_CORE).setWeight(7))
                                .add(LootItem.lootTableItem(SelectivepowersItems.HORNED_HELMET).setWeight(6))
                        )
        );

        consumer.accept(
                MUSHROOM_POWER_LOOT_TABLE,
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(UniformGenerator.between(1.0F, 1.0F))
                                .add(LootItem.lootTableItem(Items.TUFF).setWeight(4))
                                .add(LootItem.lootTableItem(Items.MYCELIUM).setWeight(2))
                                .add(LootItem.lootTableItem(Items.MUSHROOM_STEW).setWeight(2))
                                .add(LootItem.lootTableItem(SelectivepowersItems.MUSHROOM_SPORES).setWeight(5))
                                .add(LootItem.lootTableItem(SelectivepowersItems.OCTOPUS_MUSHROOM).setWeight(5))
                                .add(LootItem.lootTableItem(SelectivepowersItems.SHELF_MUSHROOM).setWeight(5))
                                .add(LootItem.lootTableItem(SelectivepowersBlocks.SPORE_MUSHROOM_BLOCK).setWeight(7))
                                .add(LootItem.lootTableItem(SelectivepowersBlocks.JUMP_MUSHROOM).setWeight(6))
                        )
        );

        consumer.accept(
                STORM_POWER_LOOT_TABLE,
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(UniformGenerator.between(1.0F, 1.0F))
                                .add(LootItem.lootTableItem(Items.HEART_OF_THE_SEA).setWeight(4))
                                .add(LootItem.lootTableItem(Items.DEAD_BUSH).setWeight(2))
                                .add(LootItem.lootTableItem(Items.LIGHTNING_ROD).setWeight(2))
                                .add(LootItem.lootTableItem(SelectivepowersItems.STORM_CLOUD).setWeight(5))
                                .add(LootItem.lootTableItem(SelectivepowersItems.LIGHTNING_BALL).setWeight(5))
                                .add(LootItem.lootTableItem(SelectivepowersItems.POCKET_TESSLA_COIL).setWeight(5))
                                .add(LootItem.lootTableItem(SelectivepowersBlocks.TESSLA_COIL).setWeight(7))
                                .add(LootItem.lootTableItem(SelectivepowersItems.LIGHTNING_IN_A_BOTTLE).setWeight(6))
                        )
        );

        consumer.accept(
                ELEMENTAL_POWER_LOOT_TABLE,
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(UniformGenerator.between(1.0F, 1.0F))
                                .add(LootItem.lootTableItem(Items.HEAVY_CORE).setWeight(4))
                                .add(LootItem.lootTableItem(Items.PACKED_ICE).setWeight(2))
                                .add(LootItem.lootTableItem(Items.MAGMA_BLOCK).setWeight(2))
                                .add(LootItem.lootTableItem(SelectivepowersItems.DUALITY_PEARL).setWeight(5))
                                .add(LootItem.lootTableItem(SelectivepowersItems.ELEMENTAL_GLOVES).setWeight(5))
                                .add(LootItem.lootTableItem(SelectivepowersItems.ELEMENTAL_CIRCLET).setWeight(5))
                                .add(LootItem.lootTableItem(SelectivepowersItems.ELEMENTAL_GUN).setWeight(7))
                                .add(LootItem.lootTableItem(SelectivepowersItems.ICE_SHIELD).setWeight(6))
                        )
        );

        consumer.accept(
                MINING_POWER_LOOT_TABLE,
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(UniformGenerator.between(1.0F, 1.0F))
                                .add(LootItem.lootTableItem(Items.CALCITE).setWeight(4))
                                .add(LootItem.lootTableItem(Items.DEEPSLATE).setWeight(2))
                                .add(LootItem.lootTableItem(Items.DIAMOND_ORE).setWeight(2))
                                .add(LootItem.lootTableItem(SelectivepowersItems.MINERS_HAT).setWeight(5))
                                .add(LootItem.lootTableItem(SelectivepowersItems.DUST_MASK).setWeight(5))
                                .add(LootItem.lootTableItem(SelectivepowersItems.EMBEDDED_CRYSTALS).setWeight(5))
                                .add(LootItem.lootTableItem(SelectivepowersItems.PROSPECTORS_SHOVEL).setWeight(7))
                                .add(LootItem.lootTableItem(SelectivepowersBlocks.OBSIDIAN_DUST).setWeight(6))
                        )
        );

        consumer.accept(
                DRAGON_POWER_LOOT_TABLE,
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(UniformGenerator.between(1.0F, 1.0F))
                                .add(LootItem.lootTableItem(Items.DRAGON_HEAD).setWeight(4))
                                .add(LootItem.lootTableItem(Items.END_ROD).setWeight(2))
                                .add(LootItem.lootTableItem(Items.PURPUR_BLOCK).setWeight(2))
                                .add(LootItem.lootTableItem(SelectivepowersItems.DRAGON_CLAWS).setWeight(5))
                                .add(LootItem.lootTableItem(SelectivepowersItems.DRAGON_HORNS).setWeight(5))
                                .add(LootItem.lootTableItem(SelectivepowersItems.BREATH_BAUBLE).setWeight(5))
                                .add(LootItem.lootTableItem(SelectivepowersItems.DRAGON_SLEEVES).setWeight(7))
                                .add(LootItem.lootTableItem(Items.DRAGON_EGG).setWeight(6))
                        )
        );

        consumer.accept(
                RAGE_POWER_LOOT_TABLE,
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(UniformGenerator.between(1.0F, 1.0F))
                                .add(LootItem.lootTableItem(Items.GOAT_HORN).setWeight(4))
                                .add(LootItem.lootTableItem(Items.EMERALD_ORE).setWeight(2))
                                .add(LootItem.lootTableItem(Items.DEEPSLATE_EMERALD_ORE).setWeight(2))
                                .add(LootItem.lootTableItem(SelectivepowersItems.FACE_PAINT).setWeight(5))
                                .add(LootItem.lootTableItem(SelectivepowersItems.HIP_HORN).setWeight(5))
                                .add(LootItem.lootTableItem(SelectivepowersItems.POLARBEAR_PELT).setWeight(5))
                                .add(LootItem.lootTableItem(SelectivepowersBlocks.GOLDEN_GOAT).setWeight(7))
                                .add(LootItem.lootTableItem(SelectivepowersItems.RAGE_COOKIE).setWeight(6))
                        )
        );

        consumer.accept(
                TRUTH_POWER_LOOT_TABLE,
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(UniformGenerator.between(1.0F, 1.0F))
                                .add(LootItem.lootTableItem(Items.INFESTED_COBBLESTONE).setWeight(4))
                                .add(LootItem.lootTableItem(Items.MAP).setWeight(2))
                                .add(LootItem.lootTableItem(Items.CALCITE).setWeight(2))
                                .add(LootItem.lootTableItem(SelectivepowersItems.GLOWING_EYES).setWeight(5))
                                .add(LootItem.lootTableItem(SelectivepowersItems.HIP_BOOK).setWeight(5))
                                .add(LootItem.lootTableItem(SelectivepowersItems.ENCHANTING_RUNE).setWeight(5))
                                .add(LootItem.lootTableItem(Items.SPECTRAL_ARROW).setWeight(7))
                                .add(LootItem.lootTableItem(SelectivepowersItems.LORE_SCROLL).setWeight(6))
                        )
        );

        consumer.accept(
                MOON_POWER_LOOT_TABLE,
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(UniformGenerator.between(1.0F, 1.0F))
                                .add(LootItem.lootTableItem(Items.MILK_BUCKET).setWeight(4))
                                .add(LootItem.lootTableItem(SelectivepowersBlocks.MOON_DUST).setWeight(1))
                                .add(LootItem.lootTableItem(SelectivepowersBlocks.MOON_ROCK).setWeight(3))
                                .add(LootItem.lootTableItem(SelectivepowersItems.MOONLIGHT_JELLY).setWeight(5))
                                .add(LootItem.lootTableItem(SelectivepowersItems.MOON_SACRIFICE).setWeight(7))
                                .add(LootItem.lootTableItem(SelectivepowersItems.MOONLIGHT_GLAIVE).setWeight(6))
                        )
        );
        consumer.accept(
                YELLOW_POWER_LOOT_TABLE,
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(UniformGenerator.between(1.0F, 1.0F))
                                .add(LootItem.lootTableItem(Items.SUNFLOWER).setWeight(4))
                                .add(LootItem.lootTableItem(SelectivepowersBlocks.SOLAR_BLOCK).setWeight(4))
                                .add(LootItem.lootTableItem(SelectivepowersBlocks.ALTAR).setWeight(5))
                                .add(LootItem.lootTableItem(SelectivepowersItems.SUN_SACRIFICE).setWeight(7))
                                .add(LootItem.lootTableItem(SelectivepowersItems.SUN_SLICER).setWeight(6))
                        )
        );
    }
}
