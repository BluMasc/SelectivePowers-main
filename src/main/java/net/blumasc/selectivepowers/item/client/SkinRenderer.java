package net.blumasc.selectivepowers.item.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.blumasc.selectivepowers.SelectivePowers;
import net.blumasc.selectivepowers.item.SelectivepowersItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public class SkinRenderer implements ICurioRenderer {
    private static final ResourceLocation TEXTURE_LIGHTNING = ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "textures/entity/armor/curio/skin_layer/lightning_skin.png");
    private static final ResourceLocation TEXTURE_LIGHTNING_GIRL = ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "textures/entity/armor/curio/skin_layer/lightning_skin_girl.png");
    private static final ResourceLocation TEXTURE_MOSS = ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "textures/entity/armor/curio/skin_layer/moss_skin.png");
    private static final ResourceLocation TEXTURE_SCULK = ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "textures/entity/armor/curio/skin_layer/sculk_skin.png");
    private static final ResourceLocation TEXTURE_SCULK_DARKNESS = ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "textures/entity/armor/curio/skin_layer/sculk_skin_darkness.png");

    public static SkinModel<LivingEntity> MODEL;

    public SkinRenderer() {
        super();
        ModelPart part = Minecraft.getInstance().getEntityModels().bakeLayer(SkinModel.LAYER_LOCATION);
        MODEL = new SkinModel<>(part);
    }

    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(
            ItemStack stack, SlotContext slotContext, PoseStack matrixStack,
            RenderLayerParent<T, M> renderLayerParent, MultiBufferSource renderTypeBuffer,
            int light, float limbSwing, float limbSwingAmount, float partialTicks,
            float ageInTicks, float netHeadYaw, float headPitch) {

        if (MODEL == null) return;

        VertexConsumer buffer = renderTypeBuffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE_SCULK));
        int usedLight = light;
        if(stack.is(SelectivepowersItems.MOSS_LAYER))
        {
            buffer = renderTypeBuffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE_MOSS));
        }
        else if(stack.is(SelectivepowersItems.LIGHTNING_BALL))
        {
            if(stack.getHoverName().getString().toLowerCase().contains("girl"))
            {
                buffer = renderTypeBuffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE_LIGHTNING_GIRL));
            }else {
                buffer = renderTypeBuffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE_LIGHTNING));
            }

        }else if(stack.getHoverName().getString().toLowerCase().contains("darkness"))
        {
            buffer = renderTypeBuffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE_SCULK_DARKNESS));
            usedLight = LightTexture.pack(0,0);
        }

        matrixStack.pushPose();

        if (renderLayerParent.getModel() instanceof HumanoidModel<?> humanoidModel) {
            humanoidModel.head.translateAndRotate(matrixStack);
        }
        MODEL.head.render(matrixStack, buffer, usedLight, OverlayTexture.NO_OVERLAY);

        matrixStack.popPose();

        matrixStack.pushPose();

        if (renderLayerParent.getModel() instanceof HumanoidModel<?> humanoidModel) {
            humanoidModel.body.translateAndRotate(matrixStack);
        }
        MODEL.body.render(matrixStack, buffer, usedLight, OverlayTexture.NO_OVERLAY);

        matrixStack.popPose();
        matrixStack.pushPose();

        if (renderLayerParent.getModel() instanceof HumanoidModel<?> humanoidModel) {
            humanoidModel.rightArm.translateAndRotate(matrixStack);
        }
        MODEL.right_arm.render(matrixStack, buffer, usedLight, OverlayTexture.NO_OVERLAY);

        matrixStack.popPose();
        matrixStack.pushPose();

        if (renderLayerParent.getModel() instanceof HumanoidModel<?> humanoidModel) {
            humanoidModel.leftArm.translateAndRotate(matrixStack);
        }
        MODEL.left_arm.render(matrixStack, buffer, usedLight, OverlayTexture.NO_OVERLAY);

        matrixStack.popPose();
        matrixStack.pushPose();

        if (renderLayerParent.getModel() instanceof HumanoidModel<?> humanoidModel) {
            humanoidModel.rightLeg.translateAndRotate(matrixStack);
        }
        MODEL.right_leg.render(matrixStack, buffer, usedLight, OverlayTexture.NO_OVERLAY);

        matrixStack.popPose();
        matrixStack.pushPose();

        if (renderLayerParent.getModel() instanceof HumanoidModel<?> humanoidModel) {
            humanoidModel.leftLeg.translateAndRotate(matrixStack);
        }
        MODEL.left_leg.render(matrixStack, buffer, usedLight, OverlayTexture.NO_OVERLAY);

        matrixStack.popPose();
    }
}
