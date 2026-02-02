package net.blumasc.selectivepowers.item.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.blumasc.selectivepowers.SelectivePowers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;

public class PageScreen extends Screen {
    private final int page;
    private static final ResourceLocation[] PAGE_TEXTURES = new ResourceLocation[10];

    static {
        for (int p=0;p<10;p++) {
            PAGE_TEXTURES[p] = ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "textures/gui/page" + p + ".png");
        }
    }

    public PageScreen(int page) {
        super(Component.literal(""));
        this.page = page;
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int centerY = this.height / 2;
        addRenderableWidget(Button.builder(Component.translatable("selectivepowers.gui.close"), button -> onClose()).bounds(centerX - 40, centerY + 60, 80, 20).build());
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, PAGE_TEXTURES[this.page]);
        int x = (width - 96) / 2;
        int y = ((height-80) - 144) / 2;

        guiGraphics.blit(PAGE_TEXTURES[this.page], x, y, 0, 0, 96, 144, 96, 144);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
