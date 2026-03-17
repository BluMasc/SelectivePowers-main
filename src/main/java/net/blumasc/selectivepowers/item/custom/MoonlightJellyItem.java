package net.blumasc.selectivepowers.item.custom;

import net.blumasc.selectivepowers.effect.SelectivepowersEffects;
import net.blumasc.selectivepowers.particles.custom.WispParticleOption;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

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
            spawnParticles(level, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), 20, new Color(0x90d5ff), new Color(0xd7e5f0));
        }
        return result;
    }

    public static void spawnParticles(Level level, double x, double y, double z, int count, Color startingColor, Color endingColor) {
        if (level instanceof ServerLevel) return;
        RandomSource rand = level.getRandom();
        for (int j = 0; j < count; j++) {
            double ox = (rand.nextDouble() - 0.5) * 1.5;
            double oy = rand.nextDouble() * 2.0;
            double oz = (rand.nextDouble() - 0.5) * 1.5;
            spawnParticle(level, x+ox, y+oy, z+oz, startingColor, endingColor, 0, 0.01f, 0);
        }
    }

    public static void spawnParticle(Level level, double x, double y, double z,
                                     Color startingColor, Color endingColor,
                                     double moveX, double moveY, double moveZ) {
        if (!(level instanceof ClientLevel)) return; // client-only

        WispParticleOption options = new WispParticleOption(
                startingColor.getRed()   / 255f, startingColor.getGreen() / 255f, startingColor.getBlue() / 255f,
                endingColor.getRed()     / 255f, endingColor.getGreen()   / 255f, endingColor.getBlue()   / 255f
        );

        level.addParticle(options, x, y, z, moveX, moveY, moveZ);
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
