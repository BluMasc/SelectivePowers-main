package net.blumasc.selectivepowers.item.custom;

import net.blumasc.selectivepowers.entity.custom.AnchorEntity;
import net.blumasc.selectivepowers.item.SelectivepowersItems;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;

import java.util.UUID;

public class ThrownAnchorBladeItem extends Item {

    private static final String ANCHOR_UUID_KEY = "AnchorUUID";

    public ThrownAnchorBladeItem(Properties properties) {
        super(properties);
    }
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide && level instanceof ServerLevel sl) {

            CompoundTag tag = getTag(stack);

            if (tag.hasUUID(ANCHOR_UUID_KEY)) {
                Entity e = sl.getEntity(tag.getUUID(ANCHOR_UUID_KEY));

                if (e instanceof AnchorEntity anchor && !anchor.isReturning()) {
                    anchor.startReturn();
                }
            }
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }
    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity,
                              int slot, boolean selected) {

        if (level.isClientSide) return;
        if (!(entity instanceof ServerPlayer player)) return;
        if (!(level instanceof ServerLevel sl)) return;

        CompoundTag tag = getTag(stack);

        if (!selected) {

            if (tag.hasUUID(ANCHOR_UUID_KEY)) {
                Entity e = sl.getEntity(tag.getUUID(ANCHOR_UUID_KEY));

                e.discard();
            }

            restoreOriginal(player, stack, slot);
            return;
        }

        if (tag.hasUUID(ANCHOR_UUID_KEY)) {
            Entity e = sl.getEntity(tag.getUUID(ANCHOR_UUID_KEY));
            if (e == null) {
                restoreOriginal(player, stack, slot);
            }
        }else{
            restoreOriginal(player, stack, slot);
        }
    }
    public static void restoreOriginal(Player player, ItemStack thrown, int slot) {

        ItemStack original = new ItemStack(SelectivepowersItems.ANCHOR.get());

        original.applyComponents(thrown.getComponents());
        original.set(
                DataComponents.CUSTOM_DATA,
                thrown.get(DataComponents.CUSTOM_DATA)
        );
        player.getInventory().setItem(slot, original);
        if(!player.hasInfiniteMaterials()) {
            ItemStack particleStack = original.copy();
            original.hurtAndBreak(1, (ServerLevel) player.level(), (ServerPlayer) player, p -> {
                ServerLevel level = (ServerLevel) player.level();
                level.playSound(
                        null,
                        player.blockPosition(),
                        SoundEvents.ITEM_BREAK,
                        SoundSource.PLAYERS,
                        1.0F,
                        1.0F
                );
                ItemParticleOption particle =
                        new ItemParticleOption(ParticleTypes.ITEM, particleStack);

                level.sendParticles(
                        particle,
                        player.getX(),
                        player.getY() + 1.0,
                        player.getZ(),
                        20,
                        0.25, 0.25, 0.25,
                        0.05
                );
            });
        }
    }
    private static CompoundTag getTag(ItemStack stack) {
        return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
    }
}