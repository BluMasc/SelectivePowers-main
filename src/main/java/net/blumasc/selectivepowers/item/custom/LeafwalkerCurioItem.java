package net.blumasc.selectivepowers.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class LeafwalkerCurioItem extends Item implements ICurioItem {
    public LeafwalkerCurioItem(Properties properties) {
        super(properties.durability(512));
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        LivingEntity e = slotContext.entity();
        if(e.level().isClientSide) return;
        if (!(e instanceof Player player)) return;
        if(player.hasInfiniteMaterials()) return;
        BlockPos pos = player.blockPosition();
        BlockState state1 = player.level().getBlockState(pos);
        BlockPos higher = pos.above();
        BlockState state2 = player.level().getBlockState(higher);
        if(!(state1.is(BlockTags.LEAVES) || state2.is(BlockTags.LEAVES))) return;
        if (player.tickCount % 20 != 0) return;

        ItemStack particleStack = stack.copy();
        stack.hurtAndBreak(1, (ServerLevel)player.level(), (ServerPlayer) player, p -> {
            ServerLevel level = (ServerLevel) player.level();
            level.playSound(
                    null,
                    player.blockPosition(),
                    SoundEvents.ITEM_BREAK,
                    SoundSource.PLAYERS,
                    1.0F,
                    1.0F
            );
            ItemParticleOption particle =
                    new ItemParticleOption(ParticleTypes.ITEM, particleStack);

            level.sendParticles(
                    particle,
                    player.getX(),
                    player.getY() + 1.0,
                    player.getZ(),
                    20,
                    0.25, 0.25, 0.25,
                    0.05
            );
        });


    }
}
