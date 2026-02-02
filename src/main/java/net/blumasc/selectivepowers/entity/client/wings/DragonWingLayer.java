package net.blumasc.selectivepowers.entity.client.wings;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.blumasc.selectivepowers.SelectivePowers;
import net.blumasc.selectivepowers.client.ClientPowerData;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class DragonWingLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "textures/entity/dragon_wings/texture.png");

    private final DragonWingModel<?> wingModel;

    public DragonWingLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> parent) {
        super(parent);
        this.wingModel = new DragonWingModel<>(DragonWingModel.createBodyLayer().bakeRoot());
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight,
                       AbstractClientPlayer player, float limbSwing, float limbSwingAmount,
                       float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {

        if (!hasDragonWings(player)) return;

        poseStack.pushPose();

        poseStack.scale(1f, 1f, 1f);

        wingModel.applyWingAnimation(ageInTicks / 20f,!player.onGround());

        VertexConsumer vertex = buffer.getBuffer(wingModel.renderType(TEXTURE));
        wingModel.renderToBuffer(poseStack, vertex, packedLight, OverlayTexture.NO_OVERLAY, 0xFFFFFF);

        poseStack.popPose();
    }

    private boolean hasDragonWings(AbstractClientPlayer player) {
        return ClientPowerData.dragonOwner != null && ClientPowerData.dragonOwner.equals(player.getUUID());
    }
}
