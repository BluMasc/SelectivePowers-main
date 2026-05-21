package net.blumasc.selectivepowers.item.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.blumasc.selectivepowers.SelectivePowers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public class PirateHatRenderer implements ICurioRenderer {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "textures/entity/armor/curio/pirate_hat.png");

    public static PirateHatModel<LivingEntity> CROWN_MODEL;

    public PirateHatRenderer() {
        super();
        ModelPart crownPart = Minecraft.getInstance().getEntityModels().bakeLayer(PirateHatModel.LAYER_LOCATION);
        CROWN_MODEL = new PirateHatModel<>(crownPart);
    }

    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(
            ItemStack stack, SlotContext slotContext, PoseStack matrixStack,
            RenderLayerParent<T, M> renderLayerParent, MultiBufferSource renderTypeBuffer,
            int light, float limbSwing, float limbSwingAmount, float partialTicks,
            float ageInTicks, float netHeadYaw, float headPitch) {

        if (CROWN_MODEL == null) return;

        matrixStack.pushPose();

        matrixStack.scale(0.7f, 0.7f, 0.7f);
        //matrixStack.translate(0, -0.5, 0);

        if (renderLayerParent.getModel() instanceof HumanoidModel<?> humanoidModel) {
            humanoidModel.head.translateAndRotate(matrixStack);
        }
        else {
            matrixStack.translate(0, slotContext.entity().getBbHeight() - 0.2F, 0);
            matrixStack.mulPose(Axis.YP.rotationDegrees(netHeadYaw));
            matrixStack.mulPose(Axis.XP.rotationDegrees(headPitch));
        }

        VertexConsumer buffer = renderTypeBuffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
        CROWN_MODEL.head.render(matrixStack, buffer, light, OverlayTexture.NO_OVERLAY);

        matrixStack.popPose();
    }
}
