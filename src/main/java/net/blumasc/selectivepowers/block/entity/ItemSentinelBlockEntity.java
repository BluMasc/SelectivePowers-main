package net.blumasc.selectivepowers.block.entity;

import net.blumasc.selectivepowers.block.custom.ItemSentinelBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;

import java.util.*;

public class ItemSentinelBlockEntity extends BaseContainerBlockEntity {

    private float rotation;
    public float getRenderingRotation(){
        rotation += 0.5f;
        if(rotation>=360){
            rotation=0;
        }
        return rotation;
    }

    private final SimpleContainer inventory = new SimpleContainer(1) {
        @Override
        public void setChanged() {
            super.setChanged();
            if (level != null && !level.isClientSide) {
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
            }
        }
    };



    private int signalStrength = 0;

    public ItemSentinelBlockEntity(BlockPos pos, BlockState state) {
        super(SelectivepowersBlockEntities.ITEM_SENTINEL_BE.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state,
                            ItemSentinelBlockEntity be) {
        if (level.isClientSide) return;

        int newStrength = be.calculateSignal(level, state);
        if (newStrength != be.signalStrength) {
            be.signalStrength = newStrength;
            boolean powered = newStrength > 0;
            level.setBlock(pos, state.setValue(ItemSentinelBlock.POWERED, powered), 3);
            level.updateNeighborsAt(pos, state.getBlock());
            Direction back = state.getValue(ItemSentinelBlock.FACING).getOpposite();
            level.updateNeighborsAt(pos.relative(back), state.getBlock());
        }
    }

    private int calculateSignal(Level level, BlockState state) {
        ItemStack filter = inventory.getItem(0);
        if (filter.isEmpty()) return 0;

        Direction front = state.getValue(ItemSentinelBlock.FACING);
        BlockPos frontPos = worldPosition.relative(front);

        int inventorySignal = checkFrontInventory(level, frontPos, filter, front);

        int entitySignal = checkItemEntities(level, frontPos, filter);

        return Math.max(inventorySignal, entitySignal);
    }

    private int checkFrontInventory(Level level, BlockPos frontPos,
                                     ItemStack filter, Direction from) {
        BlockEntity frontBe = level.getBlockEntity(frontPos);
        if (frontBe == null) return 0;

        IItemHandler handler = level.getCapability(
                Capabilities.ItemHandler.BLOCK,
                frontPos,
                from.getOpposite()
        );
        if (handler == null) return 0;

        int matchCount = 0;
        int totalCount = 0;

        for (int i = 0; i < handler.getSlots(); i++) {
            ItemStack stack = handler.getStackInSlot(i);
            if (stack.isEmpty()) continue;
            totalCount += stack.getCount();
            if (matches(filter, stack)) matchCount += stack.getCount();
        }

        if (matchCount == 0 || totalCount == 0) return 0;

        return Math.max(1, (int)((float) matchCount / totalCount * 14) + 1);
    }

    private int checkItemEntities(Level level, BlockPos frontPos, ItemStack filter) {
        AABB searchBox = new AABB(frontPos).inflate(0.5);
        List<ItemEntity> entities = level.getEntitiesOfClass(ItemEntity.class, searchBox);

        int matchCount = entities.stream()
                .filter(e -> matches(filter, e.getItem()))
                .mapToInt(e -> e.getItem().getCount())
                .sum();

        if (matchCount == 0) return 0;
        return Math.min(15, matchCount);
    }

    private boolean matches(ItemStack filter, ItemStack candidate) {
        return ItemStack.isSameItem(filter, candidate);
    }

    public int getSignalStrength() { return signalStrength; }

    @Override
    public int getContainerSize() { return 1; }

    @Override
    public boolean isEmpty() { return inventory.isEmpty(); }

    @Override
    public ItemStack getItem(int slot) { return inventory.getItem(slot); }

    @Override
    public ItemStack removeItem(int slot, int amount) { return inventory.removeItem(slot, amount); }

    @Override
    public ItemStack removeItemNoUpdate(int slot) { return inventory.removeItemNoUpdate(slot); }

    @Override
    public void setItem(int slot, ItemStack stack) { inventory.setItem(slot, stack); }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    public void clearContent() { inventory.clearContent(); }

    @Override
    protected AbstractContainerMenu createMenu(int i, Inventory inventory) {
        return null;
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.selectivepowers.item_sentinel");
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        NonNullList<ItemStack> items = NonNullList.withSize(1, ItemStack.EMPTY);
        items.set(0, inventory.getItem(0));
        return items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        inventory.setItem(0, items.get(0));
    }

    @Override
    public void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        ContainerHelper.saveAllItems(tag, getItems(), provider);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        NonNullList<ItemStack> items = NonNullList.withSize(1, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, items, provider);
        setItems(items);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        CompoundTag tag = super.getUpdateTag(provider);
        ContainerHelper.saveAllItems(tag, getItems(), provider); // ← add this
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider provider) {
        super.handleUpdateTag(tag, provider);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}