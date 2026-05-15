package net.blumasc.selectivepowers.managers;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MoonManager extends SavedData {

    private final Map<UUID, Boolean> playerMoonbound = new HashMap<>();

    public static final String DATA_NAME = "moonbound";
    @Override
    public CompoundTag save(CompoundTag compoundTag, HolderLookup.Provider provider) {
        CompoundTag damageTag = new CompoundTag();
        for (Map.Entry<UUID, Boolean> entry : playerMoonbound.entrySet()) {
            CompoundTag data = new CompoundTag();
            data.putBoolean("moonBound", entry.getValue());
            damageTag.put(entry.getKey().toString(), data);
        }
        compoundTag.put("moonboundTag", damageTag);
        return compoundTag;
    }

    public static MoonManager create(){
        return new MoonManager();
    }

    public static MoonManager get(ServerLevel world) {
        ServerLevel overworld = world.getServer().overworld();
        return overworld.getDataStorage().computeIfAbsent(new Factory<>(MoonManager::create, MoonManager::load), DATA_NAME);
    }

    public static MoonManager load(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        MoonManager rm = MoonManager.create();
        if(tag.contains("moonboundTag")) {
            CompoundTag damageTag = tag.getCompound("moonboundTag");
            for(String key : damageTag.getAllKeys()) {
                UUID playerUUID = UUID.fromString(key);
                CompoundTag pTag = damageTag.getCompound(key);
                rm.playerMoonbound.put(playerUUID, pTag.getBoolean("moonBound"));
            }

        }
        return  rm;
    }

    public void setMoonbound(UUID player, boolean mb)
    {
        boolean oldDamage = playerMoonbound.computeIfAbsent(player, (id) -> false);
        playerMoonbound.replace(player, mb);
        this.setDirty();
    }
    public boolean getMoonbound(UUID player)
    {
        return playerMoonbound.getOrDefault(player, false);
    }
}
