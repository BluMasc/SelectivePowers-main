package net.blumasc.selectivepowers.item.custom;

import net.blumasc.selectivepowers.entity.SelectivepowersEntities;
import net.blumasc.selectivepowers.entity.custom.projectile.WhirlpoolEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class WhirlpoolItem extends Item {
    public WhirlpoolItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();

        if (!(level instanceof ServerLevel serverLevel)) {
            return InteractionResult.SUCCESS;
        }

        BlockPos pos = context.getClickedPos().relative(context.getClickedFace());
        Player player = context.getPlayer();

        WhirlpoolEntity pool = SelectivepowersEntities.WHIRLPOOL.get().create(serverLevel);

        if (pool == null) {
            return InteractionResult.FAIL;
        }

        pool.moveTo(
                pos.getX() + 0.5,
                pos.getY(),
                pos.getZ() + 0.5,
                0f,
                0
        );

        serverLevel.addFreshEntity(pool);

        if (player != null && !player.getAbilities().instabuild) {
            context.getItemInHand().shrink(1);
        }

        return InteractionResult.CONSUME;
    }
}
