package net.blumasc.selectivepowers.entity.custom;

import net.blumasc.selectivepowers.PowerManager;
import net.blumasc.selectivepowers.entity.custom.projectile.RuneProjectileEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.UUID;

public class CorruptingMaskEntity extends Monster implements RangedAttackMob {
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState attackAnimationState = new AnimationState();
    private UUID summonerUUID;

    private static final int ATTACK_ANIMATION_LENGTH = 15; // ticks
    private static final int PROJECTILE_FIRE_TICK = 14;    // when projectile spawns

    private int attackAnimationTick = 0;
    private static final EntityDataAccessor<Boolean> ATTACKING =
            SynchedEntityData.defineId(CorruptingMaskEntity.class, EntityDataSerializers.BOOLEAN);

    public CorruptingMaskEntity(EntityType<? extends CorruptingMaskEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected int calculateFallDamage(float fallDistance, float damageMultiplier) {
        return 0;
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    public void setSummoner(UUID uuid) {
        this.summonerUUID = uuid;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ATTACKING, false);
    }

    public boolean isAttacking() {
        return this.entityData.get(ATTACKING);
    }

    public void setAttacking(boolean delivering) {
        this.entityData.set(ATTACKING, delivering);
    }

    @Nullable
    public LivingEntity getSummoner() {
        if (summonerUUID == null) return null;
        return this.level().getPlayerByUUID(summonerUUID);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (summonerUUID != null) {
            tag.putUUID("Summoner", summonerUUID);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.hasUUID("Summoner")) {
            summonerUUID = tag.getUUID("Summoner");
        }
    }

    @Override
    protected void registerGoals() {
        // No movement goals
        this.goalSelector.addGoal(1, new LookAtPlayerGoal(this, Player.class, 32.0F));
        this.goalSelector.addGoal(2, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(3, new RangedAttackGoal(this, 1.0D, 40, 32.0F));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(
                this,
                Player.class,
                10,
                true,
                false,
                player -> {
                    LivingEntity summoner = getSummoner();
                    if(player.level() instanceof ServerLevel sl) {
                        PowerManager pm = PowerManager.get(sl);
                        if (summoner == null) {
                            return pm.getPowerOfPlayer(player.getUUID()) != PowerManager.YELLOW_POWER;
                        }
                        return player.distanceToSqr(summoner) < 32 * 32 &&
                                pm.getPowerOfPlayer(player.getUUID()) != PowerManager.YELLOW_POWER;
                    }
                    return summoner != null &&
                            player.distanceToSqr(summoner) < 32 * 32;
                }
        ));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
    }

    @Override
    protected void applyGravity() {
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    private void setupAnimationState()
    {
        if(isAttacking())
        {
            attackAnimationState.startIfStopped(this.tickCount);
        }else{
            attackAnimationState.stop();
        }
        idleAnimationState.startIfStopped(this.tickCount);

    }

    @Override
    public void onAddedToLevel() {
        super.onAddedToLevel();
    }

    @Override
    public void tick() {
        super.tick();

        if(getDeltaMovement().length()>0.1)
        {
            setDeltaMovement(getDeltaMovement().scale(0.9));
        }else{
            setDeltaMovement(Vec3.ZERO);
        }

        maintainHoverHeight();

        if (!this.level().isClientSide && isAttacking()) {
            attackAnimationTick++;

            if (attackAnimationTick == PROJECTILE_FIRE_TICK) {
                LivingEntity target = this.getTarget();
                if (target != null && target.isAlive()) {
                    fireProjectile(target);
                }
            }

            if (attackAnimationTick >= ATTACK_ANIMATION_LENGTH) {
                setAttacking(false);
            }
        }

        if (this.level().isClientSide) {
            setupAnimationState();
        }
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void doPush(Entity entity) {
        // no
    }

    @Override
    public void knockback(double strength, double x, double z) {
        // no
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, (double)0.0F)
                .add(Attributes.FOLLOW_RANGE, (double)32.0F)
                .add(Attributes.MAX_HEALTH, (double)20.0F)
        .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }


    @Override
    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        if (isAttacking()) return;

        setAttacking(true);
        this.attackAnimationTick = 0;
    }

    private void fireProjectile(LivingEntity target) {
        RuneProjectileEntity projectile =
                new RuneProjectileEntity(this, this.level());

        projectile.setPos(
                this.getX(),
                this.getEyeY() - 0.1,
                this.getZ()
        );

        double dx = target.getX() - this.getX();
        double dy = target.getEyeY() - projectile.getY();
        double dz = target.getZ() - this.getZ();

        projectile.shoot(dx, dy, dz, 1.3F, 0.0F);
        this.level().addFreshEntity(projectile);
    }

    private void maintainHoverHeight() {
        Vec3 motion = this.getDeltaMovement();
        int yBelow1 = (int) Math.floor(this.getY() - 1);
        int yBelow2 = (int) Math.floor(this.getY() - 2);
        int yAbove1 = (int) Math.floor(this.getY() + 1);

        boolean blockBelow1IsAir = level().getBlockState(this.blockPosition().below(1)).isAir();
        boolean blockBelow2IsAir = level().getBlockState(this.blockPosition().below(2)).isAir();
        boolean blockAboveIsAir = level().getBlockState(this.blockPosition().above(1)).isAir();

        if (blockBelow1IsAir && blockBelow2IsAir) {
            this.setDeltaMovement(motion.x, motion.y-0.05, motion.z); // slowly descend
        }
        else if (!blockBelow1IsAir && blockAboveIsAir) {
            this.setDeltaMovement(motion.x, motion.y+0.05, motion.z); // slowly ascend
        }
        else {
            double blockBottomY = Math.floor(this.getY()) + 0.1; // slightly above bottom
            double deltaY = blockBottomY - this.getY();

            if(deltaY>0.2) {
                double adjustment = Math.max(Math.min(deltaY * 0.2, 0.05), -0.05);
                this.setDeltaMovement(motion.x, motion.y+adjustment, motion.z);
            }
        }
    }

}
