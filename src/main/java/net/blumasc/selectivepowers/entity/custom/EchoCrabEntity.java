package net.blumasc.selectivepowers.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class EchoCrabEntity extends Animal implements Shearable {
    public final AnimationState snapAnimationState = new AnimationState();
    public final AnimationState idleAnimationState = new AnimationState();

    private static final EntityDataAccessor<Integer> GEM =
            SynchedEntityData.defineId(EchoCrabEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> SNAPPING =
            SynchedEntityData.defineId(EchoCrabEntity.class, EntityDataSerializers.BOOLEAN);

    private int snappingTimer = 0;

    private boolean hurt = false;

    private UUID revengeTargetUUID;

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.getEntity() instanceof Player player) {
            this.revengeTargetUUID = player.getUUID();
            this.hurt = true;
        }
        return super.hurt(source, amount);
    }


    public EchoCrabEntity(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(GEM, 100);
        builder.define(SNAPPING, false);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("has_gem", this.hasGem());
        compound.putBoolean("hurt", this.hurt);
        if (this.revengeTargetUUID != null) {
            compound.putUUID("RevengeTarget", this.revengeTargetUUID);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.entityData.set(GEM, compound.getInt("has_gem"));
        this.hurt = compound.getBoolean("hurt");
        if (compound.hasUUID("RevengeTarget")) {
            this.revengeTargetUUID = compound.getUUID("RevengeTarget");
        } else {
            this.revengeTargetUUID = null;
        }
    }

    @Nullable
    private Player getRevengeTarget() {
        if (this.revengeTargetUUID == null) return null;
        if (!(this.level() instanceof ServerLevel serverLevel)) return null;

        return serverLevel.getPlayerByUUID(this.revengeTargetUUID);
    }


    public int hasGem(){
        return this.entityData.get(GEM);
    }

    private void triggerRevengeVibration() {
        Level level = this.level();
        BlockPos pos = this.blockPosition();
        if (getRevengeTarget() != null) {

            level.gameEvent(
                    getRevengeTarget(),
                    GameEvent.ENTITY_ACTION,
                    pos
            );
        }else{
            level.gameEvent(
                    this,
                    GameEvent.ENTITY_ACTION,
                    pos
            );
        }
    }

    @Override
    public boolean isFood(ItemStack itemStack) {
        return false;
    }

    public static AttributeSupplier.Builder createAttributes(){
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 10d)
                .add(Attributes.MOVEMENT_SPEED, 0.2f)
                .add(Attributes.ARMOR, 0.4F)
                .add(Attributes.FOLLOW_RANGE, 24D);
    }

    @Override
    public boolean dampensVibrations() {
        return snappingTimer<=0;
    }

    @Override
    public void tick() {
        super.tick();

        if(this.level().isClientSide()){
            this.setupAnimationStates();
        }else{
            if(this.snappingTimer>0){
                this.entityData.set(SNAPPING, true);
                --this.snappingTimer;
            }else
            {
                this.entityData.set(SNAPPING, false);
            }
        }
    }

    private void setupAnimationStates() {
        idleAnimationState.startIfStopped(tickCount);
        if(this.entityData.get(SNAPPING)){
            snapAnimationState.startIfStopped(tickCount);
        }else{
            snapAnimationState.stop();
        }
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new RevengeShearGoal(this, 2.0f));
        this.goalSelector.addGoal(2, new ConsumeExperienceGoal(this, 1.25D));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Warden.class, 16.0F, 1.5D, 2.0D));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return null;
    }

    public static boolean CheckEchoCrabSpawnRules(EntityType<EchoCrabEntity> echoCrabEntityEntityType, ServerLevelAccessor serverLevelAccessor, MobSpawnType spawnType, BlockPos blockPos, RandomSource randomSource) {
        return serverLevelAccessor.getBlockState(blockPos.below()).isSolid();
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (stack.is(Items.SHEARS) && this.readyForShearing()) {
            this.revengeTargetUUID = player.getUUID();
            this.hurt = true;
        }

        return super.mobInteract(player, hand);
    }

    @Override
    public void shear(SoundSource soundSource) {
        this.level().playSound((Player)null, this, SoundEvents.SHEEP_SHEAR, soundSource, 1.0F, 1.0F);

        if(!(this.entityData.get(GEM)>=100))return;
        this.entityData.set(GEM, 0);
            ItemEntity itementity = this.spawnAtLocation(Items.ECHO_SHARD, 1);
            if (itementity != null) {
                itementity.setDeltaMovement(itementity.getDeltaMovement().add((double)((this.random.nextFloat() - this.random.nextFloat()) * 0.1F), (double)(this.random.nextFloat() * 0.05F), (double)((this.random.nextFloat() - this.random.nextFloat()) * 0.1F)));
            }
    }

    @Override
    public boolean readyForShearing() {
        return this.entityData.get(GEM)>=100;
    }

    private class RevengeShearGoal extends Goal{

        private EchoCrabEntity crab;
        private float speed;

        private BlockPos target;

        public RevengeShearGoal(EchoCrabEntity e, float speed){
            this.crab = e;
            this.speed = speed;
        }

        @Override
        public boolean canUse() {
            target = findNearestShrieker();
            return crab.hurt;
        }

        @Override
        public boolean canContinueToUse() {
            return target != null  && crab.hurt;
        }

        @Override
        public void stop() {
            target = null;
        }

        @Override
        public void start() {
            super.start();
            if(target != null){
                crab.getNavigation().moveTo(
                        target.getX(),
                        target.getY(),
                        target.getZ() + 0.5,
                        speed
                );
            }else{
                crab.hurt = false;
            }
        }

        @Override
        public void tick() {
            super.tick();
            if (target == null)
                return;

            double dist = crab.position().distanceTo(Vec3.atCenterOf(target));

            if (dist < 2.5) {
                crab.snappingTimer = 25;
                crab.triggerRevengeVibration();
                crab.level().playSound(null, crab.getX(), crab.getY(), crab.getZ(), SoundEvents.SHEEP_SHEAR, crab.getSoundSource(), 1.3F, 1.0F + (crab.random.nextFloat() - crab.random.nextFloat()) * 0.2F);
                crab.revengeTargetUUID = null;
                crab.hurt=false;
            }
        }

        private BlockPos findNearestShrieker() {
            BlockPos origin = crab.blockPosition();
            Level level = crab.level();
            int radius = 16;

            BlockPos best = null;
            double bestDist = Double.MAX_VALUE;

            for (BlockPos pos : BlockPos.betweenClosed(
                    origin.offset(-radius, -4, -radius),
                    origin.offset(radius, 8, radius))) {

                if (!level.getBlockState(pos).is(Blocks.SCULK_SENSOR))
                    continue;


                double dist = pos.distSqr(origin);

                if (dist < bestDist) {
                    bestDist = dist;
                    best = pos.immutable();
                }
            }

            return best;
        }
    }
    private class ConsumeExperienceGoal extends Goal {

        private final EchoCrabEntity crab;
        private final double speed;
        private ExperienceOrb targetOrb;

        public ConsumeExperienceGoal(EchoCrabEntity crab, double speed) {
            this.crab = crab;
            this.speed = speed;
        }

        @Override
        public boolean canUse() {
            if (crab.hasGem() >= 100) return false;
            if (crab.hurt) return false;

            targetOrb = findNearestOrb();
            return targetOrb != null;
        }

        @Override
        public boolean canContinueToUse() {
            return targetOrb != null
                    && targetOrb.isAlive()
                    && crab.hasGem() < 100
                    && !crab.hurt;
        }

        @Override
        public void start() {
            crab.getNavigation().moveTo(targetOrb, speed);
        }

        @Override
        public void stop() {
            targetOrb = null;
        }

        @Override
        public void tick() {
            if (targetOrb == null) return;

            crab.getNavigation().moveTo(targetOrb, speed);

            if (crab.distanceTo(targetOrb) < 1.3F) {
                consumeOrb(targetOrb);
                targetOrb = null;
            }
        }

        private void consumeOrb(ExperienceOrb orb) {
            int recharge = orb.getValue();
            int newGem = Math.min(100, crab.hasGem() + recharge);

            crab.entityData.set(GEM, newGem);

            crab.level().playSound(
                    null,
                    crab.blockPosition(),
                    SoundEvents.EXPERIENCE_ORB_PICKUP,
                    crab.getSoundSource(),
                    0.8F,
                    1.2F
            );

            orb.discard();
        }

        @Nullable
        private ExperienceOrb findNearestOrb() {
            return crab.level()
                    .getEntitiesOfClass(
                            ExperienceOrb.class,
                            crab.getBoundingBox().inflate(8.0D),
                            orb -> orb.isAlive()
                    )
                    .stream()
                    .min((a, b) -> Double.compare(
                            crab.distanceToSqr(a),
                            crab.distanceToSqr(b)
                    ))
                    .orElse(null);
        }
    }
}
