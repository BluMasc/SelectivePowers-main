package net.blumasc.selectivepowers.item.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.blumasc.selectivepowers.SelectivePowers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.multiplayer.ClientLevel;
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

public class StormCloudRenderer implements ICurioRenderer {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "textures/entity/armor/curio/storm_cloud.png");

    public static StormCloudModel<LivingEntity> MODEL;

    public StormCloudRenderer() {
        super();
        ModelPart part = Minecraft.getInstance().getEntityModels().bakeLayer(StormCloudModel.LAYER_LOCATION);
        MODEL = new StormCloudModel<>(part);
    }

    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(
            ItemStack stack, SlotContext slotContext, PoseStack matrixStack,
            RenderLayerParent<T, M> renderLayerParent, MultiBufferSource renderTypeBuffer,
            int light, float limbSwing, float limbSwingAmount, float partialTicks,
            float ageInTicks, float netHeadYaw, float headPitch) {

        if (MODEL == null) return;

        matrixStack.pushPose();

        if (renderLayerParent.getModel() instanceof HumanoidModel<?> humanoidModel) {
            humanoidModel.head.translateAndRotate(matrixStack);
        }

        VertexConsumer buffer = renderTypeBuffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
        MODEL.head.render(matrixStack, buffer, light, OverlayTexture.NO_OVERLAY);

        matrixStack.popPose();
        LivingEntity entity = slotContext.entity();
        spawnParticles(entity);
    }

    private void spawnParticles(LivingEntity entity) {
        if (!(entity.level() instanceof ClientLevel level)) return;

        if (entity.tickCount % 5 != 0) return;

        double radius = 0.6;
        double angle = level.random.nextDouble() * Math.PI * 2;

        double x = entity.getX() + Math.cos(angle) * radius;
        double y = entity.getY() + 2.2;
        double z = entity.getZ() + Math.sin(angle) * radius;

        level.addParticle(
                ParticleTypes.FALLING_WATER,
                x, y, z,
                0.0, 0.02, 0.0
        );
    }
}
