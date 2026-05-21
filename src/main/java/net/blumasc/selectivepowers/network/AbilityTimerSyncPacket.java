package net.blumasc.selectivepowers.network;

import net.blumasc.selectivepowers.PowerManager;
import net.blumasc.selectivepowers.SelectivePowers;
import net.blumasc.selectivepowers.client.ClientPowerData;
import net.blumasc.selectivepowers.client.ClientTimerData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.UUID;

public record AbilityTimerSyncPacket (int ultTimer, int abilityTimer) implements CustomPacketPayload {

    public static final Type<AbilityTimerSyncPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "ability_timer_info"));
    public static final StreamCodec<FriendlyByteBuf, AbilityTimerSyncPacket> CODEC =
            StreamCodec.of(
                    (buf, pkt) -> {
                        buf.writeInt(pkt.ultTimer());
                        buf.writeInt(pkt.abilityTimer());
                    },
                    buf -> {
                        int ultTimer = buf.readInt();
                        int abilityTimer = buf.readInt();

                        return new AbilityTimerSyncPacket(ultTimer, abilityTimer);
                    }
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(AbilityTimerSyncPacket pkt, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            ClientTimerData.setAbilityTimer(pkt.abilityTimer());
            ClientTimerData.setUltTimer(pkt.ultTimer());
        });
    }
}
