package net.blumasc.selectivepowers.block.custom;

import net.blumasc.selectivepowers.block.custom.helper.MobSensorConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;

public class MobSensorBlock extends Block {

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public MobSensorBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(POWERED, false));
    }

    private static final VoxelShape SHAPE = Block.box(1, 0, 1, 15, 1, 15);

    private static final VoxelShape SHAPE_PRESSED = Block.box(1, 0, 1, 15, 0.5, 15);

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(POWERED) ? SHAPE_PRESSED : SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.below()).isFaceSturdy(level, pos.below(), Direction.UP);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWERED);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (level.isClientSide) return;
        if (!(entity instanceof LivingEntity living)) return;

        boolean matches = checkBelow(level, pos, living);

        if (matches && !state.getValue(POWERED)) {
            level.setBlock(pos, state.setValue(POWERED, true), 3);
            level.playSound(null, pos, SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON, SoundSource.BLOCKS, 0.3f, 0.8f);
            updateNeighbors(level, pos);
        }

        if (state.getValue(POWERED)) {
            level.scheduleTick(pos, this, 2);
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!state.getValue(POWERED)) return;

        List<LivingEntity> onTop = level.getEntitiesOfClass(LivingEntity.class,
                new AABB(pos).inflate(0.1));

        boolean stillValid = onTop.stream().anyMatch(e -> checkBelow(level, pos, e));

        if (!stillValid) {
            level.setBlock(pos, state.setValue(POWERED, false), 3);
            level.playSound(null, pos, SoundEvents.STONE_PRESSURE_PLATE_CLICK_OFF, SoundSource.BLOCKS, 0.3f, 0.8f);
            updateNeighbors(level, pos);
        } else {
            level.scheduleTick(pos, this, 2);
        }
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if (!level.isClientSide) {
            level.scheduleTick(pos, this, 2);
        }
        super.stepOn(level, pos, state, entity);
    }

    private boolean checkBelow(Level level, BlockPos pos, LivingEntity entity) {
        EntityType<?> type = entity.getType();

        for (int i = 2; i <= 4; i++) {
            BlockPos checkPos = pos.below(i);
            BlockState below = level.getBlockState(checkPos);

            List<LivingEntity> mobs = level.getEntitiesOfClass(LivingEntity.class,
                    new AABB(checkPos).inflate(0.1),
                    e -> e.getType() == type && e != entity);
            if (!mobs.isEmpty()) return true;

            if (MobSensorConfig.blockMatchesEntity(below.getBlock(), type)) return true;

            if (!below.isAir() && !MobSensorConfig.hasAnyMapping(below.getBlock())) break;
        }

        return false;
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    public int getSignal(BlockState state, BlockGetter level, BlockPos pos,
                         Direction direction) {
        if (direction == Direction.UP) return 0;
        return state.getValue(POWERED) ? 15 : 0;
    }

    @Override
    public int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, 
                               Direction direction) {
        return getSignal(state, level, pos, direction);
    }

    private void updateNeighbors(Level level, BlockPos pos) {
        level.updateNeighborsAt(pos, this);
        for (Direction dir : Direction.values()) {
            level.updateNeighborsAt(pos.relative(dir), this);
        }
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, 
                         BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock()) && state.getValue(POWERED)) {
            updateNeighbors(level, pos);
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }
}