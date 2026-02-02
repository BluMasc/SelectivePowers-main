package net.blumasc.selectivepowers.effect.custom;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class ParalyzeEffect extends MobEffect {


    public ParalyzeEffect() {
        super(MobEffectCategory.HARMFUL, 0xffff33);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        entity.setDeltaMovement(0, entity.getDeltaMovement().y, 0);

        if (entity instanceof Player player) {
            player.zza = 0;
            player.xxa = 0;
            player.setJumping(false);
        }
        return true;
    }
}
