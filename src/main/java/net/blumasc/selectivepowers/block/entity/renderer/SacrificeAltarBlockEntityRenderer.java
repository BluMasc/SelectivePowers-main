package net.blumasc.selectivepowers.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.blumasc.selectivepowers.SelectivePowers;
import net.blumasc.selectivepowers.block.entity.AltarBlockEntity;
import net.blumasc.selectivepowers.block.entity.SacrificeAltarBlockEntity;
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

public class SacrificeAltarBlockEntityRenderer implements BlockEntityRenderer<SacrificeAltarBlockEntity> {
    private static final ResourceLocation PLANE_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "textures/misc/experience_fluid.png");
    public SacrificeAltarBlockEntityRenderer(BlockEntityRendererProvider.Context context){

    }

    @Override
    public void render(SacrificeAltarBlockEntity altarBlockEntity, float pPartialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int pPackedLight, int pPactOverlay) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemStack stack = altarBlockEntity.inventory.getStackInSlot(0);

        poseStack.pushPose();
        poseStack.translate(0.5f, 1.30f, 0.5f);
        poseStack.scale(0.5f,0.5f,0.5f);
        float rot = altarBlockEntity.getRenderingRotation();
        poseStack.mulPose(Axis.YP.rotationDegrees(rot));

        int packedLight = getLightLevel(altarBlockEntity.getLevel(),altarBlockEntity.getBlockPos());

        itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, packedLight ,
                OverlayTexture.NO_OVERLAY, poseStack, multiBufferSource, altarBlockEntity.getLevel(),1);
        poseStack.popPose();
        if(altarBlockEntity.filled)
        {
            renderPlane(poseStack, multiBufferSource, altarBlockEntity.simple? 0.35d:0.42d, rot);
        }

    }

    private int getLightLevel(Level level, BlockPos pos){
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }

    private void renderPlane(PoseStack poseStack,
                             MultiBufferSource buffer, double pos,
                             float rot) {

        int light = LightTexture.FULL_BRIGHT;
        poseStack.pushPose();

        poseStack.translate(0.5D, pos, 0.5D);
        poseStack.mulPose(Axis.XP.rotationDegrees(90));

        float size = 10f / 16f;
        float half = size / 2f;

        PoseStack.Pose pose = poseStack.last();
        VertexConsumer consumer = buffer.getBuffer(RenderType.entityTranslucentEmissive(PLANE_TEXTURE));

        int numSlices = 20;
        float speed = 3.0f;
        int sliceIndex = (int)(((rot * speed) / 360.0) * numSlices) % numSlices;
        float vMin = sliceIndex / (float) numSlices;
        float vMax = vMin + (1f / numSlices);

        consumer.addVertex(pose, -half, -half, -0.5F)
                .setColor(255, 255, 255, 200)
                .setUv(0.0F, vMax)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(pose, 0.0F, 1.0F, 1.0F);

        consumer.addVertex(pose, half, -half, -0.5F)
                .setColor(255, 255, 255, 200)
                .setUv(1.0F, vMax)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(pose, 0.0F, 1.0F, 1.0F);

        consumer.addVertex(pose, half, half, -0.5F)
                .setColor(255, 255, 255, 200)
                .setUv(1.0F, vMin)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(pose, 0.0F, 1.0F, 1.0F);

        consumer.addVertex(pose, -half, half, -0.5F)
                .setColor(255, 255, 255, 200)
                .setUv(0.0F, vMin)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(pose, 0.0F, 1.0F, 1.0F);

        poseStack.popPose();
    }


}
