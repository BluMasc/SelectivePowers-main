package net.blumasc.selectivepowers.item.client.gui;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class GhostSlot extends Slot {

    public GhostSlot(Container inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return false;
    }

    @Override
    public boolean mayPickup(Player player) {
        return false;
    }

    @Override
    public void set(ItemStack stack) {
        if (stack.isEmpty()) {
            super.set(ItemStack.EMPTY);
        } else {
            ItemStack copy = stack.copy();
            copy.setCount(1);
            super.set(copy);
        }
    }

    public void click(Player player, boolean rightClick, ItemStack cursorStack) {
        if (rightClick) {
            super.set(ItemStack.EMPTY);
        } else {
            super.set(cursorStack.copyWithCount(1));
        }
    }

    @Override
    public void onTake(Player player, ItemStack stack) {
        super.set(ItemStack.EMPTY);
    }

    @Override
    public boolean isActive() {
        return true;
    }
}
