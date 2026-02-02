package net.blumasc.selectivepowers.network;

import net.blumasc.selectivepowers.PowerManager;
import net.blumasc.selectivepowers.SelectivePowers;
import net.blumasc.selectivepowers.client.ClientPowerData;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.UUID;
import java.util.function.Supplier;

import static net.blumasc.selectivepowers.events.PowerActiveUse.activateAbility;

public record ActivateAbilityPacket(
        boolean ult
) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<ActivateAbilityPacket> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "use_power"));

    public static final StreamCodec<FriendlyByteBuf, ActivateAbilityPacket> CODEC =
            StreamCodec.of(
                    (buf, pkt) -> {
                        buf.writeBoolean(pkt.ult()); // use the record accessor
                    },
                    buf -> new ActivateAbilityPacket(buf.readBoolean())
            );


    public static void handle(ActivateAbilityPacket pkt, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            Player player = ctx.player();
            if (player == null) return;

            if(!(player instanceof ServerPlayer sp)) return;

            ServerLevel level = (ServerLevel) sp.level();
            PowerManager pm = PowerManager.get(level);

            activateAbility(player, pkt.ult);
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
