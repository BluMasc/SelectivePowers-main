package net.blumasc.selectivepowers.item.custom;

import net.blumasc.selectivepowers.component.ModDataComponentTypes;
import net.blumasc.selectivepowers.item.client.gui.ClientPageOpener;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.Arrays;
import java.util.List;

public class PageItem extends Item {
    public PageItem(Properties properties) {
        super(properties);
    }
    private int chooseRandomPage(Player player) {

       int tries = 5;
       int page = player.getRandom().nextInt(10);
        while (playerHasPage(player, page) && tries>0) {
            page = player.getRandom().nextInt(10);
            --tries;
        }

        return page;
    }
    private boolean playerHasPage(Player player, int page) {
        if(page==9) return true;
        for (ItemStack stack : player.getInventory().items) {
            if (stack.getItem() instanceof PageItem && stack.has(ModDataComponentTypes.PAGE.get())) {
                int p = stack.get(ModDataComponentTypes.PAGE.get());
                if(p == page ){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        if (stack.has(ModDataComponentTypes.PAGE.get())) {
            int page = stack.get(ModDataComponentTypes.PAGE.get());
            tooltipComponents.add(Component.translatable("page.selectivepowers." + (page)));
        } else {
            tooltipComponents.add(Component.translatable("page.selectivepowers.unidentified"));
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        world.playSound(player, player.getOnPos(), SoundEvents.BOOK_PAGE_TURN,player.getSoundSource(), 1.0F, 1.0F);
        if (!world.isClientSide() && !stack.has(ModDataComponentTypes.PAGE.get())) {
            int page = chooseRandomPage(player);
            stack.set(ModDataComponentTypes.PAGE.get(), page);
        }

        if (world.isClientSide && stack.has(ModDataComponentTypes.PAGE.get())) {
            ClientPageOpener.open(stack.get(ModDataComponentTypes.PAGE.get()));
        }
        return InteractionResultHolder.success(stack);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (!level.isClientSide() && !stack.has(ModDataComponentTypes.PAGE.get()) && entity instanceof Player playerEntity) {
            int page = chooseRandomPage(playerEntity);
            stack.set(ModDataComponentTypes.PAGE.get(), page);
        }
    }
}
