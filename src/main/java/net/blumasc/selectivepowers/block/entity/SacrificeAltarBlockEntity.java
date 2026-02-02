package net.blumasc.selectivepowers.block.entity;

import net.blumasc.selectivepowers.PowerManager;
import net.blumasc.selectivepowers.datagen.ModChestLoot;
import net.blumasc.selectivepowers.item.SelectivepowersItems;
import net.blumasc.selectivepowers.recipe.AltarRecipe;
import net.blumasc.selectivepowers.recipe.AltarRecipeInput;
import net.blumasc.selectivepowers.recipe.SelectivePowersRecipes;
import net.blumasc.selectivepowers.sound.SelectivepowersSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;
import team.lodestar.lodestone.registry.common.particle.LodestoneParticleTypes;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.data.spin.SpinParticleData;

import java.awt.*;
import java.util.List;
import java.util.Optional;

public class SacrificeAltarBlockEntity extends BlockEntity {

    public final ItemStackHandler inventory = new ItemStackHandler(1){
        @Override
        protected int getStackLimit(int slot, ItemStack stack) {
            return 1;
        }

        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if(!level.isClientSide()){
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };

    private float rotation;
    private int progress = 0;
    private int maxProgress = 100;
    public boolean filled = false;
    public boolean simple = true;
    public String power = PowerManager.NO_POWER;
    private boolean justCompleted = false;




    public SacrificeAltarBlockEntity(BlockPos pos, BlockState blockState) {
        super(SelectivepowersBlockEntities.SAC_ALTAR_BE.get(), pos, blockState);
    }

    public float getRenderingRotation(){
        rotation += 0.5f;
        if(rotation>=360){
            rotation=0;
        }
        return rotation;
    }

    public void dropItem(){
        SimpleContainer inv = new SimpleContainer(inventory.getSlots());
        for(int i =0; i<inventory.getSlots(); i++){
            inv.setItem(i, inventory.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition.above(), inv);
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("inventory", inventory.serializeNBT(registries));
        tag.putInt("sacaltar.progress",progress);
        tag.putInt("sacaltar.max_progress",maxProgress);
        tag.putBoolean("sacaltar.filled",filled);
        tag.putBoolean("sacaltar.simple",simple);
        tag.putString("sacaltar.power",power);
        tag.putBoolean("sacaltar.just_completed", justCompleted);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        inventory.deserializeNBT(registries, tag.getCompound("inventory"));
        progress = tag.getInt("sacaltar.progress");
        maxProgress = tag.getInt("sacaltar.max_progress");
        filled = tag.getBoolean("sacaltar.filled");
        simple = tag.getBoolean("sacaltar.simple");
        power = tag.getString("sacaltar.power");
        justCompleted = tag.getBoolean("sacaltar.just_completed");
    }

    private LootTable darkPowerTable;
    private LootTable lightPowerTable;
    private LootTable animalPowerTable;
    private LootTable plantPowerTable;
    private LootTable mushroomPowerTable;
    private LootTable stormPowerTable;
    private LootTable elementalPowerTable;
    private LootTable miningPowerTable;
    private LootTable dragonPowerTable;
    private LootTable ragePowerTable;
    private LootTable truthPowerTable;
    private LootTable yellowPowerTable;
    private LootTable moonPowerTable;
    private LootTable oldAltarTable;
    private LootTable freshAltarTable;
    private boolean lootCached = false;

    public void cacheLootTables(Level level) {
        var registries = level.getServer().reloadableRegistries();
        darkPowerTable = registries.getLootTable(ModChestLoot.DARK_POWER_LOOT_TABLE);
        lightPowerTable = registries.getLootTable(ModChestLoot.LIGHT_POWER_LOOT_TABLE);
        animalPowerTable = registries.getLootTable(ModChestLoot.ANIMAL_POWER_LOOT_TABLE);
        plantPowerTable = registries.getLootTable(ModChestLoot.PLANT_POWER_LOOT_TABLE);
        mushroomPowerTable = registries.getLootTable(ModChestLoot.MUSHROOM_POWER_LOOT_TABLE);
        stormPowerTable = registries.getLootTable(ModChestLoot.STORM_POWER_LOOT_TABLE);
        elementalPowerTable = registries.getLootTable(ModChestLoot.ELEMENTAL_POWER_LOOT_TABLE);
        miningPowerTable = registries.getLootTable(ModChestLoot.MINING_POWER_LOOT_TABLE);
        dragonPowerTable = registries.getLootTable(ModChestLoot.DRAGON_POWER_LOOT_TABLE);
        ragePowerTable = registries.getLootTable(ModChestLoot.RAGE_POWER_LOOT_TABLE);
        truthPowerTable = registries.getLootTable(ModChestLoot.TRUTH_POWER_LOOT_TABLE);
        yellowPowerTable = registries.getLootTable(ModChestLoot.YELLOW_POWER_LOOT_TABLE);
        moonPowerTable = registries.getLootTable(ModChestLoot.MOON_POWER_LOOT_TABLE);
        oldAltarTable = registries.getLootTable(ModChestLoot.OLD_ALTAR_LOOT_TABLE);
        freshAltarTable = registries.getLootTable(ModChestLoot.FRESH_ALTAR_LOOT_TABLE);
    }

    private void ensureLootCached(Level level) {
        if (!lootCached) {
            cacheLootTables(level);
            lootCached = true;
        }
    }

    public LootTable getPowerBasedLootTable() {
        if (power.equals(PowerManager.DARK_POWER)) return darkPowerTable;
        if (power.equals(PowerManager.LIGHT_POWER)) return lightPowerTable;
        if (power.equals(PowerManager.ANIMAL_POWER)) return animalPowerTable;
        if (power.equals(PowerManager.FORREST_POWER)) return plantPowerTable;
        if (power.equals(PowerManager.MUSHROOM_POWER)) return mushroomPowerTable;
        if (power.equals(PowerManager.STORM_POWER)) return stormPowerTable;
        if (power.equals(PowerManager.ELEMENTAL_POWER)) return elementalPowerTable;
        if (power.equals(PowerManager.ROCK_POWER)) return miningPowerTable;
        if (power.equals(PowerManager.DRAGON_POWER)) return dragonPowerTable;
        if (power.equals(PowerManager.RAGE_POWER)) return ragePowerTable;
        if (power.equals(PowerManager.TRUTH_POWER)) return truthPowerTable;
        if (power.equals(PowerManager.YELLOW_POWER)) return yellowPowerTable;
        if (power.equals(PowerManager.MOON_POWER)) return moonPowerTable;
        return freshAltarTable;
    }

    public void insertRandomItem(Level level) {
        ensureLootCached(level);
        LootParams params = new LootParams.Builder((ServerLevel) level)
                .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(worldPosition))
                .create(LootContextParamSets.CHEST);

        LootTable tableToUse = oldAltarTable;

        if (!simple) {
            int roll = level.random.nextInt(12);

            if (roll >= 10) {
                tableToUse = freshAltarTable;
            } else if (roll >= 5) {
                tableToUse = getPowerBasedLootTable();
            }
        }

        List<ItemStack> items = tableToUse.getRandomItems(params);

        if (!items.isEmpty()) {
            inventory.setStackInSlot(0, items.getFirst());
        }
    }


    public void tick(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide) {
            if (justCompleted) {
                generateSuccessParticles(level, pos);
            }
            return;
        }
        else if(justCompleted)
        {
            justCompleted = false;
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
        }
        if (!filled) return;
        if (progress % 5 == 0 && progress < maxProgress) {
            insertRandomItem(level);
        }
        progress++;

        if (progress >= maxProgress) {
            insertRandomItem(level);
            dropItem();
            progress = 0;
            filled = false;
            simple = true;
            power = PowerManager.NO_POWER;
            level.playSound(null, pos, SelectivepowersSounds.MAGICAL_SUCCESS.get(), SoundSource.BLOCKS);
            justCompleted = true;
            setChanged();
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
        }
    }


    private void generateSuccessParticles(Level level, BlockPos blockPos) {
        RandomSource random = level.getRandom();

        double cx = blockPos.getX() + 0.5;
        double cy = blockPos.getY() + 1.1;
        double cz = blockPos.getZ() + 0.5;

        for (int i = 0; i < 30; i++) {
            double angle = random.nextDouble() * Math.PI * 2;
            double radius = 0.2 + random.nextDouble() * 0.4;

            double x = cx + Math.cos(angle) * radius;
            double y = cy + random.nextDouble() * 0.2;
            double z = cz + Math.sin(angle) * radius;

            double motionX = Math.cos(angle) * 0.01;
            double motionY = 0.05 + random.nextDouble() * 0.03;
            double motionZ = Math.sin(angle) * 0.01;

            spawnParticle(
                    level,
                    x, y, z,
                    new Color(180, 80, 255),
                    new Color(255, 255, 255),
                    motionX, motionY, motionZ
            );
        }
    }

    public static void spawnParticle(Level level, double x, double y, double z, Color startingColor, Color endingColor, double moveX, double moveY, double moveZ) {
        if (level instanceof ServerLevel) return;
        WorldParticleBuilder.create(LodestoneParticleTypes.WISP_PARTICLE)
                    .setScaleData(GenericParticleData.create(0.2f, 0).build())
                    .setTransparencyData(GenericParticleData.create(0.75f, 0.25f).build())
                    .setColorData(ColorParticleData.create(startingColor, endingColor).setCoefficient(1.4f).setEasing(Easing.BOUNCE_IN_OUT).build())
                    .setSpinData(SpinParticleData.create(0.2f, 0.4f).setSpinOffset((level.getGameTime() * 0.2f) % 6.28f).setEasing(Easing.QUARTIC_IN).build())
                    .setLifetime(40)
                    .addMotion(moveX, moveY, moveZ)
                    .enableNoClip()
                    .spawn(level, x, y, z);
    }

    public boolean working() {
        return progress>0;
    }
}
