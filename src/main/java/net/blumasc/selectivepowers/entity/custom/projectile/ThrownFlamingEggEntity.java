package net.blumasc.selectivepowers.entity.custom.projectile;

import net.blumasc.selectivepowers.entity.SelectivepowersEntities;
import net.blumasc.selectivepowers.item.SelectivepowersItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.entity.projectile.ThrownEgg;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class ThrownFlamingEggEntity extends ThrowableItemProjectile {
    private static final EntityDimensions ZERO_SIZED_DIMENSIONS = EntityDimensions.fixed(0.0F, 0.0F);

    public ThrownFlamingEggEntity(EntityType<? extends ThrownFlamingEggEntity> entityType, Level level) {
        super(entityType, level);
    }

    public ThrownFlamingEggEntity(Level level, LivingEntity shooter) {
        super(SelectivepowersEntities.FLAMING_EGG.get(), shooter, level);
    }

    public ThrownFlamingEggEntity(Level level, double x, double y, double z) {
        super(SelectivepowersEntities.FLAMING_EGG.get(), x, y, z, level);
    }

    public void handleEntityEvent(byte id) {
        if (id == 3) {
            double d0 = 0.08;

            for(int i = 0; i < 8; ++i) {
                this.level().addParticle(new ItemParticleOption(ParticleTypes.ITEM, this.getItem()), this.getX(), this.getY(), this.getZ(), ((double)this.random.nextFloat() - (double)0.5F) * 0.08, ((double)this.random.nextFloat() - (double)0.5F) * 0.08, ((double)this.random.nextFloat() - (double)0.5F) * 0.08);
            }
        }

    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);

        if (!this.level().isClientSide) {
            result.getEntity().hurt(
                    this.damageSources().thrown(this, this.getOwner()),
                    0.0F
            );

            result.getEntity().setRemainingFireTicks(5);
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);

        if (!this.level().isClientSide) {
            BlockPos pos = result.getBlockPos().relative(result.getDirection());

            if (this.level().isEmptyBlock(pos)) {
                this.level().setBlockAndUpdate(pos, Blocks.FIRE.defaultBlockState());
            }
        }
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);

        if (!this.level().isClientSide) {
            this.level().broadcastEntityEvent(this, (byte)3);
            this.discard();
        }
    }


    protected Item getDefaultItem() {
        return SelectivepowersItems.FLAMING_EGG.get();
    }
}