package net.blumasc.selectivepowers.block.custom;

import net.blumasc.selectivepowers.sound.SelectivepowersSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MagmaticBlock extends Block {
    public MagmaticBlock(Properties properties) {
        super(properties);
    }

    private static final VoxelShape COLLISION =
            Block.box(1, 1, 1, 15, 15, 15);

    @Override
    public VoxelShape getCollisionShape(
            BlockState state,
            BlockGetter level,
            BlockPos pos,
            CollisionContext context
    ) {
        return COLLISION;
    }

    @Override
    public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
        applyLavaSmashEffect(entity, world, pos);
    }

    protected void applyLavaSmashEffect(Entity entity, Level level, BlockPos pos) {
        if (entity instanceof LivingEntity living) {
            living.hurt(level.damageSources().inFire(), 4.0F);
            Vec3 direction = living.position().subtract(Vec3.atCenterOf(pos)).normalize();
            if (direction.length() == 0) direction = new Vec3(0, 1, 0);
            living.setDeltaMovement(direction.scale(2.5));
        }
        if (level instanceof ServerLevel serverLevel) {
            Vec3 center = Vec3.atCenterOf(pos);
            serverLevel.sendParticles(
                    ParticleTypes.LAVA,
                    center.x, center.y, center.z,
                    8,
                    0.3, 0.3, 0.3,
                    0.1
            );
            serverLevel.sendParticles(
                    ParticleTypes.FLAME,
                    center.x, center.y, center.z,
                    15,
                    0.4, 0.4, 0.4,
                    0.05
            );
            serverLevel.sendParticles(
                    ParticleTypes.SMOKE,
                    center.x, center.y, center.z,
                    6,
                    0.3, 0.3, 0.3,
                    0.02
            );
            serverLevel.playSound(null, pos, SelectivepowersSounds.BLAST.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
        }
    }
}
