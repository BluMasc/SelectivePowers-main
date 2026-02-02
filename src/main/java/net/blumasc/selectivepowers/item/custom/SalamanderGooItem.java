package net.blumasc.selectivepowers.item.custom;

import net.blumasc.selectivepowers.entity.SelectivepowersEntities;
import net.blumasc.selectivepowers.entity.custom.SalamanderEntity;
import net.blumasc.selectivepowers.sound.SelectivepowersSounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class SalamanderGooItem extends Item {
    public SalamanderGooItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand usedHand) {
        if (!(target instanceof Axolotl axolotl)) {
            return InteractionResult.PASS;
        }

        if (player.level().isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        ServerLevel level = (ServerLevel) player.level();

        SalamanderEntity salamander = SelectivepowersEntities.SALAMANDER.get().create(level);
        if (salamander == null)
            return InteractionResult.CONSUME;

        salamander.moveTo(axolotl.getX(), axolotl.getY(), axolotl.getZ(),
                axolotl.getYRot(), axolotl.getXRot());

        if (axolotl.hasCustomName()) {
            salamander.setCustomName(axolotl.getCustomName());
            salamander.setCustomNameVisible(axolotl.isCustomNameVisible());
        }

        salamander.setAge(axolotl.getAge());

        salamander.setPersistenceRequired();

        level.addFreshEntity(salamander);

        axolotl.discard();

        salamander.setDanceTime(150);

        level.playSound(
                null,
                player.getX(), player.getY(), player.getZ(),
                SoundEvents.BOTTLE_EMPTY,
                player.getSoundSource(),
                1.0F,
                1.0F
        );

        level.playSound(
                null,
                salamander.getX(), salamander.getY(), salamander.getZ(),
                SelectivepowersSounds.THE_AXOLOTL_SONG.get(),
                salamander.getSoundSource(),
                1.0F,
                1.0F
        );

        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }

        return InteractionResult.CONSUME;
    }

}
