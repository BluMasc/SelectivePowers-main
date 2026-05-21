package net.blumasc.selectivepowers.entity.client;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.blumasc.selectivepowers.SelectivePowers;
import net.blumasc.selectivepowers.entity.CircleVariant;
import net.blumasc.selectivepowers.entity.custom.projectile.WhirlpoolEntity;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BeaconRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public class WhirlpoolRenderer extends EntityRenderer<WhirlpoolEntity> {

    private static final ResourceLocation TEXTURE= ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "textures/entity/whirlpool/whirlpool_bottom.png");
    private static final ResourceLocation TEXTURE_TOP= ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "textures/entity/whirlpool/whirlpool_top.png");
    private static final ResourceLocation TEXTURE_TOP_TOP= ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "textures/entity/whirlpool/whirlpool_top_top.png");


    public WhirlpoolRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(WhirlpoolEntity entity, float entityYaw, float partialTicks,
                       PoseStack poseStack, MultiBufferSource buffer, int packedLight) {

        poseStack.pushPose();
        poseStack.translate(0.0, 0.01, 0.0);
        poseStack.mulPose(Axis.YP.rotationDegrees(entity.rotation));
        float size = entity.getSize();
        poseStack.scale(size, 1.0f, size);

        PoseStack.Pose posestackPose = poseStack.last();
        VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(getTextureLocation(entity)));

        vertex(consumer, posestackPose, packedLight, -0.5f, 0f, -0.5f, 0f, 0f);
        vertex(consumer, posestackPose, packedLight, -0.5f, 0f,  0.5f, 0f, 1f);
        vertex(consumer, posestackPose, packedLight,  0.5f, 0f,  0.5f, 1f, 1f);
        vertex(consumer, posestackPose, packedLight,  0.5f, 0f, -0.5f, 1f, 0f);

        poseStack.popPose();
        poseStack.pushPose();
        poseStack.translate(0.0, 0.012, 0.0);
        poseStack.mulPose(Axis.YP.rotationDegrees(entity.rotation*1.5f));
        poseStack.scale(size, 1.0f, size);

        PoseStack.Pose posestacktPose = poseStack.last();
        VertexConsumer consumert = buffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE_TOP));

        vertex(consumert, posestacktPose, packedLight, -0.5f, 0f, -0.5f, 0f, 0f);
        vertex(consumert, posestacktPose, packedLight, -0.5f, 0f,  0.5f, 0f, 1f);
        vertex(consumert, posestacktPose, packedLight,  0.5f, 0f,  0.5f, 1f, 1f);
        vertex(consumert, posestacktPose, packedLight,  0.5f, 0f, -0.5f, 1f, 0f);

        poseStack.popPose();

        poseStack.pushPose();
        poseStack.translate(0.0, 0.011, 0.0);
        poseStack.mulPose(Axis.YP.rotationDegrees(entity.rotation*0.7f));
        poseStack.scale(size, 1.0f, size);

        PoseStack.Pose posestackttPose = poseStack.last();
        VertexConsumer consumertt = buffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE_TOP_TOP));

        vertex(consumertt, posestackttPose, packedLight, -0.5f, 0f, -0.5f, 0f, 0f);
        vertex(consumertt, posestackttPose, packedLight, -0.5f, 0f,  0.5f, 0f, 1f);
        vertex(consumertt, posestackttPose, packedLight,  0.5f, 0f,  0.5f, 1f, 1f);
        vertex(consumertt, posestackttPose, packedLight,  0.5f, 0f, -0.5f, 1f, 0f);

        poseStack.popPose();

        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(WhirlpoolEntity WhirlpoolEntity) {
        return TEXTURE;
    }

    private static void vertex(VertexConsumer consumer, PoseStack.Pose pose, int light, float x, float y, float z, float u, float v) {
        consumer.addVertex(pose, x, y, z) // Z=-0.5 to render flat
                .setColor(255, 255, 255, 255)
                .setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(pose, 0.0F, 1.0F, 0.0F);
    }
}
