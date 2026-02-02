package net.blumasc.selectivepowers.state;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerEnchantmentState {
    final Map<ResourceLocation, Long> cooldowns = new HashMap<>();

    int cloudStepJumpsUsed = 0;
    private boolean dashUsed = false;
    private boolean justJumped = false;
    private UUID frenzyLastAttackedEntity = null;


    public boolean isOnCooldown(ResourceLocation id, long gameTime) {
        return cooldowns.getOrDefault(id, 0L) > gameTime;
    }

    public void setCooldown(ResourceLocation id, long untilGameTime) {
        cooldowns.put(id, untilGameTime);
    }

    public long getRemainingCooldown(ResourceLocation id, long gameTime) {
        return Math.max(0, cooldowns.getOrDefault(id, 0L) - gameTime);
    }

    public int getCloudStepJumpsUsed() { return cloudStepJumpsUsed; }
    public void incrementCloudStepJumps() { cloudStepJumpsUsed++; }
    public void resetCloudStepJumps() { cloudStepJumpsUsed = 0; }

    public boolean isDashUsed() { return dashUsed; }
    public void setDashUsed(boolean used) { dashUsed = used; }

    public boolean hasJustJumped() { return justJumped; }
    public void setJustJumped(boolean used) { justJumped= used; }

    public UUID getFrenzyLastAttackedEntity() { return frenzyLastAttackedEntity; }
    public void setFrenzyLastAttackedEntity(UUID entityId) { frenzyLastAttackedEntity = entityId; }
}
