package net.blumasc.selectivepowers.entity.custom.projectile;

import net.blumasc.selectivepowers.entity.SelectivepowersEntities;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;


public class ShardProjectileEntity extends AbstractArrow {

    private Vec3 startPos;
    private double max_distance = 6.0;
    private float strength = 3.0f;
    private final Holder<DamageType> damageType;
    private static final int DEFAULT_COLOR = 0x950606;

    private static final EntityDataAccessor<Integer> DATA_COLOR =
            SynchedEntityData.defineId(ShardProjectileEntity.class, EntityDataSerializers.INT);

    public ShardProjectileEntity(LivingEntity owner, Level level, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon) {
        super(SelectivepowersEntities.SHARD_PROJECTILE.get(), owner, level, pickupItemStack, firedFromWeapon);
        damageType = level.damageSources().arrow(this, owner).typeHolder();
    }

    public ShardProjectileEntity(Level level, double x, double y, double z, ItemStack pickUpItemStack, ItemStack weapon){
        super(SelectivepowersEntities.SHARD_PROJECTILE.get(), x, y, z, level, pickUpItemStack, weapon);
        damageType = level.damageSources().arrow(this, this.getOwner()).typeHolder();
    }

    public ShardProjectileEntity(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
        damageType = level.damageSources().arrow(this, this.getOwner()).typeHolder();
    }

    public ShardProjectileEntity(EntityType<? extends AbstractArrow> entityType, double x, double y, double z, Level level) {
        super(entityType, x, y, z, level, ItemStack.EMPTY, null);
        damageType = level.damageSources().arrow(this, this.getOwner()).typeHolder();

    }

    public ShardProjectileEntity(EntityType<? extends AbstractArrow> entityType, LivingEntity owner, Level level) {
        super(entityType, owner, level, ItemStack.EMPTY, null);
        damageType = level.damageSources().arrow(this, owner).typeHolder();
    }

    public ShardProjectileEntity(LivingEntity shooter, Level level, Holder<DamageType> damageType, int color)
    {
        super(SelectivepowersEntities.SHARD_PROJECTILE.get(), level);
        this.setOwner(shooter);
        this.entityData.set(DATA_COLOR, color);
        this.damageType = damageType;
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }


    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_COLOR, DEFAULT_COLOR);
    }

    public int getColor() {
        return this.entityData.get(DATA_COLOR);
    }

    @Override
    public void tick() {
        if (startPos == null) {
            startPos = this.position();
        }

        super.tick();

        if (!this.level().isClientSide) {
            if (this.position().distanceTo(startPos) >= max_distance) {
                this.discard();
            }
        }else if(!this.inGround){
            spawnDustParticles();
        }
    }

    private void spawnDustParticles() {
        RandomSource random = level().random;

        for (int i = 0; i < 2; i++) { // spawn multiple per tick
            double offsetX = (random.nextDouble() - 0.5) * 0.2;
            double offsetY = (random.nextDouble() - 0.5) * 0.2;
            double offsetZ = (random.nextDouble() - 0.5) * 0.2;

            double px = this.getX() + offsetX;
            double py = this.getY() + offsetY;
            double pz = this.getZ() + offsetZ;

            int color = getColor();
            float r = ((color >> 16) & 0xFF) / 255f;
            float g = ((color >> 8) & 0xFF) / 255f;
            float b = (color & 0xFF) / 255f;

            DustParticleOptions dust = new DustParticleOptions(new Vector3f(r, g, b), 0.5f);
            level().addParticle(dust, px, py, pz, 0, -0.15, 0);
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity target = result.getEntity();

        if (!this.level().isClientSide && target instanceof LivingEntity living) {
            living.hurt(
                    new DamageSource(this.damageType, this.getOwner()),
                    strength
            );
        }

        this.discard();
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.DRIPSTONE_BLOCK_BREAK;
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return ItemStack.EMPTY;
    }
}
