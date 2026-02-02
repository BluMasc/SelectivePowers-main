package net.blumasc.selectivepowers.entity.custom.projectile.helper;

import net.blumasc.selectivepowers.Config;
import net.blumasc.selectivepowers.block.SelectivepowersBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class MeteorSphere {

    public static List<BlockPos> positions(int radius) {
        List<BlockPos> result = new ArrayList<>();
        int r2 = radius * radius;

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    if (x*x + y*y + z*z <= r2) {
                        result.add(new BlockPos(x, y, z));
                    }
                }
            }
        }
        return result;
    }

    public static List<BlockPos> shell(int radius) {
        List<BlockPos> result = new ArrayList<>();
        int r2 = radius * radius;
        int inner2 = (radius - 1) * (radius - 1);

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    int d = x*x + y*y + z*z;
                    if (d <= r2 && d > inner2) {
                        result.add(new BlockPos(x, y, z));
                    }
                }
            }
        }
        return result;
    }

    public static BlockState getRandomOre(RandomSource random) {
        List<String> METEOR_ORES = (List<String>) Config.BLOCK_STRINGS_ORE.get();
        if (METEOR_ORES.isEmpty()) return SelectivepowersBlocks.OBSIDIAN_DUST.get().defaultBlockState();

        String id = METEOR_ORES.get(random.nextInt(METEOR_ORES.size()));
        ResourceLocation res = ResourceLocation.parse(id);
        Block block = BuiltInRegistries.BLOCK.get(res);
        if (block == null || block == Blocks.AIR) block = SelectivepowersBlocks.OBSIDIAN_DUST.get();

        return block.defaultBlockState();
    }
}
