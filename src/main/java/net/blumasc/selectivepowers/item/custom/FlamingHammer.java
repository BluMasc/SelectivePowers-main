package net.blumasc.selectivepowers.item.custom;

import net.blumasc.selectivepowers.PowerManager;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MaceItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;

public class FlamingHammer extends MaceItem {
    int power = 1;
    public FlamingHammer(Properties properties) {
        super(properties);
    }

    public FlamingHammer(Properties properties, int power) {
        super(properties);
        this.power = power;
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {

        var level = attacker.level();
        if (!level.isClientSide) {
            BlockPos pos = target.blockPosition();
            if (level.getBlockState(pos).is(BlockTags.REPLACEABLE) || level.getBlockState(pos).is(Blocks.AIR)) {
                level.setBlock(pos, Blocks.FIRE.defaultBlockState(), 3);
            }
        }


        return super.hurtEnemy(stack, target, attacker);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if(!(level instanceof ServerLevel sl)) return;
        if(entity instanceof Player p && p.isCreative())return;
        PowerManager pm = PowerManager.get(sl);
        if((pm.getPowerOfPlayer(entity.getUUID()).equals(PowerManager.ELEMENTAL_POWER))) {
            PowerManager.PlayerProgress progress = pm.getProgress(entity.getUUID());
            if(progress.abilityTimer>0)
            {
                return;
            }
        }
        stack.setCount(0);

    }
}
