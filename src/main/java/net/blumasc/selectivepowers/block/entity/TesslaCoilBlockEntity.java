package net.blumasc.selectivepowers.block.entity;

import net.blumasc.selectivepowers.PowerManager;
import net.blumasc.selectivepowers.entity.custom.projectile.LightningArcEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class TesslaCoilBlockEntity extends BlockEntity{

    private static final double RADIUS = 3.0;

    public TesslaCoilBlockEntity(BlockPos pos, BlockState state) {
        super(SelectivepowersBlockEntities.TESSLA_COIL_BE.get(), pos, state);
    }

    public void tick(Level level, BlockPos bPos, BlockState bState) {

        if(!(level instanceof ServerLevel sl)) return;

        if(!level.hasNeighborSignal(bPos)) return;

        if(!(level.random.nextInt(10)==4)) return;

        BlockPos center = this.worldPosition;

        List<Entity> monsters = level.getEntitiesOfClass(Entity.class, new AABB(
                center.getX() - RADIUS, center.getY() - RADIUS, center.getZ() - RADIUS,
                center.getX() + RADIUS, center.getY() + RADIUS, center.getZ() + RADIUS
        ));

        if(monsters.isEmpty())
        {
            double x = (level.random.nextDouble()*RADIUS*2)+center.getX()-RADIUS;
            double y = (level.random.nextDouble()*RADIUS*2)+center.getY()-RADIUS;
            double z = (level.random.nextDouble()*RADIUS*2)+center.getZ()-RADIUS;
            Vec3 targetPos = new Vec3(x,y,z);
            spawnVisualLightning(level, center.getCenter(), targetPos);
        }else
        {
            PowerManager pm = PowerManager.get(sl);
            for(Entity e : monsters)
            {
                spawnVisualLightning(level, center.getCenter(), e.getEyePosition());
                if(!pm.getPowerOfPlayer(e.getUUID()).equals(PowerManager.STORM_POWER)) {
                    e.hurt(level.damageSources().lightningBolt(), 3.0f);
                }
            }
        }



    }

    private static void spawnVisualLightning(Level level, Vec3 from, Vec3 to) {
        LightningArcEntity arc = new LightningArcEntity(
                level,
                from,
                to
        );

        level.addFreshEntity(arc);
    }
}