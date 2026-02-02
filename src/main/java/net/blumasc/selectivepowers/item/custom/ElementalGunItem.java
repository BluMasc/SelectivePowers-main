package net.blumasc.selectivepowers.item.custom;

import net.blumasc.selectivepowers.sound.SelectivepowersSounds;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.entity.projectile.windcharge.WindCharge;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Random;

public class ElementalGunItem extends Item {
    private static final Random RANDOM = new Random();

    public ElementalGunItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide) {

            ItemStack stack = player.getItemInHand(hand);

            if (player.getCooldowns().isOnCooldown(this)) {
                return InteractionResultHolder.fail(stack);
            }

            if (hand == InteractionHand.MAIN_HAND) {
                shootFireball(level, player);
            } else if (hand == InteractionHand.OFF_HAND) {
                shootWindCharge(level, player);
            }

            stack.hurtAndBreak(1, player, player.getEquipmentSlotForItem(stack));
        }

        player.getCooldowns().addCooldown(this, 20);

        level.playSound(player, player.blockPosition().above(), SelectivepowersSounds.SHOOT.get(),player.getSoundSource(), 1.0F, 1.0F);

        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
    }

    private void shootFireball(Level level, Player player) {
        SmallFireball fireball = new SmallFireball(
                level,
                player,
                player.getLookAngle()
        );
        fireball.setPos(player.getX(), player.getEyeY(), player.getZ());
        level.addFreshEntity(fireball);
    }

    private void shootWindCharge(Level level, Player player) {
        WindCharge wind = new WindCharge(level, player.getX(), player.getEyeY(), player.getZ(), player.getLookAngle());
        wind.setOwner(player);
        wind.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
        level.addFreshEntity(wind);
    }
}

