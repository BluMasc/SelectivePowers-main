package net.blumasc.selectivepowers.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.blumasc.selectivepowers.item.SelectivepowersItems;
import net.blumasc.selectivepowers.item.custom.FrostShieldItem;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.renderer.ItemInHandRenderer;

@Mixin(ItemInHandRenderer.class)
public class ItemInHandRendererMixin {

    @Inject(method = "renderArmWithItem", at = @At("HEAD"))
    private void renderNewShield(AbstractClientPlayer player, float partialTicks, float pitch, InteractionHand hand, float swingProgress, ItemStack stack, float equippedProgress, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, CallbackInfo ci) {
        var itemStack = hand == InteractionHand.MAIN_HAND ? player.getMainHandItem() : player.getOffhandItem();

        poseStack.pushPose();
        if (itemStack.getItem() instanceof FrostShieldItem && player.isUsingItem() && player.getUsedItemHand() == hand) {
            poseStack.translate(0, 0.3f, 0);
        }

    }

    @Inject(method = "renderArmWithItem", at = @At("RETURN"))
    private void popPose(AbstractClientPlayer player, float partialTicks, float pitch, InteractionHand hand, float swingProgress, ItemStack stack, float equippedProgress, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, CallbackInfo ci) {
        poseStack.popPose();
    }
}