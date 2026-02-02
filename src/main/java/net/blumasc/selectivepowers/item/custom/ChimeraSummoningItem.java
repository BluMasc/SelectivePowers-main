package net.blumasc.selectivepowers.item.custom;

import net.blumasc.selectivepowers.entity.SelectivepowersEntities;
import net.blumasc.selectivepowers.entity.custom.ChimeraEntity;
import net.blumasc.selectivepowers.entity.custom.SalamanderEntity;
import net.blumasc.selectivepowers.sound.SelectivepowersSounds;
import net.blumasc.selectivepowers.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import team.lodestar.lodestone.registry.common.particle.LodestoneParticleTypes;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.data.spin.SpinParticleData;

import java.awt.*;

public class ChimeraSummoningItem extends Item {
    public ChimeraSummoningItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand usedHand) {
        if (!(target.getType().is(ModTags.EntityTypes.CHIMERA_LIKE))) {
            return InteractionResult.PASS;
        }

        if (player.level().isClientSide()) {
            spawnVisualParticles(player.level(), target.getOnPos().above());
            return InteractionResult.SUCCESS;
        }

        ServerLevel level = (ServerLevel) player.level();

        ChimeraEntity chimera = SelectivepowersEntities.CHIMERA.get().create(level);
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
                    new Color(180, 80, 255),
                    new Color(255, 255, 255),
                    motionX, motionY, motionZ
            );
        }
    }

    public static void spawnParticle(Level level, double x, double y, double z, Color startingColor, Color endingColor, double moveX, double moveY, double moveZ) {
        if (level instanceof ServerLevel) return;
        WorldParticleBuilder.create(LodestoneParticleTypes.SMOKE_PARTICLE)
                .setScaleData(GenericParticleData.create(0.2f, 0).build())
                .setTransparencyData(GenericParticleData.create(0.75f, 0.25f).build())
                .setColorData(ColorParticleData.create(startingColor, endingColor).setCoefficient(1.4f).setEasing(Easing.BOUNCE_IN_OUT).build())
                .setSpinData(SpinParticleData.create(0.2f, 0.4f).setSpinOffset((level.getGameTime() * 0.2f) % 6.28f).setEasing(Easing.QUARTIC_IN).build())
                .setLifetime(40)
                .addMotion(moveX, moveY, moveZ)
                .enableNoClip()
                .spawn(level, x, y, z);
    }
}
