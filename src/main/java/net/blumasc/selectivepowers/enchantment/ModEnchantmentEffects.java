package net.blumasc.selectivepowers.enchantment;

import com.mojang.serialization.MapCodec;
import net.blumasc.selectivepowers.SelectivePowers;
import net.blumasc.selectivepowers.enchantment.custom.SplatterEnchantmentEffect;
import net.blumasc.selectivepowers.enchantment.custom.ThinkingCapEnchantmentEffect;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModEnchantmentEffects {
    public static final DeferredRegister<MapCodec<? extends EnchantmentValueEffect>> ENTITY_ENCHANTMENT_EFFECTS =
            DeferredRegister.create(Registries.ENCHANTMENT_VALUE_EFFECT_TYPE, SelectivePowers.MODID);

    public static final Supplier<MapCodec<? extends EnchantmentValueEffect>> THINKING_CAP =
            ENTITY_ENCHANTMENT_EFFECTS.register("thinking_cap", () -> ThinkingCapEnchantmentEffect.CODEC);


    public static void register(IEventBus bus){
       ENTITY_ENCHANTMENT_EFFECTS.register(bus);
   }
}
