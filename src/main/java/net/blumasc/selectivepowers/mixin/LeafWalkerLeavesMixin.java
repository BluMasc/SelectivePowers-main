package net.blumasc.selectivepowers.mixin;

import net.blumasc.selectivepowers.item.SelectivepowersItems;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.Properties;

@Mixin(BlockBehaviour.class)
public abstract class LeafWalkerLeavesMixin {

    @Inject(
            method = "getCollisionShape(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/shapes/CollisionContext;)Lnet/minecraft/world/phys/shapes/VoxelShape;",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onGetCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context, CallbackInfoReturnable<VoxelShape> cir) {
        if (!state.is(BlockTags.LEAVES)) return;
        if (!(context instanceof EntityCollisionContext ectx)) return;
        if (!(ectx.getEntity() instanceof Player player)) return;

        if (isWearingLeafWalker(player)) {
            cir.setReturnValue(Shapes.empty());
        }
    }

    private static boolean isWearingLeafWalker(Player player) {
        return CuriosApi.getCuriosInventory(player)
                .flatMap(inv -> inv.getStacksHandler("body"))
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
