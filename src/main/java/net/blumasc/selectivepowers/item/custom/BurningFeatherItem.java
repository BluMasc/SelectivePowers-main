package net.blumasc.selectivepowers.item.custom;

import net.blumasc.selectivepowers.entity.custom.projectile.FlamingFeatherEntity;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.level.Level;

public class BurningFeatherItem extends Item implements ProjectileItem {
    public BurningFeatherItem(Properties properties) {
        super(properties);
    }

    @Override
    public Projectile asProjectile(Level level, Position pos, ItemStack stack, Direction direction) {
        return new FlamingFeatherEntity(level, pos.x(), pos.y(), pos.z(), stack.copyWithCount(1));
    }
}
