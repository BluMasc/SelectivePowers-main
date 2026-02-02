package net.blumasc.selectivepowers.network;

import net.blumasc.selectivepowers.SelectivePowers;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

public class ModNetworking {

    public static void register(net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent event) {
        event.registrar(SelectivePowers.MODID).playToClient(
                        PowerInfoSyncPacket.TYPE,
                        PowerInfoSyncPacket.CODEC,
                        PowerInfoSyncPacket::handle
                );

        event.registrar(SelectivePowers.MODID).playToClient(
                DiviningDataSyncPacket.TYPE,
                DiviningDataSyncPacket.CODEC,
                DiviningDataSyncPacket::handle
        );

        event.registrar(SelectivePowers.MODID).playToServer(
                ActivateAbilityPacket.TYPE,
                ActivateAbilityPacket.CODEC,
                ActivateAbilityPacket::handle
        );
    }
}
