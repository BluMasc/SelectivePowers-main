package net.blumasc.selectivepowers.mixin;

import net.blumasc.selectivepowers.item.custom.ElementalGunItem;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerModel.class)
public abstract class PlayerModelMixin<T extends Player> extends HumanoidModel<T> {

    public PlayerModelMixin(ModelPart root) {
        super(root);
    }

    @Shadow
    public ModelPart rightSleeve;
    @Shadow
    public ModelPart leftSleeve;

    private static final float ARM_LIFT_RAD = (float) Math.toRadians(70);

    @Inject(method = "setupAnim", at = @At("TAIL"))
    private void holdGunArms(
            LivingEntity entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch,
            CallbackInfo ci
    ) {
        if (!(entity instanceof Player player)) return;

        if(player.isSwimming() || player.isVisuallyCrawling()) return;

        if (player.getMainHandItem().getItem() instanceof ElementalGunItem) {
            this.rightArm.xRot = -ARM_LIFT_RAD;
            this.rightSleeve.xRot = -ARM_LIFT_RAD;
        }

        if (player.getOffhandItem().getItem() instanceof ElementalGunItem) {
            this.leftArm.xRot = -ARM_LIFT_RAD;
            this.leftSleeve.xRot = -ARM_LIFT_RAD;
        }
    }
}

