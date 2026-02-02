package net.blumasc.selectivepowers.item.custom;

import net.blumasc.selectivepowers.entity.custom.projectile.ThrownSolidVoidPearl;
import net.blumasc.selectivepowers.item.SelectivepowersItems;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class SolidVoidItem extends Item {
    public SolidVoidItem(Properties properties) {
        super(properties);
    }
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            ThrownSolidVoidPearl pearl = new ThrownSolidVoidPearl(level, player);
            pearl.setPos(player.getX(), player.getEyeY(), player.getZ());
            pearl.shootFromRotation(
                    player,
                    player.getXRot(),
                    player.getYRot(),
                    0.0F,
                    1.5F,
                    1.0F
            );
            level.addFreshEntity(pearl);

            stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));

            player.awardStat(Stats.ITEM_USED.get(this));
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }
}
