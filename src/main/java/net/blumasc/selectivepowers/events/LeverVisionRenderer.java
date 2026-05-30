package net.blumasc.selectivepowers.events;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.blumasc.selectivepowers.SelectivePowers;
import net.blumasc.selectivepowers.client.ClientPowerData;
import net.blumasc.selectivepowers.client.CustomRenderTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(
        modid = SelectivePowers.MODID,
        value = Dist.CLIENT
)
public class LeverVisionRenderer {

    private static final int SCAN_RADIUS = 32;
    private static final long SCAN_INTERVAL_MS = 500;

    private static List<BlockPos> cachedBlocks = List.of();
    private static long lastScanTime = 0;

    @SubscribeEvent
    public static void onRenderLevel(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_PARTICLES) return;

        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null || mc.level == null) return;
        if (!ClientPowerData.shouldDisplayLeverVision) return;

        long now = System.currentTimeMillis();
        if (now - lastScanTime > SCAN_INTERVAL_MS) {
            lastScanTime = now;
            cachedBlocks = scanBlocks(mc, player);
        }

        if (cachedBlocks.isEmpty()) return;

        long  tick  = mc.level.getGameTime();
        float pulse = (float) Math.sin(tick * Math.PI / 12.0);
        float alpha = 0.275f + 0.125f * pulse;

        Vec3 camera = mc.gameRenderer.getMainCamera().getPosition();
        Matrix4f matrix = event.getPoseStack().last().pose();
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        MultiBufferSource.BufferSource buf = mc.renderBuffers().bufferSource();
        VertexConsumer consumer = buf.getBuffer(CustomRenderTypes.REDSTONE_VISION);
        for (BlockPos pos : cachedBlocks) {
            BlockState state = mc.level.getBlockState(pos);
            if(state.is(BlockTags.BUTTONS)) {
                addBlock(consumer, matrix, pos, camera, alpha, 60, 140, 255);
            }else{
                addBlock(consumer, matrix, pos, camera, alpha, 140, 255, 60);
            }

        }

        buf.endBatch(CustomRenderTypes.REDSTONE_VISION);
        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
    }

    private static List<BlockPos> scanBlocks(Minecraft mc, LocalPlayer player) {

        BlockPos center = player.blockPosition();
        List<BlockPos> out = new ArrayList<>();

        for (BlockPos pos : BlockPos.betweenClosed(
                center.offset(-SCAN_RADIUS, -SCAN_RADIUS, -SCAN_RADIUS),
                center.offset(SCAN_RADIUS, SCAN_RADIUS, SCAN_RADIUS))) {

            BlockState state = mc.level.getBlockState(pos);

            if (state.is(BlockTags.BUTTONS) || state.is(Blocks.LEVER)) {
                out.add(pos.immutable());
            }
        }

        return out;
    }

    private static void addBlock(VertexConsumer c, Matrix4f m,
                                 BlockPos pos, Vec3 cam, float alpha, int r, int g, int b) {

        float size = 0.5f;
        float inset = (1f - size) / 2f;

        float x0 = (float)(pos.getX() - cam.x) + inset;
        float y0 = (float)(pos.getY() - cam.y) + inset;
        float z0 = (float)(pos.getZ() - cam.z) + inset;

        float x1 = x0 + size;
        float y1 = y0 + size;
        float z1 = z0 + size;
        int a = Math.round(alpha * 255);

        v(c,m, x0,y0,z1, r,g,b,a); v(c,m, x1,y0,z1, r,g,b,a);
        v(c,m, x1,y1,z1, r,g,b,a); v(c,m, x0,y1,z1, r,g,b,a);

        v(c,m, x1,y0,z0, r,g,b,a); v(c,m, x0,y0,z0, r,g,b,a);
        v(c,m, x0,y1,z0, r,g,b,a); v(c,m, x1,y1,z0, r,g,b,a);

        v(c,m, x1,y0,z1, r,g,b,a); v(c,m, x1,y0,z0, r,g,b,a);
        v(c,m, x1,y1,z0, r,g,b,a); v(c,m, x1,y1,z1, r,g,b,a);

        v(c,m, x0,y0,z0, r,g,b,a); v(c,m, x0,y0,z1, r,g,b,a);
        v(c,m, x0,y1,z1, r,g,b,a); v(c,m, x0,y1,z0, r,g,b,a);

        v(c,m, x0,y1,z1, r,g,b,a); v(c,m, x1,y1,z1, r,g,b,a);
        v(c,m, x1,y1,z0, r,g,b,a); v(c,m, x0,y1,z0, r,g,b,a);

        v(c,m, x0,y0,z0, r,g,b,a); v(c,m, x1,y0,z0, r,g,b,a);
        v(c,m, x1,y0,z1, r,g,b,a); v(c,m, x0,y0,z1, r,g,b,a);
    }

    private static void v(VertexConsumer c, Matrix4f m,
                          float x, float y, float z,
                          int r, int g, int b, int a) {
        c.addVertex(m, x, y, z)
                .setColor(r, g, b, a)
                .setLight(0xF000F0);
    }
}