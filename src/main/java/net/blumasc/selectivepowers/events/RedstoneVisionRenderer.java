package net.blumasc.selectivepowers.events;

import com.mojang.blaze3d.systems.RenderSystem;
import net.blumasc.selectivepowers.SelectivePowers;
import net.blumasc.selectivepowers.client.CustomRenderTypes;
import net.blumasc.selectivepowers.item.SelectivepowersItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import com.mojang.blaze3d.vertex.VertexConsumer;
import org.joml.Matrix4f;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@EventBusSubscriber(
    modid = SelectivePowers.MODID,
    value = Dist.CLIENT
)
public class RedstoneVisionRenderer {
    private static final int  SCAN_RADIUS      = 32;
    private static final long SCAN_INTERVAL_MS = 500;
    private static List<BlockPos> cachedBlocks = List.of();
    private static long           lastScanTime = 0;
    @SubscribeEvent
    public static void onRenderLevel(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_PARTICLES) return;

        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null || mc.level == null) return;

        if (!isWearingCurioHeadItem(player, SelectivepowersItems.REDSTONE_VIZER.get())) return;

        if (!player.isShiftKeyDown()) {
            cachedBlocks = List.of();
            return;
        }

        long now = System.currentTimeMillis();
        if (now - lastScanTime > SCAN_INTERVAL_MS) {
            lastScanTime = now;
            cachedBlocks  = scanBlocks(mc, player);
        }

        if (cachedBlocks.isEmpty()) return;
        long  tick  = mc.level.getGameTime();
        float pulse = (float) Math.sin(tick * Math.PI / 12.0);
        float alpha = 0.275f + 0.125f * pulse;
        Vec3     camera   = mc.gameRenderer.getMainCamera().getPosition();
        Matrix4f matrix   = event.getPoseStack().last().pose();

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);

        MultiBufferSource.BufferSource buf = mc.renderBuffers().bufferSource();
        VertexConsumer consumer = buf.getBuffer(CustomRenderTypes.REDSTONE_VISION);

        for (BlockPos pos : cachedBlocks) {
            addBlock(consumer, matrix, pos, camera, alpha);
        }

        buf.endBatch(CustomRenderTypes.REDSTONE_VISION);

        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
    }
    private static List<BlockPos> scanBlocks(Minecraft mc, LocalPlayer player) {
        BlockPos     center = player.blockPosition();
        List<BlockPos> out  = new ArrayList<>();

        for (BlockPos pos : BlockPos.betweenClosed(
                center.offset(-SCAN_RADIUS, -SCAN_RADIUS, -SCAN_RADIUS),
                center.offset( SCAN_RADIUS,  SCAN_RADIUS,  SCAN_RADIUS))) {
            assert mc.level != null;
            BlockState state = mc.level.getBlockState(pos);
            if (state.is(Blocks.REDSTONE_WIRE)) {
                out.add(pos.immutable());
            }
        }
        return out;
    }
    private static void addBlock(VertexConsumer c, Matrix4f m,
                                 BlockPos pos, Vec3 cam, float alpha) {

        float inset = 0.2f;

        float x0 = (float)(pos.getX() - cam.x) + inset;
        float y  = (float)(pos.getY() - cam.y) + 0.02f;
        float z0 = (float)(pos.getZ() - cam.z) + inset;

        float x1 = x0 + 1f - inset * 2f;
        float z1 = z0 + 1f - inset * 2f;

        int r = 255;
        int g = 40;
        int b = 10;
        int a = Math.round(alpha * 255);
        v(c, m, x0, y, z1, r,g,b,a);
        v(c, m, x1, y, z1, r,g,b,a);
        v(c, m, x1, y, z0, r,g,b,a);
        v(c, m, x0, y, z0, r,g,b,a);
    }

    private static void v(VertexConsumer c, Matrix4f m,
                           float x, float y, float z,
                           int r, int g, int b, int a) {
        c.addVertex(m, x, y, z).setColor(r, g, b, a).setLight(0xF000F0);
    }

    private static boolean isWearingCurioHeadItem(Player player, Item expectedItem) {
        return CuriosApi.getCuriosInventory(player)
                .flatMap(curiosInventory -> curiosInventory.getStacksHandler("head"))
                .map(handler -> {
                    for (int slot = 0; slot < handler.getSlots(); slot++) {
                        ItemStack stack = handler.getStacks().getStackInSlot(slot);
                        if (!stack.isEmpty() && stack.is(expectedItem)) {
                            return true;
                        }
                    }
                    return false;
                })
                .orElse(false);
    }
}