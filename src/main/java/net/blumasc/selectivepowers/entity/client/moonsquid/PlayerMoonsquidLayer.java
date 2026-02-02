package net.blumasc.selectivepowers.entity.client.moonsquid;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.blumasc.selectivepowers.entity.SelectivepowersEntities;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;

public class PlayerMoonsquidLayer <T extends Player> extends RenderLayer<T, PlayerModel<T>> {
    private final MoonsquidModel model;

    public PlayerMoonsquidLayer(RenderLayerParent<T, PlayerModel<T>> renderer, EntityModelSet modelSet) {
        super(renderer);
        this.model = new MoonsquidModel(modelSet.bakeLayer(MoonsquidModel.LAYER_LOCATION));
    }

    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        this.render(poseStack, buffer, packedLight, livingEntity, limbSwing, limbSwingAmount, netHeadYaw, headPitch, true);
        this.render(poseStack, buffer, packedLight, livingEntity, limbSwing, limbSwingAmount, netHeadYaw, headPitch, false);
    }

    private void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T livingEntity, float limbSwing, float limbSwingAmount, float netHeadYaw, float headPitch, boolean leftShoulder) {
        CompoundTag compoundtag = leftShoulder ? livingEntity.getShoulderEntityLeft() : livingEntity.getShoulderEntityRight();
        EntityType.byString(compoundtag.getString("id"))
                .filter(type -> type == SelectivepowersEntities.MOON_SQUID.get())
                .ifPresent(type -> {
                    poseStack.pushPose();
                    poseStack.translate(
                            leftShoulder ? 0.3F : -0.3F,
                            livingEntity.isCrouching() ? -0.8F : -1.0F,
                            -0.2F
                    );
                    poseStack.scale(0.7F, 0.7F, 0.7F);
                    if (!leftShoulder) {
                        poseStack.scale(1.0F, 1.0F, -1.0F);
                        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
                    }
                    VertexConsumer vertexConsumer =
                            buffer.getBuffer(this.model.renderType(
                                    MoonsquidRenderer.getStaticTextureLocation()
                            ));
                    this.model.renderOnShoulder(
                            poseStack,
                            vertexConsumer,
                            packedLight,
                            OverlayTexture.NO_OVERLAY,
                            limbSwing,
                            limbSwingAmount,
                            netHeadYaw,
                            headPitch,
                            livingEntity.tickCount
                    );

                    poseStack.popPose();
                });
    }
}
