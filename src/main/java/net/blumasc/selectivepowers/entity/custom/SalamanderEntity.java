package net.blumasc.selectivepowers.entity.custom;

import net.blumasc.selectivepowers.entity.SelectivepowersEntities;
import net.blumasc.selectivepowers.item.SelectivepowersItems;
import net.blumasc.selectivepowers.sound.SelectivepowersSounds;
import net.blumasc.selectivepowers.util.ModTags;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.sql.ConnectionBuilder;
import java.util.List;
import java.util.Optional;

public class SalamanderEntity extends Animal {
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState infusionAnimationState = new AnimationState();
    public final AnimationState danceAnimation = new AnimationState();
    private static final EntityDataAccessor<Integer> COOKING_TIME =
            SynchedEntityData.defineId(SalamanderEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> COOK_TIME =
            SynchedEntityData.defineId(SalamanderEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DANCE_TIME =
            SynchedEntityData.defineId(SalamanderEntity.class, EntityDataSerializers.INT);
    private int scaleLayTime = this.random.nextInt(6000) + 6000;

    public SalamanderEntity(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 12d)
                .add(Attributes.MOVEMENT_SPEED, 0.1f)
                .add(Attributes.FOLLOW_RANGE, 12D);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(COOK_TIME, 200);
        builder.define(COOKING_TIME, 0);
        builder.define(DANCE_TIME, 0);
    }

    @Override
    public void addAdditionalSaveData(net.minecraft.nbt.CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("ScaleTimer", this.scaleLayTime);
    }

    @Override
    public void readAdditionalSaveData(net.minecraft.nbt.CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.scaleLayTime = compound.getInt("ScaleTimer");
    }

    @Override
    public void travel(Vec3 travelVector) {
        if (this.getDanceTime() > 0) {
            if (this.isInWater()) {
                super.travel(new Vec3(0.0, travelVector.y, 0.0));
            } else {
                super.travel(Vec3.ZERO);
            }
            this.setYRot(this.getYRot() + this.yRotO - this.getYRot());
            this.setXRot(this.getXRot() + this.xRotO - this.getXRot());
            this.yBodyRot = this.getYRot();
            this.yHeadRot = this.getYRot();
            return;
        }
        super.travel(travelVector);
    }

    public int getCookingTime() {
        return this.entityData.get(COOKING_TIME);
    }

    public void setCookingTime(int delivering) {
        this.entityData.set(COOKING_TIME, delivering);
    }

    public int getDanceTime() {
        return this.entityData.get(DANCE_TIME);
    }

    public void setDanceTime(int delivering) {
        this.entityData.set(DANCE_TIME, delivering);
    }

    public int getCookTime() {
        return this.entityData.get(COOK_TIME);
    }

    public void setCookTime(int delivering) {
        this.entityData.set(COOK_TIME, delivering);
    }

    public boolean isCooking() {
        return getCookingTime()>0;
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, (double)1.25F));
        this.goalSelector.addGoal(3, new BreedGoal(this, (double)1.0F));
        this.goalSelector.addGoal(4, new TemptGoal(this, 1.2, (p_336182_) -> p_336182_.is(ModTags.Items.SALAMANDER_FOOD), false));
        this.goalSelector.addGoal(5,new RunToDroppedSmeltableGoal(this, 1.5));
        this.goalSelector.addGoal(6, new FollowParentGoal(this, 1.1));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, (double)1.0F));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return SelectivepowersSounds.SALAMANDER.get();
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(DamageSource damageSource) {
        return SelectivepowersSounds.SALAMANDER_HURT.get();
    }



    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return SelectivepowersSounds.SALAMANDER_DEATH.get();
    }

    @Override
    public boolean isFood(ItemStack itemStack) {
        return itemStack.is(ModTags.Items.SALAMANDER_FOOD);
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return SelectivepowersEntities.SALAMANDER.get().create(serverLevel);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (itemstack.is(Items.GLASS_BOTTLE)) {
            itemstack.consume(1, player);
            boolean inserted = player.getInventory().add(new ItemStack(SelectivepowersItems.SALAMANDER_GOO.get()));
            if (!inserted) {
                ItemEntity drop = new ItemEntity(
                        player.level(),
                        player.getX(),
                        player.getY() + 0.5,
                        player.getZ(),
                        player.getMainHandItem().copy()
                );
                if (!this.isSilent()) {
                    this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.BOTTLE_FILL, this.getSoundSource(), 1.0F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
                }
                drop.setPickUpDelay(0);
                player.level().addFreshEntity(drop);
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public void tick() {
        super.tick();
        if(this.level().isClientSide())
        {
            this.setupAnimationState();
        }
        if(getDanceTime()>0)
        {
            setDanceTime(getDanceTime()-1);
        }
        if (!this.level().isClientSide && !this.isBaby()) {

            if (this.scaleLayTime > 0) {
                this.scaleLayTime--;
            } else {
                this.spawnAtLocation(SelectivepowersItems.SALAMANDER_SCALES.get());

                this.level().playSound(
                        null,
                        this.getX(), this.getY(), this.getZ(),
                        SoundEvents.BASALT_BREAK,
                        this.getSoundSource(),
                        1.0F,
                        (this.random.nextFloat() * 0.2F) + 0.9F
                );

                this.scaleLayTime = this.random.nextInt(6000) + 6000;
            }
        }
    }

    private void setupAnimationState() {
        if(getDanceTime()>0)
        {
            this.danceAnimation.startIfStopped(this.tickCount);
            return;
        }else{
            this.danceAnimation.stop();
        }
        if(getCookingTime()>0){
            this.idleAnimationState.stop();
            this.infusionAnimationState.startIfStopped(this.tickCount);
        }else {
            this.infusionAnimationState.stop();
            this.idleAnimationState.startIfStopped(this.tickCount);
        }
    }

    public class RunToDroppedSmeltableGoal extends Goal {

        private final SalamanderEntity lizard;
        private final double speed;
        private ItemEntity target;

        public RunToDroppedSmeltableGoal(SalamanderEntity lizard, double speed) {
            this.lizard = lizard;
            this.speed = speed;
        }

        @Override
        public boolean canUse() {
            if(lizard.isBaby()) return false;
            List<ItemEntity> items = lizard.level().getEntitiesOfClass(
                    ItemEntity.class,
                    lizard.getBoundingBox().inflate(16.0),
                    e -> getSmeltingResult(e.getItem()) != null
            );

            if (!items.isEmpty()) {
                this.target = items.get(0);
                return true;
            }
            return false;
        }

        @Override
        public boolean canContinueToUse() {
            return target != null && target.isAlive();
        }

        @Override
        public void stop() {
            lizard.setCookingTime(0);
            if (!lizard.isSilent()) {
                lizard.level().playSound(null, lizard.getX(), lizard.getY(), lizard.getZ(), SelectivepowersSounds.SALAMANDER_BURN_END.get(), lizard.getSoundSource(), 0.3F, 1.0F + (lizard.random.nextFloat() - lizard.random.nextFloat()) * 0.2F);
            }
            super.stop();
        }

        @Override
        public void start() {
            if (!lizard.isSilent()) {
                lizard.level().playSound(null, target.getX(), target.getY(), target.getZ(), SelectivepowersSounds.SALAMANDER_BURN_START.get(), lizard.getSoundSource(), 0.3F, 1.0F + (lizard.random.nextFloat() - lizard.random.nextFloat()) * 0.2F);
            }
            super.start();
        }

        @Override
        public void tick() {
            if(target != null) {
                lizard.getNavigation().moveTo(target, speed);

                if (lizard.distanceTo(target) < 2.0) {
                    ItemStack result = getSmeltingResult(target.getItem());
                    if (result != null) {
                        lizard.getNavigation().stop();
                        lizard.getLookControl().setLookAt(
                                target.getX(),
                                target.getY() + 0.1,
                                target.getZ(),
                                30.0F,
                                30.0F
                        );
                        ((ServerLevel) lizard.level()).sendParticles(
                                ParticleTypes.FLAME,
                                target.getX(), target.getY() + 0.25, target.getZ(),
                                5,
                                0.2, 0.2, 0.2,
                                0.01
                        );
                        if (lizard.getCookingTime()>5) {
                            if (!lizard.isSilent()) {
                                lizard.level().playSound(null, target.getX(), target.getY(), target.getZ(), SelectivepowersSounds.SALAMANDER_BURN.get(), lizard.getSoundSource(), 0.3F, 1.0F + (lizard.random.nextFloat() - lizard.random.nextFloat()) * 0.2F);
                            }
                        }
                        if (lizard.getCookingTime() < lizard.getCookTime()) {
                            lizard.setCookingTime(lizard.getCookingTime() + 1);
                        } else {
                            target.setItem(result.copyWithCount(target.getItem().getCount()));
                            target = null;
                        }
                    }
                }
            }
        }

        private @Nullable ItemStack getSmeltingResult(ItemStack input) {
            RecipeManager rm = lizard.level().getRecipeManager();
            Optional<RecipeHolder<SmeltingRecipe>> recipe =
                    rm.getRecipeFor(RecipeType.SMELTING, new SingleRecipeInput(input), lizard.level());
            if(recipe.isEmpty()) return null;
            lizard.setCookTime(recipe.get().value().getCookingTime());

            return recipe.get().value().getResultItem(lizard.level().registryAccess());
        }
    }

}
