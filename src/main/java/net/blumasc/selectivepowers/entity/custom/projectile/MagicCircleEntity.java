package net.blumasc.selectivepowers.entity.custom.projectile;

import net.blumasc.selectivepowers.PowerManager;
import net.blumasc.selectivepowers.damage.SelectivePowersDamageTypes;
import net.blumasc.selectivepowers.entity.CircleVariant;
import net.blumasc.selectivepowers.entity.custom.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import static net.blumasc.selectivepowers.entity.helper.YellowFeverHelper.isYellowFeverImmune;


public class MagicCircleEntity extends Entity {

    public float rotation = 0;
    public int damage = 10;

    private static final EntityDataAccessor<Integer> BEAMING =
            SynchedEntityData.defineId(MagicCircleEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> LIFE_TICKS =
            SynchedEntityData.defineId(MagicCircleEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> VARIANT =
            SynchedEntityData.defineId(MagicCircleEntity.class, EntityDataSerializers.INT);

    public int getBeaming(){
        return this.entityData.get(BEAMING);
    }

    private void setBeaming(int beaming){
        this.entityData.set(BEAMING, beaming);
    }

    public int getLifeTicks(){
        return this.entityData.get(LIFE_TICKS);
    }

    private void setLifeTicks(int beaming){
        this.entityData.set(LIFE_TICKS, beaming);
    }

    private int getTypeVariant() {
        return this.entityData.get(VARIANT);
    }

    public CircleVariant getVariant(){
        return CircleVariant.byId(this.getTypeVariant() & 255);
    }

    private void setVariant(CircleVariant variant){
        this.entityData.set(VARIANT, variant.getId() & 255);
    }



    public MagicCircleEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
        this.setVariant(CircleVariant.MOON);
    }

    public MagicCircleEntity(EntityType<?> entityType, Level level, CircleVariant type, int upcharge, int beam, int damage) {
        super(entityType, level);
        this.setVariant(type);
        setLifeTicks(upcharge+beam);
        setBeaming(beam);
        this.damage = damage;
    }

    public boolean displayBeam()
    {
        return getLifeTicks() <= getBeaming();
    }

    @Override
    public void tick() {
        super.tick();

        if (level().isClientSide) {
            rotation -= 1.5f;
        }

        if (!level().isClientSide) {
            setLifeTicks(getLifeTicks()-1);
            if (displayBeam()) {
                triggerBeam();
            }if(getLifeTicks() <= 0)
            {
                discard();
            }
        }
    }

    private DamageSource getDamageSource()
    {
        if(getVariant() == CircleVariant.MOON) return SelectivePowersDamageTypes.lunarDamage(this);
        if(getVariant() == CircleVariant.SUN) return SelectivePowersDamageTypes.solarDamage(this);
        return level().damageSources().magic();
    }

    private void triggerBeam() {
        AABB box = new AABB(getX() - 2, getY(), getZ() - 2, getX() + 2, getY() + 10, getZ() + 2);
        level().getEntities(null, box).forEach(e -> {
            if(!isEntityImmuneToBeam(e))e.hurt(getDamageSource(), damage);
        });

        if(getVariant() == CircleVariant.CELESTIAL && !level().isClientSide) {
            BlockPos center = blockPosition();

            boolean destroyedBelow = false;
            BlockPos below = center.below();
            BlockState belowState = level().getBlockState(below);

            if (!belowState.isAir() && belowState.isFlammable(level(), below, Direction.DOWN)) {
                level().destroyBlock(below, false);
                destroyedBelow = true;
            }

            if (belowState.isAir()) {
                destroyedBelow = true;
            }

            for(int y =0;y<=2;y++) {

                for (int x = -1; x <= 1; x++) {
                    for (int z = -1; z <= 1; z++) {
                        BlockPos pos = center.offset(x, y-1, z);
                        BlockState state = level().getBlockState(pos);

                        if (state.isFlammable(level(), pos, Direction.DOWN)) {
                            level().destroyBlock(pos, false);
                        }

                        if (state.is(BlockTags.DIRT)) {
                            level().setBlockAndUpdate(pos, Blocks.COARSE_DIRT.defaultBlockState());
                        }
                    }
                }
            }
            if (destroyedBelow) {
                setPos(getX(), getY() - 1, getZ());
            }
        }

        ((ServerLevel) level()).sendParticles(ParticleTypes.END_ROD,
                getX(), getY() + 1, getZ(), 60, 0.2, 1.5, 0.2, 0);

        level().playSound(null, blockPosition(), SoundEvents.BEACON_ACTIVATE, SoundSource.HOSTILE, 1f, 1f);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(BEAMING, 30);
        builder.define(LIFE_TICKS, 60);
        builder.define(VARIANT, 0);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {

    }

    public boolean isEntityImmuneToBeam(Entity e)
    {
        if(e instanceof ItemEntity) return true;
        if(getVariant() == CircleVariant.SUN)
        {
            return isYellowFeverImmune(e);
        }
        if(getVariant() == CircleVariant.MOON)
        {
            if(e instanceof Player p)
            {
                if(p.isCreative())
                {
                    return true;
                }
                PowerManager pm = PowerManager.get((ServerLevel) e.level());
                return pm.getPowerOfPlayer(e.getUUID()).equals(PowerManager.MOON_POWER);
            }
            return e instanceof LunarMaidenEntity;
        }
        if(getVariant() == CircleVariant.CELESTIAL)
        {
            if(e instanceof Player p)
            {
                if(p.isCreative())
                {
                    return true;
                }
                PowerManager pm = PowerManager.get((ServerLevel) e.level());
                return pm.getPowerOfPlayer(e.getUUID()).equals(PowerManager.LIGHT_POWER);
            }
        }
        return false;
    }
}
