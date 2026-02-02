package net.blumasc.selectivepowers.entity.custom.projectile;

import net.blumasc.selectivepowers.entity.SelectivepowersEntities;
import net.blumasc.selectivepowers.item.SelectivepowersItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.event.entity.EntityTeleportEvent;

public class ThrownSolidVoidPearl extends ThrownEnderpearl {
    public ThrownSolidVoidPearl(EntityType<? extends ThrownSolidVoidPearl> entityType, Level level) {
        super(entityType, level);
    }

    public ThrownSolidVoidPearl(Level level, LivingEntity shooter) {
        super(SelectivepowersEntities.SOLID_VOID_PEARL.get(), level);
        this.setOwner(shooter);
    }

    @Override
    protected Item getDefaultItem() {
        return SelectivepowersItems.VOID_PEARL.asItem();
    }

    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        result.getEntity().hurt(this.damageSources().thrown(this, this.getOwner()), 0.0F);
    }

    @Override
    protected void onHit(HitResult result) {
        if (!(this.level() instanceof ServerLevel serverLevel)) {
            this.discard();
            return;
        }

        Entity owner = this.getOwner();
        if (!(owner instanceof ServerPlayer player)) {
            this.discard();
            return;
        }

        if (!player.connection.isAcceptingMessages()) {
            this.discard();
            return;
        }

        Vec3 hitPos = result.getLocation();
        BlockPos blockPos = BlockPos.containing(hitPos);

        int blockLight = serverLevel.getBrightness(LightLayer.BLOCK, blockPos);
        int skyLight   = serverLevel.getBrightness(LightLayer.SKY, blockPos);

        if (blockLight >= 4 || skyLight >= 7) {
            this.discard();
            return;
        }

        EntityTeleportEvent.EnderPearl event =
                EventHooks.onEnderPearlLand(
                        player,
                        hitPos.x,
                        hitPos.y,
                        hitPos.z,
                        this,
                        5.0F,
                        result
                );

        if (event.isCanceled()) {
            this.discard();
            return;
        }
        player.teleportTo(
                serverLevel,
                hitPos.x,
                hitPos.y,
                hitPos.z,
                player.getYRot(),
                player.getXRot()
        );

        player.resetFallDistance();
        player.resetCurrentImpulseContext();
        player.hurt(this.damageSources().fall(), event.getAttackDamage());

        this.discard();
    }



    private static boolean isAllowedToTeleportOwner(Entity entity, Level level) {
        if (entity.level().dimension() != level.dimension()) {
            return entity.canUsePortal(true);
        } else {
            boolean var10000;
            if (entity instanceof LivingEntity) {
                LivingEntity livingentity = (LivingEntity)entity;
                var10000 = livingentity.isAlive() && !livingentity.isSleeping();
            } else {
                var10000 = entity.isAlive();
            }

            return var10000;
        }
    }
}
