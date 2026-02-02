package net.blumasc.selectivepowers.util;

import net.blumasc.selectivepowers.SelectivePowers;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.internal.NeoForgeEntityTypeTagsProvider;

public class ModTags {
    public static class Blocks{
        public static final TagKey<Block> CROW_PERCHABLE = createTag("crow_perchable");
        public static final TagKey<Block> PACKWING_SPAWNABLE = createTag("packwing_spawnable");
        public static final TagKey<Block> ECHO_CRAB_SPAWNABLE = createTag("echo_crab_spawnable");

        private static TagKey<Block> createTag(String name){
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, name));
        }

    }
    public static class Items{
        public static final TagKey<Item> CROW_BREEDING_ITEM = createTag("crow_breeding_item");
        public static final TagKey<Item> CROW_TAMING_ITEM = createTag("crow_taming_item");
        public static final TagKey<Item> PACKWING_TAMING_ITEM = createTag("packwing_taming_item");
        public static final TagKey<Item> PACKWING_BREEDING_ITEM = createTag("packwing_breeding_item");
        public static final TagKey<Item> SALAMANDER_FOOD = createTag("salamander_food");
        public static final TagKey<Item> SOLAR_BEETLE_FOOD = createTag("solar_beetle_food");
        public static final TagKey<Item> ENCHANTABLE_DROP_EDITOR = createTag("enchantable_drop_editor");

        private static TagKey<Item> createTag(String name){
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, name));
        }
    }
    public static class EntityTypes{
        public static final TagKey<EntityType<?>> CHIMERA_LIKE =
                TagKey.create(
                        Registries.ENTITY_TYPE,
                        ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "chimera_like")
                );

        public static final TagKey<EntityType<?>> SENSITIVE_TO_DEVILS_IMPALING =
                TagKey.create(
                        Registries.ENTITY_TYPE,
                        ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "sensitive_to_devils_impaling")
                );
    }
    public static class EnchantmentTypes {
        public static final TagKey<Enchantment> HEAD_SENSE =
                TagKey.create(
                        Registries.ENCHANTMENT,
                        ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "head_sense")
                );
        public static final TagKey<Enchantment> MIND_POWER =
                TagKey.create(
                        Registries.ENCHANTMENT,
                        ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "mind_power")
                );

        public static final TagKey<Enchantment> CHEST_EXCLUSIVE =
                TagKey.create(
                        Registries.ENCHANTMENT,
                        ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "chest_exclusive")
                );
        public static final TagKey<Enchantment> POCKETS =
                TagKey.create(
                        Registries.ENCHANTMENT,
                        ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "pockets")
                );
        public static final TagKey<Enchantment> LEG_MOVING =
                TagKey.create(
                        Registries.ENCHANTMENT,
                        ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "leg_moving")
                );

        public static final TagKey<Enchantment> FEET_EXCLUSIVE =
                TagKey.create(
                        Registries.ENCHANTMENT,
                        ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "feet_exclusive")
                );
        public static final TagKey<Enchantment> SMELT_SILK =
                TagKey.create(
                        Registries.ENCHANTMENT,
                        ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "smelt_silk")
                );
        public static final TagKey<Enchantment> MOLTEN_MAGNETS =
                TagKey.create(
                        Registries.ENCHANTMENT,
                        ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "molten_magnets")
                );

        public static final TagKey<Enchantment> VEIN_MINER_INCOMPATIBLE =
                TagKey.create(
                        Registries.ENCHANTMENT,
                        ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "vein_miner_incompatible")
                );

        public static final TagKey<Enchantment> SWORD_MUTUAL_EXCLUSIVE =
                TagKey.create(
                        Registries.ENCHANTMENT,
                        ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "sword_mutual_exclusive")
                );

        public static final TagKey<Enchantment> AXE_MUTUAL_EXCLUSIVE =
                TagKey.create(
                        Registries.ENCHANTMENT,
                        ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "axe_mutual_exclusive")
                );

        public static final TagKey<Enchantment> HOE_MUTUAL_EXCLUSIVE =
                TagKey.create(
                        Registries.ENCHANTMENT,
                        ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "hoe_mutual_exclusive")
                );

        public static final TagKey<Enchantment> SHOVEL_MUTUAL_EXCLUSIVE =
                TagKey.create(
                        Registries.ENCHANTMENT,
                        ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "shovel_mutual_exclusive")
                );

        public static final TagKey<Enchantment> PICKERANG_EXCLUSIVE =
                TagKey.create(
                        Registries.ENCHANTMENT,
                        ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "pickerang_exclusive")
                );

        public static final TagKey<Enchantment> DEQUIPING_EXCLUSIVE =
                TagKey.create(
                        Registries.ENCHANTMENT,
                        ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "dequiping_exclusive")
                );

        public static final TagKey<Enchantment> MACE_LAND_EFFECT =
                TagKey.create(
                        Registries.ENCHANTMENT,
                        ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "mace_land_effect")
                );

        public static final TagKey<Enchantment> ARROW_HIT_EFFECT =
                TagKey.create(
                        Registries.ENCHANTMENT,
                        ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "arrow_hit_effect")
                );

        public static final TagKey<Enchantment> ARROW_SHOOT_EFFECT =
                TagKey.create(
                        Registries.ENCHANTMENT,
                        ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "arrow_shoot_effect")
                );

        public static final TagKey<Enchantment> CROSSBOW_EXCLUSIVE =
                TagKey.create(
                        Registries.ENCHANTMENT,
                        ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "crossbow_exclusive")
                );

        public static final TagKey<Enchantment> FISHING_ROD_HOOK =
                TagKey.create(
                        Registries.ENCHANTMENT,
                        ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "fishing_rod_hook")
                );

        public static final TagKey<Enchantment> CHANNELING_EXCLUSIVE =
                TagKey.create(
                        Registries.ENCHANTMENT,
                        ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "channeling_exclusive")
                );

        public static final TagKey<Enchantment> FORK_EXCLUSIVE =
                TagKey.create(
                        Registries.ENCHANTMENT,
                        ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "fork_exclusive")
                );

        public static final TagKey<Enchantment> SHIELD_EXCLUSIVE =
                TagKey.create(
                        Registries.ENCHANTMENT,
                        ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "shield_exclusive")
                );
    }
}
