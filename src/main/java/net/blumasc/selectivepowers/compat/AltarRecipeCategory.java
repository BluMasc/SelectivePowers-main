package net.blumasc.selectivepowers.compat;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.blumasc.selectivepowers.SelectivePowers;
import net.blumasc.selectivepowers.block.SelectivepowersBlocks;
import net.blumasc.selectivepowers.item.SelectivepowersItems;
import net.blumasc.selectivepowers.recipe.AltarRecipe;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.crafting.IngredientType;
import org.checkerframework.checker.guieffect.qual.UI;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AltarRecipeCategory implements IRecipeCategory<AltarRecipe> {
    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "altar");
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID,
            "textures/misc/gui/altar.png");

    public static final RecipeType<AltarRecipe> ALTAR_RECIPE_RECIPE_TYPE =
            new RecipeType<>(UID, AltarRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public AltarRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0,0, 176,79);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(SelectivepowersBlocks.ALTAR));
    }

    @Override
    public RecipeType<AltarRecipe> getRecipeType() {
        return ALTAR_RECIPE_RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.selectivepowers.altar");
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, AltarRecipe recipe, IFocusGroup focuses) {
        builder.addInputSlot(45,30).addIngredients(VanillaTypes.ITEM_STACK, List.of(recipe.getIngredients().get(0).getItems()));
        builder.addInputSlot(22,7).addIngredient(VanillaTypes.ITEM_STACK, recipe.getIngredients().get(1).getItems()[0]);
        builder.addInputSlot(68,7).addIngredient(VanillaTypes.ITEM_STACK, recipe.getIngredients().get(2).getItems()[0]);
        builder.addInputSlot(22,53).addIngredient(VanillaTypes.ITEM_STACK, recipe.getIngredients().get(3).getItems()[0]);
        builder.addInputSlot(68,53).addIngredient(VanillaTypes.ITEM_STACK, recipe.getIngredients().get(4).getItems()[0]);
        var result = recipe.getResultItem(null);
        if(result.is(SelectivepowersItems.CROW_SPAWN_EGG))
        {
            result = SelectivepowersItems.CROW_SPAWN.toStack();
        }
        if(result.is(SelectivepowersItems.LUNAR_MAIDEN_SPAWN_EGG))
        {
            result = SelectivepowersItems.LUNAR_MAIDEN_SPAWN.toStack();
        }
        if(result.is(SelectivepowersItems.YELLOW_KING_SPAWN_EGG))
        {
            result = SelectivepowersItems.YELLOW_KING_SPAWN.toStack();
        }
        builder.addOutputSlot(143,32).addIngredient(VanillaTypes.ITEM_STACK, result);
    }

    @SuppressWarnings("removal")
    @Override
    public @Nullable IDrawable getBackground() {
        return background;
    }


    @Override
    public boolean needsRecipeBorder() {
        return false;
    }


}
