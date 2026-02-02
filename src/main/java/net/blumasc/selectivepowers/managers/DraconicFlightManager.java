package net.blumasc.selectivepowers.managers;

import net.blumasc.selectivepowers.SelectivePowers;
import net.blumasc.selectivepowers.block.SelectivepowersBlocks;
import net.blumasc.selectivepowers.block.custom.DraconicBeaconBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.saveddata.SavedData;
import net.neoforged.neoforge.common.NeoForgeMod;

import java.util.*;

public class DraconicFlightManager extends SavedData {

    private final Set<BlockPos> beacons = new HashSet<>();

    private static final int MAX_DISTANCE = 20;

    public static final String DATA_NAME = "beacon_flight";

    public DraconicFlightManager() { }

    @Override
    public CompoundTag save(CompoundTag compoundTag, HolderLookup.Provider provider) {
        ListTag beaconList = new ListTag();
        for (BlockPos pos : beacons) {
            beaconList.add(LongTag.valueOf(pos.asLong()));
        }
        compoundTag.put("beacons", beaconList);
        return compoundTag;
    }

    public static DraconicFlightManager load(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        DraconicFlightManager manager = new DraconicFlightManager();
        if (tag.contains("beacons")) {
            ListTag beaconList = tag.getList("beacons", 4);
            for (int i = 0; i < beaconList.size(); i++) {
                Tag t = beaconList.get(i);
                if (t instanceof LongTag lt) {
                    manager.beacons.add(BlockPos.of(lt.getAsLong()));
                }
            }
        }
        return manager;
    }
    public static DraconicFlightManager create() {
        return new DraconicFlightManager();
    }

    public static DraconicFlightManager get(ServerLevel world) {
        return world.getDataStorage().computeIfAbsent(new Factory<>(DraconicFlightManager::create, DraconicFlightManager::load), DATA_NAME);
    }
    public void addBeacon(BlockPos pos) {
        beacons.add(pos);
        this.setDirty();
    }

    public void removeBeacon(BlockPos pos) {
        beacons.remove(pos);
        this.setDirty();
    }

    public boolean isPlayerAffected(ServerLevel level, Player player)
    {
        cleanup(level);
        for(BlockPos beacon : beacons)
        {
            double dx = player.getX() - (beacon.getX() + 0.5);
            double dy = player.getY() - (beacon.getY() + 0.5);
            double dz = player.getZ() - (beacon.getZ() + 0.5);

            double distanceSq = dx * dx + dy * dy + dz * dz;
            if(distanceSq<(MAX_DISTANCE*MAX_DISTANCE))
            {
                return true;
            }
        }
        return false;
    }

    public void cleanup(ServerLevel level) {
        Iterator<BlockPos> it = beacons.iterator();

        while (it.hasNext()) {
            BlockPos effigy = it.next();

            BlockState be = level.getBlockState(effigy);
            if (!(be.is(SelectivepowersBlocks.DRACONIC_BEACON))) {
                it.remove();
                setDirty();
            }

        }
    }

    public void playerTick(Player player, ServerLevel level) {
        boolean inRange = isPlayerAffected(level, player);

        if(!(player instanceof ServerPlayer sp))return;

        if (inRange) {
            giveFlight(sp);
        } else {
            removeFlight(sp);
        }
    }

    public static final ResourceLocation FLIGHT_MODIFIER_RESOURCE = ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "dragon_flight_modifier");
    public static final AttributeModifier FLIGHT_MODIFIER = new AttributeModifier(
            FLIGHT_MODIFIER_RESOURCE,
            1.0,
            AttributeModifier.Operation.ADD_VALUE
    );

    public void giveFlight(ServerPlayer player) {
        var attribute = player.getAttribute(NeoForgeMod.CREATIVE_FLIGHT);
        if (attribute != null && attribute.getModifier(FLIGHT_MODIFIER_RESOURCE) == null) {
            attribute.addTransientModifier(FLIGHT_MODIFIER);
        }
    }

    public void removeFlight(ServerPlayer player) {
        var attribute = player.getAttribute(NeoForgeMod.CREATIVE_FLIGHT);
        if (attribute != null) {
            attribute.removeModifier(FLIGHT_MODIFIER_RESOURCE);
        }
    }
}
