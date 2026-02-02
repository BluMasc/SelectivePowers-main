package net.blumasc.selectivepowers.effect.custom;

import net.blumasc.selectivepowers.damage.SelectivePowersDamageTypes;
import net.blumasc.selectivepowers.effect.SelectivepowersEffects;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class PinnedEffect extends MobEffect {
    public PinnedEffect() {
        super(MobEffectCategory.HARMFUL, 0x7b1010);
    }
    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if(entity instanceof Player p)
        {
            if(p.isCrouching())
            {
                p.removeEffect(SelectivepowersEffects.PINNED);
                if (amplifier > 0) {
                    float damageAmount = 1.0F * amplifier;
                    p.hurt(SelectivePowersDamageTypes.spikeDamage(entity.level()), damageAmount);
                }
            }
        }
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}