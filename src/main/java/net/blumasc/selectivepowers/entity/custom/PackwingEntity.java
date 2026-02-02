package net.blumasc.selectivepowers.entity.custom;

import net.blumasc.selectivepowers.datagen.ModItemTagProvider;
import net.blumasc.selectivepowers.entity.PackwingVariant;
import net.blumasc.selectivepowers.entity.SelectivepowersEntities;
import net.blumasc.selectivepowers.sound.SelectivepowersSounds;
import net.blumasc.selectivepowers.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.EventHooks;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;

public class PackwingEntity extends TamableAnimal implements FlyingAnimal {
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState flyAnimationState = new AnimationState();
    public final AnimationState grabAnimationState = new AnimationState();
    public final AnimationState dropAnimationState = new AnimationState();
    private static final EntityDataAccessor<Boolean> PERCHED =
            SynchedEntityData.defineId(PackwingEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DELIVERING =
            SynchedEntityData.defineId(PackwingEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> VARIANT =
            SynchedEntityData.defineId(PackwingEntity.class, EntityDataSerializers.INT);

    public PackwingEntity(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new FlyingMoveControl(this, 20, true);
        this.navigation = new FlyingPathNavigation(this, level);
    }

    private BlockPos deliveryHomePos;
    private ResourceKey<Level> deliveryHomeDimension;

    public void setDeliveryHome(BlockPos pos, ResourceKey<Level> dimension) {
        this.deliveryHomePos = pos;
        this.deliveryHomeDimension = dimension;
    }

    public BlockPos getDeliveryHomePos() {
        return this.deliveryHomePos;
    }

    public ResourceKey<Level> getDeliveryHomeDimension() {
        return this.deliveryHomeDimension;
    }

    @Override
    public boolean isPersistenceRequired() {
        return isTame();
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return !isTame();
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new DeliverPackageGoal(this, 1.0));
        this.goalSelector.addGoal(2,new PerchWhenOrderedToSitGoal(this,1.0));
        this.goalSelector.addGoal(3, new BreedGoal(this, 1.2));
        this.goalSelector.addGoal(4, new TemptGoal(this, 1.5,stack -> stack.is(ModTags.Items.PACKWING_TAMING_ITEM) || stack.is(ModTags.Items.PACKWING_BREEDING_ITEM),false));
        this.goalSelector.addGoal(5,new FollowOwnerGoal(this,1.2,6,1));
        this.goalSelector.addGoal(6, new PackwingWanderGoal(this, 1.0));
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
        if (!this.isTame() && itemstack.is(ModTags.Items.PACKWING_TAMING_ITEM)) {
            itemstack.consume(1, player);
            if (!this.isSilent()) {
                this.level().playSound((Player)null, this.getX(), this.getY(), this.getZ(), SoundEvents.PARROT_EAT, this.getSoundSource(), 1.0F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
            }

            if (!this.level().isClientSide) {
                if (this.random.nextInt(7) == 0 && !EventHooks.onAnimalTame(this, player)) {
                    this.tame(player);
                    this.level().broadcastEntityEvent(this, (byte)7);
                } else {
                    this.level().broadcastEntityEvent(this, (byte)6);
                }
            }

            return InteractionResult.sidedSuccess(this.level().isClientSide);
        } else if (this.isTame() && itemstack.is(ModTags.Items.PACKWING_BREEDING_ITEM)) {
            if (!this.isBaby()) {
                this.usePlayerItem(player, hand, itemstack);
                this.setInLove(player);
                return InteractionResult.sidedSuccess(this.level().isClientSide);
            }
            return InteractionResult.FAIL;
        }else if (this.isTame() && this.isOwnedBy(player) && this.isHoldingItem()) {
            this.spawnAtLocation(this.getMainHandItem());
            this.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
            return InteractionResult.sidedSuccess(level().isClientSide);
        }else if (this.isTame() && this.isOwnedBy(player) && !itemstack.isEmpty()){
            this.setItemInHand(InteractionHand.MAIN_HAND, itemstack.copyWithCount(1));
            itemstack.consume(1, player);
            this.setDeliveryHome(this.blockPosition(), this.level().dimension());
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
                .add(Attributes.MAX_HEALTH, 12d)
                .add(Attributes.MOVEMENT_SPEED, 0.2f)
                .add(Attributes.FLYING_SPEED, 0.4F)
                .add(Attributes.FOLLOW_RANGE, 24D);
    }

    @Override
    public boolean isFood(ItemStack itemStack) {
        return itemStack.is(ModTags.Items.PACKWING_BREEDING_ITEM);
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float damageMultiplier, DamageSource source) {
        return false;
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return SelectivepowersSounds.PACKWING.get();
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(DamageSource damageSource) {
        return SelectivepowersSounds.PACKWING_HURT.get();
    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return SelectivepowersSounds.PACKWING_DEATH.get();
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
        PackwingEntity m = SelectivepowersEntities.PACKWING.get().create(serverLevel);
        BlockPos pos = this.blockPosition();
        Holder<Biome> biomeHolder = this.level().getBiome(pos);

        PackwingVariant variant;

        if (biomeHolder.is(BiomeTags.IS_NETHER)) {
            variant = PackwingVariant.RED;

        } else if (biomeHolder.is(BiomeTags.IS_END)) {
            variant = PackwingVariant.BLUE;

        } else if (biomeHolder.value().coldEnoughToSnow(pos)) {
            variant = PackwingVariant.CYAN;

        } else if (pos.getY() <= 20) {
            variant = PackwingVariant.PINK;

        } else {
            variant = PackwingVariant.LIME;
        }

        m.setVariant(variant);
        return m;
    }

    private void setupAnimationStates() {

        boolean flying = !this.isPerched() && !this.onGround() && !this.isSitting();

        if(!getMainHandItem().isEmpty())
        {
            this.dropAnimationState.stop();
            this.grabAnimationState.startIfStopped(this.tickCount);
        }else{
            this.grabAnimationState.stop();
            this.dropAnimationState.startIfStopped(this.tickCount);
        }

        if (flying || !getMainHandItem().isEmpty()) {
            this.idleAnimationState.stop();
            this.flyAnimationState.startIfStopped(this.tickCount);
            return;
        }

        this.flyAnimationState.stop();

        this.idleAnimationState.startIfStopped(this.tickCount);
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
        builder.define(PERCHED, false);  // NEW
        builder.define(DELIVERING, false);
    }

    private int getTypeVariant() {
        return this.entityData.get(VARIANT);
    }

    public boolean isPerched() {
        return this.entityData.get(PERCHED);
    }

    public boolean isSitting() {
        return (this.getY()%1.0<0.05 && level().getBlockState(this.blockPosition().below()).isSolidRender(level(), this.blockPosition().below()));
    }

    public boolean isDelivering() {
        return this.entityData.get(DELIVERING);
    }

    public void setDelivering(boolean delivering) {
        this.entityData.set(DELIVERING, delivering);
    }

    public void setPerched(boolean perched) {
        this.entityData.set(PERCHED, perched);
    }

    public PackwingVariant getVariant(){
        return PackwingVariant.byId(this.getTypeVariant() & 255);
    }

    private void setVariant(PackwingVariant variant){
        this.entityData.set(VARIANT, variant.getId() & 255);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Variant", this.getTypeVariant());
        if (deliveryHomePos != null) {
            compound.putInt("HomeX", deliveryHomePos.getX());
            compound.putInt("HomeY", deliveryHomePos.getY());
            compound.putInt("HomeZ", deliveryHomePos.getZ());
            compound.putString("HomeDim", deliveryHomeDimension.location().toString());
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.entityData.set(VARIANT, compound.getInt("Variant"));
        if (compound.contains("HomeX")) {
            int x = compound.getInt("HomeX");
            int y = compound.getInt("HomeY");
            int z = compound.getInt("HomeZ");
            this.deliveryHomePos = new BlockPos(x, y, z);
            ResourceLocation dimRL = ResourceLocation.tryParse(compound.getString("HomeDim"));
            if (dimRL != null) {
                this.deliveryHomeDimension = ResourceKey.create(Registries.DIMENSION, dimRL);
            }
        }
    }

    private void teleportWithEffects(ServerLevel targetLevel, BlockPos pos) {
        this.level().playSound(null, this.blockPosition(), SoundEvents.ENDERMAN_TELEPORT,
                this.getSoundSource(), 1.0f, 1.0f);

        ((ServerLevel) this.level()).sendParticles(ParticleTypes.PORTAL,
                this.getX(), this.getY() + 0.5, this.getZ(),
                50, 0.5, 1.0, 0.5, 0.2);

        this.teleportTo(targetLevel,
                pos.getX() + 0.5,
                pos.getY(),
                pos.getZ() + 0.5,
                EnumSet.noneOf(RelativeMovement.class),
                this.getYRot(), this.getXRot());

        targetLevel.playSound(null, pos, SoundEvents.ENDERMAN_TELEPORT,
                this.getSoundSource(), 1.0f, 1.0f);

        targetLevel.sendParticles(ParticleTypes.PORTAL,
                pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                50, 0.5, 1.0, 0.5, 0.2);
    }

    private void teleportNearSafe(ServerLevel targetLevel, BlockPos destination, double maxDistance) {
        Vec3 current = this.position();
        Vec3 direction = destination.getCenter().subtract(current);
        double distance = direction.length();

        if (distance <= maxDistance) return;

        direction = direction.normalize();
        Vec3 candidatePos = destination.getCenter().subtract(direction.scale(maxDistance));

        BlockPos blockPos = new BlockPos((int) candidatePos.x, (int) candidatePos.y, (int) candidatePos.z);

        for (int yOffset = 0; yOffset <= 3; yOffset++) {
            BlockPos posAbove = blockPos.offset(0, yOffset, 0);
            BlockPos posBelow = blockPos.offset(0, -yOffset, 0);

            if (isSafePos(targetLevel, posAbove)) {
                blockPos = posAbove;
                break;
            } else if (isSafePos(targetLevel, posBelow)) {
                blockPos = posBelow;
                break;
            }
        }

        teleportWithEffects(targetLevel, blockPos);
    }

    private boolean isSafePos(Level level, BlockPos pos) {
        return level.getBlockState(pos).isAir() && level.getBlockState(pos.above()).isAir();
    }


    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        BlockPos pos = this.blockPosition();
        Holder<Biome> biomeHolder = level.getBiome(pos);

        PackwingVariant variant;

        if (biomeHolder.is(BiomeTags.IS_NETHER)) {
            variant = PackwingVariant.RED;

        } else if (biomeHolder.is(BiomeTags.IS_END)) {
            variant = PackwingVariant.BLUE;

        } else if (biomeHolder.value().coldEnoughToSnow(pos)) {
            variant = PackwingVariant.CYAN;

        } else if (pos.getY() <= 20) {
            variant = PackwingVariant.PINK;

        } else {
            variant = PackwingVariant.LIME;
        }

        this.setVariant(variant);
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    @Override
    public boolean isFlying() {
        return !this.onGround();
    }

    public static boolean checkPackwingSpawnRules(EntityType<PackwingEntity> packwingEntityEntityType, ServerLevelAccessor level, MobSpawnType mobSpawnType, BlockPos pos, RandomSource randomSource) {
        return level.getBlockState(pos.below()).is(ModTags.Blocks.PACKWING_SPAWNABLE);
    }

    public class PackwingWanderGoal extends WaterAvoidingRandomFlyingGoal{

        public PackwingWanderGoal(PathfinderMob p_25981_, double p_25982_) {
            super(p_25981_, p_25982_);
        }

        @Override
        public boolean canUse() {
            if(((PackwingEntity)mob).isOrderedToSit()) return false;
            return super.canUse();
        }
    }

    public class DeliverPackageGoal extends Goal {
        private final PackwingEntity packwing;
        private final double speed;
        private ResourceKey<Level> homeDimension;
        private BlockPos homePos;
        private ServerPlayer target;
        private int stuckTicks;

        public DeliverPackageGoal(PackwingEntity packwing, double speed) {
            this.packwing = packwing;
            this.speed = speed;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            if(!packwing.isTame()) return false;
            ItemStack item = packwing.getMainHandItem();
            if(item.isEmpty()) {
                return false;
            }
            String targetName = item.getHoverName().getString();
            if (packwing.level() instanceof ServerLevel serverLevel) {
                MinecraftServer server = serverLevel.getServer();
                if (server != null) {
                    this.target = server.getPlayerList().getPlayerByName(targetName);
                }
            }

            return this.target != null;
        }

        @Override
        public boolean canContinueToUse() {
            return packwing.isDelivering();
        }

        @Override
        public void start() {
            packwing.setDelivering(true);

            homePos = packwing.getDeliveryHomePos();
            homeDimension = packwing.getDeliveryHomeDimension();
            stuckTicks = 0;
        }

        @Override
        public void stop() {
            packwing.setDelivering(false);
            this.target = null;
            this.homePos = null;
        }

        @Override
        public void tick() {

            if(packwing.isHoldingItem()) {
                if (target == null) {
                    return;
                }

                boolean differentDim = !packwing.level().dimension().equals(target.level().dimension());

                if(differentDim){
                    BlockPos pos = target.blockPosition();
                    packwing.teleportNearSafe(target.serverLevel(), pos, 10);
                    return;
                }

                double dist = packwing.position().distanceTo(target.position());

                if (dist < 2.0) {
                    packwing.spawnAtLocation(packwing.getMainHandItem());
                    packwing.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
                } else if (dist < 40) {
                    packwing.getNavigation().moveTo(
                            target.position().x,
                            target.position().y,
                            target.position().z,
                            speed
                    );
                    if(packwing.getDeltaMovement().lengthSqr() < 0.01) {
                        stuckTicks++;
                    } else {
                        stuckTicks = 0;
                    }
                    if(stuckTicks>40)
                    {
                        packwing.teleportWithEffects(target.serverLevel(), target.blockPosition());
                    }
                } else {
                    BlockPos pos = target.blockPosition();
                    packwing.teleportNearSafe(target.serverLevel(), pos,10);
                }
            } else {
                ServerLevel homeLevel = packwing.getServer().getLevel(homeDimension);
                if (homeLevel == null) {
                    packwing.setDelivering(false);
                    packwing.setOrderedToSit(false);
                    return;
                }

                boolean differentDim = !packwing.level().dimension().equals(homeDimension);

                if(differentDim){
                    packwing.teleportNearSafe(homeLevel, homePos,10);
                    return;
                }

                double dist = packwing.position().distanceTo(homePos.getCenter());

                if (dist < 2.0) {
                    packwing.getNavigation().stop();
                    packwing.setDeltaMovement(Vec3.ZERO);
                    packwing.setDelivering(false);
                    packwing.deliveryHomePos = null;
                } else if (dist < 40) {
                    packwing.getNavigation().moveTo(
                            homePos.getX(),
                            homePos.getY(),
                            homePos.getZ(),
                            speed
                    );
                    if(packwing.getDeltaMovement().lengthSqr() < 0.01) {
                        stuckTicks++;
                    } else {
                        stuckTicks = 0;
                    }
                    if(stuckTicks>40)
                    {
                        packwing.teleportWithEffects(homeLevel, homePos);
                    }
                } else {
                    packwing.teleportNearSafe(homeLevel, homePos, 10);
                }
            }
        }
    }


    public class PerchWhenOrderedToSitGoal extends Goal {
        private final PackwingEntity packwing;
        private final double speed;
        private BlockPos perchPos;

        public PerchWhenOrderedToSitGoal(PackwingEntity packwing, double speed) {
            this.packwing = packwing;
            this.speed = speed;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            if (!packwing.isOrderedToSit())
                return false;

            if (perchPos != null)
                return true;

            perchPos = findNearestPerch();
            return perchPos != null;
        }

        @Override
        public boolean canContinueToUse() {
            return packwing.isOrderedToSit();
        }

        @Override
        public void start() {
            if (perchPos != null) {
                packwing.getNavigation().moveTo(
                        perchPos.getX() + 0.5,
                        perchPos.getY() + 0.2,
                        perchPos.getZ() + 0.5,
                        speed
                );
            }
        }

        @Override
        public void stop() {
            packwing.setPerched(false);
            perchPos = null;
        }

        @Override
        public void tick() {
            if (perchPos == null)
                return;

            double dist = packwing.position().distanceTo(Vec3.atCenterOf(perchPos));

            if (dist < 0.6) {
                packwing.getNavigation().stop();
                packwing.setDeltaMovement(Vec3.ZERO);

                packwing.setPerched(true);

                packwing.setOnGround(true);
            }

        }

        private BlockPos findNearestPerch() {
            BlockPos origin = packwing.blockPosition();
            Level level = packwing.level();
            int radius = 8;

            BlockPos best = null;
            double bestDist = Double.MAX_VALUE;

            for (BlockPos pos : BlockPos.betweenClosed(
                    origin.offset(-radius, -2, -radius),
                    origin.offset(radius, 4, radius))) {

                if (!level.getBlockState(pos).isSolid())
                    continue;

                // Must have open air above
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
