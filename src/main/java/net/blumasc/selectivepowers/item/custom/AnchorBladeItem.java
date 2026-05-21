package net.blumasc.selectivepowers.item.custom;

import net.blumasc.selectivepowers.entity.SelectivepowersEntities;
import net.blumasc.selectivepowers.entity.custom.AnchorEntity;
import net.blumasc.selectivepowers.item.SelectivepowersItems;
import net.blumasc.selectivepowers.sound.SelectivepowersSounds;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;

public class AnchorBladeItem extends SwordItem {

    public AnchorBladeItem(Tier tier, Properties properties) {
        super(tier, properties);
    }

    private static final String ANCHOR_UUID_KEY = "AnchorUUID";

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide && level instanceof ServerLevel sl) {

            AnchorEntity anchor = new AnchorEntity(
                    SelectivepowersEntities.ANCHOR.get(),
                    sl,
                    player
            );

            anchor.setOwner(player);
            anchor.setDeltaMovement(player.getLookAngle().scale(3.0));

            sl.addFreshEntity(anchor);

            sl.playSound(null, player.getX(), player.getY(), player.getZ(), SelectivepowersSounds.ANCHOR_THROW.get(), SoundSource.PLAYERS);

            CompoundTag tag = new CompoundTag();
            tag.putUUID(ANCHOR_UUID_KEY, anchor.getUUID());

            ItemStack thrown = new ItemStack(SelectivepowersItems.THROWN_ANCHOR.get());
            thrown.applyComponents(stack.getComponents());
            thrown.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));

            player.setItemInHand(hand, thrown);
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }
    public static float getOxygenBonus(LivingEntity target) {
        int maxAir = target.getMaxAirSupply();
        int currentAir = target.getAirSupply();
        int missingAir = maxAir - currentAir;
        return (missingAir / 2) * 0.5f;
    }
}