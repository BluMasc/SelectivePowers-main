package net.blumasc.selectivepowers.entity.custom.projectile;
import net.blumasc.selectivepowers.entity.SelectivepowersEntities;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

public class ElementalBallEntity extends ThrowableProjectile {

    private static final EntityDataAccessor<BlockState> BLOCK=
    SynchedEntityData.defineId(ElementalBallEntity.class, EntityDataSerializers.BLOCK_STATE);
    boolean burning = false;

    public BlockState getBlock(){
        return this.entityData.get(BLOCK);
    }

    private void setBlock(BlockState block){
        this.entityData.set(BLOCK, block);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(BLOCK, Blocks.BLUE_ICE.defaultBlockState());
    }

    private static final double EFFECT_RADIUS = 5.0;

    public float rotationX;
    public float rotationY;
    public float rotationZ;

    public ElementalBallEntity(EntityType<? extends ElementalBallEntity> type, Level world) {
        super(type, world);
        initClientRotation();
    }

    public ElementalBallEntity(Level world, LivingEntity shooter, boolean burning) {
        super(SelectivepowersEntities.ELEMENTAL_BALL.get(), shooter, world);
        if(burning)
        {
            setBlock(Blocks.NETHERRACK.defaultBlockState());
            this.burning = true;
        }
        initClientRotation();
    }

    @Override
    protected void onHit(HitResult result) {
        if (!level().isClientSide) {
            Vec3 loc = result.getLocation();
            level().playSound(null, this.getOnPos(), SoundEvents.GENERIC_EXPLODE.value(), this.getSoundSource());
            BlockPos center = new BlockPos((int) loc.x, (int) loc.y, (int) loc.z);
            int radius = 3;

            for (int x = -radius; x <= radius; x++) {
                for (int y = -radius; y <= radius; y++) {
                    for (int z = -radius; z <= radius; z++) {
                        if (x*x + y*y + z*z <= radius*radius) {
                            BlockPos pos = center.offset(x, y, z);
                            BlockState state = level().getBlockState(pos);
                            if (state.isAir() || state.canBeReplaced()) {
                                if(burning){
                                    level().setBlock(pos, Blocks.LAVA.defaultBlockState(), 3);
                                }else {
                                    level().setBlock(pos, Blocks.POWDER_SNOW.defaultBlockState(), 3);
                                }
                            }
                        }
                    }
                }
            }

            this.discard();
        }
    }

    private void initClientRotation() {
        RandomSource rand = level().random;
        rotationX = rand.nextFloat() * 360f;
        rotationY = rand.nextFloat() * 360f;
        rotationZ = rand.nextFloat() * 360f;
    }

    private void tickRotation() {
        float speed = 0.5f;
        rotationX = (rotationX + speed) % 360f;
        rotationY = (rotationY + speed * 1.2f) % 360f;
        rotationZ = (rotationZ + speed * 0.8f) % 360f;
    }

    @Override
    public void tick() {
        super.tick();

        if (level().isClientSide) {
            tickRotation();
            return;
        }

        if (!level().isClientSide) {
            AABB area = this.getBoundingBox().inflate(EFFECT_RADIUS);
            for (LivingEntity e : level().getEntitiesOfClass(LivingEntity.class, area)) {
                if (e != this.getOwner()) {
                    if(burning){
                        e.setRemainingFireTicks(40);
                    }else{
                        e.setTicksFrozen(140);
                        e.setIsInPowderSnow(true);
                    }
                }
            }
        }
    }
}

