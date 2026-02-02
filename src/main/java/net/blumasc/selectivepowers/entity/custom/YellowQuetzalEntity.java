package net.blumasc.selectivepowers.entity.custom;

import net.blumasc.selectivepowers.PowerManager;
import net.blumasc.selectivepowers.entity.custom.projectile.CorruptingArrowEntity;
import net.blumasc.selectivepowers.entity.custom.projectile.FlamingFeatherEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class YellowQuetzalEntity extends QuetzalEntity{
    public YellowQuetzalEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected boolean canTargetPlayer(LivingEntity e) {
        if(!(e instanceof Player player)) return false;
        if (player.isCreative() || player.isSpectator()) {
            return false;
        }
        if(e.level() instanceof ServerLevel sl)
        {
            PowerManager pm = PowerManager.get(sl);
            return pm.getPowerOfPlayer(player.getUUID()).equals(PowerManager.YELLOW_POWER);
        }

        return true;
    }

    @Override
    protected void shootProjectile(Vec3 direction) {
        CorruptingArrowEntity projectile =
                new CorruptingArrowEntity(this, this.level(), ItemStack.EMPTY, null);

        projectile.setPos(this.getX(), this.getEyeY(), this.getZ());
        projectile.shoot(direction.x, direction.y, direction.z, 1.2F, 0.0F);
        this.level().addFreshEntity(projectile);
    }

    @Override
    protected void layEgg() {
        return;
    }
}
