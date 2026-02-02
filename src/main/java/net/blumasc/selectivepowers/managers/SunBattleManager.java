package net.blumasc.selectivepowers.managers;

import net.blumasc.selectivepowers.worldgen.ModDimensions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

public class SunBattleManager extends SavedData {
    UUID player1,player2;
    boolean started = false;
    public static final String DATA_NAME = "sun_battle";
    @Override
    public CompoundTag save(CompoundTag compoundTag, HolderLookup.Provider provider) {
        if(player1!=null) {
            compoundTag.putString("player1", player1.toString());
        }
        if(player2!=null) {
            compoundTag.putString("player2", player2.toString());
        }
        compoundTag.putBoolean("started",started);
        return compoundTag;
    }

    public static SunBattleManager create(){
        return new SunBattleManager();
    }

    public static SunBattleManager get(ServerLevel world) {
        ServerLevel overworld = world.getServer().overworld();
        return overworld.getDataStorage().computeIfAbsent(new Factory<>(SunBattleManager::create, SunBattleManager::load), DATA_NAME);
    }

    public static SunBattleManager load(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        SunBattleManager sm = SunBattleManager.create();
        if(tag.contains("player1")) {
            sm.player1 = UUID.fromString(tag.getString("player1"));
        }
        if(tag.contains("player2")) {
            sm.player2 = UUID.fromString(tag.getString("player2"));
        }
        if(tag.contains("started")) {
            sm.started = tag.getBoolean("started");
        }
        return  sm;
    }
    public void startBattle(Player p1, Player p2, ServerLevel sl)
    {
        player1=p1.getUUID();
        player2=p2.getUUID();
        started = true;
        spawnAndTeleport(sl, (ServerPlayer) p1, (ServerPlayer) p2);
        setDirty();
    }
    public void endBattle(ServerLevel sl)
    {
        started=false;
        moveHome(sl);
        player1=null;
        player2=null;
        setDirty();
    }

    private void moveHome(ServerLevel sl) {
        ServerLevel targetLevel = sl.getServer().getLevel(ModDimensions.SOLAR_DIM_LEVEL);
        ServerPlayer serverPlayer1 = sl.getServer().getPlayerList().getPlayer(player1);
        if(serverPlayer1 != null) {
            if (targetLevel == null || serverPlayer1.serverLevel().dimension() == ModDimensions.SOLAR_DIM_LEVEL) {
                movePlayerHome(sl, serverPlayer1);
            }
        }
        ServerPlayer serverPlayer2 = sl.getServer().getPlayerList().getPlayer(player2);
        if(serverPlayer2!=null) {
            if (targetLevel == null || serverPlayer2.serverLevel().dimension() == ModDimensions.SOLAR_DIM_LEVEL) {
                movePlayerHome(sl, serverPlayer2);
            }
        }

    }
    private void movePlayerHome(ServerLevel sl, ServerPlayer sp){

        BlockPos respawnPos = sp.getRespawnPosition() != null
                ? sp.getRespawnPosition()
                : sl.getSharedSpawnPos();

        ServerLevel respawnLevel = sl.getServer().getLevel(
                sp.getRespawnDimension() != null ? sp.getRespawnDimension() : Level.OVERWORLD
        );

        if (respawnLevel != null && respawnPos != null) {
            sp.teleportTo(respawnLevel,
                    respawnPos.getX() + 0.5,
                    respawnPos.getY() + 0.5,
                    respawnPos.getZ() + 0.5,
                    sp.getYRot(),
                    sp.getXRot());
        }
    }

    public boolean PlayerInBattle(Player p)
    {
        return p.getUUID().equals(player1) || p.getUUID().equals(player2);
    }
    public static void spawnAndTeleport(
            ServerLevel level,
            ServerPlayer p1,
            ServerPlayer p2
    ) {
        ServerLevel targetLevel = level.getServer().getLevel(ModDimensions.SOLAR_DIM_LEVEL);
        StructureTemplateManager manager = level.getStructureManager();

        StructureTemplate template =
                level.getStructureManager()
                        .getOrCreate(ResourceLocation.fromNamespaceAndPath(
                                "selectivepowers", "solar_arena"
                        ));
        if (template == null) return;

        BlockPos origin = BlockPos.ZERO;

        template.placeInWorld(
                targetLevel,
                origin,
                origin,
                new StructurePlaceSettings(),
                RandomSource.create(),
                3
        );
        BlockPos tp1 = origin.offset(18, 4, 4);
        BlockPos tp2 = origin.offset(18, 4, 32);

        teleport(p1, tp2.offset(0,2,0).getCenter(),targetLevel, tp1);
        teleport(p2, tp1.offset(0,2,0).getCenter(),targetLevel, tp2);
    }

    public static void teleport(ServerPlayer player, Vec3 target, ServerLevel level, BlockPos pos) {
        if(player == null)return;
        Vec3 diff = target.subtract(pos.getBottomCenter().add(0, player.getEyeHeight(), 0));

        double dx = diff.x();
        double dy = diff.y();
        double dz = diff.z();

        float yaw = (float) Math.toDegrees(Math.atan2(-dx, dz));
        float pitch = (float) Math.toDegrees(-Math.atan2(dy, Math.sqrt(dx*dx + dz*dz)));

        player.teleportTo(
                level,
                pos.getX() + 0.5,
                pos.getY(),
                pos.getZ() + 0.5,
                yaw,
                pitch
        );
    }
}
