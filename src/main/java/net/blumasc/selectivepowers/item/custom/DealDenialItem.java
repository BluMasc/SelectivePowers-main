package net.blumasc.selectivepowers.item.custom;

import net.blumasc.selectivepowers.PowerManager;
import net.blumasc.selectivepowers.particles.custom.WispParticleOption;
import net.blumasc.selectivepowers.sound.SelectivepowersSounds;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.awt.*;
import java.util.function.Supplier;

public class DealDenialItem extends Item {
    public DealDenialItem(Properties properties) {
        super(properties);
    }
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }

    public void onUseTick(Level level, LivingEntity entityLiving, ItemStack stack, int timeLeft) {
            if (entityLiving instanceof Player player) {
                int i = this.getUseDuration(stack, entityLiving) - timeLeft;
                if (i % 10 == 0) {
                    Vec3 pos = entityLiving.position();
                    if(i >= 40){
                        spawnSmokeParticles(level, pos.x, pos.y, pos.z, 10, new Color(100, 0, 100), new Color(0, 100, 200));
                    }
                    else {
                        spawnWispParticles(level, pos.x, pos.y, pos.z, 10, new Color(255, 255, 255), new Color(100, 100, 100));
                    }
                    level.playSound(player, player.getOnPos(), SelectivepowersSounds.WIND.get(), SoundSource.PLAYERS);
                }

        }
    }

    public void releaseUsing(ItemStack stack, Level level, LivingEntity entityLiving, int timeLeft) {
        if (!(entityLiving instanceof Player player)) return;
        int i = this.getUseDuration(stack, entityLiving) - timeLeft;
        if (i >= 40) {
        if (level.isClientSide()) {
            Vec3 pos = player.position();

            level.playSound(player, player.getOnPos(), SelectivepowersSounds.SPARKLE.get(), SoundSource.PLAYERS);

            spawnWispParticles(level, pos.x, pos.y, pos.z, 30,
                    new Color(100, 0, 100), new Color(0, 100, 200));

        }
        if (!(level instanceof ServerLevel serverLevel)) return;

        PowerManager manager = PowerManager.get(serverLevel);

            Vec3 pos = player.position();
            level.playSound(player, player.getOnPos(), SelectivepowersSounds.SPARKLE.get(), SoundSource.PLAYERS);

            spawnWispParticles(level, pos.x, pos.y, pos.z, 30,
                    new Color(100, 0, 100), new Color(0, 100, 200));

            stack.shrink(1);

            String power = manager.getPowerOfPlayer(player.getUUID());
            player.displayClientMessage(Component.translatable("selectivepowers.messages.nolongerbound").append(Component.translatable("selectivepowers.name."+power)).append(Component.translatable("selectivepowers.messages.nolongerbound.end")), true);

            manager.powerAssignments.remove(power);
            manager.removeProgress(player.getUUID());
            manager.setDirty();
            manager.syncToAll((ServerLevel) player.level());
        }
    }
    public static void spawnSmokeParticles(Level level, double x, double y, double z, int count, Color startingColor, Color endingColor) {
        if (level instanceof ServerLevel) return;
        RandomSource rand = level.getRandom();
        for (int j = 0; j < count; j++) {
            double ox = (rand.nextDouble() - 0.5) * 1.5;
            double oy = rand.nextDouble() * 2.0;
            double oz = (rand.nextDouble() - 0.5) * 1.5;
            level.addParticle(ParticleTypes.SMOKE, x, y, z, 0, 0.01f, 0);
        }
    }
    public static void spawnWispParticles(Level level, double x, double y, double z, int count, Color startingColor, Color endingColor) {
        if (level instanceof ServerLevel) return;
        RandomSource rand = level.getRandom();
        for (int j = 0; j < count; j++) {
            double ox = (rand.nextDouble() - 0.5) * 1.5;
            double oy = rand.nextDouble() * 2.0;
            double oz = (rand.nextDouble() - 0.5) * 1.5;
            spawnParticle(level, x+ox, y+oy, z+oz, startingColor, endingColor,0, 0.01f, 0);
        }
    }
    public static void spawnParticle(Level level, double x, double y, double z,
                                     Color startingColor, Color endingColor,
                                     double moveX, double moveY, double moveZ) {
        if (!(level instanceof ClientLevel)) return; // client-only

        WispParticleOption options = new WispParticleOption(
                startingColor.getRed()   / 255f, startingColor.getGreen() / 255f, startingColor.getBlue() / 255f,
                endingColor.getRed()     / 255f, endingColor.getGreen()   / 255f, endingColor.getBlue()   / 255f
        );

        level.addParticle(options, x, y, z, moveX, moveY, moveZ);
    }
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide()) {
            return InteractionResultHolder.pass(player.getItemInHand(hand));
        }

        if (!(level instanceof ServerLevel serverLevel)) {
            return InteractionResultHolder.pass(player.getItemInHand(hand));
        }

        PowerManager manager = PowerManager.get(serverLevel);
        ItemStack itemstack = player.getItemInHand(hand);

        PowerManager.PowerLevel powerLevel = manager.getPowerLevelOfPlayer(player.getUUID());

        if (powerLevel == PowerManager.PowerLevel.FREE) {
            player.displayClientMessage(Component.translatable("selectivepowers.messages.nopower"), true);
            return InteractionResultHolder.fail(itemstack);
        } else if (powerLevel != PowerManager.PowerLevel.BOUND) {
            player.displayClientMessage(Component.translatable("selectivepowers.messages.notjustbound"), true);
            return InteractionResultHolder.fail(itemstack);
        } else {
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(itemstack);
        }
    }
}
