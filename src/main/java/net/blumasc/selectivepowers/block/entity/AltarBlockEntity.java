package net.blumasc.selectivepowers.block.entity;

import net.blumasc.selectivepowers.PowerManager;
import net.blumasc.selectivepowers.entity.SelectivepowersEntities;
import net.blumasc.selectivepowers.entity.custom.YellowKingBossEntity;
import net.blumasc.selectivepowers.item.SelectivepowersItems;
import net.blumasc.selectivepowers.recipe.AltarRecipe;
import net.blumasc.selectivepowers.recipe.AltarRecipeInput;
import net.blumasc.selectivepowers.recipe.SelectivePowersRecipes;
import net.blumasc.selectivepowers.sound.SelectivepowersSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;
import team.lodestar.lodestone.registry.common.particle.LodestoneParticleTypes;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.data.spin.SpinParticleData;
import team.lodestar.lodestone.systems.particle.world.type.LodestoneWorldParticleType;

import java.awt.*;
import java.util.Optional;
import java.util.function.Supplier;

public class AltarBlockEntity extends BlockEntity {

    public final ItemStackHandler inventory = new ItemStackHandler(5){
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
    private int maxProgress = 72;
    private boolean succesfullCraft = false;
    public static Item sunCatalyst = SelectivepowersItems.SUN_SACRIFICE.get();
    public static Item moonCatalyst = SelectivepowersItems.MOON_SACRIFICE.get();



    public AltarBlockEntity(BlockPos pos, BlockState blockState) {
        super(SelectivepowersBlockEntities.ALTAR_BE.get(), pos, blockState);
    }

    public float getRenderingRotation(){
        rotation += 0.5f;
        if(rotation>=360){
            rotation=0;
        }
        return rotation;
    }

    public boolean isCatalyst(ItemStack stack) {
        return stack.is(sunCatalyst) || stack.is(moonCatalyst);
    }

    public void clearContents(){
        inventory.setStackInSlot(0, ItemStack.EMPTY);
        inventory.setStackInSlot(1, ItemStack.EMPTY);
        inventory.setStackInSlot(2, ItemStack.EMPTY);
        inventory.setStackInSlot(3, ItemStack.EMPTY);
        inventory.setStackInSlot(4, ItemStack.EMPTY);
    }

    public void drops(){
        SimpleContainer inv = new SimpleContainer(inventory.getSlots());
        for(int i =0; i<inventory.getSlots(); i++){
            inv.setItem(i, inventory.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inv);
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
        tag.putInt("altar.progress",progress);
        tag.putInt("altar.max_progress",maxProgress);
        tag.putBoolean("altar.just_crafted", succesfullCraft);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        inventory.deserializeNBT(registries, tag.getCompound("inventory"));
        progress = tag.getInt("altar.progress");
        maxProgress = tag.getInt("altar.max_progress");
        succesfullCraft = tag.getBoolean("altar.just_crafted");
    }

    public void tick(Level level, BlockPos blockPos, BlockState blockState){
        if (level.isClientSide) {
            if(succesfullCraft){
                generateSuccessParticles(level, blockPos);
            }
            if (progress>=1) {
                generateCraftingParticles(level, blockPos);
            }
        }else {
            if (succesfullCraft) {
                succesfullCraft = false;
                setChanged();
                level.sendBlockUpdated(blockPos, blockState, blockState, 3);
            }
            if (hasCorrectRecipe()) {
                increaseCraftingProgress();
                setChanged(level, blockPos, blockState);
                level.sendBlockUpdated(blockPos, blockState, blockState, 3);
                if (hasCraftingFinished()) {
                    finishCraft(level, blockPos);
                    resetProgress();
                    level.playSound(null, blockPos, SelectivepowersSounds.MAGICAL_SUCCESS.get(), SoundSource.BLOCKS);
                    succesfullCraft = true;
                }

            } else {
                resetProgress();
                setChanged(level, blockPos, blockState);
                level.sendBlockUpdated(blockPos, blockState, blockState, 3);
            }
        }
    }

    private void finishCraft(Level level, BlockPos blockPos) {
        Optional<RecipeHolder<AltarRecipe>> recipe = getCurrentRecipe();
        if(recipe.isEmpty()){
            return;
        }
        if(level instanceof ServerLevel sl){
            ItemStack catalyst = inventory.getStackInSlot(1);
            if (catalyst.is(sunCatalyst)) {
                PowerManager pm = PowerManager.get(sl);
                PowerManager.PlayerProgress progress = pm.getProgress(pm.getPlayerWithPower(PowerManager.YELLOW_POWER));
                progress.ascensionCounter++;
                if(progress.ascensionCounter>32)
                {
                    pm.upgradePower(PowerManager.YELLOW_POWER);
                    sl.getServer().getPlayerList().broadcastSystemMessage(Component.translatable("selectivepowers.messages.yellow_ascended"), false);
                }
                pm.setDirty();
            }else{
                PowerManager pm = PowerManager.get(sl);
                PowerManager.PlayerProgress progress = pm.getProgress(pm.getPlayerWithPower(PowerManager.MOON_POWER));
                progress.ascensionCounter++;
                if(progress.ascensionCounter>32)
                {
                    pm.upgradePower(PowerManager.MOON_POWER);
                    sl.getServer().getPlayerList().broadcastSystemMessage(Component.translatable("selectivepowers.messages.moon_ascended"), false);
                }
                pm.setDirty();
            }
        }
        clearContents();
        ItemStack output = recipe.get().value().output();
        if(output.getItem() instanceof DeferredSpawnEggItem spawnItem)
        {
            summonOutputEntity(level, blockPos, spawnItem, output);
        }else {
            craftItem(output);
        }

    }

    private void summonOutputEntity(Level level, BlockPos blockPos, DeferredSpawnEggItem spawnItem, ItemStack stack) {
        double x = blockPos.getX() + 0.5;
        double y = blockPos.getY() + 1.1;
        double z = blockPos.getZ() + 0.5;

        EntityType<?> entity = spawnItem.getType(stack);

        if (entity != null) {
            Entity sentity = entity.create(level);

            if (sentity != null) {
                sentity.setPos(x, y, z);
                level.addFreshEntity(sentity);
            }
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


    private void craftItem(ItemStack output) {
        inventory.setStackInSlot(0,output);
    }

    private void resetProgress() {
        progress = 0;
        setChanged();
    }


    private boolean hasCraftingFinished() {
        return progress>=maxProgress;
    }

    private void increaseCraftingProgress() {
        progress++;
    }

    private void generateCraftingParticles(Level level, BlockPos blockPos) {
        RandomSource random = level.getRandom();

        double cx = blockPos.getX() + 0.5;
        double cy = blockPos.getY() + 1.1;
        double cz = blockPos.getZ() + 0.5;

        double[][] corners = {
                {0.1, 0.1},
                {0.9, 0.1},
                {0.1, 0.9},
                {0.9, 0.9}
        };
        int[] slots = {1, 2, 3, 4};

        for (int i = 0; i < corners.length; i++) {
            double[] corner = corners[i];
            double sx = blockPos.getX() + corner[0];
            double sy = blockPos.getY() + 1.05;
            double sz = blockPos.getZ() + corner[1];

            double motionX = (cx - sx) * 0.05 + random.nextGaussian() * 0.005;
            double motionY = 0.02 + random.nextGaussian() * 0.005;
            double motionZ = (cz - sz) * 0.05 + random.nextGaussian() * 0.005;

            ItemStack stack = inventory.getStackInSlot(slots[i]);
            Color startColor = getItemColor(stack);
            Color endColor = getItemTargetColor(stack);

            spawnParticle(level, sx, sy, sz, startColor, endColor, motionX, motionY, motionZ);
        }
    }

    private Color getItemColor(ItemStack stack) {
        if (stack.isEmpty()) return Color.GRAY;

        if (stack.is(sunCatalyst)) return Color.YELLOW;
        if (stack.is(moonCatalyst)) return Color.BLUE;

        return Color.WHITE;
    }

    private Color getItemTargetColor(ItemStack stack) {
        if (stack.isEmpty()) return Color.GRAY;

        if (stack.is(sunCatalyst)) return Color.WHITE;
        if (stack.is(moonCatalyst)) return new Color(180, 80, 255);

        return Color.WHITE;
    }



    private boolean hasCorrectRecipe() {
        ItemStack catalyst = inventory.getStackInSlot(1);
        boolean isNight = !level.isDay();

        if (isNight && catalyst.is(sunCatalyst)) {
            return false;
        }
        if (!isNight && catalyst.is(moonCatalyst)) {
            return false;
        }
        Optional<RecipeHolder<AltarRecipe>> recipe = getCurrentRecipe();
        if(recipe.isEmpty()){
            return false;
        }
        recipe.get().value().output();
        return true;
    }

    private Optional<RecipeHolder<AltarRecipe>> getCurrentRecipe() {
        return this.level.getRecipeManager()
                .getRecipeFor(SelectivePowersRecipes.ALTAR_TYPE.get(), new AltarRecipeInput(inventory.getStackInSlot(0),inventory.getStackInSlot(1),inventory.getStackInSlot(2),inventory.getStackInSlot(3),inventory.getStackInSlot(4)), level);
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
}
