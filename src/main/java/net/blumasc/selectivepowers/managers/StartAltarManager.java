package net.blumasc.selectivepowers.managers;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StartAltarManager extends SavedData {

    private  Boolean altarGenerated = false;

    public static final String DATA_NAME = "altar_generated";
    @Override
    public CompoundTag save(CompoundTag compoundTag, HolderLookup.Provider provider) {
        compoundTag.putBoolean("altarGenerated", altarGenerated);
        return compoundTag;
    }

    public static StartAltarManager create(){
        return new StartAltarManager();
    }

    public static StartAltarManager get(ServerLevel world) {
        ServerLevel overworld = world.getServer().overworld();
        return overworld.getDataStorage().computeIfAbsent(new Factory<>(StartAltarManager::create, StartAltarManager::load), DATA_NAME);
    }

    public static StartAltarManager load(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        StartAltarManager rm = StartAltarManager.create();
        if(tag.contains("altarGenerated")) {
            rm.altarGenerated = tag.getBoolean("altarGenerated");

        }
        return  rm;
    }

    public boolean isGenerated() {
        return altarGenerated;
    }

    public void setGenerated() {
        this.altarGenerated = true;
        setDirty();
    }
}
