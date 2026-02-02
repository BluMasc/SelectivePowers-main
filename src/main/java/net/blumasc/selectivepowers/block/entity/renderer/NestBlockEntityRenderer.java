package net.blumasc.selectivepowers.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.blumasc.selectivepowers.block.entity.AltarBlockEntity;
import net.blumasc.selectivepowers.block.entity.NestBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;

public class NestBlockEntityRenderer implements BlockEntityRenderer<NestBlockEntity> {

    public NestBlockEntityRenderer(BlockEntityRendererProvider.Context context) { }

    @Override
    public void render(NestBlockEntity nestBlockEntity, float partialTick, PoseStack poseStack,
                       MultiBufferSource buffer, int packedLight, int packedOverlay) {

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        Level level = nestBlockEntity.getLevel();
        BlockPos pos = nestBlockEntity.getBlockPos();

        int light = getLightLevel(level, pos);
        placeItem(nestBlockEntity.inventory.getStackInSlot(0), poseStack, itemRenderer, light, buffer, level, 0.5f, 0.13f, 0.37f, 40f, 180f, 0f);
        placeItem(nestBlockEntity.inventory.getStackInSlot(1), poseStack, itemRenderer, light, buffer, level, 0.60f, 0.13f, 0.60f, -40f, -30f, 30f);
        placeItem(nestBlockEntity.inventory.getStackInSlot(2), poseStack, itemRenderer, light, buffer, level, 0.41f, 0.13f, 0.60f, -40f, 50f, -30f);

    }

    private int getLightLevel(Level level, BlockPos pos){
        int blockLight = level.getBrightness(LightLayer.BLOCK, pos);
        int skyLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(blockLight, skyLight);
    }

    private void placeItem(ItemStack stack, PoseStack poseStack, ItemRenderer itemRenderer, int light, MultiBufferSource buffer, Level level,float x, float y, float z, float xRot, float zRot, float yRot)
    {
        if (stack.isEmpty()) return;

        poseStack.pushPose();

        poseStack.translate(x, y, z);

        poseStack.mulPose(Axis.XP.rotationDegrees(90 + xRot));
        poseStack.mulPose(Axis.ZP.rotationDegrees(zRot));
        poseStack.mulPose(Axis.YP.rotationDegrees(yRot));

        // Scale down
        poseStack.scale(0.2f, 0.2f, 0.2f);

        itemRenderer.renderStatic(
                stack,
                ItemDisplayContext.FIXED,
                light,
                OverlayTexture.NO_OVERLAY,
                poseStack,
                buffer,
                level,
                0
        );

        poseStack.popPose();
    }
}
