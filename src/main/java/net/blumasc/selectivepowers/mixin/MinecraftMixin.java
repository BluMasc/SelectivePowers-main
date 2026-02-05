package net.blumasc.selectivepowers.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.blumasc.selectivepowers.client.ClientPowerData;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {

    @ModifyReturnValue(
            method = "shouldEntityAppearGlowing",
            at = @At("RETURN")
    )
    private boolean forceTruthVisionGlow(boolean original, Entity entity) {

        Minecraft mc = (Minecraft)(Object)this;
        LocalPlayer player = mc.player;

        if (player == null) return original;
        if (!(entity instanceof LivingEntity living)) return original;
        if (!living.isInvisible()) return original;
        if (ClientPowerData.truthOwner == null) return original;
        if (!ClientPowerData.truthOwner.equals(player.getUUID())) return original;

        return true;
    }
}
