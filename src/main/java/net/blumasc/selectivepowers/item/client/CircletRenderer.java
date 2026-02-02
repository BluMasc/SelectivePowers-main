package net.blumasc.selectivepowers.item.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.blumasc.selectivepowers.SelectivePowers;
import net.blumasc.selectivepowers.item.SelectivepowersItems;
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

public class CircletRenderer implements ICurioRenderer {
    private static final ResourceLocation TEXTURE_ELEMENTAL = ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "textures/entity/armor/curio/crowns/circlet_of_duality.png");
    private static final ResourceLocation TEXTURE_FLOWERS = ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "textures/entity/armor/curio/crowns/flower_crown.png");
    public static CircletModel<LivingEntity> MODEL;

    public CircletRenderer() {
        super();
        ModelPart part = Minecraft.getInstance().getEntityModels().bakeLayer(CircletModel.LAYER_LOCATION);
        MODEL = new CircletModel<>(part);
    }

    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(
            ItemStack stack, SlotContext slotContext, PoseStack matrixStack,
            RenderLayerParent<T, M> renderLayerParent, MultiBufferSource renderTypeBuffer,
            int light, float limbSwing, float limbSwingAmount, float partialTicks,
            float ageInTicks, float netHeadYaw, float headPitch) {

        if (MODEL == null) return;

        VertexConsumer buffer = renderTypeBuffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE_ELEMENTAL));
        if(stack.is(SelectivepowersItems.FLOWER_CROWN))
        {
            buffer = renderTypeBuffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE_FLOWERS));
        }

        matrixStack.pushPose();

        if (renderLayerParent.getModel() instanceof HumanoidModel<?> humanoidModel) {
            humanoidModel.head.translateAndRotate(matrixStack);
        }
        MODEL.hat.render(matrixStack, buffer, light, OverlayTexture.NO_OVERLAY);

        matrixStack.popPose();
    }
}
