package net.blumasc.selectivepowers.entity.client.anchor;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.blumasc.selectivepowers.SelectivePowers;
import net.blumasc.selectivepowers.entity.custom.AnchorEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public class AnchorEntityRenderer extends EntityRenderer<AnchorEntity> {

    private static final ResourceLocation ANCHOR_TEXTURE =
        ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "textures/item/anchor_3d.png");

    private final ModelPart anchorModel;

    private static final double MODEL_Y_OFFSET = -1.5;

    public AnchorEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.anchorModel = context.bakeLayer(AnchorModel.LAYER_LOCATION);
    }

    @Override
    public void render(AnchorEntity entity, float entityYaw, float partialTick,
                       PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {

        Entity owner = entity.getOwner();
        if (owner != null) {
            renderRope(entity, partialTick, poseStack, bufferSource, owner);
        }
        // Render anchor model
        poseStack.pushPose();

        if (entity.isStuck()) {

            poseStack.mulPose(
                    Axis.YP.rotationDegrees(entity.getStuckYaw())
            );

            poseStack.mulPose(
                    Axis.XP.rotationDegrees(-entity.getStuckPitch())
            );

            // model correction
            poseStack.mulPose(Axis.XP.rotationDegrees(90));

        } else {

            Vec3 vel = entity.getDeltaMovement();

            if (vel.lengthSqr() > 0.0001) {

                float yaw = (float)(
                        Math.atan2(vel.x, vel.z) * 180F / Math.PI
                );

                float pitch = (float)(
                        Math.atan2(
                                vel.y,
                                Math.sqrt(vel.x * vel.x + vel.z * vel.z)
                        ) * 180F / Math.PI
                );

                poseStack.mulPose(Axis.YP.rotationDegrees(yaw));

                poseStack.mulPose(Axis.XP.rotationDegrees(-pitch));

                // model correction
                poseStack.mulPose(Axis.XP.rotationDegrees(90));

                if (entity.isReturning()) {
                    poseStack.mulPose(Axis.ZP.rotationDegrees(180));
                }
            }
        }

        VertexConsumer consumer =
                bufferSource.getBuffer(RenderType.entityCutoutNoCull(ANCHOR_TEXTURE));

        poseStack.translate(0, MODEL_Y_OFFSET, 0);

        anchorModel.render(
                poseStack,
                consumer,
                packedLight,
                OverlayTexture.NO_OVERLAY
        );

        poseStack.popPose();

        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    private void renderRope(
            AnchorEntity entity,
            float partialTick,
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            Entity holder
    ) {
        Vec3 holderPos = holder.getRopeHoldPosition(partialTick);
        Vec3 anchorPos = getAnchorPos(entity, partialTick);

        // Offset from entity origin to the ring/attachment point on the model.
        // MODEL_Y_OFFSET (-1.5) in model-local Y = 1.5 units behind travel direction.
        Vec3 travelDir = getTravelDirection(entity);
        Vec3 attachOffset = travelDir.lengthSqr() > 0.0001
                ? travelDir.scale(MODEL_Y_OFFSET/1.5)
                : Vec3.ZERO;

        // dx/dy/dz goes from the attachment point (not entity origin) to holder
        Vec3 attachPoint = anchorPos.add(attachOffset);
        double dx = holderPos.x - attachPoint.x;
        double dy = holderPos.y - attachPoint.y;
        double dz = holderPos.z - attachPoint.z;

        // Move poseStack origin TO the attachment point so f=0 vertices land there
        poseStack.pushPose();
        poseStack.translate(attachOffset.x, attachOffset.y, attachOffset.z);

        VertexConsumer consumer = bufferSource.getBuffer(RenderType.leash());
        Matrix4f matrix = poseStack.last().pose();

        float x = (float) dx;
        float y = (float) dy;
        float z = (float) dz;

        float horizontal = Mth.invSqrt(x * x + z * z) * 0.025F / 2.0F;
        float offsetX = z * horizontal;
        float offsetZ = x * horizontal;

        BlockPos blockpos  = BlockPos.containing(attachPoint);
        BlockPos blockpos1 = BlockPos.containing(holderPos);

        int blockLightStart = this.getBlockLightLevel(entity, blockpos);
        int blockLightEnd   = holder.level().getBrightness(LightLayer.BLOCK, blockpos1);
        int skyLightStart   = entity.level().getBrightness(LightLayer.SKY, blockpos);
        int skyLightEnd     = holder.level().getBrightness(LightLayer.SKY, blockpos1);

        for (int i = 0; i <= 24; ++i) {
            addVertexPair(consumer, matrix, x, y, z,
                    blockLightStart, blockLightEnd, skyLightStart, skyLightEnd,
                    0.025F, 0.025F, offsetX, offsetZ, i, false);
        }
        for (int i = 24; i >= 0; --i) {
            addVertexPair(consumer, matrix, x, y, z,
                    blockLightStart, blockLightEnd, skyLightStart, skyLightEnd,
                    0.025F, 0.0F, offsetX, offsetZ, i, true);
        }

        poseStack.popPose();
    }

    private static void addVertexPair(
            VertexConsumer consumer,
            Matrix4f matrix,
            float x,
            float y,
            float z,
            int blockLightStart,
            int blockLightEnd,
            int skyLightStart,
            int skyLightEnd,
            float width1,
            float width2,
            float offsetX,
            float offsetZ,
            int index,
            boolean reverse
    ) {

        float f = index / 24.0F;

        int blockLight = (int)Mth.lerp(f, blockLightStart, blockLightEnd);
        int skyLight = (int)Mth.lerp(f, skyLightStart, skyLightEnd);

        int packedLight = LightTexture.pack(blockLight, skyLight);

        float brightness = index % 2 == (reverse ? 1 : 0) ? 0.7F : 1.0F;

        float r = 0.5F * brightness;
        float g = 0.4F * brightness;
        float b = 0.3F * brightness;

        float px = x * f;
        float py = y > 0.0F
                ? y * f * f
                : y - y * (1.0F - f) * (1.0F - f);

        float pz = z * f;

        consumer.addVertex(matrix,
                        px - offsetX,
                        py + width2,
                        pz + offsetZ)
                .setColor(r, g, b, 1.0F)
                .setLight(packedLight);

        consumer.addVertex(matrix,
                        px + offsetX,
                        py + width1 - width2,
                        pz - offsetZ)
                .setColor(r, g, b, 1.0F)
                .setLight(packedLight);
    }

    private Vec3 getAnchorPos(AnchorEntity entity, float partialTick) {
        return new Vec3(
                Mth.lerp(partialTick, entity.xOld, entity.getX()),
                Mth.lerp(partialTick, entity.yOld, entity.getY()),
                Mth.lerp(partialTick, entity.zOld, entity.getZ())
        );
    }
    /** Returns the unit vector the anchor is currently flying toward. */
    private Vec3 getTravelDirection(AnchorEntity entity) {
        if (entity.isStuck()) {
            // Reconstruct from the stored yaw/pitch (same convention as the renderer rotations)
            double yawRad   = entity.getStuckYaw()   * Mth.DEG_TO_RAD;
            double pitchRad = entity.getStuckPitch() * Mth.DEG_TO_RAD;
            double cosPitch = Math.cos(pitchRad);
            return new Vec3(
                    cosPitch * Math.sin(yawRad),
                    Math.sin(pitchRad),
                    cosPitch * Math.cos(yawRad)
            );
        }
        Vec3 vel = entity.getDeltaMovement();
        return vel.lengthSqr() > 0.0001 ? vel.normalize() : Vec3.ZERO;
    }

    @Override
    public ResourceLocation getTextureLocation(AnchorEntity entity) {
        return ANCHOR_TEXTURE;
    }
}