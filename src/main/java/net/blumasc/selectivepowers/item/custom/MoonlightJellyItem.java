package net.blumasc.selectivepowers.item.custom;

import net.blumasc.selectivepowers.effect.SelectivepowersEffects;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import team.lodestar.lodestone.registry.common.particle.LodestoneParticleTypes;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.data.spin.SpinParticleData;
import team.lodestar.lodestone.systems.particle.world.type.LodestoneWorldParticleType;

import java.awt.*;
import java.util.function.Supplier;

public class MoonlightJellyItem extends Item {
    public MoonlightJellyItem(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        ItemStack result = super.finishUsingItem(stack, level, livingEntity);
        if(!level.isClientSide())
        {
            reduceEffect(livingEntity, SelectivepowersEffects.YELLOW_FEVER_EFFECT);
        }else
        {
            spawnParticles(level, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), 20, LodestoneParticleTypes.TWINKLE_PARTICLE, new Color(0x90d5ff), new Color(0xd7e5f0));
        }
        return result;
    }

    public static void spawnParticles(Level level, double x, double y, double z, int count, Supplier<LodestoneWorldParticleType> particle, Color startingColor, Color endingColor) {
        if (level instanceof ServerLevel) return;
        RandomSource rand = level.getRandom();
        for (int j = 0; j < count; j++) {
            double ox = (rand.nextDouble() - 0.5) * 1.5;
            double oy = rand.nextDouble() * 2.0;
            double oz = (rand.nextDouble() - 0.5) * 1.5;
            WorldParticleBuilder.create(particle)
                    .setScaleData(GenericParticleData.create(0.5f, 0).build())
                    .setTransparencyData(GenericParticleData.create(0.75f, 0.25f).build())
                    .setColorData(ColorParticleData.create(startingColor, endingColor).setCoefficient(1.4f).setEasing(Easing.BOUNCE_IN_OUT).build())
                    .setSpinData(SpinParticleData.create(0.2f, 0.4f).setSpinOffset((level.getGameTime() * 0.2f) % 6.28f).setEasing(Easing.QUARTIC_IN).build())
                    .setLifetime(40)
                    .addMotion(0, 0.01f, 0)
                    .enableNoClip()
                    .spawn(level, x+ox, y+oy, z+oz);
        }
    }

    private void reduceEffect(LivingEntity entity, Holder<MobEffect> effect) {
        MobEffectInstance instance = entity.getEffect(effect);

        if (instance == null) return;

        int amplifier = instance.getAmplifier();
        int duration  = instance.getDuration();

        if (amplifier > 0) {
            entity.removeEffect(effect);
            entity.addEffect(new MobEffectInstance(effect, duration, amplifier - 1,
                    instance.isAmbient(), instance.isVisible(), instance.showIcon()));
        } else {
            entity.removeEffect(effect);
        }
    }
}
