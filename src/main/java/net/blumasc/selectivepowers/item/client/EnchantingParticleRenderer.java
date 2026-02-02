package net.blumasc.selectivepowers.item.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public class EnchantingParticleRenderer implements ICurioRenderer {
    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack, SlotContext slotContext, PoseStack matrixStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource renderTypeBuffer, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        LivingEntity entity = slotContext.entity();

        spawnParticles(entity);
    }
    private void spawnParticles(LivingEntity entity) {
        if (!(entity.level() instanceof ClientLevel level)) return;

        if (entity.tickCount % 5 != 0) return;

        double radius = 0.6;
        double angle = level.random.nextDouble() * Math.PI * 2;

        double x = entity.getX() + Math.cos(angle) * radius;
        double y = entity.getY() + level.random.nextDouble()*2.0;
        double z = entity.getZ() + Math.sin(angle) * radius;

        level.addParticle(
                ParticleTypes.ENCHANT,
                x, y, z,
                0.0, 0.02, 0.0
        );
    }
}
