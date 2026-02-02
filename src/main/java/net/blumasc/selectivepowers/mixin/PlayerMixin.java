package net.blumasc.selectivepowers.mixin;

import net.blumasc.selectivepowers.enchantment.ModEnchantments;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Player.class)
public class PlayerMixin {

    @ModifyArg(
            method = "causeFoodExhaustion",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/food/FoodData;addExhaustion(F)V"
            ),
            index = 0
    )
    public float selectivepowers$reduceExhaustion(float exhaustion) {
        Player player = (Player)(Object)this;

        Holder<Enchantment> staminaHolder = player.level().registryAccess()
                .registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(ModEnchantments.ENDURANCE);

        ItemStack boots = player.getItemBySlot(EquipmentSlot.LEGS);
        int level = boots.getEnchantmentLevel(staminaHolder);
        if (level <= 0) return exhaustion;
        return exhaustion * (0.75f);

    }
}
