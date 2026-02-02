package net.blumasc.selectivepowers.effect.custom;

import net.blumasc.selectivepowers.worldgen.ModDimensions;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.Set;

public class MoonboundEffect extends MobEffect {
    public MoonboundEffect() {
        super(MobEffectCategory.NEUTRAL, 0xadd8e6);
    }
    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        teleportToCustomDimensionSurface(entity);
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    private void teleportToCustomDimensionSurface(LivingEntity entity) {
        if(!(entity.level() instanceof ServerLevel))return;
        ServerLevel targetLevel = entity.getServer().getLevel(ModDimensions.LUNAR_DIM_LEVEL);
        if (targetLevel == null || entity.level().dimension() == ModDimensions.LUNAR_DIM_LEVEL) return;

        targetLevel.getChunk(entity.getBlockX() >> 4, entity.getBlockZ() >> 4);
        BlockPos surfacePos = targetLevel.getHeightmapPos(Heightmap.Types.WORLD_SURFACE, entity.blockPosition());
        entity.teleportTo(targetLevel, surfacePos.getX()+0.5, surfacePos.getY(), surfacePos.getZ() + 0.5, Set.of(), entity.getYRot(), entity.getXRot());
    }
}
