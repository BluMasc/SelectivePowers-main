package net.blumasc.selectivepowers.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.blumasc.selectivepowers.SelectivePowers;
import net.blumasc.selectivepowers.block.entity.ItemSentinelBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;

public class ItemSentinelBlockEntityRenderer implements BlockEntityRenderer<ItemSentinelBlockEntity> {
    public ItemSentinelBlockEntityRenderer(BlockEntityRendererProvider.Context context){

    }

    @Override
    public void render(ItemSentinelBlockEntity altarBlockEntity, float pPartialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int pPackedLight, int pPactOverlay) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemStack stack = altarBlockEntity.getItem(0);

        poseStack.pushPose();
        poseStack.translate(0.5f, 0.30f, 0.5f);
        poseStack.scale(0.3f,0.3f,0.3f);
        float rot = altarBlockEntity.getRenderingRotation();
        poseStack.mulPose(Axis.YP.rotationDegrees(rot));

        int packedLight = getLightLevel(altarBlockEntity.getLevel(),altarBlockEntity.getBlockPos());

        itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, packedLight ,
                OverlayTexture.NO_OVERLAY, poseStack, multiBufferSource, altarBlockEntity.getLevel(),1);
        poseStack.popPose();

    }

    private int getLightLevel(Level level, BlockPos pos){
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }


}
