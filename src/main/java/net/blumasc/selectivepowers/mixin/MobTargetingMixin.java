package net.blumasc.selectivepowers.mixin;

import net.blumasc.selectivepowers.PowerManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.level.LightLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.function.Predicate;

@Mixin(NearestAttackableTargetGoal.class)
public abstract class MobTargetingMixin<T extends LivingEntity> {

    @ModifyVariable(
            method = "<init>(Lnet/minecraft/world/entity/Mob;Ljava/lang/Class;IZZLjava/util/function/Predicate;)V",
            at = @At("HEAD"),
            index = 6 // index of targetPredicate parameter
    )
    private static Predicate<LivingEntity> changeTargetPredicate(Predicate<LivingEntity> original) {
        return original != null ? original.and(e -> notDark(e)) : e -> notDark(e);
    }

    private static boolean notDark(LivingEntity e) {
        if (!(e.level() instanceof ServerLevel sl)) return true;
        var pm = PowerManager.get(sl);
        if (pm.getPowerOfPlayer(e.getUUID()).equals(PowerManager.DARK_POWER)) {
            var pos = e.blockPosition();
            int blockLight = sl.getBrightness(LightLayer.BLOCK, pos);
            int skyLight = sl.getBrightness(LightLayer.SKY, pos);
            return !(blockLight <= 5 && skyLight <= 7);
        }
        return true;
    }
}

