package net.blumasc.selectivepowers.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.List;

public class PhaseBlock extends Block {

    public static final MapCodec<PhaseBlock> CODEC = simpleCodec(PhaseBlock::new);

    public PhaseBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (context instanceof EntityCollisionContext entityContext) {
            Entity entity = entityContext.getEntity();
            if (entity instanceof ItemEntity || entity instanceof ExperienceOrb) {
                return Shapes.empty();
            }
        }
        return Shapes.block();
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (level.isClientSide() || !(entity instanceof ItemEntity itemEntity)) return;

        ItemStack stack = itemEntity.getItem();
        if (!(stack.getItem() instanceof BlockItem blockItem)) return;

        Direction exitDir = findExitDirection(itemEntity.getDeltaMovement(), itemEntity.position(), pos);
        if (exitDir == null) return;

        tryPlaceAt(level, pos.relative(exitDir), blockItem, itemEntity, stack);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!level.isClientSide() && movedByPiston) {

            List<ItemEntity> items = level.getEntitiesOfClass(ItemEntity.class, new AABB(pos));
            for (ItemEntity itemEntity : items) {
                ItemStack stack = itemEntity.getItem();
                if (!(stack.getItem() instanceof BlockItem blockItem)) continue;

                tryPlaceAt(level, pos, blockItem, itemEntity, stack);
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Nullable
    private static Direction findExitDirection(Vec3 velocity, Vec3 itemPos, BlockPos blockPos) {
        if (velocity.lengthSqr() < 1e-8) return null;

        double minTime = Double.MAX_VALUE;
        Direction bestDir = null;

        for (Direction dir : Direction.values()) {
            double velComp  = axisComponent(velocity, dir.getAxis());
            int    axisSign = dir.getAxisDirection().getStep();

            if (velComp * axisSign <= 0) continue;

            double faceCoord = axisComponent(Vec3.atLowerCornerOf(blockPos), dir.getAxis())
                    + (dir.getAxisDirection() == Direction.AxisDirection.POSITIVE ? 1.0 : 0.0);
            double itemCoord = axisComponent(itemPos, dir.getAxis());
            double time      = (faceCoord - itemCoord) / velComp;

            if (time >= 0 && time <= 1.0 && time < minTime) {
                minTime  = time;
                bestDir  = dir;
            }
        }
        return bestDir;
    }

    private static boolean tryPlaceAt(Level level, BlockPos targetPos, BlockItem blockItem,
                                      ItemEntity itemEntity, ItemStack stack) {
        BlockState target = level.getBlockState(targetPos);
        if (!target.isAir() && !target.canBeReplaced()) return false;

        BlockState placement = blockItem.getBlock().defaultBlockState();
        if (!placement.canSurvive(level, targetPos)) return false;

        level.setBlock(targetPos, placement, Block.UPDATE_ALL);
        stack.shrink(1);
        if (stack.isEmpty()) {
            itemEntity.discard();
        } else {
            itemEntity.setItem(stack);
        }
        return true;
    }

    private static double axisComponent(Vec3 vec, Direction.Axis axis) {
        return switch (axis) {
            case X -> vec.x;
            case Y -> vec.y;
            case Z -> vec.z;
        };
    }
}