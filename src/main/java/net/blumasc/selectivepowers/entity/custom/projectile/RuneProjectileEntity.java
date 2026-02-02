package net.blumasc.selectivepowers.entity.custom.projectile;

import net.blumasc.selectivepowers.effect.SelectivepowersEffects;
import net.blumasc.selectivepowers.entity.SelectivepowersEntities;
import net.blumasc.selectivepowers.item.SelectivepowersItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import team.lodestar.lodestone.registry.common.particle.LodestoneParticleTypes;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.data.spin.SpinParticleData;

import java.awt.*;

import static net.blumasc.selectivepowers.entity.helper.YellowFeverHelper.increaseEffect;
import static net.blumasc.selectivepowers.entity.helper.YellowFeverHelper.isYellowFeverImmune;


public class RuneProjectileEntity extends AbstractHurtingProjectile {
    private static final EntityDataAccessor<ItemStack> DATA_ITEM_STACK;

    protected RuneProjectileEntity(double x, double y, double z, Level level) {
        super(SelectivepowersEntities.RUNE_PROJECTILE.get(), x, y, z, level);
    }

    public RuneProjectileEntity(double x, double y, double z, Vec3 movement, Level level) {
        super(SelectivepowersEntities.RUNE_PROJECTILE.get(), x, y, z, movement, level);
    }

    public RuneProjectileEntity(LivingEntity owner, Vec3 movement, Level level) {
        super(SelectivepowersEntities.RUNE_PROJECTILE.get(), owner, movement, level);
    }

    public RuneProjectileEntity(LivingEntity shooter, Level level) {
        super(SelectivepowersEntities.RUNE_PROJECTILE.get(), shooter, new Vec3(0,0,0), level);
    }

    public RuneProjectileEntity(EntityType<RuneProjectileEntity> runeProjectileEntityEntityType, Level level) {
        super(runeProjectileEntityEntityType, level);
    }

    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        Level var3 = this.level();
        if (var3 instanceof ServerLevel serverlevel) {
            Entity entity1 = result.getEntity();
            if(!isYellowFeverImmune(entity1)) {
                Entity owner = this.getOwner();
                DamageSource $$6 = this.damageSources().thrown(this, owner);
                if (entity1.hurt($$6, 1.0F) && entity1 instanceof LivingEntity livingEntity) {
                    increaseEffect(livingEntity, SelectivepowersEffects.YELLOW_FEVER_EFFECT, this.getRandom());
                }
            }
        }

    }

    @Override
    public void tick() {
        super.tick();
        spawnParticle(this.level(), this.getX(), this.getY()+0.1f, this.getZ(), new Color(0xd3af37), new Color(0xd3af37), 0, 0, 0);
    }

    public static void spawnParticle(Level level, double x, double y, double z, Color startingColor, Color endingColor, double moveX, double moveY, double moveZ) {
        if (level instanceof ServerLevel) return;
        WorldParticleBuilder.create(LodestoneParticleTypes.WISP_PARTICLE)
                .setScaleData(GenericParticleData.create(0.2f, 0).build())
                .setTransparencyData(GenericParticleData.create(0.75f, 0.25f).build())
                .setColorData(ColorParticleData.create(startingColor, endingColor).setCoefficient(1.4f).setEasing(Easing.BOUNCE_IN_OUT).build())
                .setSpinData(SpinParticleData.create(0.2f, 0.4f).setSpinOffset((level.getGameTime() * 0.2f) % 6.28f).setEasing(Easing.QUARTIC_IN).build())
                .setLifetime(20)
                .addMotion(moveX, moveY, moveZ)
                .enableNoClip()
                .spawn(level, x, y, z);
    }

    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
    }

    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level().isClientSide) {
            this.discard();
        }

    }


    public void setItem(ItemStack stack) {
        if (stack.isEmpty()) {
            this.getEntityData().set(DATA_ITEM_STACK, this.getDefaultItem());
        } else {
            this.getEntityData().set(DATA_ITEM_STACK, stack.copyWithCount(1));
        }

    }

    public ItemStack getItem() {
        return (ItemStack)this.getEntityData().get(DATA_ITEM_STACK);
    }

    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_ITEM_STACK, this.getDefaultItem());
    }

    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.put("Item", this.getItem().save(this.registryAccess()));
    }

    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("Item", 10)) {
            this.setItem((ItemStack)ItemStack.parse(this.registryAccess(), compound.getCompound("Item")).orElse(this.getDefaultItem()));
        } else {
            this.setItem(this.getDefaultItem());
        }

    }

    private ItemStack getDefaultItem() {
        return new ItemStack(SelectivepowersItems.LIGHT_RUNE.get());
    }

    public SlotAccess getSlot(int slot) {
        return slot == 0 ? SlotAccess.of(this::getItem, this::setItem) : super.getSlot(slot);
    }

    public boolean hurt(DamageSource source, float amount) {
        return false;
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    @Override
    public boolean displayFireAnimation() {
        return false;
    }

    static {
        DATA_ITEM_STACK = SynchedEntityData.defineId(RuneProjectileEntity.class, EntityDataSerializers.ITEM_STACK);
    }
}
