package net.blumasc.selectivepowers.entity.custom;

import net.blumasc.selectivepowers.block.SelectivepowersBlocks;
import net.blumasc.selectivepowers.entity.CircleVariant;
import net.blumasc.selectivepowers.entity.SelectivepowersEntities;
import net.blumasc.selectivepowers.entity.custom.projectile.MagicCircleEntity;
import net.blumasc.selectivepowers.entity.helper.BossMusicSongPlayer;
import net.blumasc.selectivepowers.item.SelectivepowersItems;
import net.blumasc.selectivepowers.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.JukeboxSong;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.damagesource.DamageContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static net.blumasc.selectivepowers.entity.helper.YellowFeverHelper.isYellowFeverImmune;

public class YellowKingBossEntity extends YellowKingEntity{

    public enum YellowKingAction {
        NONE,
        ATTACK,
        SUMMON
    }

    private int actionTimer = 0;
    private int despawnTimer = 0;
    private BossMusicSongPlayer jukeboxSongPlayer;
    private boolean buildingArena = false;
    private int arenaBuildIndex = 0;
    private int arenaRadius = 20;
    private int arenaHeight = 5;

    private List<BlockPos> arenaBlocks = new ArrayList<>();


    private static final EntityDataAccessor<Integer> ACTION =
            SynchedEntityData.defineId(YellowKingBossEntity.class, EntityDataSerializers.INT);


    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ACTION, YellowKingAction.NONE.ordinal());
    }

    private void setAction(YellowKingAction action) {
        this.entityData.set(ACTION, action.ordinal());
        this.actionTimer = 50;
    }

    private YellowKingAction lastAction = YellowKingAction.NONE;

    private int prayingFanaticId = -1;

    private BlockPos homePos;
    private boolean initialized = false;
    private int teleportCooldown = 3000;
    private int emergencyTeleportCooldown = 300;
    private int emergencyDamage= 0;
    private int abilityCooldown = 300;

    public YellowKingBossEntity(EntityType<? extends YellowKingEntity> entityType, Level level) {
        super(entityType, level);
    }

    private void initializeBoss() {
        this.homePos = this.blockPosition();
        this.initialized = true;

        this.jukeboxSongPlayer = new BossMusicSongPlayer(() ->{}, homePos);
        Optional<Holder<JukeboxSong>> optional = JukeboxSong.fromStack(this.level().registryAccess(), SelectivepowersItems.BATTLE_FOR_ETERNITY_MUSIC_DISC.toStack());
        optional.ifPresent(jukeboxSongHolder -> this.jukeboxSongPlayer.play(this.level(), jukeboxSongHolder));

        prepareArena(homePos, arenaRadius, arenaHeight);

        setAction(YellowKingAction.ATTACK);
        playAttackSequence(32, 60, 60);
        this.summonMinions(4, 4);
    }

    @Override
    public void onDamageTaken(DamageContainer damageContainer) {
        super.onDamageTaken(damageContainer);
        emergencyDamage += damageContainer.getNewDamage();
        if(emergencyDamage>(getMaxHealth()/10))
        {
            teleportAwayFromPlayers();
            emergencyDamage = 0;
        }
    }

    private void handleActionAnimation() {
        YellowKingAction action =
                YellowKingAction.values()[this.entityData.get(ACTION)];

        if (action != lastAction) {
            attackAnimationState.stop();
            summonAnimationState.stop();

            switch (action) {
                case ATTACK -> attackAnimationState.start(this.tickCount);
                case SUMMON -> summonAnimationState.start(this.tickCount);
                default -> {}
            }

            lastAction = action;
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, (double)0.0F)
                .add(Attributes.FOLLOW_RANGE, (double)23.0F)
                .add(Attributes.MAX_HEALTH, (double)200.0F);
    }

    private final ServerBossEvent bossEvent =
            new ServerBossEvent(Component.translatable("entity.selectivepowers.yellow_king"), BossEvent.BossBarColor.YELLOW, BossEvent.BossBarOverlay.NOTCHED_20);

    public boolean tryAssignPrayingFanatic(YellowFanaticEntity fanatic) {
        if (prayingFanaticId != -1) return false;
        prayingFanaticId = fanatic.getId();
        return true;
    }

    public void clearPrayingFanatic(YellowFanaticEntity fanatic) {
        if (fanatic.getId() == (prayingFanaticId)) {
            prayingFanaticId = -1;
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (prayingFanaticId != -1) {
            tag.putInt("PrayingFanatic", prayingFanaticId);
        }
        if (homePos != null) {
            tag.putLong("HomePos", homePos.asLong());
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("PrayingFanatic")) {
            prayingFanaticId = tag.getInt("PrayingFanatic");
        }
        if (tag.contains("HomePos")) {
            homePos = BlockPos.of(tag.getLong("HomePos"));
            this.jukeboxSongPlayer = new BossMusicSongPlayer(() ->{}, homePos);
            Optional<Holder<JukeboxSong>> optional = JukeboxSong.fromStack(this.level().registryAccess(), SelectivepowersItems.BATTLE_FOR_ETERNITY_MUSIC_DISC.toStack());
            optional.ifPresent(jukeboxSongHolder -> this.jukeboxSongPlayer.play(this.level(), jukeboxSongHolder));

            initialized = true;
        }
    }

    @Override
    public void tick() {
        super.tick();

        if(this.level().isClientSide)
        {
            handleActionAnimation();
        }

        if (!initialized && !this.level().isClientSide) {
            initializeBoss();
        }

        if (buildingArena && !this.level().isClientSide) {
            buildArenaTick();
        }


        if (!this.level().isClientSide) {
            this.jukeboxSongPlayer.tick(level(),null);
            if (actionTimer > 0 && --actionTimer == 0) {
                setAction(YellowKingAction.NONE);
            }
            bossLogicTick();
        }

        if (prayingFanaticId != -1 &&
                this.level().getEntity(prayingFanaticId) == null) {
            prayingFanaticId = -1;
        }
        checkForPlayers();
    }

    private void checkForPlayers() {

        for (Entity e : this.level().getEntities(this, this.getBoundingBox().inflate(64))) {
            if (e instanceof Player) {
                return;
            }
        }
        this.despawnTimer++;
        if(this.despawnTimer>2400)
        {
            this.discard();
        }
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    private void bossLogicTick() {
        if (teleportCooldown > 0) teleportCooldown--;
        if (abilityCooldown > 0) abilityCooldown--;
        if (emergencyTeleportCooldown > 0) emergencyTeleportCooldown--;

        if(emergencyTeleportCooldown<=0 && farAwayFromHome())
        {
            teleportAwayFromPlayers();
        }

        if (teleportCooldown <= 0) {
            teleportAwayFromPlayers();
            teleportCooldown = 2000 + this.getRandom().nextInt(60);
        }

        if (abilityCooldown <= 0) {
            useRandomAbility();
            abilityCooldown = 100 + this.getRandom().nextInt(60);
        }
    }

    private boolean farAwayFromHome() {
        Vec3 curr = position();
        Vec3 target = Vec3.atCenterOf(homePos);
        return !curr.closerThan(target, 16);
    }

    private void teleportAwayFromPlayers() {
        ServerLevel level = (ServerLevel) this.level();
        if(getHealth()<getMaxHealth()/20) return;

        Vec3 bestPos = null;
        double bestScore = -Double.MAX_VALUE;

        for (int i = 0; i < 20; i++) {
            double angle = this.getRandom().nextDouble() * Math.PI * 2;
            double dist = this.getRandom().nextDouble() * 16;

            Vec3 pos = Vec3.atCenterOf(homePos)
                    .add(Math.cos(angle) * dist, 0, Math.sin(angle) * dist);

            if (isUnsafePosition(pos)) continue;

            double score = pos.distanceToSqr(this.position());

            for (ServerPlayer p : level.players()) {
                score -= pos.distanceToSqr(p.position());
            }

            if (score > bestScore) {
                bestScore = score;
                bestPos = pos;
            }
        }

        if (bestPos != null) {
            if (!isSilent()) {
                level().playSound(null, getX(),getY(), getZ(), SoundEvents.ENDERMAN_TELEPORT, getSoundSource(), 0.3F, 1.0F + (random.nextFloat() - random.nextFloat()) * 0.2F);
            }
            this.teleportTo(bestPos.x, bestPos.y, bestPos.z);
            emergencyTeleportCooldown = 200 + this.getRandom().nextInt(60);
        }
    }

    private boolean isUnsafePosition(Vec3 pos) {
        BlockPos p = new BlockPos((int) pos.x, (int) pos.y, (int) pos.z);
        return !level().getBlockState(p).isAir();
    }

    private void useRandomAbility() {
        if (this.getRandom().nextBoolean()) {
            setAction(YellowKingAction.ATTACK);
            playAttackSequence(16, 40, 20);
        } else {
            setAction(YellowKingAction.SUMMON);
            summonMinions(this.getRandom().nextInt(5), this.getRandom().nextInt(5));
        }
    }

    public boolean canAcceptPrayingFanatic() {
        return prayingFanaticId == -1;
    }

    public boolean isPrayingFanatic(int id){
        return prayingFanaticId==id;
    }




    private void playAttackSequence(int radius, int chargeTicks, int beamTicks) {

        for (Entity e : this.level().getEntities(this, this.getBoundingBox().inflate(radius))) {
            if (e instanceof LivingEntity le && le != this && !(isYellowFeverImmune(le))) {
                MagicCircleEntity circle =
                        new MagicCircleEntity(SelectivepowersEntities.MAGIC_CIRCLE.get(), this.level(), CircleVariant.SUN, chargeTicks, beamTicks, 4);
                circle.moveTo(le.position());
                this.level().addFreshEntity(circle);
            }
        }
    }

    private void summonMinions(int countCultists, int countMasks) {

        for (int i = 0; i < Math.min(countCultists, 6-countActiveFanaticMinions()); i++) {
            double angle = this.getRandom().nextDouble() * Math.PI * 2;
            double dist = 4 + this.getRandom().nextDouble() * 12;

            double x = this.homePos.getX() + Math.cos(angle) * dist;
            double z = this.homePos.getZ() + Math.sin(angle) * dist;

            if(isUnsafePosition(new Vec3(x, this.getY() + 5, z)))
            {
                continue;
            }

            if(getHealth()<getMaxHealth()/2){
                YellowQuetzalEntity fanatic =
                        SelectivepowersEntities.QUETZAL_YELLOW.get().create(this.level());

                if (fanatic != null) {
                    fanatic.moveTo(x, this.getY()+5, z, this.getYRot(), 0);
                    this.level().addFreshEntity(fanatic);
                }

            }else {

                YellowFanaticEntity fanatic =
                        SelectivepowersEntities.YELLOW_FANATIC.get().create(this.level());

                if (fanatic != null) {
                    fanatic.moveTo(x, this.getY(), z, this.getYRot(), 0);
                    fanatic.setSummoner(this.getId());
                    this.level().addFreshEntity(fanatic);
                }
            }
        }
        for (int i = 0; i < Math.min(countMasks, 4- countActiveMascMinions()); i++) {
            double angle = this.getRandom().nextDouble() * Math.PI * 2;
            double dist = 4 + this.getRandom().nextDouble() * 12;

            double x = this.homePos.getX() + Math.cos(angle) * dist;
            double z = this.homePos.getZ() + Math.sin(angle) * dist;

            if(isUnsafePosition(new Vec3(x, this.getY() + 5, z)))
            {
                continue;
            }

            CorruptingMaskEntity  mask=
                    SelectivepowersEntities.CORRUPTING_MASK.get().create(this.level());

            if (mask != null) {
                mask.moveTo(x, this.getY()+1, z, this.getYRot(), 0);
                mask.setSummoner(this.getUUID());
                this.level().addFreshEntity(mask);
            }
        }
    }

    private int countActiveFanaticMinions() {
        return this.level().getEntitiesOfClass(
                YellowFanaticEntity.class,
                this.getBoundingBox().inflate(32),
                e -> e.isAlive()
        ).size()+this.level().getEntitiesOfClass(
                YellowQuetzalEntity.class,
                this.getBoundingBox().inflate(32),
                e -> e.isAlive()
        ).size();
    }

    private int countActiveMascMinions() {
        return this.level().getEntitiesOfClass(
                CorruptingMaskEntity.class,
                this.getBoundingBox().inflate(32),
                e -> e.isAlive()
        ).size();
    }


    @Override
    public void startSeenByPlayer(ServerPlayer serverPlayer) {
        super.startSeenByPlayer(serverPlayer);
        this.bossEvent.addPlayer(serverPlayer);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer serverPlayer) {
        super.stopSeenByPlayer(serverPlayer);
        this.bossEvent.removePlayer(serverPlayer);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        this.bossEvent.setProgress(this.getHealth()/this.getMaxHealth());
    }

    @Override
    protected void registerGoals() {
    }

    @Override
    public void remove(RemovalReason reason) {
        if(this.jukeboxSongPlayer != null) {
            this.jukeboxSongPlayer.stop(this.level(), null);
        }
        super.remove(reason);
    }

    private void prepareArena(BlockPos center, int radius, int height) {
        arenaBlocks.clear();
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                if (dx * dx + dz * dz <= radius * radius) {
                    BlockPos floorPos = center.offset(dx, -2, dz);
                    Block currentFloor = level().getBlockState(floorPos).getBlock();

                    if (currentFloor != SelectivepowersBlocks.SOLAR_BRICKS.get() &&
                            currentFloor != SelectivepowersBlocks.CRACKED_SOLAR_BRICKS.get()) {
                        arenaBlocks.add(floorPos);
                    }

                    for (int dy = 1; dy < height; dy++) {
                        BlockPos airPos = floorPos.above(dy);
                        if (!airPos.equals(center.offset(0, -1, 0))) {
                            if (!level().getBlockState(airPos).isAir()) {
                                arenaBlocks.add(airPos);
                            }
                        }
                    }
                }
            }
        }
        buildingArena = true;
        arenaBuildIndex = 0;
    }
    private void buildArenaTick() {
        ServerLevel level = (ServerLevel) this.level();
        int blocksPerTick = 100;

        for (int i = 0; i < blocksPerTick && arenaBuildIndex < arenaBlocks.size(); i++, arenaBuildIndex++) {
            BlockPos pos = arenaBlocks.get(arenaBuildIndex);
            Block floorBlock = pos.getY() == homePos.getY() - 2 ? this.getRandom().nextBoolean()? SelectivepowersBlocks.SOLAR_BRICKS.get():SelectivepowersBlocks.CRACKED_SOLAR_BRICKS.get() : Blocks.AIR;
            level.setBlockAndUpdate(pos, floorBlock.defaultBlockState());
        }

        if (arenaBuildIndex >= arenaBlocks.size()) {
            buildingArena = false;
        }
    }
}
