package net.blumasc.selectivepowers.client;

public class ClientTimerData {
    public static int abilityTimer = 0;
    public static int ultTimer = 0;
    public static int maxAbilityTimer = 0;
    public static int maxUltTimer = 0;

    public static void setAbilityTimer(int value) {
        if (value > maxAbilityTimer) maxAbilityTimer = value;
        if (maxAbilityTimer > 0 && value == 0) maxAbilityTimer = 0;
        abilityTimer = value;
    }

    public static void setUltTimer(int value) {
        if (value > maxUltTimer) maxUltTimer = value;
        if (maxUltTimer > 0 && value == 0) maxUltTimer = 0;
        ultTimer = value;
    }
}
