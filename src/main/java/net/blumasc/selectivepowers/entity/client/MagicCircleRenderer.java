package net.blumasc.selectivepowers.entity.client;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.blumasc.selectivepowers.SelectivePowers;
import net.blumasc.selectivepowers.entity.CircleVariant;
import net.blumasc.selectivepowers.entity.PackwingVariant;
import net.blumasc.selectivepowers.entity.custom.projectile.MagicCircleEntity;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BeaconRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

import java.awt.*;
import java.util.Map;

public class MagicCircleRenderer extends EntityRenderer<MagicCircleEntity> {

    private static final Map<CircleVariant, ResourceLocation> LOCATION_BY_VARIANT=
            Util.make(Maps.newEnumMap(CircleVariant.class), map ->{
                map.put(CircleVariant.SUN,
                        ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "textures/entity/spell_circle/sun_circle.png"));
                map.put(CircleVariant.MOON,
                        ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "textures/entity/spell_circle/moon_circle.png"));
                map.put(CircleVariant.CELESTIAL,
                        ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "textures/entity/spell_circle/celestial_circle.png"));

            });

    private static final Map<CircleVariant, Integer> COLOR_BY_VARIANT=
            Util.make(Maps.newEnumMap(CircleVariant.class), map ->{
                map.put(CircleVariant.SUN,
                        0xd3af37);
                map.put(CircleVariant.MOON,
                        0xadd8e6);
                map.put(CircleVariant.CELESTIAL,
                        0xfefefe);

            });


    private static final ResourceLocation BEAM_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "textures/entity/spell_circle/beam.png");

    public MagicCircleRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(MagicCircleEntity entity, float entityYaw, float partialTicks,
                       PoseStack poseStack, MultiBufferSource buffer, int packedLight) {

        poseStack.pushPose();
        poseStack.translate(0.0, 0.01, 0.0);
        poseStack.mulPose(Axis.YP.rotationDegrees(entity.rotation));
        poseStack.scale(3.0f, 1.0f, 3.0f);

        PoseStack.Pose posestackPose = poseStack.last();
        VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(getTextureLocation(entity)));

        vertex(consumer, posestackPose, packedLight, -0.5f, 0f, -0.5f, 0f, 0f);
        vertex(consumer, posestackPose, packedLight, -0.5f, 0f,  0.5f, 0f, 1f);
        vertex(consumer, posestackPose, packedLight,  0.5f, 0f,  0.5f, 1f, 1f);
        vertex(consumer, posestackPose, packedLight,  0.5f, 0f, -0.5f, 1f, 0f);

        poseStack.popPose();

        if(entity.displayBeam()) {
            poseStack.pushPose();

            poseStack.translate(-0.5, 0.01, -0.5);

            int color = COLOR_BY_VARIANT.get(entity.getVariant());

            BeaconRenderer.renderBeaconBeam(
                    poseStack,
                    buffer,
                    BEAM_TEXTURE,
                    partialTicks,
                    1.0F,
                    entity.level().getGameTime(),
                    0,
                    256,
                    color,
                    1.4F, // inner radius
                    0.5F  // outer radius
            );

            poseStack.popPose();
        }


        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    private static void vertex(VertexConsumer consumer, PoseStack.Pose pose, int light, float x, float y, float z, float u, float v) {
        consumer.addVertex(pose, x, y, z) // Z=-0.5 to render flat
                .setColor(255, 255, 255, 255)
                .setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(pose, 0.0F, 1.0F, 0.0F);
    }

    @Override
    public ResourceLocation getTextureLocation(MagicCircleEntity magicCircleEntity) {
        return LOCATION_BY_VARIANT.get(magicCircleEntity.getVariant());
    }
}
