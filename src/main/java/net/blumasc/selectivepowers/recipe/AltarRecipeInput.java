package net.blumasc.selectivepowers.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public record AltarRecipeInput(ItemStack input, ItemStack sacrifice1, ItemStack sacrifice2, ItemStack sacrifice3, ItemStack sacrifice4) implements RecipeInput {
    @Override
    public ItemStack getItem(int i) {
        ItemStack ret;
        switch(i){
            case 0 -> ret = this.input;
            case 1 -> ret = this.sacrifice1;
            case 2 -> ret = this.sacrifice2;
            case 3 -> ret = this.sacrifice3;
            case 4 -> ret = this.sacrifice4;
            default -> throw new IllegalArgumentException("Recipe does not contain slot " + i);
        }
        return ret;
    }

    @Override
    public int size() {
        return 1;
    }
}
