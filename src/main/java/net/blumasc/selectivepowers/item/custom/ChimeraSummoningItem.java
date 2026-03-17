package net.blumasc.selectivepowers.item.custom;

import net.blumasc.blubasics.entity.BaseModEntities;
import net.blumasc.blubasics.entity.custom.ChimeraEntity;
import net.blumasc.blubasics.util.BaseModTags;
import net.blumasc.selectivepowers.entity.SelectivepowersEntities;
import net.blumasc.selectivepowers.sound.SelectivepowersSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.awt.*;

public class ChimeraSummoningItem extends Item {
    public ChimeraSummoningItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand usedHand) {
        if (!(target.getType().is(BaseModTags.EntityTypes.CHIMERA_LIKE))) {
            return InteractionResult.PASS;
        }

        if (player.level().isClientSide()) {
            spawnVisualParticles(player.level(), target.getOnPos().above());
            return InteractionResult.SUCCESS;
        }

        ServerLevel level = (ServerLevel) player.level();

        ChimeraEntity chimera = BaseModEntities.CHIMERA.get().create(level);
        if (chimera == null)
            return InteractionResult.CONSUME;

        chimera.moveTo(target.getX(), target.getY(), target.getZ(),
                target.getYRot(), target.getXRot());

        if (target.hasCustomName()) {
            chimera.setCustomName(target.getCustomName());
            chimera.setCustomNameVisible(target.isCustomNameVisible());
        }

        chimera.setPersistenceRequired();

        chimera.setOwnerUUID(player.getUUID());
        chimera.setTame(true, true);

        level.addFreshEntity(chimera);

        target.discard();

        level.playSound(
                null,
                player.getX(), player.getY(), player.getZ(),
                SoundEvents.GLASS_BREAK,
                player.getSoundSource(),
                1.0F,
                1.0F
        );

        level.playSound(
                null,
                player.getX(), player.getY(), player.getZ(),
                SelectivepowersSounds.MAGICAL_SUCCESS.get(),
                player.getSoundSource(),
                1.0F,
                1.0F
        );

        level.playSound(
                null,
                chimera.getX(), chimera.getY(), chimera.getZ(),
                SoundEvents.FOX_SCREECH,
                chimera.getSoundSource(),
                1.0F,
                1.0F
        );

        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }

        return InteractionResult.CONSUME;
    }

    private void spawnVisualParticles(Level level, BlockPos pos) {
        RandomSource random = level.getRandom();

        double radius = 0.7;

        for(int i=0;i<25;i++) {

            double theta = random.nextDouble() * Math.PI * 2;
            double phi = Math.acos(2 * random.nextDouble() - 1);

            double xOffset = radius * Math.sin(phi) * Math.cos(theta);
            double yOffset = radius * Math.cos(phi);
            double zOffset = radius * Math.sin(phi) * Math.sin(theta);

            double x = pos.getX() + 0.5 + xOffset;
            double y = pos.getY() + 0.8 + yOffset;
            double z = pos.getZ() + 0.5 + zOffset;

            double motionX = xOffset * 0.02;
            double motionY = yOffset * 0.02;
            double motionZ = zOffset * 0.02;

            spawnParticle(
                    level,
                    x, y, z,
                    motionX, motionY, motionZ
            );
        }
    }

    public static void spawnParticle(Level level, double x, double y, double z, double moveX, double moveY, double moveZ) {
        if (level instanceof ServerLevel) return;
        level.addParticle(ParticleTypes.SMOKE, x, y, z, moveX, moveY, moveZ);
    }
}
