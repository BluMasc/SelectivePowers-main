package net.blumasc.selectivepowers.events;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.blumasc.blubasics.effect.BaseModEffects;
import net.blumasc.blubasics.shader.VoidVisionRenderer;
import net.blumasc.selectivepowers.SelectivePowers;
import net.blumasc.selectivepowers.effect.SelectivepowersEffects;
import net.blumasc.selectivepowers.shader.YellowPostProcessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = SelectivePowers.MODID, value = Dist.CLIENT)
public class PotionEffectRenderers {

    private static final ResourceLocation YELLOW_OVERLAY =
            ResourceLocation.fromNamespaceAndPath("selectivepowers", "textures/misc/yellow_fever_overlay.png");

    private static final ResourceLocation HEART_OVERLAY =
            ResourceLocation.fromNamespaceAndPath("selectivepowers", "textures/misc/rage_icon.png");

    @SubscribeEvent
    public static void renderYellowMask(RenderGuiEvent.Pre event) {

        Minecraft mc = Minecraft.getInstance();

        if (mc.player == null || mc.level == null) return;

        if (!mc.player.hasEffect(SelectivepowersEffects.YELLOW_FEVER_EFFECT)) return;

        GuiGraphics gui = event.getGuiGraphics();
        int w = event.getGuiGraphics().guiWidth();
        int h = event.getGuiGraphics().guiHeight();

        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        final int texW = 256;
        final int texH = 256;

        gui.blit(YELLOW_OVERLAY,
                0, 0,
                w, h,
                0, 0,
                texW, texH,
                texW, texH
        );

        RenderSystem.disableBlend();
        RenderSystem.enableDepthTest();
    }

    @SubscribeEvent
    public static void onRenderGui(RenderGuiLayerEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null) return;

        if (player.hasEffect(SelectivepowersEffects.RAGE_EFFECT) && !player.isCreative()  && !player.isSpectator()) {
            GuiGraphics gui = event.getGuiGraphics();

            renderCustomHearts(gui, player);
        }
    }

    private static void renderCustomHearts(GuiGraphics gui, Player player) {

        Minecraft mc = Minecraft.getInstance();

        int width = mc.getWindow().getGuiScaledWidth();
        int height = mc.getWindow().getGuiScaledHeight();

        int left = width / 2 - 91;
        int top = height - 39;

        int hearts = (int) Math.ceil(player.getHealth()/2);

        for (int i = 0; i < hearts; i++) {
            int x = left + ((i%10)*8);
            int overlayX = x + 4;
            int overlayY = top - 1-((i/10)*10);
            gui.pose().pushPose();
            gui.pose().translate(0, 0, 200);
            gui.blit(HEART_OVERLAY , overlayX, overlayY, 0,0,5, 5, 5, 5);
            gui.pose().popPose();
        }
    }
    @SubscribeEvent
    public static void onRenderLevel(RenderGuiEvent.Pre event) {
        Minecraft mc = Minecraft.getInstance();
            if (mc.player.hasEffect(SelectivepowersEffects.TRUTH_VISION_EFFECT)) {
                YellowPostProcessor.apply(event.getPartialTick().getGameTimeDeltaTicks());
                mc.getMainRenderTarget().bindWrite(true);
            }
    }
    @SubscribeEvent
    public static void onRegisterReloadListeners(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener((ResourceManagerReloadListener) rm -> YellowPostProcessor.load());
    }
}
