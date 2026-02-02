package net.blumasc.selectivepowers;

import net.blumasc.selectivepowers.item.SelectivepowersItems;
import net.blumasc.selectivepowers.network.PowerInfoSyncPacket;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.saveddata.SavedData;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class PowerManager extends SavedData {

    public static Collection<String> getPowerNames() {
        return POWERS;
    }

    public enum PowerLevel {
        FREE,
        BOUND,
        AWOKEN,
        ASCENDED
    }


    public static class PowerAssignment {
        public UUID owner;
        public PowerLevel level;
        public boolean devoured;

        public PowerAssignment(UUID owner, PowerLevel level, boolean dev) {
            this.owner = owner;
            this.level = level;
            this.devoured = dev;
        }
    }

    public static class PlayerProgress {
        public UUID playerUUID;

        // Timers
        public int lookAtTameableTimer = 0;
        public int minedOres = 0;
        public int darknessTimer = 0;
        public int skylightTimer = 0;

        public int abilityTimer = 0;
        public int ultTimer = 0;

        // Counters
        public int booksCrafted = 0;
        public int ascensionCounter = 0;

        // Flags
        public boolean enteredLava = false;
        public boolean enteredPowderedSnow = false;

        public PlayerProgress(UUID uuid) {
            this.playerUUID = uuid;
        }
    }

    private final Map<UUID, PlayerProgress> playerProgress = new HashMap<>();


    public final Map<String, PowerAssignment> powerAssignments = new HashMap<>();

    public int yellowLives = 3;

    public static final String DATA_NAME = "unique_powers";

    public static final String FORREST_POWER = "forest";
    public static final String ROCK_POWER = "rock";
    public static final String MUSHROOM_POWER = "mushroom";
    public static final String DRAGON_POWER = "dragon";
    public static final String ELEMENTAL_POWER = "elemental";
    public static final String STORM_POWER = "storm";
    public static final String RAGE_POWER = "rage";
    public static final String DARK_POWER = "dark";
    public static final String ANIMAL_POWER = "animal";
    public static final String TRUTH_POWER = "truth";
    public static final String LIGHT_POWER = "light";
    public static final String YELLOW_POWER = "yellow";
    public static final String MOON_POWER = "moon";

    public static final String NO_POWER = "none";

    public static final List<String> POWERS = List.of(
            FORREST_POWER,
            ROCK_POWER,
            MUSHROOM_POWER,
            DRAGON_POWER,
            ELEMENTAL_POWER,
            STORM_POWER,
            RAGE_POWER,
            DARK_POWER,
            ANIMAL_POWER,
            TRUTH_POWER,
            LIGHT_POWER,
            YELLOW_POWER,
            MOON_POWER
            );

    public static PowerManager create(){
        return new PowerManager();
    }

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag tag, HolderLookup.Provider provider) {
        for (Map.Entry<String, PowerAssignment> entry : powerAssignments.entrySet()) {
            CompoundTag data = new CompoundTag();
            if(entry.getValue().owner != null) {
                data.putUUID("Owner", entry.getValue().owner);
            }
            data.putString("Level", entry.getValue().level.name());
            data.putBoolean("Devoured", entry.getValue().devoured);
            tag.put(entry.getKey(), data);
        }
        CompoundTag progressTag = new CompoundTag();
        for (Map.Entry<UUID, PlayerProgress> entry : playerProgress.entrySet()) {
            if(entry.getKey() == null) continue;
            CompoundTag pTag = new CompoundTag();
            PlayerProgress p = entry.getValue();
            pTag.putInt("lookAtTameableTimer", p.lookAtTameableTimer);
            pTag.putInt("lookAtEldritchTimer", p.minedOres);
            pTag.putInt("darknessTimer", p.darknessTimer);
            pTag.putInt("skylightTimer", p.skylightTimer);
            pTag.putInt("ascensionCounter", p.ascensionCounter);
            pTag.putInt("booksCrafted", p.booksCrafted);
            pTag.putBoolean("enteredLava", p.enteredLava);
            pTag.putBoolean("enteredPowderedSnow", p.enteredPowderedSnow);
            pTag.putInt("abilityTimer", p.abilityTimer);
            pTag.putInt("ultTimer", p.ultTimer);

            progressTag.put(entry.getKey().toString(), pTag);
        }
        tag.put("playerProgress", progressTag);
        tag.putInt("yellowLives", yellowLives);
        return tag;
    }

    public static PowerManager get(ServerLevel world) {
        ServerLevel overworld = world.getServer().overworld();
        return overworld.getDataStorage().computeIfAbsent(new Factory<>(PowerManager::create, PowerManager::load), DATA_NAME);
    }

    public static PowerManager load(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        PowerManager pm = PowerManager.create();
        for (String power : POWERS) {
            if (tag.contains(power)) {
                CompoundTag data = tag.getCompound(power);
                UUID owner = null;
                if(data.contains("Owner")) {
                    owner = data.getUUID("Owner");
                }
                PowerLevel level = PowerLevel.valueOf(data.getString("Level"));
                boolean devoured = data.getBoolean("Devoured");
                pm.powerAssignments.put(power, new PowerAssignment(owner, level, devoured));
            }
        }
        if(tag.contains("playerProgress")) {
            CompoundTag progressTag = tag.getCompound("playerProgress");

            for(String key : progressTag.getAllKeys()) {
                UUID playerUUID = UUID.fromString(key);
                CompoundTag pTag = progressTag.getCompound(key);

                PlayerProgress p = new PlayerProgress(playerUUID);
                p.lookAtTameableTimer = pTag.getInt("lookAtTameableTimer");
                p.minedOres = pTag.getInt("lookAtEldritchTimer");
                p.darknessTimer = pTag.getInt("darknessTimer");
                p.skylightTimer = pTag.getInt("skylightTimer");
                p.ascensionCounter = pTag.getInt("ascensionCounter");
                p.booksCrafted = pTag.getInt("booksCrafted");
                p.enteredLava = pTag.getBoolean("enteredLava");
                p.enteredPowderedSnow = pTag.getBoolean("enteredPowderedSnow");
                p.abilityTimer = pTag.getInt("abilityTimer");
                p.ultTimer = pTag.getInt("ultTimer");

                pm.playerProgress.put(playerUUID, p);
            }
        }
        if (tag.contains("yellowLives")) {
            pm.yellowLives = tag.getInt("yellowLives");
        }

        return pm;
    }


    public boolean isPowerNotTaken(String power) {

        if(!powerAssignments.containsKey(power)) return true;
        PowerAssignment p = powerAssignments.get(power);
        return p.owner == null;
    }

    public void assignPower(String power, Player player) {
        if (isPowerNotTaken(power)) {
            powerAssignments.put(power, new PowerAssignment(player.getUUID(), PowerLevel.BOUND, false));
            player.addItem(new ItemStack(SelectivepowersItems.BOUND_CONTRACT.get()));
            setDirty();
        }
    }

    public void takePower(String power)
    {
        PowerAssignment p = powerAssignments.get(power);
        if (p == null) return;
        p.devoured = true;
        p.owner = null;
        setDirty();
    }

    public boolean isPowerDevoured(String power)
    {
        PowerAssignment p = powerAssignments.get(power);
        if (p == null) return false;
        return p.devoured;
    }

    public boolean isPowerFree(String power)
    {
        return (isPowerNotTaken(power)) && (!isPowerDevoured(power));
    }

    public void upgradePower(String power) {
        PowerAssignment p = powerAssignments.get(power);
        if (p == null) return;

        switch (p.level) {
            case BOUND -> p.level = PowerLevel.AWOKEN;
            case AWOKEN -> p.level = PowerLevel.ASCENDED;
            case ASCENDED -> { return; }  // already max
        }

        setDirty();
    }

    public void downgradePower(String power) {
        PowerAssignment p = powerAssignments.get(power);
        if (p == null) return;

        switch (p.level) {
            case BOUND -> { return; }
            case AWOKEN -> p.level = PowerLevel.BOUND;
            case ASCENDED -> p.level = PowerLevel.AWOKEN;
        }

        setDirty();
    }

    public UUID getPlayerWithPower(String power) {
        PowerAssignment assignment = powerAssignments.get(power);
        return assignment != null ? assignment.owner : null;
    }

    public boolean doesPlayerHaveAnyPower(UUID playerUUID) {
        for (PowerAssignment assignment : powerAssignments.values()) {
                if (assignment.owner != null) {
                    if (assignment.owner.equals(playerUUID)) {
                        return true;
                    }
                }
        }
        return false;
    }

    public String getPowerOfPlayer(UUID playerUUID) {
        for (Map.Entry<String, PowerAssignment> entry : powerAssignments.entrySet()) {
            if(entry.getValue()!=null) {
                if(entry.getValue().owner!=null) {
                    if (entry.getValue().owner.equals(playerUUID)) {
                        return entry.getKey();
                    }
                }
            }
        }
        return NO_POWER;
    }

    public PowerLevel getPowerLevelOfPlayer(UUID playerUUID) {
        for (PowerAssignment assignment : powerAssignments.values()) {
            if (assignment.owner!=null  && assignment.owner.equals(playerUUID)) {
                return assignment.level;
            }
        }
        return PowerLevel.FREE;
    }

    public PlayerProgress getProgress(UUID playerUUID) {
        return playerProgress.computeIfAbsent(playerUUID, PlayerProgress::new);
    }

    public void removeProgress(UUID playerUUID) {
        playerProgress.remove(playerUUID);
        setDirty();
    }

    public int countAtLeastAwokenAbilities() {
        int count = 0;

        for (PowerAssignment assignment : powerAssignments.values()) {
            if (assignment.level == PowerLevel.AWOKEN || assignment.level == PowerLevel.ASCENDED) {
                count++;
            }
        }

        return count;
    }
    public void syncToAll(ServerLevel l)
    {
        l.getServer().getPlayerList().getPlayers()
                .forEach(this::syncTo);

    }
    public void syncTo(ServerPlayer player) {
        UUID moon = getPlayerWithPower(MOON_POWER);
        UUID yellow = getPlayerWithPower(YELLOW_POWER);
        UUID dark = getPlayerWithPower(DARK_POWER);
        UUID truth = getPlayerWithPower(TRUTH_POWER);
        UUID dragon = getPlayerWithPower(DRAGON_POWER);

        PowerLevel truthLevel = PowerLevel.FREE;
        if (truth != null && powerAssignments.containsKey(TRUTH_POWER)) {
            truthLevel = powerAssignments.get(TRUTH_POWER).level;
        }

        PacketDistributor.sendToPlayer(
                player,
                new PowerInfoSyncPacket(moon, yellow, dark, dragon, truth,  truthLevel)
        );
    }
}

