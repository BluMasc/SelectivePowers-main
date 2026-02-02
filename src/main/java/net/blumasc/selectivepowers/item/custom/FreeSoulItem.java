package net.blumasc.selectivepowers.item.custom;

import net.blumasc.selectivepowers.PowerManager;
import net.blumasc.selectivepowers.sound.SelectivepowersSounds;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import team.lodestar.lodestone.registry.common.particle.LodestoneParticleTypes;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.data.spin.SpinParticleData;
import team.lodestar.lodestone.systems.particle.world.type.LodestoneWorldParticleType;

import java.awt.*;
import java.util.function.Supplier;

public class FreeSoulItem extends Item {
    public FreeSoulItem(Properties properties) {
        super(properties);
    }
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }

    public static DamageSource createMagicDamage(Level level) {
        var registryAccess = level.registryAccess();

        var damageTypeRegistry = registryAccess.registryOrThrow(Registries.DAMAGE_TYPE);

        var damageTypeKey = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath("minecraft", "magic"));

        var damageTypeHolder = damageTypeRegistry.getHolderOrThrow(damageTypeKey);

        return new DamageSource(damageTypeHolder);
    }

    public void onUseTick(Level level, LivingEntity entityLiving, ItemStack stack, int timeLeft) {
        if (entityLiving instanceof Player player) {
            int i = this.getUseDuration(stack, entityLiving) - timeLeft;
            if (i % 10 == 0) {
                Vec3 pos = entityLiving.position();
                if(i >= 120){
                    spawnParticles(level, pos.x, pos.y, pos.z, 10, LodestoneParticleTypes.TWINKLE_PARTICLE, new Color(255, 255, 255), new Color(100, 100, 100));
                }
                else {
                    spawnParticles(level, pos.x, pos.y, pos.z, 15, LodestoneParticleTypes.SMOKE_PARTICLE, new Color(0, 0, 255), new Color(100, 100, 100));
                }
                level.playSound(player, player.getOnPos(), SelectivepowersSounds.WIND.get(), SoundSource.PLAYERS);
            }

            player.hurt(createMagicDamage(level), (float) (level.getRandom().nextDouble()*2.0));

        }
    }

    public void releaseUsing(ItemStack stack, Level level, LivingEntity entityLiving, int timeLeft) {
        if (!(entityLiving instanceof Player player)) return;
        int i = this.getUseDuration(stack, entityLiving) - timeLeft;
        if (i >= 120) {
            if (level.isClientSide()) {
                Vec3 pos = player.position();

                level.playSound(player, player.getOnPos(), SelectivepowersSounds.SPARKLE.get(), SoundSource.PLAYERS);

                spawnParticles(level, pos.x, pos.y, pos.z, 30, LodestoneParticleTypes.TWINKLE_PARTICLE,
                        new Color(100, 0, 100), new Color(0, 100, 200));

            }
            if (!(level instanceof ServerLevel serverLevel)) return;

            PowerManager manager = PowerManager.get(serverLevel);

            Vec3 pos = player.position();
            level.playSound(player, player.getOnPos(), SelectivepowersSounds.SPARKLE.get(), SoundSource.PLAYERS);

            spawnParticles(level, pos.x, pos.y, pos.z, 30, LodestoneParticleTypes.TWINKLE_PARTICLE,
                    new Color(100, 0, 100), new Color(0, 100, 200));

            stack.shrink(1);

            String power = manager.getPowerOfPlayer(player.getUUID());
            player.displayClientMessage(Component.translatable("selectivepowers.messages.bondbroken").append(Component.translatable("selectivepowers.name."+power)).append(Component.translatable("selectivepowers.messages.bondbroken.end")), true);

            manager.powerAssignments.remove(power);
            manager.removeProgress(player.getUUID());
            manager.setDirty();
            manager.syncToAll((ServerLevel) player.level());
        }
    }

    public static void spawnParticles(Level level, double x, double y, double z, int count,Supplier<LodestoneWorldParticleType> particle, Color startingColor, Color endingColor) {
        if (level instanceof ServerLevel) return;
        RandomSource rand = level.getRandom();
        for (int j = 0; j < count; j++) {
            double ox = (rand.nextDouble() - 0.5) * 1.5;
            double oy = rand.nextDouble() * 2.0;
            double oz = (rand.nextDouble() - 0.5) * 1.5;
            WorldParticleBuilder.create(particle)
                    .setScaleData(GenericParticleData.create(0.5f, 0).build())
                    .setTransparencyData(GenericParticleData.create(0.75f, 0.25f).build())
                    .setColorData(ColorParticleData.create(startingColor, endingColor).setCoefficient(1.4f).setEasing(Easing.BOUNCE_IN_OUT).build())
                    .setSpinData(SpinParticleData.create(0.2f, 0.4f).setSpinOffset((level.getGameTime() * 0.2f) % 6.28f).setEasing(Easing.QUARTIC_IN).build())
                    .setLifetime(40)
                    .addMotion(0, 0.01f, 0)
                    .enableNoClip()
                    .spawn(level, x+ox, y+oy, z+oz);
        }
    }
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide()) {
            return InteractionResultHolder.pass(player.getItemInHand(hand));
        }

        if (!(level instanceof ServerLevel serverLevel)) {
            return InteractionResultHolder.pass(player.getItemInHand(hand));
        }

        PowerManager manager = PowerManager.get(serverLevel);
        ItemStack itemstack = player.getItemInHand(hand);

        PowerManager.PowerLevel powerLevel = manager.getPowerLevelOfPlayer(player.getUUID());

        if (powerLevel == PowerManager.PowerLevel.FREE) {
            player.displayClientMessage(Component.translatable("selectivepowers.messages.nopower"), true);
            return InteractionResultHolder.fail(itemstack);
        } else if (powerLevel == PowerManager.PowerLevel.BOUND) {
            String power = manager.getPowerOfPlayer(player.getUUID());
            player.displayClientMessage(Component.translatable("selectivepowers.messages.nolongerbound").append(Component.translatable("selectivepowers.name."+power)).append(Component.translatable("selectivepowers.messages.nolongerbound.end")), true);
            manager.powerAssignments.remove(power);
            manager.removeProgress(player.getUUID());
            manager.setDirty();
            manager.syncToAll((ServerLevel) player.level());
            return InteractionResultHolder.pass(itemstack);
        } else {
            player.startUsingItem(hand);
            player.addTag(powerLevel.toString());
            return InteractionResultHolder.consume(itemstack);
        }
    }
}
