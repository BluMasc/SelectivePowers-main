package net.blumasc.selectivepowers.entity.client.yellowking;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.blumasc.selectivepowers.SelectivePowers;
import net.blumasc.selectivepowers.entity.custom.YellowKingEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class YellowKingRenderer extends MobRenderer<YellowKingEntity, YellowKingModel<YellowKingEntity>> {

    public YellowKingRenderer(EntityRendererProvider.Context context) {
        super(context, new YellowKingModel<>(context.bakeLayer(YellowKingModel.LAYER_LOCATION)), 0.5f);
    }

    @Override
    public void render(
            YellowKingEntity entity,
            float entityYaw,
            float partialTick,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight
    ) {
        Minecraft mc = Minecraft.getInstance();

        if (mc.gameRenderer != null && mc.level != null) {
            Vec3 cameraPos = mc.gameRenderer.getMainCamera().getPosition();

            double ex = Mth.lerp(partialTick, entity.xOld, entity.getX());
            double ey = Mth.lerp(partialTick, entity.yOld, entity.getY());
            double ez = Mth.lerp(partialTick, entity.zOld, entity.getZ());

            Vec3 entityPos = new Vec3(ex, ey + entity.getBbHeight() * 0.5, ez);

            Vec3 dir = cameraPos.subtract(entityPos);
            double dx = dir.x;
            double dz = dir.z;

            float targetYaw = (float) Math.toDegrees(Math.atan2(dz, dx)) - 90f;

            float entityRenderYaw = Mth.rotLerp(partialTick, entity.yRotO, entity.getYRot());

            final float MODEL_YAW_OFFSET = 180f;

            float yawDelta = Mth.wrapDegrees(targetYaw - (entityRenderYaw + MODEL_YAW_OFFSET));

            poseStack.pushPose();

            poseStack.mulPose(Axis.YP.rotationDegrees(-yawDelta));


            super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);

            poseStack.popPose();

            return;
        }

        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(YellowKingEntity yellowKingEntity) {
        return ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "textures/entity/yellow_king/texture.png");
    }
}

