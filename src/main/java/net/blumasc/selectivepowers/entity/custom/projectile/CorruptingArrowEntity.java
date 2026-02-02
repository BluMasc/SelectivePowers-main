package net.blumasc.selectivepowers.entity.custom.projectile;

import net.blumasc.selectivepowers.effect.SelectivepowersEffects;
import net.blumasc.selectivepowers.entity.SelectivepowersEntities;
import net.blumasc.selectivepowers.item.SelectivepowersItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.Nullable;

import static net.blumasc.selectivepowers.entity.helper.YellowFeverHelper.increaseEffect;
import static net.blumasc.selectivepowers.entity.helper.YellowFeverHelper.isYellowFeverImmune;

public class CorruptingArrowEntity extends AbstractArrow {

    public CorruptingArrowEntity(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
    }

    public CorruptingArrowEntity(EntityType<? extends AbstractArrow> entityType, double x, double y, double z, Level level, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon) {
        super(entityType, x, y, z, level, pickupItemStack, firedFromWeapon);
    }

    public CorruptingArrowEntity(EntityType<? extends AbstractArrow> entityType, LivingEntity owner, Level level, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon) {
        super(entityType, owner, level, pickupItemStack, firedFromWeapon);
    }

    public CorruptingArrowEntity(LivingEntity owner, Level level, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon) {
        super(SelectivepowersEntities.CORRUPTING_ARROW.get(), owner, level, pickupItemStack, firedFromWeapon);
    }

    public CorruptingArrowEntity(Level level, double x, double y, double z, ItemStack pickUpItemStack, ItemStack weapon){
        super(SelectivepowersEntities.CORRUPTING_ARROW.get(), x, y, z, level, pickUpItemStack, weapon);
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return SelectivepowersItems.CORRUPTING_ARROW.get().getDefaultInstance();
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        Level var3 = this.level();
        if (var3 instanceof ServerLevel serverlevel) {
            Entity entity1 = result.getEntity();
            if(!isYellowFeverImmune(entity1)) {
                if (entity1 instanceof LivingEntity livingEntity) {
                    increaseEffect(livingEntity, SelectivepowersEffects.YELLOW_FEVER_EFFECT, this.getRandom());
                }
            }
        }
    }


}
