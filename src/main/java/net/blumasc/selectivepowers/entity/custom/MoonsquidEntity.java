package net.blumasc.selectivepowers.entity.custom;

import net.blumasc.selectivepowers.effect.SelectivepowersEffects;
import net.blumasc.selectivepowers.item.SelectivepowersItems;
import net.blumasc.selectivepowers.sound.SelectivepowersSounds;
import net.blumasc.selectivepowers.util.ModTags;
import net.blumasc.selectivepowers.worldgen.ModDimensions;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static net.blumasc.selectivepowers.entity.helper.SquidSortHelper.insertionSortStep;
import static net.blumasc.selectivepowers.entity.helper.SquidSortHelper.isSorted;


public class MoonsquidEntity extends PathfinderMob {
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState swirlAnimationState = new AnimationState();
    public final AnimationState sitAnimationState = new AnimationState();
    private static final EntityDataAccessor<Integer> SWIRL_TIMER =
            SynchedEntityData.defineId(MoonsquidEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> SORTING =
            SynchedEntityData.defineId(MoonsquidEntity.class, EntityDataSerializers.BOOLEAN);
    private int feedTimer = 0;
    public MoonsquidEntity(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new FlyingMoveControl(this, 90, true);
        this.navigation = new FlyingPathNavigation(this, level);
    }

    public static AttributeSupplier.Builder createAttributes(){
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 8d)
                .add(Attributes.MOVEMENT_SPEED, 0.2f)
                .add(Attributes.FLYING_SPEED, 0.4F)
                .add(Attributes.FOLLOW_RANGE, 24D);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SWIRL_TIMER, 0);
        builder.define(SORTING, false);
    }

    @Override
    public void addAdditionalSaveData(net.minecraft.nbt.CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("FeedTimer", this.feedTimer);
    }

    @Override
    public void readAdditionalSaveData(net.minecraft.nbt.CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.feedTimer = compound.getInt("FeedTimer");
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return SelectivepowersSounds.MOON_SQUID.get();
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(DamageSource damageSource) {
        return SelectivepowersSounds.MOON_SQUID_HURT.get();
    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return SelectivepowersSounds.MOON_SQUID_DEATH.get();
    }

    @Override
    public boolean canDrownInFluidType(FluidType type) {
        return false;
    }

    public int getSwirlTimer() {
        return this.entityData.get(SWIRL_TIMER);
    }

    public void setSwirlTimer(int delivering) {
        this.entityData.set(SWIRL_TIMER, delivering);
    }

    public boolean getSorting() {
        return this.entityData.get(SORTING);
    }

    public void setSorting(boolean delivering) {
        this.entityData.set(SORTING, delivering);
    }


    public static boolean checkSquidSpawnRules(EntityType<MoonsquidEntity> moonsquidEntityEntityType, ServerLevelAccessor level, MobSpawnType mobSpawnType, BlockPos pos, RandomSource randomSource) {
        return true;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new LatchAndEscapeGoal(this));
        this.goalSelector.addGoal(2, new FeedHungryPlayerGoal(this));
        this.goalSelector.addGoal(2, new RandomSortChestGoal(this));
        this.goalSelector.addGoal(3, new SquidRandomMovementGoal (this));
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    public void animate()
    {
        if(this.getSorting())
        {
            idleAnimationState.stop();
            sitAnimationState.startIfStopped(this.tickCount);
        }else{
            sitAnimationState.stop();
            idleAnimationState.startIfStopped(tickCount);
        }
        if(getSwirlTimer()>0)
        {
            swirlAnimationState.startIfStopped(tickCount);

        }else
        {
            swirlAnimationState.stop();
        }

    }

    @Override
    public void tick() {
        if(this.level().isClientSide())
        {
            this.animate();
        }
        if(this.feedTimer>=0)
        {
            --this.feedTimer;
        }
        int st = this.getSwirlTimer();
        if(st>=0)
        {
            this.setSwirlTimer(st-1);
        }else if(this.random.nextInt(1000)<2)
        {
            this.setSwirlTimer(40);
        }
        super.tick();
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
        // no
    }

    public Vec3 getAnimationMotion() {
        return this.getDeltaMovement();
    }

    public class FeedHungryPlayerGoal extends Goal {
        private final MoonsquidEntity mob;
        private Player target;

        public FeedHungryPlayerGoal(MoonsquidEntity mob) {
            this.mob = mob;
        }

        @Override
        public boolean canUse() {
            if(this.mob.feedTimer>0) return false;
            if (!mob.level().dimension().equals(ModDimensions.LUNAR_DIM)) return false;

            List<Player> players = mob.level().getEntitiesOfClass(
                    Player.class,
                    mob.getBoundingBox().inflate(12),
                    p -> p.getFoodData().getFoodLevel() < 20
            );

            if (players.isEmpty()) return false;
            target = players.get(0);
            return true;
        }

        @Override
        public boolean canContinueToUse() {
            return !(mob.feedTimer>0);
        }

        @Override
        public void tick() {
            mob.getNavigation().moveTo(target, 1.2);

            if (mob.distanceTo(target) < 2.0) {
                this.mob.feedTimer = 800;
                this.mob.setSwirlTimer(40);
                this.mob.spawnAtLocation(SelectivepowersItems.MOON_TAKOYAKI.get());
            }
        }
    }
    class SquidRandomMovementGoal extends Goal {
        private final MoonsquidEntity squid;

        public SquidRandomMovementGoal(MoonsquidEntity squid) {
            this.squid = squid;
        }

        public boolean canUse() {
            return !squid.getSorting();
        }

        public void tick() {
            if (squid.getNoActionTime() > 100) return;

            if (squid.getRandom().nextInt(reducedTickDelay(50)) == 0) {
                float angle = squid.getRandom().nextFloat() * ((float)Math.PI * 2F);
                double dx = Mth.cos(angle) * 2.0;
                double dz = Mth.sin(angle) * 2.0;

                BlockPos posBelow = squid.blockPosition().below();
                int groundY = squid.level().getHeight(Heightmap.Types.MOTION_BLOCKING,posBelow.getX(),posBelow.getZ());

                double targetY = squid.getY() - 0.1 + squid.getRandom().nextDouble() * 2.0;
                targetY = Mth.clamp(targetY, groundY, groundY + 8);

                squid.getNavigation().moveTo(squid.getX() + dx, targetY, squid.getZ() + dz, 1.0f);
            }
        }
    }
    public class LatchAndEscapeGoal extends Goal {
        private final MoonsquidEntity mob;
        private Player target;

        public LatchAndEscapeGoal(MoonsquidEntity mob) {
            this.mob = mob;
        }

        @Override
        public boolean canUse() {
            if (mob.isPassenger()) return false;
            if (!mob.level().dimension().equals(ModDimensions.LUNAR_DIM)) return false;

            List<Player> players = mob.level().getEntitiesOfClass(
                    Player.class,
                    mob.getBoundingBox().inflate(16),
                    p -> {
                        MobEffectInstance effect =
                                p.getEffect(SelectivepowersEffects.MOON_BOUND_EFFECT);
                        return effect != null && effect.getDuration() < 200 && !isShoulderFull(p);
                    }
            );

            if (players.isEmpty()) return false;

            target = players.get(0);
            return true;
        }

        @Override
        public boolean canContinueToUse() {
            if (mob.isPassenger()) return false;
            if (target == null || !target.isAlive()) return false;
            if (isShoulderFull(target)) return false;

            MobEffectInstance effect =
                    target.getEffect(SelectivepowersEffects.MOON_BOUND_EFFECT);
            return effect != null && effect.getDuration() > 0;
        }

        public boolean isShoulderFull(Player player) {
            CompoundTag left = player.getShoulderEntityLeft();
            CompoundTag right = player.getShoulderEntityRight();
            return !left.isEmpty() && !right.isEmpty();
        }

        @Override
        public void start() {
            mob.getNavigation().moveTo(target, 1.3);
        }

        @Override
        public void tick() {
            if (target == null) return;

            mob.getLookControl().setLookAt(target, 30.0F, 30.0F);

            if (!mob.getNavigation().isInProgress()) {
                mob.getNavigation().moveTo(target, 1.3);
            }

            if (mob.distanceTo(target) < 1.4) {
                mob.getNavigation().stop();
                CompoundTag compoundtag = new CompoundTag();
                compoundtag.putString("id", mob.getEncodeId());
                mob.saveWithoutId(compoundtag);
                if (target.setEntityOnShoulder(compoundtag)) {
                    mob.discard();
                }
            }
        }

        @Override
        public void stop() {
            target = null;
            mob.getNavigation().stop();
        }
    }

    public class RandomSortChestGoal extends Goal {
        private final MoonsquidEntity mob;
        private BlockPos targetContainerPos;
        private int tickDelay = 0;

        public RandomSortChestGoal(MoonsquidEntity mob) {
            this.mob = mob;
        }

        @Override
        public boolean canUse() {
            if (random.nextDouble() > 0.2) return false;
            if (mob.level().dimension().equals(ModDimensions.LUNAR_DIM)) return false;

            targetContainerPos = findNearbyContainer(mob.level(), mob.blockPosition(), 15);
            if(targetContainerPos == null) return false;
            List<MoonsquidEntity> nearbySquids = level().getEntitiesOfClass(
                    MoonsquidEntity.class,
                    new AABB(targetContainerPos).inflate(1.0),
                    squid -> squid.getSorting()
            );
            return nearbySquids.isEmpty();
        }

        @Override
        public void start() {
            tickDelay = 0;
        }

        @Override
        public void stop() {
            mob.setSorting(false);
        }

        @Override
        public boolean canContinueToUse() {

            if (level().getBlockEntity(targetContainerPos)==null) return false;
            if(!(level().getBlockEntity(targetContainerPos) instanceof ChestBlockEntity cbe)) return false;
            List<MoonsquidEntity> nearbySquids = level().getEntitiesOfClass(
                    MoonsquidEntity.class,
                    new AABB(targetContainerPos).inflate(1.0), // 1 block radius around chest
                    squid -> squid.getSorting() && squid != mob // optional method to check if it's currently sorting
            );
            if(!nearbySquids.isEmpty())return false;
            return !isSorted(cbe);
        }

        @Override
        public void tick() {
            if (level().getBlockEntity(targetContainerPos)==null) return;
            if(!(level().getBlockEntity(targetContainerPos) instanceof ChestBlockEntity cbe)) return;

            Vec3 chestCenter = Vec3.atCenterOf(targetContainerPos);
            Vec3 squidPos = mob.position();

            Vec3 direction = chestCenter.subtract(squidPos).normalize();
            double distance = squidPos.distanceTo(chestCenter);

            if (distance > 1.0) {
                mob.getNavigation().moveTo(chestCenter.x, chestCenter.y, chestCenter.z, 1.3);
            } else {
                mob.setPos(chestCenter.x, chestCenter.y+0.5f, chestCenter.z);
                mob.setSorting(true);
                Vec3 lookVector = chestCenter.subtract(mob.position());
                float yaw = (float) (Math.atan2(lookVector.z, lookVector.x) * (180 / Math.PI)) - 90;
                float pitch = (float) -(Math.atan2(lookVector.y, Math.sqrt(lookVector.x * lookVector.x + lookVector.z * lookVector.z)) * (180 / Math.PI));

                mob.setYRot(yaw);
                mob.setXRot(pitch);

                tickDelay++;

                insertionSortStep(cbe, tickDelay/10);
            }

        }

        private BlockPos findNearbyContainer(Level level, BlockPos origin, int radius) {
            List<BlockPos> candidates = new ArrayList<>();
            for (int x = -radius; x <= radius; x++) {
                for (int y = -radius; y <= radius; y++) {
                    for (int z = -radius; z <= radius; z++) {
                        BlockPos pos = origin.offset(x, y, z);
                        BlockEntity be = level.getBlockEntity(pos);
                        if (be instanceof ChestBlockEntity container && !isSorted(container)) {
                            candidates.add(pos);
                        }
                    }
                }
            }

            if (candidates.isEmpty()) return null;

            return candidates.stream()
                    .min(Comparator.comparingDouble(pos -> pos.distSqr(origin)))
                    .orElse(null);
        }

    }


}
