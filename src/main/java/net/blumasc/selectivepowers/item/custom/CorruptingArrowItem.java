package net.blumasc.selectivepowers.item.custom;

import net.blumasc.selectivepowers.entity.custom.projectile.CorruptingArrowEntity;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.SpectralArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class CorruptingArrowItem extends ArrowItem {
    public CorruptingArrowItem(Properties properties) {
        super(properties);
    }


    @Override
    public AbstractArrow createArrow(Level level, ItemStack stack, LivingEntity entity, @Nullable ItemStack weapon) {
        return new CorruptingArrowEntity(entity, level, stack.copyWithCount(1), weapon);
    }


    @Override
    public Projectile asProjectile(Level level, Position pos, ItemStack stack, Direction direction) {
        CorruptingArrowEntity spectralarrow = new CorruptingArrowEntity(level, pos.x(), pos.y(), pos.z(), stack.copyWithCount(1), (ItemStack)null);
        spectralarrow.pickup = AbstractArrow.Pickup.ALLOWED;
        return spectralarrow;
    }
}
