package net.blumasc.selectivepowers.item.custom;

import net.blumasc.selectivepowers.PowerManager;
import net.blumasc.selectivepowers.entity.custom.projectile.ShardProjectileEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Random;

public class FrostShieldItem extends ShieldItem {
    boolean powered = false;
    int useTimer = 0;
    public FrostShieldItem(Properties properties) {
        super(properties);
    }
    public FrostShieldItem(Properties properties, boolean powered) {
        super(properties);
        this.powered = powered;
    }

    private static Vec3 rotateYaw(Vec3 vec, float degrees) {
        double radians = Math.toRadians(degrees);
        double cos = Math.cos(radians);
        double sin = Math.sin(radians);

        double x = vec.x * cos - vec.z * sin;
        double z = vec.x * sin + vec.z * cos;

        return new Vec3(x, vec.y, z).normalize();
    }

    private Vec3 rotatePitch(Vec3 vec, float degrees) {
        double rad = Math.toRadians(degrees);
        double cos = Math.cos(rad);
        double sin = Math.sin(rad);
        return new Vec3(
                vec.x,
                vec.y * cos - vec.z * sin,
                vec.y * sin + vec.z * cos
        );
    }

    public void rebuke(Level level, Player player) {
        if (level.isClientSide) return;
        if (useTimer >0) return;

        if (player.getCooldowns().isOnCooldown(this)) return;
        player.getCooldowns().addCooldown(this, 10);

        Vec3 forward = player.getLookAngle().normalize();

        int shardCount = 4;
        if (powered) shardCount += 2;

        RandomSource random = level.random;

        for (int i = 0; i < shardCount; i++) {
            float yawOffset = (random.nextFloat() - 0.5f) * 120f;   // -75° to +75°
            float pitchOffset = (random.nextFloat() - 0.5f) * 120f; // -75° to +75°

            Vec3 dir = rotateYaw(forward, yawOffset);
            dir = rotatePitch(dir, pitchOffset);

            double offsetX = (random.nextDouble() - 0.5) * 0.6; // -0.3 to +0.3 blocks
            double offsetY = (random.nextDouble() - 0.5) * 0.4; // -0.2 to +0.2 blocks
            double offsetZ = (random.nextDouble() - 0.5) * 0.6; // -0.3 to +0.3 blocks

            ShardProjectileEntity projectile = new ShardProjectileEntity(
                    player,
                    level,
                    level.damageSources().freeze().typeHolder(),
                    0xbaf2ef
            );

            projectile.setPos(
                    player.getX() + offsetX,
                    player.getEyeY() - 0.2 + offsetY,
                    player.getZ() + offsetZ
            );

            projectile.shoot(
                    dir.x,
                    dir.y,
                    dir.z,
                    1.0F,
                    0.3F
            );

            level.addFreshEntity(projectile);
        }

        level.playSound(
                null,
                player.blockPosition(),
                SoundEvents.SNIFFER_EGG_CRACK,
                SoundSource.PLAYERS,
                1.0F,
                0.8F
        );
        useTimer = this.powered? 600:900;
    }
    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if(useTimer>0){
            useTimer--;
        }
        if(!this.powered) return;
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
