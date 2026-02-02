package net.blumasc.selectivepowers.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.InstrumentTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class DrinkingHornItem extends Item {

    private final TagKey<Instrument> instruments;

    public DrinkingHornItem(Properties properties) {
        super(properties);
        instruments = InstrumentTags.GOAT_HORNS;
    }

    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        Optional<ResourceKey<Instrument>> optional = this.getInstrument(stack).flatMap(Holder::unwrapKey);
        if (optional.isPresent()) {
            MutableComponent mutablecomponent = Component.translatable(Util.makeDescriptionId("instrument", ((ResourceKey)optional.get()).location()));
            tooltipComponents.add(mutablecomponent.withStyle(ChatFormatting.GRAY));
        }

    }

    private Optional<Holder<Instrument>> getInstrument(ItemStack stack) {
        Holder<Instrument> holder = (Holder)stack.get(DataComponents.INSTRUMENT);
        if (holder != null) {
            return Optional.of(holder);
        } else {
            Iterator<Holder<Instrument>> iterator = BuiltInRegistries.INSTRUMENT.getTagOrEmpty(this.instruments).iterator();
            return iterator.hasNext() ? Optional.of((Holder)iterator.next()) : Optional.empty();
        }
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity user) {
        var instrumentData = stack.get(DataComponents.INSTRUMENT);
        super.finishUsingItem(stack, level, user);


        ItemStack goatHorn = new ItemStack(Items.GOAT_HORN);
        goatHorn.set(DataComponents.INSTRUMENT, instrumentData);
        return goatHorn;
    }
}
