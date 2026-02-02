package net.blumasc.selectivepowers.effect;

import net.blumasc.selectivepowers.SelectivePowers;
import net.blumasc.selectivepowers.effect.custom.*;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;


public class SelectivepowersEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS= DeferredRegister.create(
            BuiltInRegistries.MOB_EFFECT, SelectivePowers.MODID
    );

    public static final Holder<MobEffect> YELLOW_FEVER_EFFECT = MOB_EFFECTS.register("yellow_fever",
            () -> new YellowFeverEffect(MobEffectCategory.HARMFUL, 0xd3af37));
    public static final Holder<MobEffect> FALL_IMMUNITY_EFFECT = MOB_EFFECTS.register("fall_immunity",
            () -> new FallImmunityEffect()
                    .addAttributeModifier(Attributes.FALL_DAMAGE_MULTIPLIER, ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "fall_imunity"), -1.0, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
    public static final Holder<MobEffect> MOON_BOUND_EFFECT = MOB_EFFECTS.register("moon_bound",
            () -> new MoonboundEffect()
                    .addAttributeModifier(Attributes.GRAVITY, ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "low_moon_gravity"), -0.6, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                    .addAttributeModifier(Attributes.FALL_DAMAGE_MULTIPLIER, ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "fall_moon_gravity"), -1.0, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
    );
    public static final Holder<MobEffect> RAGE_EFFECT = MOB_EFFECTS.register("rage",
            () -> new RageEffect());
    public static final Holder<MobEffect> TRUTH_VISION_EFFECT = MOB_EFFECTS.register("truth_vision",
            () -> new TruthVisionEffect()
                    .addAttributeModifier(Attributes.ATTACK_DAMAGE, ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "weakness_from_truth"), -0.3, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                    .addAttributeModifier(Attributes.MAX_HEALTH, ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "health_loss_from_truth"), -0.2, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
    public static final Holder<MobEffect> PARALYZE_EFFECT = MOB_EFFECTS.register("paralyzed",
            () -> new ParalyzeEffect()
                    .addAttributeModifier(Attributes.MOVEMENT_SPEED, ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "slowness_paralyzed"), -0.8, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                    .addAttributeModifier(Attributes.ATTACK_SPEED, ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "fatigue_paralyzed"), -0.5, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                    .addAttributeModifier(Attributes.JUMP_STRENGTH, ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "weak_knees_paralyzed"), -0.5, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
    public static final Holder<MobEffect> DRAKNESS_EFFECT = MOB_EFFECTS.register("darkness",
            () -> new DarknessVisionEffect());
    public static final Holder<MobEffect> ABILITY_TIMER = MOB_EFFECTS.register("ability_timer",
            () -> new TimerEffect());
    public static final Holder<MobEffect> ULT_TIMER = MOB_EFFECTS.register("ult_timer",
            () -> new TimerEffect());
    public static final Holder<MobEffect> SPIKED = MOB_EFFECTS.register("spiked",
            () -> new BleedingEffect());
    public static final Holder<MobEffect> PINNED = MOB_EFFECTS.register("pinned",
            () -> new PinnedEffect()
                    .addAttributeModifier(Attributes.MOVEMENT_SPEED, ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "slowness_pinned"), -1.0, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                    .addAttributeModifier(Attributes.JUMP_STRENGTH, ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "weak_knees_pinned"), -1.0, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
    public static final Holder<MobEffect> PHEROMONES = MOB_EFFECTS.register("pheromones",
            () -> new PheromonesEffect());

    public static final Holder<MobEffect> HALLUCINATION =
            MOB_EFFECTS.register("hallucination", HallucinationEffect::new);

    public static void register(IEventBus eventBus)
    {
        MOB_EFFECTS.register(eventBus);
    }
}
