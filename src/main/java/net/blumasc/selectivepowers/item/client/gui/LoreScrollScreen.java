package net.blumasc.selectivepowers.item.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import mezz.jei.api.gui.handlers.IGhostIngredientHandler;
import mezz.jei.api.ingredients.ITypedIngredient;
import net.blumasc.selectivepowers.SelectivePowers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.locale.Language;

import java.util.List;

public class LoreScrollScreen extends AbstractContainerScreen<LoreScrollMenu> {

    private static final ResourceLocation GUI_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "textures/gui/lore_scroll.png");

    private int scrollOffset = 0;
    private int maxScroll = 0;
    private boolean mouseUp= false;
    private boolean mouseDown= false;

    public LoreScrollScreen(LoreScrollMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void renderLabels(GuiGraphics gui, int mouseX, int mouseY) {

        ItemStack stack = menu.getAnalyzedItem();
        if (!stack.isEmpty()) {
            renderScaledName(gui, stack.getHoverName().getString(), 30, 18, 0xFFFFFF, 140);

            String key = stack.getDescriptionId().replace("block.", "lore.").replace("item.", "lore.");
            String aetherkey = "lore."+stack.getDescriptionId();

            Component lore;
            if (Language.getInstance().has(key)) {
                lore = Component.translatable(key);
            } else if (Language.getInstance().has(aetherkey)) {
                lore = Component.translatable(aetherkey);
            } else {
                lore = Component.translatable("lore.selectivepowers.empty");
            }

            drawWordWrap(gui, font, lore, 9, 35, 150, 0x000000);
            if(scrollOffset>0) {
                gui.drawString(font, "⬆", 160, 31, mouseUp?0x777777:0x000000);
            }
            if(scrollOffset<maxScroll) {
                gui.drawString(font, "⬇", 160, 31 + 4 * 9, mouseDown?0x777777:0x000000);
            }

        }
    }

    public void drawWordWrap(GuiGraphics gui, Font font, FormattedText text, int x, int y, int lineWidth, int color) {
        int printY = y-scrollOffset*9;
        for(FormattedCharSequence formattedcharsequence : font.split(text, lineWidth)) {
            if(printY>=y && printY<y+4*9) {
                gui.drawString(font, formattedcharsequence, x, printY, color, false);
            }
            printY += 9;
        }
        maxScroll = font.split(text, lineWidth).size()-4;
        if(maxScroll<0){maxScroll=0;}
        if(scrollOffset>maxScroll){scrollOffset=maxScroll;}

    }

    private void renderScaledName(GuiGraphics gui, String name, int x, int y, int color, int maxWidth) {
        int textWidth = font.width(name);

        if (textWidth <= maxWidth) {
            gui.drawString(font, name, x, y, color);
        } else {
            float scale = (float) maxWidth / textWidth;
            gui.pose().pushPose();
            gui.pose().translate(x, y, 0);
            gui.pose().scale(scale, scale, 1.0F);
            gui.drawString(font, name, 0, 0, color);
            gui.pose().popPose();
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

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double pScrollX, double pScrollY) {

            int textAreaScreenX = this.leftPos + 9;
            int textAreaScreenY = this.topPos + 35;
            int textAreaScreenX2 = textAreaScreenX + 155;
            int textAreaScreenY2 = textAreaScreenY + 35 + 5*9;
            if (mouseX >= textAreaScreenX && mouseX <= textAreaScreenX2 &&
                    mouseY >= textAreaScreenY && mouseY <= textAreaScreenY2) {

            scrollOffset -= pScrollY;
            if(scrollOffset<0){scrollOffset=0;}
            if(scrollOffset>maxScroll){scrollOffset=maxScroll;}
            return true;
        }


        return super.mouseScrolled(mouseX, mouseY, pScrollX, pScrollY);

    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int buttonAreaScreenX = this.leftPos + 160;
        int downAreaScreenY = this.topPos + 31 + 4 * 9;
        int buttonAreaScreenX2 = buttonAreaScreenX + 10;
        int downAreaScreenY2 = downAreaScreenY + 9;
        int upAreaScreenY = this.topPos + 31;
        int upAreaScreenY2 = upAreaScreenY + 9;

        if (mouseX >= buttonAreaScreenX && mouseX <= buttonAreaScreenX2 &&
                mouseY >= downAreaScreenY && mouseY <= downAreaScreenY2) {
            scrollOffset += 1;
            if(scrollOffset<0){scrollOffset=0;}
            if(scrollOffset>maxScroll){scrollOffset=maxScroll;}
            playScrollSound();
            return true;
        }
        if (mouseX >= buttonAreaScreenX && mouseX <= buttonAreaScreenX2 &&
                mouseY >= upAreaScreenY && mouseY <= upAreaScreenY2) {
            scrollOffset -= 1;
            if(scrollOffset<0){scrollOffset=0;}
            if(scrollOffset>maxScroll){scrollOffset=maxScroll;}
            playScrollSound();
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        int buttonAreaScreenX = this.leftPos + 160;
        int downAreaScreenY = this.topPos + 31 + 4 * 9;
        int buttonAreaScreenX2 = buttonAreaScreenX + 10;
        int downAreaScreenY2 = downAreaScreenY + 9;
        int upAreaScreenY = this.topPos + 31;
        int upAreaScreenY2 = upAreaScreenY + 9;

        if (mouseX >= buttonAreaScreenX && mouseX <= buttonAreaScreenX2 &&
                mouseY >= downAreaScreenY && mouseY <= downAreaScreenY2) {
            mouseDown = true;
        }else{
            mouseDown=false;
        }
        if (mouseX >= buttonAreaScreenX && mouseX <= buttonAreaScreenX2 &&
                mouseY >= upAreaScreenY && mouseY <= upAreaScreenY2) {
            mouseUp=true;
        }else{
            mouseUp=false;
        }
        super.mouseMoved(mouseX, mouseY);
    }
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 265 || keyCode == 87) {
            scrollOffset -= 1;
            if(scrollOffset<0){scrollOffset=0;}
            if(scrollOffset>maxScroll){scrollOffset=maxScroll;}
            return true;
        }
        if (keyCode == 264 || keyCode == 83) {
            scrollOffset += 1;
            if(scrollOffset<0){scrollOffset=0;}
            if(scrollOffset>maxScroll){scrollOffset=maxScroll;}
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
    private void playScrollSound() {
        if (this.minecraft != null && this.minecraft.player != null) {
            this.minecraft.player.playNotifySound(
                    SoundEvents.UI_BUTTON_CLICK.value(),
                    net.minecraft.sounds.SoundSource.MASTER,
                    1.0F,
                    1.0F
            );
        }
    }
}
