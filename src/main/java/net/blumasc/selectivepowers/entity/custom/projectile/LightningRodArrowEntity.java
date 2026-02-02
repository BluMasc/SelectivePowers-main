package net.blumasc.selectivepowers.entity.custom.projectile;

import net.blumasc.selectivepowers.PowerManager;
import net.blumasc.selectivepowers.entity.SelectivepowersEntities;
import net.blumasc.selectivepowers.item.custom.LightningInABottleItem;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.*;

public class LightningRodArrowEntity extends AbstractArrow {
    private int chainNumber;
    public LightningRodArrowEntity(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
        this.chainNumber = 1;
    }

    public LightningRodArrowEntity(Level level, int chainingCount) {
        super(SelectivepowersEntities.LIGHTNING_ROD_ARROW.get(), level);
        this.chainNumber = chainingCount;
    }

    public LightningRodArrowEntity(Level level, LivingEntity shooter, int chainingCount, @Nullable ItemStack firedFromWeapon) {
        super(SelectivepowersEntities.LIGHTNING_ROD_ARROW.get(), shooter, level, Items.LIGHTNING_ROD.getDefaultInstance(), firedFromWeapon);
        this.chainNumber = chainingCount;
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return Items.LIGHTNING_ROD.getDefaultInstance();
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        if(startChainFromArrow()){
            this.discard();
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        if(result.getEntity() instanceof LivingEntity le) {
            castChainLightning(getOwner(), 3, this.chainNumber, 7.5, le);
        }
        else{
            startChainFromArrow();
        }
        super.onHitEntity(result);
    }

    private boolean startChainFromArrow() {
        if (this.level().isClientSide) return false;

        Entity owner = this.getOwner();
        if (!(this.level() instanceof ServerLevel sl)) return false;

        Vec3 arrowPos = this.position();

        List<LivingEntity> nearby = sl.getEntitiesOfClass(
                LivingEntity.class,
                this.getBoundingBox().inflate(7.5),
                e -> e.isAlive()
        );

        LivingEntity closest = null;
        double closestDist = Double.MAX_VALUE;

        for (LivingEntity e : nearby) {
            double dist = e.position().distanceToSqr(arrowPos);
            if (dist < closestDist) {
                closestDist = dist;
                closest = e;
            }
        }

        if (closest == null) return false;

        Vec3 targetPos = closest.getEyePosition();
        spawnVisualLightning(this.level(), arrowPos, targetPos);
        return castChainLightning(
                owner,
                3.0f,
                this.chainNumber-1,
                7.5,
                closest
        );
    }

    public static boolean castChainLightning(
            Entity player,
            float damage,
            int maxChains,
            double chainRadius,
            LivingEntity firstTarget
    ) {
        Level level = player.level();
        if (!(level instanceof ServerLevel sl)) return false;

        if(firstTarget == null) return false;

        PowerManager pm = PowerManager.get(sl);

        Set<LivingEntity> hit = new HashSet<>();
        Map<LivingEntity, Candidate> candidates = new HashMap<>();

        Vec3 firstPos = firstTarget.getEyePosition();

        Holder<DamageType> lightningHolder = level.registryAccess()
                .registryOrThrow(Registries.DAMAGE_TYPE)
                .getHolderOrThrow(DamageTypes.LIGHTNING_BOLT);

        DamageSource lightning = new DamageSource(
                lightningHolder,
                player
        );
        if(!pm.getPowerOfPlayer(firstTarget.getUUID()).equals(PowerManager.STORM_POWER)) {
            firstTarget.setRemainingFireTicks(10);
            firstTarget.hurt(lightning, damage);
        }
        hit.add(firstTarget);
        if(player instanceof LivingEntity le) {
            hit.add(le);
        }

        seedCandidates(level, firstTarget, firstPos, candidates, hit, chainRadius);

        int chains = 0;

        while (!candidates.isEmpty() && chains < maxChains) {
            Map.Entry<LivingEntity, Candidate> next = candidates.entrySet()
                    .stream()
                    .min(Comparator.comparingDouble(e -> e.getValue().distance))
                    .orElse(null);

            if (next == null) break;

            LivingEntity entity = next.getKey();
            Candidate data = next.getValue();

            candidates.remove(entity);
            if (!entity.isAlive()) continue;

            Vec3 targetPos = entity.getEyePosition();

            spawnVisualLightning(level, data.fromPos, targetPos);

            if(!pm.getPowerOfPlayer(firstTarget.getUUID()).equals(PowerManager.STORM_POWER)) {
                entity.setRemainingFireTicks(10);
                entity.hurt(lightning, damage);
            }

            hit.add(entity);
            chains++;

            seedCandidates(level, entity, targetPos, candidates, hit, chainRadius);
        }
        return true;
    }

    private static void seedCandidates(
            Level level,
            LivingEntity source,
            Vec3 sourcePos,
            Map<LivingEntity, Candidate> candidates,
            Set<LivingEntity> hit,
            double radius
    ) {
        for (LivingEntity e : level.getEntitiesOfClass(
                LivingEntity.class,
                source.getBoundingBox().inflate(radius),
                ent -> ent.isAlive() && !hit.contains(ent)
        )) {
            Vec3 ePos = e.getEyePosition();
            double dist = sourcePos.distanceTo(ePos);

            Candidate existing = candidates.get(e);
            if (existing == null || dist < existing.distance) {
                candidates.put(e, new Candidate(dist, sourcePos));
            }
        }
    }

    private static class Candidate {
        final double distance;
        final Vec3 fromPos;

        Candidate(double distance, Vec3 fromPos) {
            this.distance = distance;
            this.fromPos = fromPos;
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
