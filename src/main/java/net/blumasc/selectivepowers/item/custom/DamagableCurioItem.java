package net.blumasc.selectivepowers.item.custom;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class DamagableCurioItem extends Item implements ICurioItem {

    public DamagableCurioItem(Properties p) {
        super(p.stacksTo(1));
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
    }
}
