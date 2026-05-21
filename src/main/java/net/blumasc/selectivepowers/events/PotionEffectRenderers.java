package net.blumasc.selectivepowers.events;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.blumasc.blubasics.client.ClientEffectData;
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
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.joml.Matrix4f;

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
    private static final ResourceLocation BOX_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(
                    SelectivePowers.MODID,
                    "textures/effect/bubbled.png"
            );

    @SubscribeEvent
    public static void onRenderLiving(RenderLivingEvent.Post<?, ?> event) {
        LivingEntity entity = event.getEntity();

        if (!ClientEffectData.hasEffect(entity.getUUID(), SelectivepowersEffects.BUBBLE_EFFECT.getKey().location()))
            return;

        PoseStack ps = event.getPoseStack();
        MultiBufferSource buffers = event.getMultiBufferSource();

        AABB box = entity.getBoundingBox();

        float width = entity.getBbWidth() + 0.1f;
        float height = entity.getBbHeight() + 0.1f;

        float hw = width / 2f;

        ps.pushPose();
        ps.translate(0, 0.05f, 0);

        VertexConsumer vc =
                buffers.getBuffer(RenderType.entityTranslucent(BOX_TEXTURE));

        Matrix4f mat = ps.last().pose();
        int light = event.getPackedLight();

        float x0 = -hw;
        float x1 = hw;

        float y0 = 0f;
        float y1 = height;

        float z0 = -hw;
        float z1 = hw;

        quad(vc, mat, light,
                x0, y1, z1,
                x1, y1, z1,
                x1, y0, z1,
                x0, y0, z1);

        quad(vc, mat, light,
                x1, y1, z0,
                x0, y1, z0,
                x0, y0, z0,
                x1, y0, z0);

        quad(vc, mat, light,
                x1, y1, z1,
                x1, y1, z0,
                x1, y0, z0,
                x1, y0, z1);

        quad(vc, mat, light,
                x0, y1, z0,
                x0, y1, z1,
                x0, y0, z1,
                x0, y0, z0);

        quad(vc, mat, light,
                x0, y1, z0,
                x1, y1, z0,
                x1, y1, z1,
                x0, y1, z1);

        quad(vc, mat, light,
                x0, y0, z1,
                x1, y0, z1,
                x1, y0, z0,
                x0, y0, z0);

        ps.popPose();
    }

    private static void quad(
            VertexConsumer vc,
            Matrix4f mat,
            int light,

            float x0, float y0, float z0,
            float x1, float y1, float z1,
            float x2, float y2, float z2,
            float x3, float y3, float z3
    ) {
        vc.addVertex(mat, x0, y0, z0)
                .setColor(1f, 1f, 1f, 0.7f)
                .setUv(0, 0)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(0, 1, 0);

        vc.addVertex(mat, x1, y1, z1)
                .setColor(1f, 1f, 1f, 0.7f)
                .setUv(1, 0)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(0, 1, 0);

        vc.addVertex(mat, x2, y2, z2)
                .setColor(1f, 1f, 1f, 0.7f)
                .setUv(1, 1)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(0, 1, 0);

        vc.addVertex(mat, x3, y3, z3)
                .setColor(1f, 1f, 1f, 0.7f)
                .setUv(0, 1)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(0, 1, 0);
    }
}
