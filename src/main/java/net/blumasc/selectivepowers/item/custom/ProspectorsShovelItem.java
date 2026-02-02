package net.blumasc.selectivepowers.item.custom;

import net.blumasc.selectivepowers.block.SelectivepowersBlocks;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.block.state.BlockState;

public class ProspectorsShovelItem extends ShovelItem {
    public ProspectorsShovelItem(Properties p_43117_) {
        super(Tiers.NETHERITE, p_43117_);
    }
    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        if (state.is(SelectivepowersBlocks.OBSIDIAN_DUST.get())) {
            return 10_000.0F;
        }
        return super.getDestroySpeed(stack, state);
    }
}
