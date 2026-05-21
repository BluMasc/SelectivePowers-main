package net.blumasc.selectivepowers.entity.custom.projectile;

import net.blumasc.selectivepowers.PowerManager;
import net.blumasc.selectivepowers.damage.SelectivePowersDamageTypes;
import net.blumasc.selectivepowers.entity.CircleVariant;
import net.blumasc.selectivepowers.entity.custom.LunarMaidenEntity;
import net.blumasc.selectivepowers.sound.SelectivepowersSounds;
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
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;

import static net.blumasc.selectivepowers.entity.helper.YellowFeverHelper.isYellowFeverImmune;


public class WhirlpoolEntity extends Entity {

    public float rotation = 0;

    private static final EntityDataAccessor<Integer> LIFE_TICKS =
            SynchedEntityData.defineId(WhirlpoolEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> SIZE =
            SynchedEntityData.defineId(WhirlpoolEntity.class, EntityDataSerializers.INT);

    public int getSize(){
        return this.entityData.get(SIZE);
    }

    public void setSize(int beaming){
        this.entityData.set(SIZE, beaming);
    }

    public int getLifeTicks(){
        return this.entityData.get(LIFE_TICKS);
    }

    public void setLifeTicks(int beaming){
        this.entityData.set(LIFE_TICKS, beaming);
    }



    public WhirlpoolEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public WhirlpoolEntity(EntityType<?> entityType, Level level, int uptime) {
        super(entityType, level);
        setLifeTicks(uptime);
    }

    @Override
    public void tick() {
        super.tick();

        if (level().isClientSide) {
            rotation -= 4.7f;
        }

        double radius = getSize()/2f;
        AABB searchBox = new AABB(
                this.getX() - radius, this.getY() - radius, this.getZ() - radius,
                this.getX() + radius, this.getY() + radius, this.getZ() + radius
        );

        List<Entity> entities = level().getEntities(this, searchBox, e ->
                e instanceof LivingEntity && !e.isSpectator()
        );

        if (!level().isClientSide) {
            setLifeTicks(getLifeTicks()-1);

            if(getLifeTicks() <= 0)
            {
                discard();
            }
            if(this.tickCount%19==0){
                if(level() instanceof ServerLevel sl){
                    sl.playSound(null, this.getX(), this.getY(), this.getZ(), SelectivepowersSounds.WHIRLPOOL.get(), SoundSource.AMBIENT);
                }
            }
        }

        int splash_particles = 0;

        for (Entity entity : entities) {
            double dx = this.getX() - entity.getX();
            double dy = this.getY() - entity.getY();
            double dz = this.getZ() - entity.getZ();

            double distance = Math.sqrt(dx * dx + dz * dz+ dy*dy);
            if (distance > radius) continue;

            if(level() instanceof ServerLevel sl){
                entity.setRemainingFireTicks(0);
                if(entity instanceof LivingEntity le && le.isSensitiveToWater()){
                    entity.hurt(entity.damageSources().drown(), 1.0F);
                }
                if (splash_particles < 10*radius) {
                    int count = random.nextInt(5) - 2;
                    if (count > 0) {
                        sl.sendParticles(ParticleTypes.SPLASH, entity.getX(), this.getY(), entity.getZ(), count, this.getY() - entity.getY(), (double) 1, (double) 1, 0.2);
                        splash_particles += count;
                    }
                }
            }

            if(distance < 0.5) continue;

            double force = 0.02 + 0.085 * (distance / radius);
            double nx = dx / distance;
            double ny = dy / distance;
            double nz = dz / distance;

            entity.setDeltaMovement(
                    entity.getDeltaMovement().add(nx * force, ny*force, nz * force)
            );
        }
        if(level() instanceof ServerLevel sl && splash_particles<10*radius){
            sl.sendParticles(ParticleTypes.SPLASH, this.getX(), this.getY(), this.getZ(), (10*(int)radius)-splash_particles, radius/2, 0.1, radius/2, 0.2);
        }
        if (level() instanceof ServerLevel sl) {

            int radiusInt = (int)Math.ceil(radius);

            BlockPos.betweenClosedStream(
                    this.blockPosition().offset(-radiusInt, -radiusInt, -radiusInt),
                    this.blockPosition().offset(radiusInt, radiusInt, radiusInt)
            ).forEach(pos -> {

                if (pos.distSqr(this.blockPosition()) > radius * radius) {
                    return;
                }

                BlockState state = level().getBlockState(pos);
                if (state.is(BlockTags.FIRE)) {
                    level().removeBlock(pos, false);
                    level().playSound(
                            null,
                            pos,
                            SoundEvents.FIRE_EXTINGUISH,
                            SoundSource.BLOCKS,
                            0.5F,
                            1.0F
                    );
                }
                if (state.is(Blocks.LAVA)) {

                    int lavaLevel = state.getValue(net.minecraft.world.level.block.LiquidBlock.LEVEL);
                    if (lavaLevel == 0) {
                        level().setBlockAndUpdate(pos, Blocks.OBSIDIAN.defaultBlockState());
                    }
                    else {
                        level().setBlockAndUpdate(pos, Blocks.STONE.defaultBlockState());
                    }

                    level().playSound(
                            null,
                            pos,
                            SoundEvents.LAVA_EXTINGUISH,
                            SoundSource.BLOCKS,
                            0.5F,
                            1.0F
                    );

                    sl.sendParticles(
                            ParticleTypes.LARGE_SMOKE,
                            pos.getX() + 0.5,
                            pos.getY() + 0.5,
                            pos.getZ() + 0.5,
                            8,
                            0.2,
                            0.2,
                            0.2,
                            0.01
                    );
                }
            });
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(SIZE, 5);
        builder.define(LIFE_TICKS, 100);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {

    }
}
