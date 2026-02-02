package net.blumasc.selectivepowers.block.entity;

import net.blumasc.selectivepowers.effect.SelectivepowersEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class SporeMushroomEntity extends BlockEntity {
    public SporeMushroomEntity(BlockPos pos, BlockState blockState) {
        super(SelectivepowersBlockEntities.SPORE_MUSHROOM_BE.get(), pos, blockState);
    }

    public void tick(Level level, BlockPos blockPos, BlockState blockState){
        if (level == null || level.isClientSide) return;

        AABB box = new AABB(worldPosition).inflate(8);
        for (LivingEntity e : level.getEntitiesOfClass(LivingEntity.class, box)) {
            e.addEffect(new MobEffectInstance(SelectivepowersEffects.HALLUCINATION, 300, 0, false, true));
        }
    }
}
