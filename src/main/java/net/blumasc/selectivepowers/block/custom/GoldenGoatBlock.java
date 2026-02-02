package net.blumasc.selectivepowers.block.custom;

import com.mojang.serialization.MapCodec;
import net.blumasc.selectivepowers.item.SelectivepowersItems;
import net.blumasc.selectivepowers.managers.DraconicFlightManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class GoldenGoatBlock extends HorizontalDirectionalBlock {


    private static final VoxelShape SHAPE_1 = Block.box(2.5, 0, 0, 13.5, 15, 16);
    private static final VoxelShape SHAPE_2 = Block.box(0, 0, 2.5, 16, 15, 13.5);

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case NORTH -> SHAPE_1;
            case SOUTH -> SHAPE_1;
            case WEST -> SHAPE_2;
            case EAST -> SHAPE_2;
            default -> SHAPE_1;
        };
    }

    public static final MapCodec<GoldenGoatBlock> CODEC = simpleCodec(GoldenGoatBlock::new);


    public GoldenGoatBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }


    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (stack.is(Items.GOAT_HORN)) {

            if (!level.isClientSide) {
                ItemStack newStack = new ItemStack(
                        SelectivepowersItems.DRINKING_HORN.get(),
                        stack.getCount()
                );

                newStack.set(DataComponents.INSTRUMENT, stack.get(DataComponents.INSTRUMENT));

                player.setItemInHand(hand, newStack);
                player.playSound(SoundEvents.BUCKET_FILL, 1.0f, 1.0f);
            }

            return ItemInteractionResult.CONSUME;
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }
}
