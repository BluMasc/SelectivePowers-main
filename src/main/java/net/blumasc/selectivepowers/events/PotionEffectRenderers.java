package net.blumasc.selectivepowers.events;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.blumasc.selectivepowers.SelectivePowers;
import net.blumasc.selectivepowers.client.ClientDiviningData;
import net.blumasc.selectivepowers.effect.SelectivepowersEffects;
import net.blumasc.selectivepowers.effect.custom.helper.ClientHallucinationHandler;
import net.blumasc.selectivepowers.entity.client.fakeMob.FakeMob;
import net.blumasc.selectivepowers.shader.ShadowPostProcessor;
import net.blumasc.selectivepowers.shader.YellowPostProcessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
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
    public static void renderYellowFilter(RenderGuiEvent.Pre event) {

        Minecraft mc = Minecraft.getInstance();

        if (mc.player == null || mc.level == null) return;

        YellowPostProcessor.INSTANCE.setActive(mc.player.hasEffect(SelectivepowersEffects.TRUTH_VISION_EFFECT));

        ShadowPostProcessor.INSTANCE.setActive(mc.player.hasEffect(SelectivepowersEffects.DRAKNESS_EFFECT));

    }
    @SubscribeEvent
    public static void onRenderLevel(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_ENTITIES) return;

        Minecraft mc = Minecraft.getInstance();
        EntityRenderDispatcher dispatcher = mc.getEntityRenderDispatcher();
        MultiBufferSource.BufferSource buffer =
                mc.renderBuffers().bufferSource();

        Vec3 camPos = event.getCamera().getPosition();
        PoseStack poseStack = event.getPoseStack();

        for (FakeMob fake : ClientHallucinationHandler.getActiveMobs()) {

            Entity mob = fake.getEntity();

            double x = mob.getX() - camPos.x;
            double y = mob.getY() - camPos.y;
            double z = mob.getZ() - camPos.z;

            BlockPos mobPos = new BlockPos((int) mob.getX(), (int) mob.getY(), (int) mob.getZ());
            int blockLight = mob.level().getBrightness(LightLayer.BLOCK, mobPos);
            int skyLight   = mob.level().getBrightness(LightLayer.SKY, mobPos);
            int packedLight = LightTexture.pack(blockLight, skyLight);
            dispatcher.render(
                    mob,
                    x, y, z,
                    fake.getEntity().getYRot(),
                    event.getPartialTick().getGameTimeDeltaPartialTick(true),
                    poseStack,
                    buffer,
                    packedLight
            );
        }
    }
    @SubscribeEvent
    public static void onClientPlayerTick(PlayerTickEvent.Pre event) {
        if (!(event.getEntity() instanceof LocalPlayer player)) return;
        if (!player.level().isClientSide) return;

        ClientHallucinationHandler.tickPlayer(player);
    }

    private static final Minecraft mc = Minecraft.getInstance();
    private static final ResourceLocation ARROW_TEXTURE = ResourceLocation.fromNamespaceAndPath("selectivepowers", "textures/gui/arrow.png");
    private static final int ARROW_SIZE = 5;
    private static final float DISTANCE_FROM_CROSSHAIR = 10f;

    @SubscribeEvent
    public static void onRenderDiviningGui(RenderGuiLayerEvent.Post event) {
        LocalPlayer player = mc.player;
        if (player == null) return;

        GuiGraphics gui = event.getGuiGraphics();

        int centerX = mc.getWindow().getGuiScaledWidth() / 2;
        int centerY = mc.getWindow().getGuiScaledHeight() / 2;

        Vec3 playerPos = player.position();

        for (Vec3 mobPos : ClientDiviningData.EnemyPositions) {
            renderArrow(gui, centerX, centerY, playerPos, mobPos, 0xFFFF0000);
        }

        for (Vec3 otherPlayerPos : ClientDiviningData.PlayerPositions) {
            renderArrow(gui, centerX, centerY, playerPos, otherPlayerPos, 0xFF0000FF);
        }
    }

    private static void renderArrow(GuiGraphics gui, int centerX, int centerY, Vec3 playerPos, Vec3 targetPos, int color) {
        double dx = targetPos.x - playerPos.x;
        double dz = targetPos.z - playerPos.z;

        double angle = Math.toDegrees(Math.atan2(dz, dx)) - mc.player.getYRot();

        double rad = Math.toRadians(angle);

        int x = (int) (centerX - Math.cos(rad) * DISTANCE_FROM_CROSSHAIR);
        int y = (int) (centerY - Math.sin(rad) * DISTANCE_FROM_CROSSHAIR);

        gui.pose().pushPose();
        gui.pose().translate(x, y, 200);
        gui.pose().mulPose(com.mojang.math.Axis.ZP.rotationDegrees((float) angle-90));

        float r = ((color >> 16) & 0xFF) / 255f;
        float g = ((color >> 8) & 0xFF) / 255f;
        float b = (color & 0xFF) / 255f;
        float a = ((color >> 24) & 0xFF) / 255f;

        RenderSystem.setShaderColor(r, g, b, a);

        gui.blit(ARROW_TEXTURE, -ARROW_SIZE / 2, -ARROW_SIZE / 2, ARROW_SIZE, ARROW_SIZE,
                0, 0, ARROW_SIZE, ARROW_SIZE, ARROW_SIZE, ARROW_SIZE);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        gui.pose().popPose();
    }
}
