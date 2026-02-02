package net.blumasc.selectivepowers.mixin;

import net.blumasc.selectivepowers.enchantment.ModEnchantments;
import net.blumasc.selectivepowers.entity.custom.projectile.GrapeShotProjectile;
import net.blumasc.selectivepowers.entity.custom.projectile.LightningRodArrowEntity;
import net.blumasc.selectivepowers.entity.custom.projectile.WardenBeamProjectile;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CrossbowItem.class)
public class CrossbowMixin {
    @Inject(
            method = "createProjectile",
            at = @At("HEAD"),
            cancellable = true
    )
    private void enchanment$replaceProjectile(
            Level level,
            LivingEntity shooter,
            ItemStack weapon,
            ItemStack ammo,
            boolean isCrit,
            CallbackInfoReturnable<Projectile> cir
    ) {
        if (ammo.is(Items.ECHO_SHARD)) {
            cir.setReturnValue(new WardenBeamProjectile(level, shooter));
        }
        if (ammo.is(Items.LIGHTNING_ROD)) {
            Holder<Enchantment> galvanizingHolder = shooter.level().registryAccess()
                    .registryOrThrow(Registries.ENCHANTMENT)
                    .getHolderOrThrow(ModEnchantments.GALVANIZING);

            int enchantLevel = weapon.getEnchantmentLevel(galvanizingHolder);
            cir.setReturnValue(new LightningRodArrowEntity(level, shooter, 1+enchantLevel, weapon));
        }
        if (ammo.is(ItemTags.STONE_TOOL_MATERIALS)) {
            Holder<Enchantment> galvanizingHolder = shooter.level().registryAccess()
                    .registryOrThrow(Registries.ENCHANTMENT)
                    .getHolderOrThrow(ModEnchantments.GRAPE_SHOT);

            int enchantLevel = weapon.getEnchantmentLevel(galvanizingHolder);
            cir.setReturnValue(new GrapeShotProjectile(level, ammo, enchantLevel, shooter ));
        }
    }
}
