package net.blumasc.selectivepowers.entity.custom.projectile;

import net.blumasc.selectivepowers.entity.SelectivepowersEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class WoodArrow extends AbstractArrow {

    public WoodArrow(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
    }

    public WoodArrow(Level level, ItemStack ammo, LivingEntity shooter, ItemStack weapon) {
        super(SelectivepowersEntities.WOOD_ARROW.get(), shooter, level, ammo, null);
        this.pickup = Pickup.CREATIVE_ONLY;
    }

    @Override
    protected @NotNull ItemStack getPickupItem() {
        return new ItemStack(Items.STICK);
    }

    @Override
    protected @NotNull ItemStack getDefaultPickupItem() {
        return new ItemStack(Items.STICK);
    }

    @Override
    public double getBaseDamage() {
        return super.getBaseDamage()*0.7;
    }

    @Override
    public void onAddedToLevel() {
        super.onAddedToLevel();
    }

    public ItemStack getWeaponItem() {
        return null;
    }
}
