package net.blumasc.selectivepowers.block.custom;

import com.mojang.serialization.MapCodec;
import net.blumasc.selectivepowers.block.entity.ItemSentinelBlockEntity;
import net.blumasc.selectivepowers.block.entity.SelectivepowersBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class ItemSentinelBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;


    private static final VoxelShape SHAPE = Block.box(
            0.0,
            0.0,
            0.0,
            16.0,
            2.0,
            16.0
    );

    public ItemSentinelBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(POWERED, false));
    }

    public static final MapCodec<ItemSentinelBlock> CODEC =
            simpleCodec(ItemSentinelBlock::new);

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, POWERED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection());
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }
    private Direction getFront(BlockState state) {
        return state.getValue(FACING);
    }

    private Direction getBack(BlockState state) {
        return state.getValue(FACING).getOpposite();
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        if (direction.getOpposite() != getBack(state)) return 0;
        return state.getValue(POWERED) ? getAnalogOutputSignal(state, (Level) level, pos) : 0;
    }

    @Override
    public int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return getSignal(state, level, pos, direction);
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof ItemSentinelBlockEntity be) {
            return be.getSignalStrength();
        }
        return 0;
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ItemSentinelBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state,
                                                                  BlockEntityType<T> type) {
        return createTickerHelper(type, SelectivepowersBlockEntities.ITEM_SENTINEL_BE.get(),
                ItemSentinelBlockEntity::tick);
    }

    @Override
    protected ItemInteractionResult useItemOn(
            ItemStack stack,
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            InteractionHand hand,
            BlockHitResult hit) {

        if (!(level.getBlockEntity(pos) instanceof ItemSentinelBlockEntity be)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        if (level.isClientSide) {
            return ItemInteractionResult.SUCCESS;
        }

        ItemStack stored = be.getItem(0);

        if (stored.isEmpty()) {

            if (!stack.isEmpty()) {

                ItemStack insert = stack.copyWithCount(1);

                be.setItem(0, insert);

                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1f, 1f);

                return ItemInteractionResult.CONSUME;
            }
        }

        else {

            ItemStack extracted = stored.copy();

            be.setItem(0, ItemStack.EMPTY);

            if (!player.addItem(extracted)) {
                player.drop(extracted, false);
            }
            level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1f, 1f);

            return ItemInteractionResult.CONSUME;
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    protected boolean useShapeForLightOcclusion(BlockState state) {
        return true;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos,
                         BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock())) {
            if (level.getBlockEntity(pos) instanceof ItemSentinelBlockEntity be) {
                Containers.dropContents(level, pos, be);
            }
            level.updateNeighborsAt(pos, this);
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
}