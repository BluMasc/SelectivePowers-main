package net.blumasc.selectivepowers.entity.custom.projectile;

import net.blumasc.selectivepowers.block.SelectivepowersBlocks;
import net.blumasc.selectivepowers.entity.SelectivepowersEntities;
import net.blumasc.selectivepowers.entity.custom.projectile.helper.MeteorSphere;
import net.blumasc.selectivepowers.sound.SelectivepowersSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MeteoriteEntity extends Entity {
    private static final double GRAVITY = 0.03;
    private static final int RADIUS = 7;

    public float rotationX;
    public float rotationY;
    public float rotationZ;

    private static final BlockPos[] CLEAR_OFFSETS = {
            new BlockPos(0, -1, 0),
            new BlockPos(1, -1, 0), new BlockPos(-1, -1, 0),
            new BlockPos(0, -1, 1), new BlockPos(0, -1, -1),
            new BlockPos(1, -1, 1), new BlockPos(-1, -1, -1),
            new BlockPos(-1, -1, 1), new BlockPos(1, -1, -1),
    };


    public MeteoriteEntity(Level level) {
        super(SelectivepowersEntities.METEOR.get(), level);
        initClientRotation();
    }

    public MeteoriteEntity(EntityType<MeteoriteEntity> type, Level level) {
        super(type, level);
        initClientRotation();
    }

    private void initClientRotation() {
        RandomSource rand = level().random;
        rotationX = rand.nextFloat() * 360f;
        rotationY = rand.nextFloat() * 360f;
        rotationZ = rand.nextFloat() * 360f;
    }

    private void tickRotation() {
        float speed = 0.5f;
        rotationX = (rotationX + speed) % 360f;
        rotationY = (rotationY + speed * 1.2f) % 360f;
        rotationZ = (rotationZ + speed * 0.8f) % 360f;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {}

    @Override
    public void tick() {
        super.tick();

        if (level().isClientSide) {
            if (level().random.nextInt(2) == 0) {
                spawnFallingParticles();
                spawnBottomFlames();
            }
            tickRotation();
            return;
        }

        this.setDeltaMovement(this.getDeltaMovement().add(0, -GRAVITY, 0));
        this.move(MoverType.SELF, this.getDeltaMovement());

        if (this.onGround()) {
            impact();
            discard();
            level().playSound(this, this.blockPosition(), SelectivepowersSounds.METEOR.get(), SoundSource.WEATHER, 5.0f, 1.0f);
        }
    }


    private void spawnFallingParticles() {
        Vec3 motion = getDeltaMovement();
        if (motion.y > -0.05) return;

        RandomSource rand = level().random;
        int count = Mth.clamp((int) (-motion.y * 6), 1, 5);

        for (int i = 0; i < count; i++) {
            double angle = rand.nextDouble() * Math.PI * 2;
            double radius = 3.0 + rand.nextDouble() * 2.0;

            double ox = Math.cos(angle) * radius;
            double oz = Math.sin(angle) * radius;
            double oy = rand.nextDouble() * 2.0 - 1.0;

            level().addParticle(
                    ParticleTypes.CAMPFIRE_COSY_SMOKE,
                    getX() + ox,
                    getY() + oy,
                    getZ() + oz,
                    0.0,
                    0.07 + rand.nextDouble() * 0.04,
                    0.0
            );
        }
    }

    private void spawnBottomFlames() {
        Vec3 motion = getDeltaMovement();
        if (motion.y > -0.1) return;

        RandomSource rand = level().random;
        int count = Mth.clamp((int) (-motion.y * 4), 1, 4);

        for (int i = 0; i < count; i++) {
            double angle = rand.nextDouble() * Math.PI * 2;
            double radius = 2.5 + rand.nextDouble() * 2.0;

            double ox = Math.cos(angle) * radius;
            double oz = Math.sin(angle) * radius;
            double oy = -3.0 + rand.nextDouble() * 0.5;

            level().addParticle(
                    ParticleTypes.FLAME,
                    getX() + ox,
                    getY() + oy,
                    getZ() + oz,
                    motion.x * -0.05,
                    -0.02,
                    motion.z * -0.05
            );
        }
    }

    private void impact() {
        BlockPos center = blockPosition();
        level().explode(
                this,
                center.getX() + 0.5,
                center.getY(),
                center.getZ() + 0.5,
                14.0F,
                true,
                Level.ExplosionInteraction.TNT
        );

        evacuateEntities(center.below(4));

        Map<BlockPos, BlockState> changes = new HashMap<>();
        for (BlockPos offset : MeteorSphere.positions(RADIUS)) {
            BlockPos pos = center.offset(offset).below(4);
            if (level().getBlockEntity(pos)!=null) continue;

            BlockState state = isShell(offset)
                    ? SelectivepowersBlocks.OBSIDIAN_DUST.get().defaultBlockState()
                    : MeteorSphere.getRandomOre(this.random);

            changes.put(pos, state);
        }

        changes.forEach((pos, state) -> level().setBlock(pos, state, 18));
        discard();
    }

    private void evacuateEntities(BlockPos center) {
        AABB meteorBox = new AABB(
                center.offset(-RADIUS, -RADIUS, -RADIUS).getCenter(),
                center.offset(RADIUS + 1, RADIUS + 1, RADIUS + 1).getCenter()
        );

        List<LivingEntity> entities = level().getEntitiesOfClass(LivingEntity.class, meteorBox);

        for (LivingEntity entity : entities) {
            Vec3 dir = entity.position().subtract(center.getX() + 0.5, center.getY() + 0.5, center.getZ() + 0.5);
            if (dir.lengthSqr() == 0) dir = new Vec3(0, 1, 0);

            dir = dir.normalize().scale(2.0);
            entity.move(MoverType.SELF, dir);
            entity.setDeltaMovement(entity.getDeltaMovement().add(dir.x * 0.2, dir.y * 0.2, dir.z * 0.2));
        }
    }

    private boolean isShell(BlockPos p) {
        int d = p.getX() * p.getX() + p.getY() * p.getY() + p.getZ() * p.getZ();
        return d > (RADIUS - 1) * (RADIUS - 1);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {}

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {}
}
