package net.blumasc.selectivepowers.item.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import mezz.jei.api.gui.handlers.IGhostIngredientHandler;
import mezz.jei.api.ingredients.ITypedIngredient;
import net.blumasc.selectivepowers.SelectivePowers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.locale.Language;

import java.util.List;

public class LoreScrollScreen extends AbstractContainerScreen<LoreScrollMenu> {

    private static final ResourceLocation GUI_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "textures/gui/lore_scroll.png");

    public LoreScrollScreen(LoreScrollMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void renderLabels(GuiGraphics gui, int mouseX, int mouseY) {

        ItemStack stack = menu.getAnalyzedItem();
        if (!stack.isEmpty()) {
            gui.drawString(font, stack.getHoverName(), 30, 18, 0xFFFFFF);

            String key = stack.getDescriptionId() + ".lore";

            Component lore;
            if (Language.getInstance().has(key)) {
                lore = Component.translatable(key);
            } else {
                lore = Component.translatable("lore.selectivepowers.empty");
            }

            gui.drawWordWrap(font, lore, 9, 35, 160, 0x000000);
        }
    }

    @Override
    protected void renderBg(GuiGraphics gui, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, GUI_TEXTURE);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        gui.blit(GUI_TEXTURE, x, y, 0, 0, imageWidth, imageHeight, imageWidth, imageHeight);
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }
}
