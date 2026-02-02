package net.blumasc.selectivepowers.entity.custom;

import net.blumasc.selectivepowers.SelectivePowers;
import net.blumasc.selectivepowers.entity.CrowVariant;
import net.blumasc.selectivepowers.entity.SelectivepowersEntities;
import net.blumasc.selectivepowers.sound.SelectivepowersSounds;
import net.blumasc.selectivepowers.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.EventHooks;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;

public class CrowEntity extends TamableAnimal implements FlyingAnimal {

    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState flyAnimationState = new AnimationState();
    private static final EntityDataAccessor<Boolean> PERCHED =
            SynchedEntityData.defineId(CrowEntity.class, EntityDataSerializers.BOOLEAN);


    private static final EntityDataAccessor<Integer> VARIANT =
            SynchedEntityData.defineId(CrowEntity.class, EntityDataSerializers.INT);

    public CrowEntity(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new FlyingMoveControl(this, 20, true);
        this.navigation = new FlyingPathNavigation(this, level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1,new PerchWhenOrderedToSitGoal(this,1.0));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.2));
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.2, true));
        this.targetSelector.addGoal(0, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
        this.goalSelector.addGoal(4, new TemptGoal(this, 1.5,stack -> stack.has(DataComponents.FOOD) || stack.is(ModTags.Items.CROW_BREEDING_ITEM) || stack.is(ModTags.Items.CROW_TAMING_ITEM),false));
        this.goalSelector.addGoal(5, new PickupFoodGoal(this, 1.2));
        this.goalSelector.addGoal(6, new BringItemsToOwnerGoal(this));
        this.goalSelector.addGoal(7,new FollowOwnerGoal(this,1.2,6,1));
        this.goalSelector.addGoal(8, new RandomPerchGoal(this, 1.1));
        this.goalSelector.addGoal(9, new CrowWanderGoal(this, 1.0));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Player.class, 6.0F));
    }

    public boolean isHoldingItem() {
        return !this.getMainHandItem().isEmpty();
    }

    @Override
    public ItemStack getItemInHand(InteractionHand hand) {
        return hand == InteractionHand.MAIN_HAND ? super.getMainHandItem() : ItemStack.EMPTY;
    }

    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (!this.isTame() && itemstack.is(ModTags.Items.CROW_TAMING_ITEM)) {
            itemstack.consume(1, player);
            if (!this.isSilent()) {
                this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.PARROT_EAT, this.getSoundSource(), 1.0F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
            }

            if (!this.level().isClientSide) {
                if (this.random.nextInt(5) == 0 && !EventHooks.onAnimalTame(this, player)) {
                    this.tame(player);
                    this.level().broadcastEntityEvent(this, (byte)7);
                } else {
                    this.level().broadcastEntityEvent(this, (byte)6);
                }
            }

            return InteractionResult.sidedSuccess(this.level().isClientSide);
        } else if (this.isTame() && itemstack.is(ModTags.Items.CROW_BREEDING_ITEM)) {
            if (!this.isBaby()) {
                this.usePlayerItem(player, hand, itemstack);
                this.setInLove(player);
                return InteractionResult.sidedSuccess(this.level().isClientSide);
            }
            return InteractionResult.FAIL;
        }else if (itemstack.has(DataComponents.FOOD)){
            itemstack.consume(1, player);
            this.heal(2);
            if (!this.isSilent()) {
                this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.PARROT_EAT, this.getSoundSource(), 1.0F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }else{
            if (this.isTame() && this.isOwnedBy(player)) {
                if (!this.level().isClientSide) {
                    this.setOrderedToSit(!this.isOrderedToSit());
                }

                return InteractionResult.sidedSuccess(this.level().isClientSide);
            } else {
                return super.mobInteract(player, hand);
            }
        }
    }

    public static AttributeSupplier.Builder createAttributes(){
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 6d)
                .add(Attributes.ATTACK_DAMAGE, 2d)
                .add(Attributes.MOVEMENT_SPEED, 0.2f)
                .add(Attributes.FLYING_SPEED, 0.4F)
                .add(Attributes.FOLLOW_RANGE, 24D);
    }

    @Override
    public boolean isFood(ItemStack itemStack) {
        return itemStack.is(ModTags.Items.CROW_BREEDING_ITEM);
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float damageMultiplier, DamageSource source) {
        return false;
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return SelectivepowersSounds.CROW.get();
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(DamageSource damageSource) {
        return SelectivepowersSounds.CROW_HURT.get();
    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return SelectivepowersSounds.CROW_DEATH.get();
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
        // no
    }

    @Override
    public void travel(Vec3 vec3) {
        if (this.isFlying()) {
            this.moveRelative(0.1F, vec3);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9));
        } else {
            super.travel(vec3);
        }
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        CrowEntity m = SelectivepowersEntities.CROW.get().create(serverLevel);
        m.setVariant(this.getVariant());
        return m;
    }

    private void setupAnimationStates() {

        boolean flying = !this.isPerched() && !this.onGround() && !this.isSitting();

        if (flying) {
            this.idleAnimationState.stop();
            this.flyAnimationState.startIfStopped(this.tickCount);
            return;
        }

        this.flyAnimationState.stop();

        this.idleAnimationState.startIfStopped(this.tickCount);
    }

    public boolean isSitting() {
        return (this.getY()%1.0<0.05 && level().getBlockState(this.blockPosition().below()).isSolidRender(level(), this.blockPosition().below()));
    }

    @Override
    public void tick(){
        super.tick();

        if(this.level().isClientSide()){
            this.setupAnimationStates();
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(VARIANT, 0);
        builder.define(PERCHED, false);
    }

    private int getTypeVariant() {
        return this.entityData.get(VARIANT);
    }

    public boolean isPerched() {
        return this.entityData.get(PERCHED);
    }

    public void setPerched(boolean perched) {
        this.entityData.set(PERCHED, perched);
    }

    public CrowVariant getVariant(){
        return CrowVariant.byId(this.getTypeVariant() & 255);
    }

    private void setVariant(CrowVariant variant){
        this.entityData.set(VARIANT, variant.getId() & 255);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Variant", this.getTypeVariant());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.entityData.set(VARIANT, compound.getInt("Variant"));
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        CrowVariant variant = CrowVariant.GRAY;
        int rand = this.random.nextInt(14);
        if(rand<2)
        {
            variant = CrowVariant.WHITE;
        }else if(rand<7){
            variant = CrowVariant.BLUE;
        }
        this.setVariant(variant);
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    @Override
    public boolean isFlying() {
        return !this.onGround();
    }


    public class PickupFoodGoal extends Goal {
        private final CrowEntity crow;
        private final double speed;

        public PickupFoodGoal(CrowEntity crow, double speed) {
            this.crow = crow;
            this.speed = speed;
        }

        @Override
        public boolean canUse() {
            if (crow.isTame()) return false;

            return !crow.level().getEntitiesOfClass(
                    ItemEntity.class,
                    crow.getBoundingBox().inflate(8),
                    item -> item.getItem().has(DataComponents.FOOD)
            ).isEmpty();
        }

        @Override
        public void tick() {
            List<ItemEntity> items = crow.level().getEntitiesOfClass(
                    ItemEntity.class,
                    crow.getBoundingBox().inflate(8),
                    item -> item.getItem().has(DataComponents.FOOD)
            );

            if (!items.isEmpty()) {
                ItemEntity target = items.get(0);
                crow.getNavigation().moveTo(target, speed);

                if (crow.distanceTo(target) < 1.5) {
                    ItemStack stack = target.getItem();
                    crow.heal(2);
                    stack.shrink(1);
                    if (!crow.isSilent()) {
                        crow.level().playSound(null, crow.getX(), crow.getY(), crow.getZ(), SoundEvents.PARROT_EAT, crow.getSoundSource(), 1.0F, 1.0F + (crow.random.nextFloat() - crow.random.nextFloat()) * 0.2F);
                    }
                    if (stack.isEmpty()) target.discard();
                }
            }
        }
    }

    public class CrowWanderGoal extends WaterAvoidingRandomFlyingGoal{

        public CrowWanderGoal(PathfinderMob p_25981_, double p_25982_) {
            super(p_25981_, p_25982_);
        }

        @Override
        public boolean canUse() {
            if(((CrowEntity)mob).isOrderedToSit()) return false;
            return super.canUse();
        }
    }

    public class BringItemsToOwnerGoal extends Goal {
        private final CrowEntity crow;
        private LivingEntity owner;

        public BringItemsToOwnerGoal(CrowEntity crow) {
            this.crow = crow;
        }

        @Override
        public boolean canUse() {
            if (!crow.isTame()) return false;
            if(crow.isOrderedToSit()) return false;
            owner = crow.getOwner();
            return owner != null;
        }

        @Override
        public void tick() {

            if (!crow.getMainHandItem().isEmpty()) {
                if (crow.getMainHandItem().has(DataComponents.FOOD)) {
                    crow.heal(2);
                    crow.getMainHandItem().shrink(1);
                    if (!crow.isSilent()) {
                        crow.level().playSound(null, crow.getX(), crow.getY(), crow.getZ(), SoundEvents.PARROT_EAT, crow.getSoundSource(), 1.0F, 1.0F + (crow.random.nextFloat() - crow.random.nextFloat()) * 0.2F);
                    }
                }
                else {
                    crow.getNavigation().moveTo(owner, 1.2);
                    if (crow.distanceTo(owner) < 1.5) {
                        boolean inserted = ((Player)owner).getInventory().add(crow.getMainHandItem());
                        if (!inserted) {
                            ItemEntity drop = new ItemEntity(
                                    crow.level(),
                                    crow.getX(),
                                    crow.getY() + 0.5,
                                    crow.getZ(),
                                    crow.getMainHandItem().copy()
                            );
                            if (!crow.isSilent()) {
                                crow.level().playSound(null, crow.getX(), crow.getY(), crow.getZ(), SoundEvents.SNIFFER_DROP_SEED, crow.getSoundSource(), 1.0F, 1.0F + (crow.random.nextFloat() - crow.random.nextFloat()) * 0.2F);
                            }
                            drop.setPickUpDelay(0);
                            owner.level().addFreshEntity(drop);
                        }else{
                            if (!crow.isSilent()) {
                                crow.level().playSound(null, crow.getX(), crow.getY(), crow.getZ(), SoundEvents.ITEM_PICKUP, crow.getSoundSource(), 1.0F, 1.0F + (crow.random.nextFloat() - crow.random.nextFloat()) * 0.2F);
                            }
                        }

                        crow.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
                    }
                }
            }else {
                List<ItemEntity> items = crow.level().getEntitiesOfClass(
                        ItemEntity.class,
                        owner.getBoundingBox().inflate(6),
                        item -> item.distanceTo(owner)>2.0
                );

                if (items.isEmpty()) return;

                ItemEntity target = items.get(0);
                crow.getNavigation().moveTo(target, 1.1);

                if (crow.distanceTo(target) < 1.2) {
                    ItemStack stack = target.getItem();

                    if (!crow.getMainHandItem().isEmpty()) return;
                    crow.setItemInHand(InteractionHand.MAIN_HAND, stack.copy());
                    target.discard();

                }
            }
        }
    }

    public class PerchWhenOrderedToSitGoal extends Goal {
        private final CrowEntity crow;
        private final double speed;
        private BlockPos perchPos;

        public PerchWhenOrderedToSitGoal(CrowEntity crow, double speed) {
            this.crow = crow;
            this.speed = speed;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            if (!crow.isOrderedToSit())
                return false;

            if (perchPos != null)
                return true;

            perchPos = findNearestPerch();
            return perchPos != null;
        }

        @Override
        public boolean canContinueToUse() {
            return crow.isOrderedToSit();
        }

        @Override
        public void start() {
            if (perchPos != null) {
                crow.getNavigation().moveTo(
                        perchPos.getX() + 0.5,
                        perchPos.getY() + 0.2,
                        perchPos.getZ() + 0.5,
                        speed
                );
            }
        }

        @Override
        public void stop() {
            crow.setPerched(false);
            perchPos = null;
        }

        @Override
        public void tick() {
            if (perchPos == null)
                return;

            double dist = crow.position().distanceTo(Vec3.atCenterOf(perchPos));

            if (dist < 0.6) {
                crow.getNavigation().stop();
                crow.setDeltaMovement(Vec3.ZERO);

                crow.setPerched(true);

                crow.setOnGround(true);
            }

        }

        private BlockPos findNearestPerch() {
            BlockPos origin = crow.blockPosition();
            Level level = crow.level();
            int radius = 8;

            BlockPos best = null;
            double bestDist = Double.MAX_VALUE;

            for (BlockPos pos : BlockPos.betweenClosed(
                    origin.offset(-radius, -2, -radius),
                    origin.offset(radius, 4, radius))) {

                if (!level.getBlockState(pos).isSolid())
                    continue;

                if (!level.getBlockState(pos.above()).isAir())
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

    public class RandomPerchGoal extends Goal {
        private final CrowEntity crow;
        private final double speed;
        private BlockPos perchPos;
        private int perchTime;

        public RandomPerchGoal(CrowEntity crow, double speed) {
            this.crow = crow;
            this.speed = speed;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            if (crow.isOrderedToSit()) return false;
            if (crow.isPerched()) return false;
            if (crow.random.nextInt(2000) != 0) return false;

            perchPos = findPerch();
            return perchPos != null;
        }

        @Override
        public boolean canContinueToUse() {
            return perchPos != null && perchTime > 0;
        }

        @Override
        public void start() {
            crow.getNavigation().moveTo(
                    perchPos.getX() + 0.5,
                    perchPos.getY() + 0.2,
                    perchPos.getZ() + 0.5,
                    speed
            );

            perchTime = 60 + crow.random.nextInt(140);
        }

        @Override
        public void stop() {
            crow.setPerched(false);
            perchPos = null;
        }

        @Override
        public void tick() {
            if (perchPos == null) return;

            double dist = crow.position().distanceTo(Vec3.atCenterOf(perchPos));

            if (!crow.isPerched()) {
                if (dist < 0.6) {
                    crow.getNavigation().stop();
                    crow.setDeltaMovement(Vec3.ZERO);
                    crow.setPerched(true);
                    crow.setOnGround(true);
                }
            } else {
                perchTime--;
                if (perchTime <= 0) {
                    crow.setPerched(false);
                    perchPos = null;
                }
            }
        }

        private BlockPos findPerch() {
            BlockPos origin = crow.blockPosition();
            Level level = crow.level();
            int radius = 20;

            BlockPos best = null;
            double bestDist = Double.MAX_VALUE;

            for (BlockPos pos : BlockPos.betweenClosed(
                    origin.offset(-radius, -4, -radius),
                    origin.offset(radius, 6, radius))) {

                BlockState state = level.getBlockState(pos);

                if (!state.is(ModTags.Blocks.CROW_PERCHABLE))
                    continue;

                if (!level.getBlockState(pos.above()).isAir())
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




}
