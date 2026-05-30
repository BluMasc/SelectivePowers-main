package net.blumasc.selectivepowers.network;

import net.blumasc.selectivepowers.PowerManager;
import net.blumasc.selectivepowers.SelectivePowers;
import net.blumasc.selectivepowers.client.ClientPowerData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import static net.blumasc.selectivepowers.events.PowerActiveUse.activateAbility;

public record LeverVisionSyncPacket(
        boolean vision
) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<LeverVisionSyncPacket> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "lever_vision"));

    public static final StreamCodec<FriendlyByteBuf, LeverVisionSyncPacket> CODEC =
            StreamCodec.of(
                    (buf, pkt) -> {
                        buf.writeBoolean(pkt.vision()); // use the record accessor
                    },
                    buf -> new LeverVisionSyncPacket(buf.readBoolean())
            );


    public static void handle(LeverVisionSyncPacket pkt, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            ClientPowerData.shouldDisplayLeverVision=pkt.vision;
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}