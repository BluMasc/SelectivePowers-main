package net.blumasc.selectivepowers.util;

import net.blumasc.selectivepowers.SelectivePowers;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.*;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.internal.NeoForgeEntityTypeTagsProvider;

public class ModTags {
    public static class Blocks{
        public static final TagKey<Block> CROW_PERCHABLE = createTag("crow_perchable");
        public static final TagKey<Block> ICE_CORE = createTag("ice_core");
        public static final TagKey<Block> LAVA_CORE = createTag("lava_core");
        public static final TagKey<Block> REDSTONE_ACTIVATOR = createTag("redstone_activator");

        private static TagKey<Block> createTag(String name){
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, name));
        }

    }
    public static class Items{
        public static final TagKey<Item> CROW_BREEDING_ITEM = createTag("crow_breeding_item");
        public static final TagKey<Item> CROW_TAMING_ITEM = createTag("crow_taming_item");
        public static final TagKey<Item> MUSHROOM_ITEMS = createTag("mushroom_items");
        public static final TagKey<Item> PLANT_ITEMS = createTag("plant_items");

        private static TagKey<Item> createTag(String name){
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, name));
        }
    }
    public static class EntityTypes{

        public static final TagKey<EntityType<?>> DRAGON_LIKE =createTag("dragon_like");
        public static final TagKey<EntityType<?>> VILLAGER_LIKE =createTag("villager_like");
        public static final TagKey<EntityType<?>> ELEMENTAL_PROJECTILES =createTag("elemental_projectiles");
        private static TagKey<EntityType<?>> createTag(String name){
            return TagKey.create(
                    Registries.ENTITY_TYPE,
                    ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, name)
            );
        }
    }
}
