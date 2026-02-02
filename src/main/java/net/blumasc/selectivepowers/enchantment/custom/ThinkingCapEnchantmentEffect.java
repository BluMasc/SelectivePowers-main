package net.blumasc.selectivepowers.enchantment.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect;

public record ThinkingCapEnchantmentEffect() implements EnchantmentValueEffect {
    public static final MapCodec<ThinkingCapEnchantmentEffect> CODEC = MapCodec.unit(ThinkingCapEnchantmentEffect::new);
    @Override
    public float process(int level, RandomSource random, float base) {
        float bonus = 0.1f + (0.1f * level);
        return base * (1.0f + bonus);
    }

    @Override
    public MapCodec<? extends EnchantmentValueEffect> codec() {
        return CODEC;
    }
}
