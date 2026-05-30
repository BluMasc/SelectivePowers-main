package net.blumasc.selectivepowers.network;

import net.blumasc.selectivepowers.SelectivePowers;

public class ModNetworking {

    public static void register(net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent event) {
        event.registrar(SelectivePowers.MODID).playToClient(
                        PowerInfoSyncPacket.TYPE,
                        PowerInfoSyncPacket.CODEC,
                        PowerInfoSyncPacket::handle
                );

        event.registrar(SelectivePowers.MODID).playToClient(
                AbilityTimerSyncPacket.TYPE,
                AbilityTimerSyncPacket.CODEC,
                AbilityTimerSyncPacket::handle
        );

        event.registrar(SelectivePowers.MODID).playToClient(
                LeverVisionSyncPacket.TYPE,
                LeverVisionSyncPacket.CODEC,
                LeverVisionSyncPacket::handle
        );

        event.registrar(SelectivePowers.MODID).playToServer(
                ActivateAbilityPacket.TYPE,
                ActivateAbilityPacket.CODEC,
                ActivateAbilityPacket::handle
        );

        event.registrar(SelectivePowers.MODID).playToServer(
                LeftClickPayload.TYPE,
                LeftClickPayload.STREAM_CODEC,
                LeftClickPayload::handle
        );
    }
}
