package net.blumasc.selectivepowers.block.custom;

import net.blumasc.selectivepowers.PowerManager;
import net.blumasc.selectivepowers.item.SelectivepowersItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import top.theillusivec4.curios.api.CuriosApi;

public class ThornsBlock extends Block {
    public ThornsBlock(Properties properties) {
        super(properties);
    }

    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        Vec3 vec3 = new Vec3((double)0.25F, (double)0.05F, (double)0.25F);
        if (entity instanceof LivingEntity livingentity) {
            if (livingentity.hasEffect(MobEffects.WEAVING)) {
                vec3 = new Vec3((double)0.5F, (double)0.25F, (double)0.5F);
            }
            if(livingentity instanceof Player p && isWearingLeafwalkerItem(p))
            {
                vec3 = new Vec3((double)0.8F, (double)0.5F, (double)0.8F);
            }
            if (!level.isClientSide && (entity.xOld != entity.getX() || entity.zOld != entity.getZ())) {
                PowerManager pm = PowerManager.get((ServerLevel) level);
                if (pm.getPowerOfPlayer(entity.getUUID())!=PowerManager.FORREST_POWER) {
                    double d0 = Math.abs(entity.getX() - entity.xOld);
                    double d1 = Math.abs(entity.getZ() - entity.zOld);
                    if (d0 >= (double) 0.003F || d1 >= (double) 0.003F) {
                        entity.hurt(level.damageSources().sweetBerryBush(), 1.0F);
                    }
                }
            }
        }

        entity.makeStuckInBlock(state, vec3);
    }

    public static boolean isWearingLeafwalkerItem(Player player) {
        return CuriosApi.getCuriosInventory(player)
                .flatMap(curiosInventory -> curiosInventory.getStacksHandler("body"))
                .map(handler -> {
                    for (int slot = 0; slot < handler.getSlots(); slot++) {
                        ItemStack stack = handler.getStacks().getStackInSlot(slot);
                        if (!stack.isEmpty() && stack.is(SelectivepowersItems.LEAFWALKER_CURIO.get())) {
                            return true;
                        }
                    }
                    return false;
                })
                .orElse(false);
    }
}
