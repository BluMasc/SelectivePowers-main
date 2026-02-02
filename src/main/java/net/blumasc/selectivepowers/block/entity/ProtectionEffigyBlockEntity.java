package net.blumasc.selectivepowers.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.common.Tags;
import team.lodestar.lodestone.registry.common.particle.LodestoneParticleTypes;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.data.spin.SpinParticleData;

import java.awt.*;
import java.util.List;

public class ProtectionEffigyBlockEntity  extends BlockEntity{

    public float rotationAngle = 0f;

    public static final int PROTECTION_RADIUS = 20;
    private static final double PUSH_RADIUS = 15.0;
    private static final double PUSH_FORCE = 0.1;

    public ProtectionEffigyBlockEntity(BlockPos pos, BlockState state) {
        super(SelectivepowersBlockEntities.PROTECTION_EFFIGY_BE.get(), pos, state);
    }

    public void tick(Level level, BlockPos bPos, BlockState bState) {


        if(level == null || level.isClientSide) {
            rotationAngle += 0.5f;
            if(rotationAngle >= 360) rotationAngle -= 360;
            spawnVisualParticles(level, bPos);
            return;
        }

        BlockPos center = this.worldPosition;

        List<Monster> monsters = level.getEntitiesOfClass(Monster.class, new AABB(
                center.getX() - PUSH_RADIUS, center.getY() - PUSH_RADIUS, center.getZ() - PUSH_RADIUS,
                center.getX() + PUSH_RADIUS, center.getY() + PUSH_RADIUS, center.getZ() + PUSH_RADIUS
        ));

        for(Monster mob : monsters) {
            if (mob.getType().is(Tags.EntityTypes.BOSSES)) continue;
            double dx = mob.getX() - center.getX();
            double dz = mob.getZ() - center.getZ();
            double distance = Math.sqrt(dx*dx + dz*dz);

            if(distance < 0.01) continue;

            dx /= distance;
            dz /= distance;

            mob.setDeltaMovement(mob.getDeltaMovement().add(dx * PUSH_FORCE, 0, dz * PUSH_FORCE));
            mob.hurtMarked = true;
        }

    }

    private void spawnVisualParticles(Level level, BlockPos pos) {
        RandomSource random = level.getRandom();

        if (random.nextInt(3) != 0) return;

        double radius = 1.5;

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
                new Color(255, 255, 255),
                new Color(180, 200, 255),
                motionX, motionY, motionZ
        );
    }

    public static void spawnParticle(Level level, double x, double y, double z, Color startingColor, Color endingColor, double moveX, double moveY, double moveZ) {
        if (level instanceof ServerLevel) return;
        WorldParticleBuilder.create(LodestoneParticleTypes.STAR_PARTICLE)
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