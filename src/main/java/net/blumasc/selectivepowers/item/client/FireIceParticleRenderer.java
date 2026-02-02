package net.blumasc.selectivepowers.item.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector3f;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public class FireIceParticleRenderer implements ICurioRenderer {
    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack, SlotContext slotContext, PoseStack matrixStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource renderTypeBuffer, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        LivingEntity entity = slotContext.entity();

        spawnFireParticles(entity);
        spawnIceParticles(entity);
    }
    private void spawnFireParticles(LivingEntity entity) {
        if (!(entity.level() instanceof ClientLevel level)) return;

        if (entity.tickCount % 5 != 0) return;

        double radius = 0.6;
        double angle = level.random.nextDouble() * Math.PI * 2;

        double x = entity.getX() + Math.cos(angle) * radius;
        double y = entity.getY() + level.random.nextDouble()* 0.8;
        double z = entity.getZ() + Math.sin(angle) * radius;

        level.addParticle(
                ParticleTypes.FLAME,
                x, y, z,
                0.0, 0.02, 0.0
        );
    }
    private void spawnIceParticles(LivingEntity entity) {
        if (!(entity.level() instanceof ClientLevel level)) return;

        if (entity.tickCount % 5 != 0) return;

        double radius = 0.6;
        double angle = level.random.nextDouble() * Math.PI * 2;

        double x = entity.getX() + Math.cos(angle) * radius;
        double y = 0.8 + entity.getY() + level.random.nextDouble()*0.8;
        double z = entity.getZ() + Math.sin(angle) * radius;

        level.addParticle(
                ParticleTypes.SNOWFLAKE,
                x, y, z,
                0.0, -0.15, 0.0
        );
    }
}
