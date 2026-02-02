package net.blumasc.selectivepowers.block.custom;

import net.blumasc.selectivepowers.effect.SelectivepowersEffects;
import net.blumasc.selectivepowers.sound.SelectivepowersSounds;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.joml.Vector3f;

public class JumpingMushroom extends Block {

    public static final BooleanProperty SPAWNED = BooleanProperty.create("spawned");

    public JumpingMushroom(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(SPAWNED, false));
    }

    @Override
    public VoxelShape getCollisionShape(
            BlockState state,
            BlockGetter level,
            BlockPos pos,
            CollisionContext context
    ) {
        return Shapes.empty();
    }

    @Override
    public VoxelShape getOcclusionShape(
            BlockState state,
            BlockGetter level,
            BlockPos pos
    ) {
        return Shapes.empty();
    }

    @Override
    public boolean isOcclusionShapeFullBlock(BlockState state, BlockGetter level, BlockPos pos) {
        return false;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return  Block.box((double)0.0F, (double)0.0F, (double)0.0F, (double)16.0F, (double)12.0F, (double)16.0F);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (level.isClientSide) return;
        if (!(entity instanceof LivingEntity living)) return;

        boolean spawned = state.getValue(SPAWNED);

        double bounceHeight = spawned ? 15.0D : 5.0D;

        double velocityY = Math.sqrt(2 * 0.08D * bounceHeight);

        Vec3 motion = entity.getDeltaMovement();
        entity.setDeltaMovement(motion.x, velocityY, motion.z);
        entity.hurtMarked = true;

        level.playSound(
                null,
                pos,
                SelectivepowersSounds.BOUNCE.get(),
                SoundSource.BLOCKS,
                spawned ? 1.2F : 0.8F,
                spawned ? 0.9F : 1.2F
        );

        DustParticleOptions dust = new DustParticleOptions(
                new Vector3f(0.7F, 0.9F, 0.7F),
                spawned ? 1.4F : 1.0F
        );

        int count = spawned ? 45 : 20;

        ((ServerLevel) level).sendParticles(
                dust,
                pos.getX() + 0.5D,
                pos.getY() + 0.2D,
                pos.getZ() + 0.5D,
                count,
                0.6D,
                0.15D,
                0.6D,
                0.05D
        );

        if (spawned) {
            living.addEffect(new MobEffectInstance(
                    SelectivepowersEffects.FALL_IMMUNITY_EFFECT,
                    60,
                    0,
                    false,
                    false,
                    false
            ));

            goAway(state, (ServerLevel) level, pos);
        }
    }
    @Override
    public void attack(BlockState state, Level level, BlockPos pos, Player player) {
        if (level.isClientSide) return;

        goAway(state, (ServerLevel) level, pos);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean moved) {
        if (level.isClientSide) return;
        if (!state.getValue(SPAWNED)) return;

        RandomSource random = level.getRandom();
        int delay = 100 + random.nextInt(201); // 5–15 seconds

        level.scheduleTick(pos, this, delay);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        goAway(state, level, pos);
    }

    public void goAway(BlockState b, ServerLevel l, BlockPos pos){
        if (b.getValue(SPAWNED)) {
            l.removeBlock(pos, false);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(SPAWNED);
    }
}

