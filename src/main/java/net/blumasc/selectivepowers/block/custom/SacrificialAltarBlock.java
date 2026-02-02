package net.blumasc.selectivepowers.block.custom;

import com.mojang.serialization.MapCodec;
import net.blumasc.selectivepowers.PowerManager;
import net.blumasc.selectivepowers.block.entity.AltarBlockEntity;
import net.blumasc.selectivepowers.block.entity.SacrificeAltarBlockEntity;
import net.blumasc.selectivepowers.block.entity.SelectivepowersBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class SacrificialAltarBlock extends BaseEntityBlock {

    public static final MapCodec<SacrificialAltarBlock> CODEC = simpleCodec(SacrificialAltarBlock::new);

    public SacrificialAltarBlock(Properties properties) {
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
        return new SacrificeAltarBlockEntity(blockPos,blockState);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return  Block.box((double)1.0F, (double)0.0F, (double)1.0F, (double)15.0F, (double)15.0F, (double)15.0F);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
                                              Player player, InteractionHand hand, BlockHitResult hitResult) {

        if (!(level.getBlockEntity(pos) instanceof SacrificeAltarBlockEntity altar)) {
            return ItemInteractionResult.FAIL;
        }

        if (level.isClientSide) {
            return ItemInteractionResult.SUCCESS;
        }
        if(stack.isEmpty() && !altar.working()) {
            if (player.isCrouching()) {
                if (player.experienceLevel >= 5 || player.hasInfiniteMaterials()) {
                    PowerManager pm = PowerManager.get((ServerLevel) level);
                    if (pm.doesPlayerHaveAnyPower(player.getUUID())) {
                        player.giveExperienceLevels(-5);
                        altar.simple=false;
                        altar.filled=true;
                        altar.power=pm.getPowerOfPlayer(player.getUUID());
                        altar.setChanged();
                        level.sendBlockUpdated(pos, state, state, 3);
                        level.playSound(null, pos, SoundEvents.BOTTLE_FILL_DRAGONBREATH, SoundSource.BLOCKS, 1f, 0.8f);
                        return ItemInteractionResult.SUCCESS;
                    }

                }
            }else if (player.experienceLevel >= 3 || player.hasInfiniteMaterials())
            {
                player.giveExperienceLevels(-3);
                altar.simple=true;
                altar.filled=true;
                altar.power=PowerManager.NO_POWER;
                altar.setChanged();
                level.sendBlockUpdated(pos, state, state, 3);
                level.playSound(null, pos, SoundEvents.BOTTLE_FILL_DRAGONBREATH, SoundSource.BLOCKS, 1f, 0.8f);
                return ItemInteractionResult.SUCCESS;
            }
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, SelectivepowersBlockEntities.SAC_ALTAR_BE.get(),
                (nlevel, blockPos, blockState, blockEntity) -> blockEntity.tick(nlevel, blockPos, blockState));
    }
}
