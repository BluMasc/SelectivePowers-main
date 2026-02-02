package net.blumasc.selectivepowers.entity.custom.projectile;

import net.blumasc.selectivepowers.entity.SelectivepowersEntities;
import net.blumasc.selectivepowers.item.SelectivepowersItems;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.Nullable;

public class FlamingFeatherEntity extends AbstractArrow {
    public FlamingFeatherEntity(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
    }

    public FlamingFeatherEntity(Level level, LivingEntity shooter) {
        super(SelectivepowersEntities.FLAMING_FEATHER.get(), level);
        this.setOwner(shooter);
        this.setPickupItemStack(SelectivepowersItems.BURNING_FEATHER.toStack());
        this.pickup = Pickup.DISALLOWED;
    }

    public FlamingFeatherEntity(Level level, double x, double y, double z, ItemStack itemStack) {
        super(SelectivepowersEntities.FLAMING_FEATHER.get(), x,y,z, level, itemStack, null);
    }

    @Override
    public boolean displayFireAnimation() {
        return true;
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return ItemStack.EMPTY;
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        Entity target = result.getEntity();
        target.setRemainingFireTicks(100);
    }
}
