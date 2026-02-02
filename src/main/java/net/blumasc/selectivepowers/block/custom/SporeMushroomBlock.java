package net.blumasc.selectivepowers.block.custom;

import com.mojang.serialization.MapCodec;
import net.blumasc.selectivepowers.block.entity.SelectivepowersBlockEntities;
import net.blumasc.selectivepowers.block.entity.SporeMushroomEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class SporeMushroomBlock extends BaseEntityBlock {

    public static final MapCodec<SporeMushroomBlock> CODEC = simpleCodec(SporeMushroomBlock::new);

    public SporeMushroomBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return  Block.box((double)1.0F, (double)0.0F, (double)1.0F, (double)15.0F, (double)10.0F, (double)15.0F);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new SporeMushroomEntity(blockPos, blockState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, SelectivepowersBlockEntities.SPORE_MUSHROOM_BE.get(),
                (nlevel, blockPos, blockState, blockEntity) -> blockEntity.tick(nlevel, blockPos, blockState));
    }
}
