package net.blumasc.selectivepowers.managers;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.UUID;

public class CelestialBeamManager extends SavedData {
    public UUID beacon;
    public static final String DATA_NAME = "celestial_beam_tracker";

    public CelestialBeamManager(){}

    @Override
    public CompoundTag save(CompoundTag compoundTag, HolderLookup.Provider provider) {
        compoundTag.putUUID("celestial_beam",beacon);
        return compoundTag;
    }

    public static CelestialBeamManager load(CompoundTag tag, HolderLookup.Provider lookupProvider){
        CelestialBeamManager cbm = new CelestialBeamManager();
        cbm.beacon = tag.getUUID("celestial_beam");
        return cbm;
    }

    public static CelestialBeamManager create()
    {
        return new CelestialBeamManager();
    }

    public static CelestialBeamManager get(ServerLevel world) {
        ServerLevel overworld = world.getServer().overworld();
        return overworld.getDataStorage().computeIfAbsent(new Factory<>(CelestialBeamManager::create, CelestialBeamManager::load), DATA_NAME);
    }
}
