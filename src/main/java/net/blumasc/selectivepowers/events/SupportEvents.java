package net.blumasc.selectivepowers.events;

import net.blumasc.selectivepowers.Config;
import net.blumasc.selectivepowers.util.ModTags;
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

    public static int countUniqueItems(Player player, TagKey<Item> tag) {
        Set<ResourceLocation> found = new HashSet<>();

        for (ItemStack stack : player.getInventory().items) {
            if (stack.isEmpty()) continue;

            if (stack.is(tag)) {
                found.add(BuiltInRegistries.ITEM.getKey(stack.getItem()));
            }
        }

        return found.size();
    }

    public static int countTotalItems(Player player, TagKey<Item> tag) {
        int total = 0;

        for (ItemStack stack : player.getInventory().items) {
            if (stack.isEmpty()) continue;

            ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(stack.getItem());
            String itemIdString = itemId.toString();

            if (stack.is(tag)) {
                total += stack.getCount();
                continue;
            }
        }

        return total;
    }
}
