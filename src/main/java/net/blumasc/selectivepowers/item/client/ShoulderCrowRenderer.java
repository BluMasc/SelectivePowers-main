package net.blumasc.selectivepowers.item.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.blumasc.selectivepowers.SelectivePowers;
import net.blumasc.selectivepowers.entity.client.crow.CrowModel;
import net.blumasc.selectivepowers.entity.custom.CrowEntity;
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

public class ShoulderCrowRenderer implements ICurioRenderer {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "textures/entity/crow/gray_variant.png");

    private static final ResourceLocation PINK = ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID,"textures/entity/crow/pink_variant.png");
    private static final ResourceLocation NETT = ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID,"textures/entity/crow/nett_variant.png");

    public static CrowModel<CrowEntity> CROW_MODEL;

    public ShoulderCrowRenderer() {
        super();
        ModelPart part = Minecraft.getInstance().getEntityModels().bakeLayer(CrowModel.LAYER_LOCATION);
        CROW_MODEL = new CrowModel<CrowEntity>(part);
    }

    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(
            ItemStack stack, SlotContext slotContext, PoseStack matrixStack,
            RenderLayerParent<T, M> renderLayerParent, MultiBufferSource renderTypeBuffer,
            int light, float limbSwing, float limbSwingAmount, float partialTicks,
            float ageInTicks, float netHeadYaw, float headPitch) {

        if (CROW_MODEL == null) return;

        VertexConsumer buffer = renderTypeBuffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
        if(stack.getHoverName().getString().toLowerCase().contains("nett"))
        {
            buffer = renderTypeBuffer.getBuffer(RenderType.entityCutoutNoCull(NETT));
        }
        if(stack.getHoverName().getString().toLowerCase().contains("strawberry"))
        {
            buffer = renderTypeBuffer.getBuffer(RenderType.entityCutoutNoCull(PINK));
        }

        matrixStack.pushPose();

        if (renderLayerParent.getModel() instanceof HumanoidModel<?> humanoidModel) {
            humanoidModel.body.translateAndRotate(matrixStack);
        }
        matrixStack.scale(0.4f, 0.4f, 0.4f);
        matrixStack.translate(-0.9f, -1.5f, 0.0f);
        CROW_MODEL.renderToBuffer(matrixStack, buffer, light, OverlayTexture.NO_OVERLAY);

        matrixStack.popPose();
    }
}
