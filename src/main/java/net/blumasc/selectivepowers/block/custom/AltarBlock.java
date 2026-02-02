package net.blumasc.selectivepowers.block.custom;

import com.mojang.serialization.MapCodec;
import net.blumasc.selectivepowers.block.entity.AltarBlockEntity;
import net.blumasc.selectivepowers.block.entity.SelectivepowersBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class AltarBlock extends BaseEntityBlock {

    public static final MapCodec<AltarBlock> CODEC = simpleCodec(AltarBlock::new);

    public AltarBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new AltarBlockEntity(blockPos,blockState);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if(state.getBlock() != newState.getBlock()){
            if(level.getBlockEntity(pos) instanceof AltarBlockEntity pedestalBlockEntity){
                pedestalBlockEntity.drops();
                level.updateNeighbourForOutputSignal(pos, this);
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
                                              Player player, InteractionHand hand, BlockHitResult hitResult) {

        if (!(level.getBlockEntity(pos) instanceof AltarBlockEntity altar)) {
            return ItemInteractionResult.FAIL;
        }

        if (level.isClientSide) {
            return ItemInteractionResult.SUCCESS;
        }
        if (player.isCrouching()) {
            boolean success = false;
            for (int i = 0; i < altar.inventory.getSlots(); i++) {
                ItemStack extracted = altar.inventory.extractItem(i, 1, false);
                if (!extracted.isEmpty()) {
                    success = true;
                    giveItemToPlayer(player, extracted);
                }
            }
            if(success) level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1f, 0.8f);
            return ItemInteractionResult.SUCCESS;
        }

        if (!stack.isEmpty() && altar.isCatalyst(stack)) {
            for (int i = 1; i <= 4; i++) {
                if (altar.inventory.getStackInSlot(i).isEmpty()) {
                    ItemStack toInsert = stack.copyWithCount(1);
                    altar.inventory.insertItem(i, toInsert, false);
                    stack.shrink(1);

                    level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1f, 1.5f);
                    return ItemInteractionResult.SUCCESS;
                }
            }
            return ItemInteractionResult.SUCCESS;
        }
        if (!stack.isEmpty()) {
            if (altar.inventory.getStackInSlot(0).isEmpty()) {
                altar.inventory.insertItem(0, stack.copyWithCount(1), false);
                stack.shrink(1);

                level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1f, 2f);
            }

            return ItemInteractionResult.SUCCESS;
        }
        if (stack.isEmpty()) {

            ItemStack extracted = altar.inventory.extractItem(0, 1, false);
            if (!extracted.isEmpty()) {
                giveItemToPlayer(player, extracted);
                level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1f, 1f);
                return ItemInteractionResult.SUCCESS;
            }

            for (int i = 1; i <= 4; i++) {
                extracted = altar.inventory.extractItem(i, 1, false);
                if (!extracted.isEmpty()) {
                    giveItemToPlayer(player, extracted);
                    level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1f, 1f);
                    return ItemInteractionResult.SUCCESS;
                }
            }
        }

        return ItemInteractionResult.SUCCESS;
    }

    private void giveItemToPlayer(Player player, ItemStack stack){
        boolean inserted = player.getInventory().add(stack);
        if (!inserted) {
            ItemEntity drop = new ItemEntity(
                    player.level(),
                    player.getX(),
                    player.getY() + 0.5,
                    player.getZ(),
                    stack.copy()
            );
            drop.setPickUpDelay(0);
            player.level().addFreshEntity(drop);
        }
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, SelectivepowersBlockEntities.ALTAR_BE.get(),
                (nlevel, blockPos, blockState, blockEntity) -> blockEntity.tick(nlevel, blockPos, blockState));
    }
}
