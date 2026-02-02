package net.blumasc.selectivepowers.events;

import net.blumasc.selectivepowers.Config;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SupportEvents {

    public static int countUniqueItems(Player player, List<? extends String> list) {
        Set<ResourceLocation> found = new HashSet<>();

        for (ItemStack stack : player.getInventory().items) {
            if (stack.isEmpty()) continue;

            ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(stack.getItem());
            String itemIdString = itemId.toString();

            if (list.contains(itemIdString)) {
                found.add(itemId);
                continue;
            }

            for (String s : list) {
                if (!s.startsWith("#")) continue;

                String tagId = s.substring(1);
                ResourceLocation tagRL = ResourceLocation.parse(tagId);
                TagKey<Item> itemTag = TagKey.create(Registries.ITEM, tagRL);

                if (stack.is(itemTag)) {
                    found.add(itemId);
                    break;
                }
            }
        }

        return found.size();
    }

    public static int countTotalItems(Player player, List<? extends String> list) {
        int total = 0;

        for (ItemStack stack : player.getInventory().items) {
            if (stack.isEmpty()) continue;

            ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(stack.getItem());
            String itemIdString = itemId.toString();

            if (list.contains(itemIdString)) {
                total += stack.getCount();
                continue;
            }

            for (String s : list) {
                if (!s.startsWith("#")) continue;

                String tagId = s.substring(1);
                ResourceLocation tagRL = ResourceLocation.parse(tagId);
                TagKey<Item> itemTag = TagKey.create(Registries.ITEM, tagRL);

                if (stack.is(itemTag)) {
                    total += stack.getCount();
                    break;
                }
            }
        }

        return total;
    }

    public static boolean isMushroom(ItemStack stack) {
        Set<Item> items = new HashSet<>();
        Set<TagKey<Item>> tagKeys = new HashSet<>();

        for (String s : Config.ITEM_STRINGS_MUSHROOM.get()) {
            if (s.startsWith("#")) {
                ResourceLocation id = ResourceLocation.tryParse(s.substring(1));
                if (id != null) {
                    tagKeys.add(TagKey.create(Registries.ITEM, id));
                }
            } else {
                ResourceLocation id = ResourceLocation.tryParse(s);
                if (id != null) {
                    Item item = BuiltInRegistries.ITEM.get(id);
                    if (item != null && item != Items.AIR) {
                        items.add(item);
                    }
                }
            }
        }
        if (items.contains(stack.getItem())) return true;

        for (TagKey<Item> tag : tagKeys) {
            if (stack.is(tag)) return true;
        }

        return false;
    }
}
