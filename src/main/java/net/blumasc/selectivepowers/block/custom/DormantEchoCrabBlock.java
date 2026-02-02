package net.blumasc.selectivepowers.block.custom;

import net.blumasc.selectivepowers.entity.SelectivepowersEntities;
import net.blumasc.selectivepowers.entity.custom.EchoCrabEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class DormantEchoCrabBlock extends Block {
    public DormantEchoCrabBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        super.randomTick(state, level, pos, random);
        spawnCrab(level, pos);
        level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
    }

    private void spawnCrab(ServerLevel level, BlockPos pos){
        EchoCrabEntity crab = SelectivepowersEntities.ECHO_CRAB.get().create(level);
        if(crab != null){
            crab.moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0f, 0f);
            level.addFreshEntity(crab);
        }
    }
    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return  Block.box((double)3.0F, (double)0.0F, (double)3.0F, (double)13.0F, (double)8.0F, (double)13.0F);
    }
}
