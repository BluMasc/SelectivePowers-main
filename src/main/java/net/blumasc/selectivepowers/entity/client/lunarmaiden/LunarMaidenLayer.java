package net.blumasc.selectivepowers.entity.client.lunarmaiden;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.blumasc.selectivepowers.SelectivePowers;
import net.blumasc.selectivepowers.client.ClientPowerData;
import net.blumasc.selectivepowers.entity.SelectivepowersEntities;
import net.blumasc.selectivepowers.entity.custom.LunarMaidenEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import java.util.UUID;

public class LunarMaidenLayer
        extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "textures/entity/lunar_maiden/texture.png");

    private final LunarMaidenModel<LunarMaidenEntity> model;

    private LunarMaidenEntity fake;

    public LunarMaidenLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> parent,
                            EntityRendererProvider.Context ctx) {
        super(parent);
        this.model = new LunarMaidenModel<>(
                ctx.bakeLayer(LunarMaidenModel.LAYER_LOCATION)
        );
    }

    @Override
    public void render(
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            AbstractClientPlayer player,
            float limbSwing,
            float limbSwingAmount,
            float partialTicks,
            float ageInTicks,
            float netHeadYaw,
            float headPitch
    ) {
        if (!shouldRenderMoonForm(player)) return;

        PlayerModel<AbstractClientPlayer> playerModel = this.getParentModel();

        playerModel.head.skipDraw = true;
        playerModel.hat.skipDraw = true;
        playerModel.body.skipDraw = true;
        playerModel.leftArm.skipDraw = true;
        playerModel.rightArm.skipDraw = true;
        playerModel.leftLeg.skipDraw = true;
        playerModel.rightLeg.skipDraw = true;

        poseStack.pushPose();

        poseStack.translate(0.0D, 1.0D, 0.0D);


        LunarMaidenEntity faker = createFakeEntity(player);

        model.setupAnim(faker, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        VertexConsumer vc = buffer.getBuffer(model.renderType(TEXTURE));
        model.renderToBuffer(poseStack, vc, packedLight, OverlayTexture.NO_OVERLAY, 0xFFFFFF);

        poseStack.popPose();
    }
    public static boolean shouldRenderMoonForm(AbstractClientPlayer player) {
        if (!player.isCreative()) return false;

        UUID moonOwner = ClientPowerData.moonOwner;
        return moonOwner != null && moonOwner.equals(player.getUUID());
    }
    private LunarMaidenEntity createFakeEntity(AbstractClientPlayer player) {
        Minecraft mc = Minecraft.getInstance();
        Level level = mc.level;

        if (level == null) return null;

        if(fake == null) {
            fake = new LunarMaidenEntity(
                    SelectivepowersEntities.LUNAR_MAIDEN.get(),
                    level
            );
        }

        fake.setPos(player.getX(), player.getY(), player.getZ());
        fake.setYRot(player.getYRot());
        fake.setXRot(player.getXRot());
        fake.yBodyRot = player.yBodyRot;
        fake.yHeadRot = player.yHeadRot;
        fake.idleAnimationState.startIfStopped(player.tickCount);


        return fake;
    }

}
