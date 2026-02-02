package net.blumasc.selectivepowers.entity.custom;

import net.blumasc.selectivepowers.PowerManager;
import net.blumasc.selectivepowers.effect.SelectivepowersEffects;
import net.blumasc.selectivepowers.sound.SelectivepowersSounds;
import net.minecraft.core.Holder;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import team.lodestar.lodestone.registry.common.particle.LodestoneParticleTypes;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.data.spin.SpinParticleData;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.EnumSet;
import java.util.UUID;

import static net.blumasc.selectivepowers.entity.helper.YellowFeverHelper.increaseEffect;

public class YellowFanaticEntity extends Monster {
    public final AnimationState prayingAnimationState = new AnimationState();
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState attackAnimationState = new AnimationState();
    private static final EntityDataAccessor<Integer> SUMMONER_ID =
            SynchedEntityData.defineId(YellowFanaticEntity.class, EntityDataSerializers.INT);

    private int prayerTick = 0;
    private int attackTick = 0;
    private static final EntityDataAccessor<Boolean> PRAYING =
            SynchedEntityData.defineId(YellowFanaticEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> ATTACKING =
            SynchedEntityData.defineId(YellowFanaticEntity.class, EntityDataSerializers.BOOLEAN);

    private static final int HEAL_INTERVAL = 40; // 2 seconds
    private static final float HEAL_AMOUNT = 1.0F;
    public YellowFanaticEntity(EntityType<? extends YellowFanaticEntity> entityType, Level level) {
        super(entityType, level);
    }

    private void setupAnimationState() {
        idleAnimationState.startIfStopped(this.tickCount);
        if (isPraying()) {
            prayingAnimationState.startIfStopped(this.tickCount);
        } else {
            prayingAnimationState.stop();
        }
        if(isAttacking())
        {
            attackAnimationState.startIfStopped(this.tickCount);
        }else{
            attackAnimationState.stop();
        }
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        if(this.random.nextInt(10)<4 && target instanceof LivingEntity le)
        {
            increaseEffect(le, SelectivepowersEffects.YELLOW_FEVER_EFFECT, this.getRandom());
        }
        return super.doHurtTarget(target);
    }

    private void renderPrayerBeam(YellowKingBossEntity summoner) {
        if (!this.level().isClientSide) return;

        Vec3 from = this.position().add(0, 0.7, 0.0);
        Vec3 to   = summoner.position().add(0, 1.8, 0.0);

        Vec3 motion = to.subtract(from).normalize().scale(0.09)
                .add(
                        this.random.nextGaussian() * 0.01,
                        this.random.nextGaussian() * 0.01,
                        this.random.nextGaussian() * 0.01
                );

        for (int i = 0; i < 2; i++) {
            spawnParticle(
                    this.level(),
                    from.x + (this.random.nextGaussian() * 0.05),
                    from.y + (this.random.nextGaussian() * 0.05),
                    from.z + (this.random.nextGaussian() * 0.05),
                    new Color(255, 215, 64),   // yellow
                    new Color(255, 160, 32),   // orange
                    motion.x,
                    motion.y,
                    motion.z
            );
        }
    }

    public static void spawnParticle(Level level, double x, double y, double z, Color startingColor, Color endingColor, double moveX, double moveY, double moveZ) {
        if (level instanceof ServerLevel) return;
        WorldParticleBuilder.create(LodestoneParticleTypes.WISP_PARTICLE)
                .setScaleData(GenericParticleData.create(0.2f, 0).build())
                .setTransparencyData(GenericParticleData.create(0.75f, 0.25f).build())
                .setColorData(ColorParticleData.create(startingColor, endingColor).setCoefficient(1.4f).setEasing(Easing.BOUNCE_IN_OUT).build())
                .setSpinData(SpinParticleData.create(0.2f, 0.4f).setSpinOffset((level.getGameTime() * 0.2f) % 6.28f).setEasing(Easing.QUARTIC_IN).build())
                .setLifetime(80)
                .addMotion(moveX, moveY, moveZ)
                .enableNoClip()
                .spawn(level, x, y, z);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ATTACKING, false);
        builder.define(PRAYING, false);
        builder.define(SUMMONER_ID, -1);
    }

    @Override
    public void addAdditionalSaveData(net.minecraft.nbt.CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("SummonerID", this.entityData.get(SUMMONER_ID));
    }

    @Override
    public void readAdditionalSaveData(net.minecraft.nbt.CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setSummoner(compound.getInt("SummonerID"));
    }

    public boolean isAttacking() {
        return this.entityData.get(ATTACKING);
    }

    public void setAttacking(boolean delivering) {
        this.entityData.set(ATTACKING, delivering);
    }

    public boolean isPraying() {
        return this.entityData.get(PRAYING);
    }

    public void setPraying(boolean delivering) {
        this.entityData.set(PRAYING, delivering);
    }

    @Override
    public void swing(InteractionHand hand) {
        setAttacking(true);
        if (!isSilent()) {
            level().playSound(null, getX(),getY(), getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, getSoundSource(), 0.3F, 1.0F + (random.nextFloat() - random.nextFloat()) * 0.2F);
        }
        attackTick = 9;
        super.swing(hand);
    }

    @Nullable
    public YellowKingBossEntity getSummoner() {
        int id = this.entityData.get(SUMMONER_ID);
        if (id == -1) return null;

        return this.level().getEntity(id) instanceof YellowKingBossEntity boss
                ? boss
                : null;
    }

    public void setSummoner(int id) {
        this.entityData.set(SUMMONER_ID, id);
    }

    @Override
    protected @org.jetbrains.annotations.Nullable SoundEvent getAmbientSound() {
        return isPraying()? SelectivepowersSounds.FANATIC_PRAYING.get():SoundEvents.PLAYER_BREATH;
    }

    @Override
    protected @org.jetbrains.annotations.Nullable SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.PLAYER_HURT;
    }

    @Override
    protected @org.jetbrains.annotations.Nullable SoundEvent getDeathSound() {
        return SoundEvents.PLAYER_DEATH;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new PrayForSummonerGoal(this));
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(4, new MoveTowardsRestrictionGoal(this, 1.0D));
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));

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
    public void tick() {
        super.tick();

        if(attackTick>0)
        {
            --attackTick;
        }else{
            setAttacking(false);
        }

        if(this.level().isClientSide())
        {
            setupAnimationState();
            if (isPraying()) {
                YellowKingBossEntity summoner = getSummoner();
                if (summoner != null) {
                    renderPrayerBeam(summoner);
                }
            }
        }
    }

    @Override
    public boolean canAttack(LivingEntity target) {
        return !isPraying() && super.canAttack(target);
    }

    @Override
    public void die(DamageSource source) {
        YellowKingBossEntity summoner = getSummoner();
        if(summoner !=null) summoner.clearPrayingFanatic(this);
        super.die(source);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, (double)0.3F)
                .add(Attributes.FOLLOW_RANGE, (double)12.0F)
                .add(Attributes.MAX_HEALTH, (double)24.0F)
                .add(Attributes.ATTACK_DAMAGE, (double)4.0f);
    }

    public class PrayForSummonerGoal extends Goal {
        private final YellowFanaticEntity fanatic;
        private YellowKingBossEntity summoner;

        public PrayForSummonerGoal(YellowFanaticEntity fanatic) {
            this.fanatic = fanatic;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            summoner = fanatic.getSummoner();
            if(summoner == null) return false;
            if (fanatic.isPraying()) return false;

            return summoner.tryAssignPrayingFanatic(fanatic);
        }

        @Override
        public boolean canContinueToUse() {
                if(!summoner.isPrayingFanatic(fanatic.getId())) return false;
                if (summoner == null || !summoner.isAlive()) return false;
                if (!fanatic.isPraying()) return false;
            return !(fanatic.distanceToSqr(summoner) > 10 * 10);

        }

        @Override
        public void start() {
            fanatic.setPraying(true);
            fanatic.prayerTick = 0;
        }



        @Override
        public void tick() {
            fanatic.getLookControl().setLookAt(summoner);
                fanatic.getNavigation().stop();

                fanatic.prayerTick++;
                if (fanatic.prayerTick % HEAL_INTERVAL == 0) {
                    summoner.heal(HEAL_AMOUNT);
                }
        }

        @Override
        public void stop() {
            fanatic.setPraying(false);
            fanatic.prayingAnimationState.stop();
            summoner.clearPrayingFanatic(fanatic);
        }
    }

}
