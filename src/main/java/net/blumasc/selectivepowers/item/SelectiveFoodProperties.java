package net.blumasc.selectivepowers.item;

import net.blumasc.selectivepowers.effect.SelectivepowersEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class SelectiveFoodProperties {
    public static final FoodProperties MOONCAP_MUSHROOM = new FoodProperties.Builder().nutrition(2).saturationModifier(0.25f).fast().build();
    public static final FoodProperties MOONLIGHT_JELLY = new FoodProperties.Builder().nutrition(1).saturationModifier(0.1f).alwaysEdible().build();
    public static final FoodProperties LUNAR_TAKOYAKI = new FoodProperties.Builder().nutrition(4).saturationModifier(0.2f).fast().build();
    public static final FoodProperties RAGE_COOKIE = new FoodProperties.Builder().nutrition(2).saturationModifier(0.4f).alwaysEdible().fast()
            .effect(new MobEffectInstance(SelectivepowersEffects.RAGE_EFFECT, 100, 0,
            false, true, true), 1.0f).build();
    public static final FoodProperties MUSHROOM_SKEWER = new FoodProperties.Builder().nutrition(4).saturationModifier(3.4f)
            .effect(new MobEffectInstance(MobEffects.LEVITATION, 240, 0,
                    false, true, true), 1.0f).build();

    public static final FoodProperties COOKED_MUSHROOM_SKEWER = new FoodProperties.Builder().nutrition(4).saturationModifier(3.4f)
            .effect(new MobEffectInstance(MobEffects.JUMP, 240, 1,
                    false, true, true), 1.0f).build();

    public static final FoodProperties DRINKING_HORN = new FoodProperties.Builder().nutrition(0).saturationModifier(0.0f)
            .effect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 600, 1,
                    false, true, true), 1.0f)
            .effect(new MobEffectInstance(MobEffects.WITHER, 800, 1,
                    false, true, true), 1.0f)
            .effect(new MobEffectInstance(MobEffects.CONFUSION, 600, 1,
                    false, true, true), 1.0f)
            .effect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 800, 1,
                    false, true, true), 1.0f)
            .effect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 600, 2,
                    false, true, true), 1.0f)
            .effect(new MobEffectInstance(MobEffects.ABSORPTION, 1800, 2,
                    false, true, true), 1.0f).alwaysEdible().build();
}
