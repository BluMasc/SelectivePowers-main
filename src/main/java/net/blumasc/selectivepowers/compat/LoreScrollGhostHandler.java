package net.blumasc.selectivepowers.compat;

import mezz.jei.api.gui.handlers.IGhostIngredientHandler;
import mezz.jei.api.ingredients.ITypedIngredient;
import net.blumasc.selectivepowers.item.client.gui.GhostSlot;
import net.blumasc.selectivepowers.item.client.gui.LoreScrollScreen;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class LoreScrollGhostHandler implements IGhostIngredientHandler<LoreScrollScreen> {
    @Override
    public <I> List<Target<I>> getTargetsTyped(LoreScrollScreen gui, ITypedIngredient<I> ingredient, boolean doStart) {
        if (!(ingredient.getIngredient() instanceof ItemStack)) {
            return List.of();
        }

        int x = gui.getGuiLeft() + 9;
        int y = gui.getGuiTop() + 14;
        Rect2i slotArea = new Rect2i(x, y, 16, 16);

        return List.of(new IGhostIngredientHandler.Target<>() {
            @Override
            public Rect2i getArea() {
                return slotArea;
            }

            @Override
            public void accept(I i) {
                if (i instanceof ItemStack stack) {
                    var slots = gui.getMenu().slots;
                    Slot slot = slots.get(0);
                    if (slot instanceof GhostSlot ghost) {
                        ghost.set(stack.copyWithCount(1));
                    }
                }
            }
        });
    }

    @Override
    public void onComplete() {

    }
}
