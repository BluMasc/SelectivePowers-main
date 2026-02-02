package net.blumasc.selectivepowers.entity.helper;

import net.blumasc.selectivepowers.PowerManager;
import net.blumasc.selectivepowers.entity.custom.*;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class YellowFeverHelper {
    public static boolean isYellowFeverImmune(Entity entity) {
        if(entity instanceof YellowKingBossEntity) return true;
        if(entity instanceof YellowKingEntity) return true;
        if(entity instanceof YellowFanaticEntity) return true;
        if(entity instanceof CorruptingMaskEntity) return true;
        if(entity instanceof YellowQuetzalEntity) return true;
        if(entity instanceof Player)
        {
            PowerManager pm = PowerManager.get((ServerLevel) entity.level());
            return pm.getPowerOfPlayer(entity.getUUID()).equals(PowerManager.YELLOW_POWER);
        }
        return false;
    }

    public static void increaseEffect(LivingEntity entity, Holder<MobEffect> effect, RandomSource random) {
        MobEffectInstance instance = entity.getEffect(effect);

        if (instance == null) {
            entity.addEffect(new MobEffectInstance(effect, (random.nextInt(10)+5)*20, 0,
                    false, true, true));
            return;
        }

        int amplifier = instance.getAmplifier();
        int duration  = instance.getDuration();
        entity.removeEffect(effect);
        entity.addEffect(new MobEffectInstance(effect, duration+(random.nextInt(10)+5)*20, Math.min(amplifier + 1, 6),
                instance.isAmbient(), instance.isVisible(), instance.showIcon()));

    }
}
