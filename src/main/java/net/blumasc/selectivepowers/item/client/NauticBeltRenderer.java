package net.blumasc.selectivepowers.item.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
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

public class NauticBeltRenderer implements ICurioRenderer {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "textures/entity/armor/curio/nautic_belt.png");
    private static final ResourceLocation TEXTURE_ABYSSAL = ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "textures/entity/armor/curio/nautic_belt_abyssal.png");

    public static NauticBeltModel<LivingEntity> MODEL;

    public NauticBeltRenderer() {
        super();
        ModelPart part = Minecraft.getInstance().getEntityModels().bakeLayer(NauticBeltModel.LAYER_LOCATION);
        MODEL = new NauticBeltModel<>(part);
    }

    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(
            ItemStack stack, SlotContext slotContext, PoseStack matrixStack,
            RenderLayerParent<T, M> renderLayerParent, MultiBufferSource renderTypeBuffer,
            int light, float limbSwing, float limbSwingAmount, float partialTicks,
            float ageInTicks, float netHeadYaw, float headPitch) {

        if (MODEL == null) return;

        matrixStack.pushPose();

        matrixStack.scale(0.7f, 0.7f, 0.7f);

        if (renderLayerParent.getModel() instanceof HumanoidModel<?> humanoidModel) {
            humanoidModel.body.translateAndRotate(matrixStack);
        }
        ResourceLocation texture = TEXTURE;

        if(stack.getHoverName().getString().toLowerCase().contains("abyssal"))
        {
            texture = TEXTURE_ABYSSAL;
        }

        VertexConsumer buffer = renderTypeBuffer.getBuffer(RenderType.entityCutoutNoCull(texture));
        MODEL.body.render(matrixStack, buffer, light, OverlayTexture.NO_OVERLAY);

        matrixStack.popPose();
    }
}
