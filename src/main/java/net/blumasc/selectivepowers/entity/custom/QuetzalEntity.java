package net.blumasc.selectivepowers.entity.custom;

import net.blumasc.selectivepowers.entity.custom.projectile.FlamingFeatherEntity;
import net.blumasc.selectivepowers.item.SelectivepowersItems;
import net.blumasc.selectivepowers.sound.SelectivepowersSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.damagesource.DamageContainer;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;

public class QuetzalEntity extends Monster implements FlyingAnimal {


    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState attackAnimationState = new AnimationState();
    public final AnimationState shootAnimationState = new AnimationState();


    protected static final int SHOOT_COOLDOWN = 20 * 60;


    private static final EntityDataAccessor<Integer> DATA_ATTACK_PHASE =
            SynchedEntityData.defineId(QuetzalEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> DATA_SHOOT_COOLDOWN =
            SynchedEntityData.defineId(QuetzalEntity.class, EntityDataSerializers.INT);


    protected int eggLayTimer = 3000;

    protected float orbitAngle = 0;

    protected enum AttackPhase {
        CIRCLE,
        SWOOP
    }

    @Override
    public void addAdditionalSaveData(net.minecraft.nbt.CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("EggTimer", this.eggLayTimer);
        compound.putInt("ShootTimer", this.getShootCooldownTicks());
    }

    @Override
    public void readAdditionalSaveData(net.minecraft.nbt.CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.eggLayTimer = compound.getInt("EggTimer");
        this.setShootCooldownTicks(compound.getInt("ShootTimer"));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_ATTACK_PHASE, AttackPhase.CIRCLE.ordinal());
        builder.define(DATA_SHOOT_COOLDOWN, 0);
    }

    public AttackPhase getAttackPhase() {
        return AttackPhase.values()[this.entityData.get(DATA_ATTACK_PHASE)];
    }

    public void setAttackPhase(AttackPhase phase) {
        this.entityData.set(DATA_ATTACK_PHASE, phase.ordinal());
    }

    public int getShootCooldownTicks() {
        return this.entityData.get(DATA_SHOOT_COOLDOWN);
    }

    public void setShootCooldownTicks(int ticks) {
        this.entityData.set(DATA_SHOOT_COOLDOWN, ticks);
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return SelectivepowersSounds.QUETZAL_HISS.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SelectivepowersSounds.QUETZAL_HIT.get();
    }


    public QuetzalEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level);
        this.moveControl = new QuetzalMoveControl(this);
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    public void onDamageTaken(DamageContainer damageContainer) {
        super.onDamageTaken(damageContainer);
        setAttackPhase(AttackPhase.CIRCLE);
    }


    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.FLYING_SPEED, 0.6D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.ATTACK_DAMAGE, 6.0D)
                .add(Attributes.FOLLOW_RANGE, 32.0D);
    }


    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new QuetzalShootGoal(this));
        this.goalSelector.addGoal(2, new QuetzalCircleGoal(this));
        this.goalSelector.addGoal(3, new QuetzalSwoopGoal(this));
        this.goalSelector.addGoal(4, new QuetzalRandomFlyGoal(this));

        this.targetSelector.addGoal(1,
                new NearestAttackableTargetGoal<>(
                        this,
                        Player.class,
                        true,
                        this::canTargetPlayer
                )
        );
    }

    protected boolean canTargetPlayer(LivingEntity entity) {
        if (!(entity instanceof Player player)) return false;
        return !player.isCreative() && !player.isSpectator();
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new FlyingPathNavigation(this, level);
    }

    @Override
    public boolean isFlying() {
        return true;
    }

    @Override
    public boolean causeFallDamage(float f, float g, DamageSource source) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {}

    @Override
    public void tick() {
        super.tick();

        if (getShootCooldownTicks() > 0) setShootCooldownTicks(getShootCooldownTicks() - 1);

        if(getShootCooldownTicks() == SHOOT_COOLDOWN-10) shootAtPlayers();

        updateRotationFromMovement();

        if (level().isClientSide) updateAnimations();

        if (!level().isClientSide) layEgg();
    }

    protected void updateAnimations() {
        if(getShootCooldownTicks() >=SHOOT_COOLDOWN-30)
        {
            idleAnimationState.stop();
            attackAnimationState.stop();
            shootAnimationState.startIfStopped(tickCount);
        }
        else if (getAttackPhase() == AttackPhase.SWOOP) {
            idleAnimationState.stop();
            shootAnimationState.stop();
            attackAnimationState.startIfStopped(tickCount);
        } else {
            attackAnimationState.stop();
            shootAnimationState.stop();
            idleAnimationState.startIfStopped(tickCount);
        }
    }

    protected void updateRotationFromMovement() {
        Vec3 vel = this.getDeltaMovement();
        if (vel.lengthSqr() < 0.001) return;

        double horizontal = Math.sqrt(vel.x * vel.x + vel.z * vel.z);

        float yaw = (float)(Math.atan2(vel.z, vel.x) * (180F / Math.PI)) - 90F;
        float pitch = (float)(-(Math.atan2(vel.y, horizontal) * (180F / Math.PI)));

        this.setYRot(yaw);
        this.setXRot(pitch);

        this.yRotO = yaw;
        this.xRotO = pitch;
    }

    protected void layEgg() {
        --eggLayTimer;
        if (eggLayTimer > 0) return;

        spawnAtLocation(SelectivepowersItems.FLAMING_EGG.get());
        level().playSound(null, blockPosition(), SoundEvents.TURTLE_LAY_EGG, getSoundSource(), 1F, 1F);
        eggLayTimer = random.nextInt(3000) + 3000;
    }

    protected void shootProjectile(Vec3 direction) {
        FlamingFeatherEntity projectile = new FlamingFeatherEntity(level(), this);
        projectile.setPos(getX(), getEyeY(), getZ());
        projectile.shoot(direction.x, direction.y, direction.z, 1.2F, 0);
        level().addFreshEntity(projectile);
    }

    protected void shootAtPlayers() {
        List<Player> players = level().getNearbyPlayers(
                TargetingConditions.forCombat(), this,
                getBoundingBox().inflate(20)
        );

        for (int i = 0; i < 4; i++) {
            Vec3 dir = i < players.size()
                    ? players.get(i).position().subtract(position()).normalize()
                    : Vec3.directionFromRotation(0, i * 90);

            shootProjectile(dir);
        }
    }

    protected static class QuetzalMoveControl extends MoveControl {
        protected final QuetzalEntity q;

        protected QuetzalMoveControl(QuetzalEntity q) {
            super(q);
            this.q = q;
        }

        @Override
        public void tick() {
            if (operation != Operation.MOVE_TO) return;

            Vec3 delta = new Vec3(wantedX - q.getX(), wantedY - q.getY(), wantedZ - q.getZ());
            double len = delta.length();
            if (len < 0.3) {
                operation = Operation.WAIT;
                return;
            }

            q.setDeltaMovement(q.getDeltaMovement().add(delta.normalize().scale(0.08)));
        }
    }

    protected static class QuetzalCircleGoal extends Goal {
        protected final QuetzalEntity q;

        protected QuetzalCircleGoal(QuetzalEntity q) {
            this.q = q;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return q.getTarget() != null && q.getAttackPhase() == AttackPhase.CIRCLE;
        }

        @Override
        public boolean canContinueToUse() {
            return q.getTarget() != null && q.getAttackPhase() == AttackPhase.CIRCLE;
        }

        @Override
        public void tick() {
            LivingEntity target = q.getTarget();
            if (target == null) return;
            if (q.tickCount % (q.getRandom().nextInt(100)+100) == 0) {
                q.setAttackPhase(AttackPhase.SWOOP);
            }

            q.orbitAngle += 0.03F;

            double radius = 10.0;
            double x = target.getX() + radius * Math.cos(q.orbitAngle);
            double z = target.getZ() + radius * Math.sin(q.orbitAngle);
            double y = target.getY()+10.0;

            while(!target.level().getBlockState(BlockPos.containing(x,y,z)).isAir() &&!target.level().getBlockState(BlockPos.containing(x,y,z).above()).isAir()){
                y-=1;
                if(y==target.getY()){
                    return;
                }
            }

            Vec3 orbitPos = new Vec3(
                    x,
                    y,
                    z

            );

            Vec3 current = q.position();
            Vec3 smooth = current.lerp(orbitPos, 0.1);

            q.getMoveControl().setWantedPosition(
                    smooth.x,
                    smooth.y,
                    smooth.z,
                    2.0
            );
        }

    }

    protected static class QuetzalSwoopGoal extends Goal {
        protected final QuetzalEntity q;

        protected QuetzalSwoopGoal(QuetzalEntity q) {
            this.q = q;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return q.getTarget() != null && q.getAttackPhase() == AttackPhase.SWOOP;
        }

        @Override
        public boolean canContinueToUse() {
            return q.getTarget() != null && q.getAttackPhase() == AttackPhase.SWOOP;
        }

        @Override
        public void stop() {
            q.setAttackPhase(AttackPhase.CIRCLE);
        }

        @Override
        public void start() {
            LivingEntity target = q.getTarget();
            if (target != null) {
                q.getMoveControl().setWantedPosition(
                        target.getX(),
                        target.getY(),
                        target.getZ(),
                        2.5
                );
            }
        }

        @Override
        public void tick() {
            LivingEntity target = q.getTarget();
            if (target != null && q.distanceToSqr(target) < 3.0) {
                q.doHurtTarget(target);
                q.setAttackPhase(AttackPhase.CIRCLE);
            }
            Vec3 from = q.position().add(0, q.getBbHeight() * 0.5, 0);
            Vec3 to = target.position().add(0, target.getBbHeight() * 0.5, 0);
            var hit = q.level().clip(new net.minecraft.world.level.ClipContext(
                    from,
                    to,
                    net.minecraft.world.level.ClipContext.Block.COLLIDER,
                    net.minecraft.world.level.ClipContext.Fluid.NONE,
                    q
            ));

            if (hit.getType() == net.minecraft.world.phys.HitResult.Type.BLOCK) {
                q.setAttackPhase(AttackPhase.CIRCLE);
                return;
            }
            if(q.onGround())
            {
                q.setAttackPhase(AttackPhase.CIRCLE);
                return;
            }
            moveTowardsTarget();
        }
        private void moveTowardsTarget() {
            LivingEntity target = q.getTarget();
            if (target != null) {
                q.getMoveControl().setWantedPosition(
                        target.getX(),
                        target.getY(),
                        target.getZ(),
                        2.5
                );
            }
        }
    }

    protected class QuetzalShootGoal extends Goal {
        protected final QuetzalEntity q;

        protected QuetzalShootGoal(QuetzalEntity q) {
            this.q = q;
        }

        @Override
        public boolean canUse() {
            return q.getShootCooldownTicks() <= 0 &&
                    !q.level().getNearbyPlayers(
                            TargetingConditions.forCombat(), q,
                            q.getBoundingBox().inflate(20)
                    ).isEmpty();
        }

        @Override
        public boolean canContinueToUse() {
            return q.getShootCooldownTicks() <= 0;
        }

        @Override
        public void start() {
            q.setShootCooldownTicks(SHOOT_COOLDOWN);
        }
    }
    protected static class QuetzalRandomFlyGoal extends Goal {
        protected final QuetzalEntity q;
        protected int cooldown = 0;

        protected QuetzalRandomFlyGoal(QuetzalEntity q) {
            this.q = q;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return q.getTarget() == null;
        }

        @Override
        public boolean canContinueToUse() {
            return q.getTarget() == null;
        }

        @Override
        public void tick() {
            if (cooldown-- > 0) return;
            cooldown = 40 + q.random.nextInt(60);

            Vec3 pos = getRandomAirPosition();
            if (pos != null) {
                q.getMoveControl().setWantedPosition(pos.x, pos.y, pos.z, 0.8);
            }
        }

        protected Vec3 getRandomAirPosition() {
            Vec3 base = q.position();

            double dx = base.x + q.random.nextGaussian() * 10;
            double dy = base.y + q.random.nextGaussian() * 4;
            double dz = base.z + q.random.nextGaussian() * 10;

            return new Vec3(dx, dy, dz);
        }
    }
}
