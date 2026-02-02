package net.blumasc.selectivepowers.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.blumasc.selectivepowers.client.ClientPowerData;
import net.blumasc.selectivepowers.effect.SelectivepowersEffects;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ElytraLayer.class)
public abstract class ElytraLayerMixin<T extends LivingEntity, M extends HumanoidModel<T>> extends RenderLayer<T, M> {


    public ElytraLayerMixin(RenderLayerParent<T, M> renderer) {
        super(renderer);
    }

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void onRenderElytra(PoseStack poseStack,
                                MultiBufferSource buffer,
                                int packedLight,
                                T entity,
                                float limbSwing,
                                float limbSwingAmount,
                                float partialTicks,
                                float ageInTicks,
                                float netHeadYaw,
                                float headPitch,
                                CallbackInfo ci) {
        if (entity instanceof Player player) {
            if (ClientPowerData.dragonOwner!= null && ClientPowerData.dragonOwner.equals(player.getUUID())) {
                ci.cancel();
            }
        }
    }
}
