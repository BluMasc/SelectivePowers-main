package net.blumasc.selectivepowers.effect.custom;

import net.blumasc.selectivepowers.sound.SelectivepowersSounds;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class YellowFeverEffect extends MobEffect {
    public YellowFeverEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if (!(livingEntity instanceof ServerPlayer player)) {
            return super.applyEffectTick(livingEntity, amplifier);
        }

        int whisperChance = ((amplifier+1) * 3);

        if (player.level().random.nextInt(500) < whisperChance) {
            float volume = 0.55f;
            float pitch = 0.8f + player.level().random.nextFloat() * 0.4f;
            double offsetX = (player.level().random.nextDouble() - 0.5) * 6.0;
            double offsetY = (player.level().random.nextDouble() * 2.0) + 0.5;
            double offsetZ = (player.level().random.nextDouble() - 0.5) * 6.0;
            player.connection.send(
                        new ClientboundSoundPacket(
                                BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SelectivepowersSounds.YELLOW_WHISPERS.get()),
                                SoundSource.PLAYERS,
                                player.getX()+offsetX, player.getY()+offsetY, player.getZ()+offsetZ,
                                volume, pitch,
                                player.level().random.nextLong()
                        )
                );
            }
        return super.applyEffectTick(livingEntity, amplifier);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

}
