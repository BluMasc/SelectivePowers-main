package net.blumasc.selectivepowers.compat;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.blumasc.selectivepowers.SelectivePowers;
import net.blumasc.selectivepowers.block.SelectivepowersBlocks;
import net.blumasc.selectivepowers.item.client.gui.LoreScrollScreen;
import net.blumasc.selectivepowers.recipe.AltarRecipe;
import net.blumasc.selectivepowers.recipe.SelectivePowersRecipes;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.List;

@JeiPlugin
public class JeiSelectiepowersPlugin implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new AltarRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();

        List<AltarRecipe> altarRecipes = recipeManager
                .getAllRecipesFor(SelectivePowersRecipes.ALTAR_TYPE.get()).stream().map(RecipeHolder::value).toList();
        registration.addRecipes(AltarRecipeCategory.ALTAR_RECIPE_RECIPE_TYPE, altarRecipes);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(SelectivepowersBlocks.ALTAR, AltarRecipeCategory.ALTAR_RECIPE_RECIPE_TYPE);
    }
    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addGhostIngredientHandler(LoreScrollScreen.class, new LoreScrollGhostHandler());
    }
}
