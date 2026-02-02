package net.blumasc.selectivepowers.client;

import net.blumasc.selectivepowers.PowerManager;

import java.util.UUID;

public class ClientPowerData {

    public static UUID moonOwner;
    public static UUID yellowOwner;
    public static UUID darkOwner;
    public static UUID dragonOwner;
    public static UUID truthOwner;
    public static PowerManager.PowerLevel truthLevel = PowerManager.PowerLevel.FREE;

    public static void clear() {
        moonOwner = null;
        yellowOwner = null;
        darkOwner = null;
        dragonOwner = null;
        truthOwner = null;
        truthLevel = PowerManager.PowerLevel.FREE;
    }
}
