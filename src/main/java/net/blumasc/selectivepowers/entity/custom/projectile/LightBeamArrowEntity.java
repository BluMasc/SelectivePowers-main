package net.blumasc.selectivepowers.entity.custom.projectile;

import net.blumasc.selectivepowers.entity.CircleVariant;
import net.blumasc.selectivepowers.entity.SelectivepowersEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class LightBeamArrowEntity extends AbstractArrow {


    public LightBeamArrowEntity(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
    }

    public LightBeamArrowEntity(LivingEntity owner, Level level, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon) {
        super(SelectivepowersEntities.LIGHT_BEAM_ARROW.get(), owner, level, pickupItemStack, firedFromWeapon);
    }

    public LightBeamArrowEntity(Level level, double x, double y, double z, ItemStack pickUpItemStack, ItemStack weapon){
        super(SelectivepowersEntities.LIGHT_BEAM_ARROW.get(), x, y, z, level, pickUpItemStack, weapon);
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return null;
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        BlockPos pos = result.getBlockPos();
        MagicCircleEntity circle =
                new MagicCircleEntity(SelectivepowersEntities.MAGIC_CIRCLE.get(), this.level(), CircleVariant.CELESTIAL, 40, 60, 3);
        circle.moveTo(pos.above().getBottomCenter());
        this.level().addFreshEntity(circle);
        this.discard();
    }
}
