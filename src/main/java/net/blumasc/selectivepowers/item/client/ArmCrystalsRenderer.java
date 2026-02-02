package net.blumasc.selectivepowers.item.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.blumasc.selectivepowers.SelectivePowers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public class ArmCrystalsRenderer implements ICurioRenderer {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "textures/entity/armor/curio/arm_crystals.png");
    private static final ResourceLocation TEXTURE_DIAMOND = ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "textures/entity/armor/curio/arm_crystals_diamond.png");

    public static ArmCrystalsModel<LivingEntity> MODEL;

    public ArmCrystalsRenderer() {
        super();
        ModelPart part = Minecraft.getInstance().getEntityModels().bakeLayer(ArmCrystalsModel.LAYER_LOCATION);
        MODEL = new ArmCrystalsModel<>(part);
    }

    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(
            ItemStack stack, SlotContext slotContext, PoseStack matrixStack,
            RenderLayerParent<T, M> renderLayerParent, MultiBufferSource renderTypeBuffer,
            int light, float limbSwing, float limbSwingAmount, float partialTicks,
            float ageInTicks, float netHeadYaw, float headPitch) {

        if (MODEL == null) return;

        matrixStack.pushPose();

        LivingEntity entity = slotContext.entity();

        if (entity instanceof AbstractClientPlayer player) {
            var textures = player.getSkin();
            if (textures != null && textures.model() != null) {
                if(textures.model().id().equals("slim")) {
                    matrixStack.translate(0.05, 0, 0);
                }
            }
        }

        if (renderLayerParent.getModel() instanceof HumanoidModel<?> humanoidModel) {
            humanoidModel.rightArm.translateAndRotate(matrixStack);
        }

        VertexConsumer buffer = renderTypeBuffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
        if(stack.getHoverName().getString().toLowerCase().contains("diamond"))
        {
            buffer = renderTypeBuffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE_DIAMOND));
        }
        MODEL.right_arm.render(matrixStack, buffer, light, OverlayTexture.NO_OVERLAY);

        matrixStack.popPose();
    }
}
