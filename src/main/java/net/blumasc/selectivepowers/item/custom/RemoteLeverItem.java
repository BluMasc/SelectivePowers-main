package net.blumasc.selectivepowers.item.custom;

import net.blumasc.selectivepowers.components.ModDataComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.List;

public class RemoteLeverItem extends Item {
    public RemoteLeverItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();
        BlockPos pos = context.getClickedPos();

        if (player == null || level.isClientSide) return InteractionResult.PASS;

        if (player.isShiftKeyDown() && level.getBlockState(pos).getBlock() instanceof LeverBlock) {

            stack.set(ModDataComponents.BOUND_LEVER.get(), pos);

            player.displayClientMessage(
                    Component.literal("Lever bound!"),
                    true
            );

            return InteractionResult.CONSUME;
        }

        return InteractionResult.PASS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (level.isClientSide) {
            return InteractionResultHolder.pass(stack);
        }
        BlockHitResult hit = Item.getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);

        if (hit.getType() == HitResult.Type.BLOCK) {
            if(level.getBlockState(hit.getBlockPos()).is(Blocks.LEVER)) {
                return InteractionResultHolder.pass(stack);
            }
        }

        BlockPos pos = stack.get(ModDataComponents.BOUND_LEVER.get());

        if (pos == null) {
            return InteractionResultHolder.pass(stack);
        }

        BlockState state = level.getBlockState(pos);

        if (!(state.getBlock() instanceof LeverBlock)) {
            return InteractionResultHolder.pass(stack);
        }

        level.setBlock(pos,
                state.setValue(LeverBlock.POWERED, !state.getValue(LeverBlock.POWERED)),
                Block.UPDATE_ALL
        );
        level.playSound(null, pos, SoundEvents.LEVER_CLICK, SoundSource.BLOCKS);

        stack.shrink(1);

        return InteractionResultHolder.success(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        BlockPos data = stack.get(ModDataComponents.BOUND_LEVER.get());

        if (data != null) {
            tooltip.add(Component.literal("Bound: " + data.getX() + ", " + data.getY() + ", " + data.getZ()));
        } else {
            tooltip.add(Component.literal("Not bound"));
        }
    }
}
