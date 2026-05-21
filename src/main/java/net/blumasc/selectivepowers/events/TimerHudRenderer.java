package net.blumasc.selectivepowers.events;

import net.blumasc.selectivepowers.SelectivePowers;
import net.blumasc.selectivepowers.client.ClientTimerData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

@EventBusSubscriber(modid = SelectivePowers.MODID, value = Dist.CLIENT)
public class TimerHudRenderer {

    private static final ResourceLocation ABILITY_ICON = ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "textures/mob_effect/ability_timer.png");
    private static final ResourceLocation ULT_ICON = ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "textures/mob_effect/ult_timer.png");

    private static final int ICON_SIZE = 18;
    private static final int BAR_WIDTH = 2;
    private static final int BAR_MAX_HEIGHT = 18;
    private static final int PADDING = 4;

    @SubscribeEvent
    public static void onRenderHud(RenderGuiLayerEvent.Pre event) {
        if (ClientTimerData.abilityTimer <= 0 && ClientTimerData.ultTimer <= 0) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.screen != null) return;

        GuiGraphics graphics = event.getGuiGraphics();
        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();
        int hotbarLeft = (screenWidth / 2) - 91 - PADDING;
        int baseY = screenHeight - 22;

        boolean both = ClientTimerData.abilityTimer > 0 && ClientTimerData.ultTimer > 0;

        if (both) {
            int ultX = hotbarLeft - ICON_SIZE - BAR_WIDTH - PADDING;
            int abilityX = ultX - ICON_SIZE - BAR_WIDTH - PADDING * 2;

            renderHudTimer(graphics, ClientTimerData.ultTimer, ULT_ICON,
                    ultX, baseY, ClientTimerData.maxUltTimer, true);
            renderHudTimer(graphics, ClientTimerData.abilityTimer, ABILITY_ICON,
                    abilityX, baseY, ClientTimerData.maxAbilityTimer, false);
        } else if (ClientTimerData.ultTimer > 0) {
            renderHudTimer(graphics, ClientTimerData.ultTimer, ULT_ICON,
                    hotbarLeft - ICON_SIZE - BAR_WIDTH - PADDING, baseY,
                    ClientTimerData.maxUltTimer, true);
        } else if (ClientTimerData.abilityTimer > 0) {
            renderHudTimer(graphics, ClientTimerData.abilityTimer, ABILITY_ICON,
                    hotbarLeft - ICON_SIZE - BAR_WIDTH - PADDING, baseY,
                    ClientTimerData.maxAbilityTimer, false);
        }
    }

    @SubscribeEvent
    public static void onRenderScreen(ScreenEvent.Render.Post event) {
        if (!(event.getScreen() instanceof InventoryScreen)) return;
        if (ClientTimerData.abilityTimer <= 0 && ClientTimerData.ultTimer <= 0) return;
        Minecraft mc = Minecraft.getInstance();
        GuiGraphics graphics = event.getGuiGraphics();
        InventoryScreen screen = (InventoryScreen) event.getScreen();
        int guiLeft = screen.getGuiLeft();
        int guiTop = screen.getGuiTop();

        boolean both = ClientTimerData.abilityTimer > 0 && ClientTimerData.ultTimer > 0;

        int x = screen.width/2-(both?ICON_SIZE+60+PADDING:(ICON_SIZE+60+PADDING)/2);
        int y = guiTop - ICON_SIZE - PADDING;

        if (ClientTimerData.abilityTimer > 0) {
            renderMenuTimer(graphics, mc, ClientTimerData.abilityTimer, ABILITY_ICON, x, y);
            x += ICON_SIZE + 60 + PADDING;
        }
        if (ClientTimerData.ultTimer > 0) {
            renderMenuTimer(graphics, mc, ClientTimerData.ultTimer, ULT_ICON, x, y);
        }
    }

    private static void renderHudTimer(GuiGraphics graphics, int timer, ResourceLocation icon,
                                        int x, int y, int maxTimer, boolean ult) {
        if (timer <= 0) return;
        graphics.blit(icon, x, y, 0, 0, ICON_SIZE, ICON_SIZE, ICON_SIZE, ICON_SIZE);
        int barX = x + ICON_SIZE + 2;
        int barY = y;
        graphics.fill(barX, barY, barX + BAR_WIDTH, barY + BAR_MAX_HEIGHT, 0xFF333333);
        float fraction = maxTimer > 0 ? (float) timer / maxTimer : 1f;
        int fillHeight = (int) (BAR_MAX_HEIGHT * fraction);
        int fillY = barY + (BAR_MAX_HEIGHT - fillHeight);
        int color = getBarColor(fraction, ult);
        graphics.fill(barX, fillY, barX + BAR_WIDTH, barY + BAR_MAX_HEIGHT, color);
    }

    private static void renderMenuTimer(GuiGraphics graphics, Minecraft mc,
                                         int timer, ResourceLocation icon, int x, int y) {
        graphics.blit(icon, x, y, 0, 0, ICON_SIZE, ICON_SIZE, ICON_SIZE, ICON_SIZE);
        int totalSeconds = timer / 20;
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        String timeText = String.format("%d:%02d", minutes, seconds);

        graphics.drawString(mc.font, timeText, x + ICON_SIZE + 4, y + 5, 0xFFFFFF);
    }

    private static int getBarColor(float fraction, boolean ult) {
        if (ult) {
            int r = (int) (148 * fraction);
            int g = 100;
            int b = 255;
            return 0xFF000000 | (r << 16) | (g << 8) | b;
        } else {
            int g = (int) (220 * (1-fraction));
            return 0xFF000000 | (0xFE << 16) | (g << 8);
        }
    }
    private static int getMaxTimer(boolean ult) {
        return ult ? ClientTimerData.maxUltTimer : ClientTimerData.maxAbilityTimer;
    }
}