package net.blumasc.selectivepowers.item.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.blumasc.selectivepowers.SelectivePowers;
import net.blumasc.selectivepowers.item.SelectivepowersItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public class FaceTextureRenderer implements ICurioRenderer {
    private static final ResourceLocation TEXTURE_PAINT = ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "textures/entity/armor/curio/head_layer/face_paint.png");
    private static final ResourceLocation TEXTURE_EYES = ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "textures/entity/armor/curio/head_layer/glowing_eyes.png");
    private static final ResourceLocation TEXTURE_EYES_EVIL = ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "textures/entity/armor/curio/head_layer/glowing_eyes_evil.png");

    public static FaceTextureModel<LivingEntity> MODEL;

    public FaceTextureRenderer() {
        super();
        ModelPart part = Minecraft.getInstance().getEntityModels().bakeLayer(FaceTextureModel.LAYER_LOCATION);
        MODEL = new FaceTextureModel<>(part);
    }

    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(
            ItemStack stack, SlotContext slotContext, PoseStack matrixStack,
            RenderLayerParent<T, M> renderLayerParent, MultiBufferSource renderTypeBuffer,
            int light, float limbSwing, float limbSwingAmount, float partialTicks,
            float ageInTicks, float netHeadYaw, float headPitch) {

        if (MODEL == null) return;

        VertexConsumer buffer = renderTypeBuffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE_EYES));
        if(stack.is(SelectivepowersItems.GLOWING_EYES))
        {
            if(stack.getHoverName().getString().toLowerCase().contains("evil"))
            {
                buffer = renderTypeBuffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE_EYES_EVIL));
            }
            matrixStack.pushPose();

            if (renderLayerParent.getModel() instanceof HumanoidModel<?> humanoidModel) {
                humanoidModel.head.translateAndRotate(matrixStack);
            }
            MODEL.head.render(matrixStack, buffer, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);

            matrixStack.popPose();
        }
        if(stack.is(SelectivepowersItems.FACE_PAINT))
        {
            buffer = renderTypeBuffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE_PAINT));
            matrixStack.pushPose();

            if (renderLayerParent.getModel() instanceof HumanoidModel<?> humanoidModel) {
                humanoidModel.head.translateAndRotate(matrixStack);
            }
            MODEL.head.render(matrixStack, buffer, light, OverlayTexture.NO_OVERLAY);

            matrixStack.popPose();
        }
    }
}
