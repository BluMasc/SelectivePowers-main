package net.blumasc.selectivepowers.entity.custom;

import net.blumasc.selectivepowers.PowerManager;
import net.blumasc.selectivepowers.entity.CircleVariant;
import net.blumasc.selectivepowers.entity.SelectivepowersEntities;
import net.blumasc.selectivepowers.entity.custom.projectile.MagicCircleEntity;
import net.blumasc.selectivepowers.item.SelectivepowersItems;
import net.blumasc.selectivepowers.sound.SelectivepowersSounds;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ambient.AmbientCreature;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import team.lodestar.lodestone.registry.common.particle.LodestoneParticleTypes;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.data.spin.SpinParticleData;
import top.theillusivec4.curios.api.CuriosApi;

import java.awt.*;

public class LunarMaidenEntity extends AmbientCreature {

    private int spawnTicks = 0;
    private boolean itemSpawned = false;

    private static final EntityDataAccessor<Boolean> ANIMATE_ACTION =
            SynchedEntityData.defineId(LunarMaidenEntity.class, EntityDataSerializers.BOOLEAN);

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ANIMATE_ACTION, false);
    }

    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState blessAnimationState = new AnimationState();

    public LunarMaidenEntity(EntityType<? extends AmbientCreature> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 40d)
                .add(Attributes.MOVEMENT_SPEED, 1f)
                .add(Attributes.FOLLOW_RANGE, 0d);
    }

    @Override
    public void tick() {
        super.tick();
        if(this.level().isClientSide())
        {
            this.setupAnimationState();
            for(int i=0;i<30;i++) {
                spawnParticle(level(), this.getX(), this.getY(), this.getZ());
            }
        }
        if (!level().isClientSide) {
            spawnTicks++;
            if(spawnTicks==2)
            {
                level().playSound(this, this.getOnPos(), SelectivepowersSounds.LUNAR_LADY.get(), this.getSoundSource(), 1.0f, 1.0f);
            }
            if (spawnTicks >= 20 && !itemSpawned) {
                this.entityData.set(ANIMATE_ACTION, true);
            }
            if(spawnTicks >= 50 && !itemSpawned)
            {
                spawnHoverItem();
                itemSpawned = true;
            }
            if (spawnTicks > 80) {
                discard();
            }
        }
    }

    private void setupAnimationState() {

        this.idleAnimationState.startIfStopped(this.tickCount);

        if(this.entityData.get(ANIMATE_ACTION))
        {
            this.blessAnimationState.startIfStopped(this.tickCount);
            this.entityData.set(ANIMATE_ACTION, false);
        }
    }

    private void spawnHoverItem() {
        Player nearest = level().getNearestPlayer(this, 10);

        if (nearest == null) return;

        PowerManager pm = PowerManager.get((ServerLevel) this.level());

        if(pm.getPowerOfPlayer(nearest.getUUID()).equals(PowerManager.YELLOW_POWER)
        || isWearingCurioHeadItem(nearest, SelectivepowersItems.TRUE_CROWN.get())
        || isWearingCurioHeadItem(nearest, SelectivepowersItems.FAKE_CROWN.get()))
        {
            MagicCircleEntity circle =
                    new MagicCircleEntity(SelectivepowersEntities.MAGIC_CIRCLE.get(), level(), CircleVariant.MOON, 10, 120, 5);
            circle.moveTo(nearest.position());
            level().addFreshEntity(circle);
            return;
        }

        ItemStack stack = new ItemStack(SelectivepowersItems.MOON_PENDANT.get());

        ItemEntity item = new ItemEntity(
                level(),
                getX(),
                getY() + 2.0,
                getZ(),
                stack
        );
        level().addFreshEntity(item);
    }

    public static void spawnParticle(Level level, double x, double y, double z) {
        RandomSource rand = level.getRandom();

        if (rand.nextDouble() < 0.1) {
            double ox = (rand.nextDouble() - 0.5) * 1.5;
            double oy = (rand.nextDouble() * 2.5)+0.5;
            double oz = (rand.nextDouble() - 0.5) * 1.5;

            WorldParticleBuilder.create(LodestoneParticleTypes.WISP_PARTICLE)
                    .setScaleData(GenericParticleData.create(0.2f, 0).build())
                    .setTransparencyData(GenericParticleData.create(0.75f, 0.25f).build())
                    .setColorData(
                            ColorParticleData.create(Color.BLUE, new Color(180, 80, 255))
                                    .setCoefficient(1.4f)
                                    .setEasing(Easing.BOUNCE_IN_OUT)
                                    .build()
                    )
                    .setSpinData(
                            SpinParticleData.create(0.2f, 0.4f)
                                    .setSpinOffset((level.getGameTime() * 0.2f) % 6.28f)
                                    .setEasing(Easing.QUARTIC_IN)
                                    .build()
                    )
                    .setLifetime(40)
                    .addMotion(0, 0.01f, 0)
                    .enableNoClip()
                    .spawn(level, x + ox, y + oy, z + oz);
        }
    }
    @Override
    public boolean hurt(net.minecraft.world.damagesource.DamageSource source, float amount) {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void doPush(Entity entity) {
    }

    @Override
    public void push(double x, double y, double z) {
    }

    @Override
    public void knockback(double strength, double x, double z) {
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    public static boolean isWearingCurioHeadItem(Player player, Item expectedItem) {
        return CuriosApi.getCuriosInventory(player)
                .flatMap(curiosInventory -> curiosInventory.getStacksHandler("head"))
                .map(handler -> {
                    for (int slot = 0; slot < handler.getSlots(); slot++) {
                        ItemStack stack = handler.getStacks().getStackInSlot(slot);
                        if (!stack.isEmpty() && stack.is(expectedItem)) {
                            return true;
                        }
                    }
                    return false;
                })
                .orElse(false);
    }

}
