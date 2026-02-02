package net.blumasc.selectivepowers.events;

import net.blumasc.selectivepowers.SelectivePowers;
import net.blumasc.selectivepowers.enchantment.ModEnchantments;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.VibrationParticleOption;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.level.gameevent.PositionSourceType;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.sound.PlaySoundEvent;
import net.neoforged.neoforge.event.level.NoteBlockEvent;

import java.util.Optional;
import java.util.Random;

@EventBusSubscriber(modid = SelectivePowers.MODID, value = Dist.CLIENT)
public class ClientSculkHook {
    @SubscribeEvent
    public static void onPlaySound(PlaySoundEvent event) {
        if (event.getSound() == null) return;
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if(player != null) {
            Holder<Enchantment> hungryHolder = player.level().registryAccess()
                    .registryOrThrow(Registries.ENCHANTMENT)
                    .getHolderOrThrow(ModEnchantments.SCULKING);

            ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
            int level = helmet.getEnchantmentLevel(hungryHolder);
            if (level>0) {
                double dx = player.getX() - event.getSound().getX();
                double dy = player.getY() - event.getSound().getY();
                double dz = player.getZ() - event.getSound().getZ();
                double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);

                double maxDistance = 5.0 +(5.0 * level);
                if (distance <= maxDistance && distance>=0.1) {
                    spawnSculkParticles(mc.player, event.getSound().getX(), event.getSound().getY(), event.getSound().getZ());
                }
            }
        }
    }

    private static void spawnSculkParticles(LocalPlayer player, double x, double y, double z) {
        ClientLevel world = (ClientLevel) player.level();

        VibrationParticleOption vpo = new VibrationParticleOption(new PositionSource() {
            @Override
            public Optional<Vec3> getPosition(Level level) {
                return Optional.of(player.position().add(0,player.getEyeHeight()-0.1,0));
            }

            @Override
            public PositionSourceType<? extends PositionSource> getType() {
                return PositionSourceType.ENTITY;
            }
        }, 15);

        for (int i = 0; i < 20; i++) {
            world.addParticle(
                    vpo,
                    x,
                    y,
                    z,
                    0.1,
                    0.15,
                    0.1
            );
        }
    }
}
