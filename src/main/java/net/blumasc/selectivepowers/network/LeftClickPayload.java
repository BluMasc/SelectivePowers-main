package net.blumasc.selectivepowers.network;

import net.blumasc.selectivepowers.SelectivePowers;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import static net.blumasc.selectivepowers.events.PowerUseEvents.remoteUseBlock;

public record LeftClickPayload() implements CustomPacketPayload {
    public static final Type<LeftClickPayload> TYPE =
        new Type<>(ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "left_clicked"));

    public static final StreamCodec<FriendlyByteBuf, LeftClickPayload> STREAM_CODEC =
            StreamCodec.of(
                    (buf, packet) -> {},
                    buf -> new LeftClickPayload()
            );

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void handle(LeftClickPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            remoteUseBlock(context.player(), 32);
                }
        );
    }

}