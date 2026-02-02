package net.blumasc.selectivepowers.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class UltraMagmaticBlock extends MagmaticBlock{
    public UltraMagmaticBlock(Properties properties) {
        super(properties);
    }
    @Override
    public void attack(BlockState state, Level world, BlockPos pos, Player player) {
        super.attack(state, world, pos, player);
        if(!player.isCreative()) {
            applyLavaSmashEffect(player, world,pos);
        }
    }
}
