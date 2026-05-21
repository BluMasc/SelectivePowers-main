package net.blumasc.selectivepowers.entity.custom;

import net.blumasc.selectivepowers.item.SelectivepowersItems;
import net.blumasc.selectivepowers.item.custom.AnchorBladeItem;
import net.blumasc.selectivepowers.item.custom.ThrownAnchorBladeItem;
import net.blumasc.selectivepowers.sound.SelectivepowersSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AnchorEntity extends Entity {

    private static final EntityDataAccessor<Boolean> RETURNING =
            SynchedEntityData.defineId(AnchorEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> STUCK =
            SynchedEntityData.defineId(AnchorEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> OWNER_ID =
            SynchedEntityData.defineId(
                    AnchorEntity.class,
                    EntityDataSerializers.INT
            );

    private static final EntityDataAccessor<Float> STUCK_YAW =
            SynchedEntityData.defineId(
                    AnchorEntity.class,
                    EntityDataSerializers.FLOAT
            );

    private static final EntityDataAccessor<Float> STUCK_PITCH =
            SynchedEntityData.defineId(
                    AnchorEntity.class,
                    EntityDataSerializers.FLOAT
            );


    private static final int MAX_RANGE = 25;
    private static final float THROW_SPEED = 3.0f;
    private static final float RETURN_SPEED = 2.5f;

    private UUID ownerUUID;
    private Player owner;
    private BlockPos stuckPos;
    private int distanceTravelled = 0;
    private List<Entity> draggedEntities = new ArrayList<>();
    private Vec3 spawnPos;

    public Player getOwner() {

        if (owner != null && owner.isAlive()) {
            return owner;
        }

        int id = this.entityData.get(OWNER_ID);

        if (id == -1) {
            return null;
        }

        Entity entity = level().getEntity(id);

        if (entity instanceof Player player) {
            this.owner = player;
            return player;
        }

        return null;
    }

    public void setOwner(Player p) {
        this.owner = p;
        this.ownerUUID = p.getUUID();
        this.entityData.set(OWNER_ID, p.getId());
    }

    public AnchorEntity(EntityType<?> type, Level level) {
        super(type, level);
        this.noPhysics = false;
    }

    public AnchorEntity(EntityType<?> type, Level level, Player owner) {
        this(type, level);
        this.owner = owner;
        this.ownerUUID = owner.getUUID();
        this.spawnPos = owner.position();
        setPos(owner.getX(), owner.getEyeY() - 0.1, owner.getZ());
    }

    public float getStuckYaw() {
        return this.entityData.get(STUCK_YAW);
    }

    public float getStuckPitch() {
        return this.entityData.get(STUCK_PITCH);
    }

    public boolean isReturning() {
        return this.entityData.get(RETURNING);
    }

    public boolean isStuck() {
        return this.entityData.get(STUCK);
    }

    public void startReturn() {
        this.entityData.set(RETURNING, true);
        this.entityData.set(STUCK, false);
        if(this.level() instanceof ServerLevel sl) {
            sl.playSound(null, this.getX(), this.getY(), this.getZ(), SelectivepowersSounds.ROPE_TIGHTENING.get(), SoundSource.PLAYERS, 1.0f, 0.7f);
            sl.playSound(null, owner.getX(), owner.getY(), owner.getZ(), SelectivepowersSounds.ROPE_TIGHTENING.get(), SoundSource.PLAYERS, 1.0f, 0.7f);
        }
        this.stuckPos = null;
    }

    @Override
    public void tick() {
        super.tick();

        if (level().isClientSide) return;

        ServerLevel sl = (ServerLevel) level();

        if (owner == null && ownerUUID != null) {
            owner = (Player) sl.getPlayerByUUID(ownerUUID);
        }
        if (owner == null || !owner.isAlive()) {
            discard();
            return;
        }
        if (!isReturning() && position().distanceTo(owner.position()) > MAX_RANGE) {
            startReturn();
        }
        if (!isReturning() && !playerHoldsLinkedAnchor(owner)) {
            discard();
            return;
        }

        if (isStuck()) {
            if (stuckPos != null && level().isEmptyBlock(stuckPos)) {
                startReturn();
            }
            return;
        }

        if (!isReturning()) {
            tickForward(sl);
        } else {
            tickReturn(sl);
        }
    }

    private void tickForward(ServerLevel sl) {
        Vec3 movement = getDeltaMovement();
        Vec3 nextPos = position().add(movement);

        distanceTravelled += movement.length();
        if (distanceTravelled >= MAX_RANGE) {
            startReturn();
            return;
        }
        BlockHitResult blockHit = sl.clip(new ClipContext(
                position(), nextPos,
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE, this
        ));

        if (blockHit.getType() == HitResult.Type.BLOCK) {
            stuckPos = blockHit.getBlockPos();
            setPos(blockHit.getLocation());
            sl.playSound(null, this.getX(), this.getY(), this.getZ(), SelectivepowersSounds.ANCHOR_LAND.get(), SoundSource.PLAYERS);
            Vec3 vel = getDeltaMovement();
            if (vel.lengthSqr() > 0.0001) {

                vel = vel.normalize();

                float yaw = (float)(
                        Math.atan2(vel.x, vel.z) * 180F / Math.PI
                );

                float pitch = (float)(
                        Math.atan2(
                                vel.y,
                                Math.sqrt(vel.x * vel.x + vel.z * vel.z)
                        ) * 180F / Math.PI
                );

                this.entityData.set(STUCK_YAW, yaw);
                this.entityData.set(STUCK_PITCH, pitch);
            }
            setDeltaMovement(Vec3.ZERO);
            this.entityData.set(STUCK, true);
            return;
        }
        AABB hitBox = getBoundingBox().expandTowards(movement);
        List<Entity> hit = sl.getEntities(this, hitBox, 
            e -> e instanceof LivingEntity && e != owner);
        
        for (Entity e : hit) {
            if (e instanceof LivingEntity living) {
                float damage = 4.0f + AnchorBladeItem.getOxygenBonus(living);
                living.hurt(damageSources().thrown(this, owner), damage);
            }
        }
        setPos(nextPos);
        setDeltaMovement(movement.add(0, -0.01, 0));
    }

    private void tickReturn(ServerLevel sl) {
        if (owner == null) { discard(); return; }

        Vec3 toPlayer = owner.getEyePosition().subtract(position());
        double dist = toPlayer.length();

        if (dist < 1.5) {
            clearDraggedEntities();
            discard();

            if (owner instanceof Player player) {

                for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                    ItemStack stack = player.getInventory().getItem(i);

                    if (stack.getItem() instanceof ThrownAnchorBladeItem) {

                        ThrownAnchorBladeItem.restoreOriginal(player, stack, i);
                        break;
                    }
                }
            }
            return;
        }

        Vec3 returnDir = toPlayer.normalize();
        setDeltaMovement(returnDir.scale(RETURN_SPEED));
        Vec3 nextPos = position().add(getDeltaMovement());

        AABB sweepBox = getBoundingBox().expandTowards(getDeltaMovement()).inflate(0.5);
        List<Entity> inPath = sl.getEntities(this, sweepBox,
            e -> e != owner && !draggedEntities.contains(e));

        draggedEntities.addAll(inPath);

        Vec3 playerPos = owner.getEyePosition();

        for (Entity dragged : draggedEntities) {

            if (dist > 0.5) {
                dragged.setDeltaMovement(
                        toPlayer.normalize().scale(0.4 * RETURN_SPEED)
                );
            }
        }

        sweepBlocks(sl, position(), returnDir, -1, 3);
        stuckPos = null;

        setPos(nextPos);
    }

    private void sweepBlocks(ServerLevel sl, Vec3 origin, Vec3 direction,
                             int minSteps, int maxSteps) {
        java.util.Set<BlockPos> seen = new java.util.HashSet<>();

        for (int step = minSteps; step <= maxSteps; step++) {
            Vec3 samplePos = origin.add(direction.scale(step));
            BlockPos bp = BlockPos.containing(samplePos);

            if (!seen.add(bp)) continue;

            BlockState state = sl.getBlockState(bp);
            if (!state.isAir() && !state.hasBlockEntity()  && state.getPistonPushReaction() == PushReaction.NORMAL) {
                spawnFallingBlock(sl, bp, state, direction);
            }
        }
    }

    private void spawnFallingBlock(ServerLevel sl, BlockPos pos,
                                   BlockState state, Vec3 direction) {
        if (state.getPistonPushReaction() != PushReaction.NORMAL) return;

        RandomSource rand = sl.random;
        Vec3 throwDir = direction.normalize().add(
                (rand.nextDouble() - 0.5) * 0.25,
                rand.nextDouble() * 0.3 + 0.15,
                (rand.nextDouble() - 0.5) * 0.25
        ).normalize().scale(1.8);

        FallingBlockEntity falling = new FallingBlockEntity(
                sl,
                pos.getX() + 0.5,
                pos.getY(),
                pos.getZ() + 0.5,
                state
        );
        falling.time = 2;
        falling.setDeltaMovement(throwDir);

        sl.setBlock(pos, net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(), 3);
        sl.addFreshEntity(falling);
    }

    private void clearDraggedEntities() {
        draggedEntities.clear();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(RETURNING, false);
        builder.define(STUCK, false);
        builder.define(STUCK_YAW, 0f);
        builder.define(STUCK_PITCH, 0f);
        builder.define(OWNER_ID, -1);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        if (tag.hasUUID("OwnerUUID")) ownerUUID = tag.getUUID("OwnerUUID");
        distanceTravelled = tag.getInt("Distance");
        this.entityData.set(RETURNING, tag.getBoolean("Returning"));
        this.entityData.set(STUCK, tag.getBoolean("Stuck"));
        this.entityData.set(STUCK_YAW, tag.getFloat("StuckYaw"));
        this.entityData.set(STUCK_PITCH, tag.getFloat("StuckPitch"));
        if (tag.contains("StuckPosX")) {
            stuckPos = new BlockPos(
                    tag.getInt("StuckPosX"),
                    tag.getInt("StuckPosY"),
                    tag.getInt("StuckPosZ")
            );
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        if (ownerUUID != null) tag.putUUID("OwnerUUID", ownerUUID);
        tag.putInt("Distance", distanceTravelled);
        tag.putBoolean("Returning", this.entityData.get(RETURNING));
        tag.putBoolean("Stuck", this.entityData.get(STUCK));
        tag.putFloat("StuckYaw", this.entityData.get(STUCK_YAW));
        tag.putFloat("StuckPitch", this.entityData.get(STUCK_PITCH));
        if (stuckPos != null) {
            tag.putInt("StuckPosX", stuckPos.getX());
            tag.putInt("StuckPosY", stuckPos.getY());
            tag.putInt("StuckPosZ", stuckPos.getZ());
        }
    }
    private boolean playerHoldsLinkedAnchor(Player player) {
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.getItem() instanceof ThrownAnchorBladeItem) {
                CompoundTag tag = stack.getOrDefault(
                        DataComponents.CUSTOM_DATA,
                        net.minecraft.world.item.component.CustomData.EMPTY
                ).copyTag();
                if (tag.hasUUID("AnchorUUID") && tag.getUUID("AnchorUUID").equals(this.getUUID())) {
                    return true;
                }
            }
        }
        return false;
    }
}