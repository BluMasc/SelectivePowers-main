package net.blumasc.selectivepowers.potion;

import net.blumasc.selectivepowers.SelectivePowers;
import net.blumasc.selectivepowers.effect.SelectivepowersEffects;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class SelectivePowerPotions {
    public static final DeferredRegister<Potion> POTIONS =
            DeferredRegister.create(BuiltInRegistries.POTION, SelectivePowers.MODID);

    public static final Holder<Potion> TRUTH_VISION_POTION = POTIONS.register("sp_truth_vision_potion",
            () -> new Potion(new MobEffectInstance(SelectivepowersEffects.TRUTH_VISION_EFFECT, 1200, 0)));

    public static void register(IEventBus bus)
    {
        POTIONS.register(bus);
    }
}
