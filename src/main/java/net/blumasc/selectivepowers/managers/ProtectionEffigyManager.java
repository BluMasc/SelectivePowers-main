package net.blumasc.selectivepowers.managers;

import net.blumasc.selectivepowers.SelectivePowers;
import net.blumasc.selectivepowers.block.entity.ProtectionEffigyBlockEntity;
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
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForgeMod;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ProtectionEffigyManager extends SavedData {

    private final Set<BlockPos> effigys = new HashSet<>();

    private static final int MAX_DISTANCE = ProtectionEffigyBlockEntity.PROTECTION_RADIUS;

    public static final String DATA_NAME = "protection_effigys";

    public ProtectionEffigyManager() { }

    @Override
    public CompoundTag save(CompoundTag compoundTag, HolderLookup.Provider provider) {
        ListTag effigyList = new ListTag();
        for (BlockPos pos : effigys) {
            effigyList.add(LongTag.valueOf(pos.asLong()));
        }
        compoundTag.put("effigys", effigyList);
        return compoundTag;
    }

    public static ProtectionEffigyManager load(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        ProtectionEffigyManager manager = new ProtectionEffigyManager();
        if (tag.contains("effigys")) {
            ListTag effigyList = tag.getList("effigys", 4);
            for (int i = 0; i < effigyList.size(); i++) {
                Tag t = effigyList.get(i);
                if (t instanceof LongTag lt) {
                    manager.effigys.add(BlockPos.of(lt.getAsLong()));
                }
            }
        }
        return manager;
    }
    public static ProtectionEffigyManager create() {
        return new ProtectionEffigyManager();
    }

    public static ProtectionEffigyManager get(ServerLevel world) {
        return world.getDataStorage().computeIfAbsent(new Factory<>(ProtectionEffigyManager::create, ProtectionEffigyManager::load), DATA_NAME);
    }
    public void addEffigy(BlockPos pos) {
        effigys.add(pos);
        this.setDirty();
    }

    public void removeEffigy(BlockPos pos) {
        effigys.remove(pos);
        this.setDirty();
    }

    public boolean isPositionAffected(ServerLevel level, Vec3 pos) {
        Iterator<BlockPos> it = effigys.iterator();

        while (it.hasNext()) {
            BlockPos effigy = it.next();

            BlockEntity be = level.getBlockEntity(effigy);
            if (!(be instanceof ProtectionEffigyBlockEntity)) {
                it.remove();
                setDirty();
                continue;
            }

            double dx = pos.x - (effigy.getX() + 0.5);
            double dy = pos.y - (effigy.getY() + 0.5);
            double dz = pos.z - (effigy.getZ() + 0.5);

            if (dx * dx + dy * dy + dz * dz < MAX_DISTANCE * MAX_DISTANCE) {
                return true;
            }
        }
        return false;
    }
}
