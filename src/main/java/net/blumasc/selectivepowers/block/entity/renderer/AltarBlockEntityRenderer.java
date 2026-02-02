package net.blumasc.selectivepowers.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.blumasc.selectivepowers.block.entity.AltarBlockEntity;
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

public class AltarBlockEntityRenderer implements BlockEntityRenderer<AltarBlockEntity> {
    public AltarBlockEntityRenderer(BlockEntityRendererProvider.Context context){

    }

    @Override
    public void render(AltarBlockEntity altarBlockEntity, float pPartialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int pPackedLight, int pPactOverlay) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemStack stack = altarBlockEntity.inventory.getStackInSlot(0);

        poseStack.pushPose();
        poseStack.translate(0.5f, 1.35f, 0.5f);
        poseStack.scale(0.5f,0.5f,0.5f);
        poseStack.mulPose(Axis.YP.rotationDegrees(altarBlockEntity.getRenderingRotation()));

        int packedLight = getLightLevel(altarBlockEntity.getLevel(),altarBlockEntity.getBlockPos());

        itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, packedLight ,
                OverlayTexture.NO_OVERLAY, poseStack, multiBufferSource, altarBlockEntity.getLevel(),1);
        poseStack.popPose();

        renderCornerItem(altarBlockEntity, altarBlockEntity.inventory.getStackInSlot(1), poseStack, multiBufferSource, packedLight,
                0.1f, 0.1f, 315);

        renderCornerItem(altarBlockEntity, altarBlockEntity.inventory.getStackInSlot(2), poseStack, multiBufferSource, packedLight,
                0.9f, 0.1f, 45);

        renderCornerItem(altarBlockEntity, altarBlockEntity.inventory.getStackInSlot(3), poseStack, multiBufferSource, packedLight,
                0.1f, 0.9f, 225);

        renderCornerItem(altarBlockEntity, altarBlockEntity.inventory.getStackInSlot(4), poseStack, multiBufferSource, packedLight,
                0.9f, 0.9f, 135);
    }

    private int getLightLevel(Level level, BlockPos pos){
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }

    private void renderCornerItem(AltarBlockEntity entity, ItemStack stack, PoseStack poseStack, MultiBufferSource buffer, int light,
                                  float xOffset, float zOffset, float yRot) {

        if (stack.isEmpty()) return;

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

        poseStack.pushPose();

        poseStack.translate(xOffset, 1.08f, zOffset);

        poseStack.mulPose(Axis.XP.rotationDegrees(90));

        poseStack.mulPose(Axis.ZP.rotationDegrees(yRot));

        poseStack.scale(0.25f, 0.25f, 0.25f);

        itemRenderer.renderStatic(
                stack,
                ItemDisplayContext.FIXED,
                light,
                OverlayTexture.NO_OVERLAY,
                poseStack,
                buffer,
                entity.getLevel(),
                0
        );

        poseStack.popPose();
    }
}
