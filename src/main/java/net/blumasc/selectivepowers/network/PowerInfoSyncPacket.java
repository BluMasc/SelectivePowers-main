package net.blumasc.selectivepowers.network;

import net.blumasc.selectivepowers.PowerManager;
import net.blumasc.selectivepowers.SelectivePowers;
import net.blumasc.selectivepowers.client.ClientPowerData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.UUID;

public record PowerInfoSyncPacket(
        UUID moonOwner,
        UUID yellowOwner,
        UUID darkOwner,
        UUID dragonOwner,
        UUID truthOwner,
        PowerManager.PowerLevel truthLevel
) implements CustomPacketPayload {

    public static final Type<PowerInfoSyncPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "power_info"));

    public static final StreamCodec<FriendlyByteBuf, PowerInfoSyncPacket> CODEC =
            StreamCodec.of(
                    (buf, pkt) -> {
                        writeUUID(buf, pkt.moonOwner());
                        writeUUID(buf, pkt.yellowOwner());
                        writeUUID(buf, pkt.darkOwner());
                        writeUUID(buf, pkt.dragonOwner());

                        buf.writeBoolean(pkt.truthOwner() != null);
                        if (pkt.truthOwner() != null) {
                            buf.writeUUID(pkt.truthOwner());
                            buf.writeEnum(pkt.truthLevel());
                        }
                    },
                    buf -> {
                        UUID moon = readUUID(buf);
                        UUID yellow = readUUID(buf);
                        UUID dark = readUUID(buf);
                        UUID dragon = readUUID(buf);

                        UUID truth = null;
                        PowerManager.PowerLevel level = PowerManager.PowerLevel.FREE;
                        if (buf.readBoolean()) {
                            truth = buf.readUUID();
                            level = buf.readEnum(PowerManager.PowerLevel.class);
                        }

                        return new PowerInfoSyncPacket(moon, yellow, dark, dragon, truth,  level);
                    }
            );

    private static void writeUUID(FriendlyByteBuf buf, UUID uuid) {
        buf.writeBoolean(uuid != null);
        if (uuid != null) buf.writeUUID(uuid);
    }

    private static UUID readUUID(FriendlyByteBuf buf) {
        return buf.readBoolean() ? buf.readUUID() : null;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(PowerInfoSyncPacket pkt, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            ClientPowerData.moonOwner = pkt.moonOwner();
            ClientPowerData.yellowOwner = pkt.yellowOwner();
            ClientPowerData.darkOwner = pkt.darkOwner();
            ClientPowerData.dragonOwner = pkt.dragonOwner();
            ClientPowerData.truthOwner = pkt.truthOwner();
            ClientPowerData.truthLevel = pkt.truthLevel();
        });
    }
}
