package net.blumasc.selectivepowers.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.blumasc.selectivepowers.client.ClientPowerData;
import net.blumasc.selectivepowers.effect.SelectivepowersEffects;
import net.blumasc.selectivepowers.entity.client.lunarmaiden.LunarMaidenLayer;
import net.blumasc.selectivepowers.item.custom.ElementalGunItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.level.LightLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.blumasc.selectivepowers.entity.client.lunarmaiden.LunarMaidenLayer.shouldRenderMoonForm;
import static net.blumasc.selectivepowers.entity.client.yellowking.YellowKingLayer.shouldRenderSunForm;
import static net.blumasc.selectivepowers.events.ClientEventHandler.moonLayer;
import static net.blumasc.selectivepowers.events.ClientEventHandler.yellowLayer;

@Mixin(PlayerRenderer.class)
public class PlayerLayerMixin {
    @Inject(
            method = "render",
            at = @At("HEAD"),
            cancellable = true
    )
    private void renderSpecialForm(
            AbstractClientPlayer player,
            float entityYaw,
            float partialTicks,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            CallbackInfo ci
    ) {
        if (shouldRenderMoonForm(player)) {

            poseStack.pushPose();

            poseStack.translate(0.0, 2.0D, 0.0);

            poseStack.mulPose(Axis.YP.rotationDegrees(-player.getYRot()));

            poseStack.mulPose(Axis.XP.rotationDegrees(180f));

            moonLayer.render(
                    poseStack,
                    buffer,
                    packedLight,
                    player,
                    0.0f,
                    0.0f,
                    partialTicks,
                    player.tickCount,
                    entityYaw,
                    player.getYHeadRot()
            );

            poseStack.popPose();

            ci.cancel();
            return;
        }
        if (shouldRenderSunForm(player)) {


            poseStack.pushPose();

            poseStack.translate(0.0, 1.6D, 0.0);

            poseStack.mulPose(Axis.YP.rotationDegrees(-player.getYRot()));

            poseStack.mulPose(Axis.XP.rotationDegrees(180f));

            yellowLayer.render(
                    poseStack,
                    buffer,
                    packedLight,
                    player,
                    0.0f,
                    0.0f,
                    partialTicks,
                    player.tickCount,
                    entityYaw,
                    player.getYHeadRot()
            );
            poseStack.popPose();

            ci.cancel();
            return;
        }
        if (player.getUUID().equals(ClientPowerData.darkOwner)) {

            var level = player.level();
            if (level == null) return;
            var pos = player.blockPosition();

            int blockLight = level.getBrightness(LightLayer.BLOCK, pos);
            int skyLight = level.getBrightness(LightLayer.SKY, pos);

            if ((blockLight <= 5 && skyLight <= 7) ) {
                ci.cancel();
                return;
            }

            Minecraft mc = Minecraft.getInstance();

            if(mc != null)
            {
                AbstractClientPlayer viewingPlayer = mc.player;
                if(viewingPlayer!=null){
                    if(viewingPlayer.hasEffect(SelectivepowersEffects.DRAKNESS_EFFECT))
                    {
                        ci.cancel();
                        return;
                    }
                }

            }

        }

    }

}
