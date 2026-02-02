package net.blumasc.selectivepowers.item.custom;

import net.blumasc.selectivepowers.PowerManager;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class DealStatusItem extends Item {
    public DealStatusItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide()) {
            ServerPlayer sp = (ServerPlayer) player;
            PowerManager pm = PowerManager.get((ServerLevel) level);

            if(pm.doesPlayerHaveAnyPower(player.getUUID())) {

                String power = pm.getPowerOfPlayer(player.getUUID());
                String levelName = pm.getPowerLevelOfPlayer(player.getUUID()).name().toLowerCase();

                var send = Component.translatable("selectivepowers.tooltip.heading")
                        .append(Component.literal("\n\n"))
                        .append(Component.translatable("selectivepowers.tooltip.contract").append(Component.literal(" ").append(Component.translatable("selectivepowers.name." + power).append(Component.literal("\n")))))
                        .append(Component.translatable("selectivepowers.tooltip.level").append(Component.literal(" ")).append(Component.translatable("selectivepowers.level." + levelName).append(Component.literal("\n"))))
                        .append(Component.translatable("selectivepowers.tooltip.abilities").append(Component.literal("\n")));

                switch(levelName){
                    case "bound" -> send = send.append(Component.translatable("selectivepowers.abilities.bound."+power).append(Component.literal("\n\n")).append(Component.translatable("selectivepowers.tooltip.todo")).append(Component.translatable("selectivepowers.messages.offer."+power+".reminder")));
                    case "awoken" -> send = send.append(Component.translatable("selectivepowers.abilities.bound."+power).append(Component.literal("\n")).append(Component.translatable("selectivepowers.abilities.awoken."+power)));
                    case "ascended" -> send = send.append(Component.translatable("selectivepowers.abilities.bound."+power).append(Component.literal("\n")).append(Component.translatable("selectivepowers.abilities.awoken."+power)).append(Component.literal("\n")).append(Component.translatable("selectivepowers.abilities.ascended."+power)));
                }

                player.sendSystemMessage(send);

            }else{
                player.sendSystemMessage(Component.translatable("selectivepowers.tooltip.noaffiliation"));
            }

        }
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }
}
