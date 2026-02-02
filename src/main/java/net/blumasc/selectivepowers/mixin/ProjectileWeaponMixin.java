package net.blumasc.selectivepowers.mixin;

import net.blumasc.selectivepowers.entity.custom.projectile.WoodArrow;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ProjectileWeaponItem.class)
public abstract class ProjectileWeaponMixin {

    @Shadow
    public abstract  AbstractArrow customArrow(
            AbstractArrow arrow,
            ItemStack projectileStack,
            ItemStack weaponStack
    );
    @Inject(
            method = "createProjectile",
            at = @At("HEAD"),
            cancellable = true
    )
    protected void enchanment$woodArrow(Level level, LivingEntity shooter, ItemStack weapon, ItemStack ammo, boolean isCrit, CallbackInfoReturnable<Projectile> cir) {
        if(ammo.is(Items.STICK))
        {
            AbstractArrow arrow = new WoodArrow(level, ammo, shooter, weapon);
            if (isCrit) {
                arrow.setCritArrow(true);
            }
            cir.setReturnValue(arrow);
            cir.cancel();
        }
    }
}
