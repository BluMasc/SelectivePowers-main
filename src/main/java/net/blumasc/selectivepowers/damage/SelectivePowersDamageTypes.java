package net.blumasc.selectivepowers.damage;

import net.blumasc.selectivepowers.SelectivePowers;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class SelectivePowersDamageTypes {
    public static final ResourceKey<DamageType> LUNAR_DAMAGE =
            ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "lunar"));
    public static final ResourceKey<DamageType> SOLAR_DAMAGE =
            ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "solar"));
    public static final ResourceKey<DamageType> RAGE_DAMAGE =
            ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "rage"));
    public static final ResourceKey<DamageType> SPIKE_DAMAGE =
            ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "spike"));
    public static final ResourceKey<DamageType> SOUL_DAMAGE =
            ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "soul"));
    public static final ResourceKey<DamageType> GRAPE_SHOT_DAMAGE =
            ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "grape_shot"));

    public static DamageSource lunarDamage(Entity causer) {
        return new DamageSource(
                causer.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(LUNAR_DAMAGE),
                causer);
    }

    public static DamageSource solarDamage(Entity causer) {
        return new DamageSource(
                causer.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(SOLAR_DAMAGE),
                causer);
    }

    public static DamageSource rageDamage(Level level) {
        return new DamageSource(
                level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(RAGE_DAMAGE));
    }

    public static DamageSource spikeDamage(Level level) {
        return new DamageSource(
                level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(SPIKE_DAMAGE));
    }

    public static DamageSource soulDamage(Level level, @Nullable Entity cause) {
        return new DamageSource(
                level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(SOUL_DAMAGE), cause);
    }
    public static DamageSource grapeShotDamage(Level level, @Nullable Entity cause) {
        return new DamageSource(
                level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(GRAPE_SHOT_DAMAGE), cause);
    }
}
