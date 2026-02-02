package net.blumasc.selectivepowers.item.client.gui;

import mezz.jei.api.gui.handlers.IGhostIngredientHandler;
import mezz.jei.api.ingredients.ITypedIngredient;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.awt.*;
import java.util.List;

public class LoreScrollMenu extends AbstractContainerMenu  {

    private final Container container;

    public LoreScrollMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        this(id, inv);
    }

    public LoreScrollMenu(int id, Inventory playerInv) {
        super(ModMenuTypes.LORE_SCROLL_MENU.get(), id);
        this.container = new SimpleContainer(1);

        this.addSlot(new GhostSlot(container, 0, 9, 14));

        addPlayerInventory(playerInv);
        addPlayerHotbar(playerInv);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        if (index == 0) {
            Slot ghost = slots.get(0);
            ghost.set(ItemStack.EMPTY);
            return ItemStack.EMPTY;
        }

        Slot slot = slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack stack = slot.getItem();
            Slot ghostSlot = slots.get(0);
            if (ghostSlot instanceof GhostSlot ghost) {
                ghost.set(stack.copyWithCount(1));
            }
            return ItemStack.EMPTY;
        }

        return ItemStack.EMPTY;
    }




    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    public ItemStack getAnalyzedItem() {
        return container.getItem(0);
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    @Override
    public void clicked(int slotId, int button, ClickType clickType, Player player) {
        if (slotId >= 0 && slotId < slots.size()) {
            Slot slot = slots.get(slotId);
            if (slot instanceof GhostSlot ghost) {
                ItemStack cursor = player.containerMenu.getCarried();
                boolean rightClick = button == 1;
                ghost.click(player, rightClick, cursor);
                return;
            }
        }

        super.clicked(slotId, button, clickType, player);
    }
}

