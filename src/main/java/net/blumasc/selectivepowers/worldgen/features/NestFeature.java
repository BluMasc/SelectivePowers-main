package net.blumasc.selectivepowers.worldgen.features;

import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntListIterator;
import net.blumasc.selectivepowers.block.SelectivepowersBlocks;
import net.blumasc.selectivepowers.block.custom.NestBlock;
import net.blumasc.selectivepowers.block.entity.NestBlockEntity;
import net.blumasc.selectivepowers.datagen.ModChestLoot;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.RandomizableContainer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

import java.util.stream.IntStream;

public class NestFeature extends Feature<NoneFeatureConfiguration> {

    public NestFeature() {
        super(NoneFeatureConfiguration.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();

        BlockPos top = level.getHeightmapPos(
                Heightmap.Types.MOTION_BLOCKING,
                origin
        );

        BlockPos below = top.below();

        if (!level.getBlockState(below).is(BlockTags.LEAVES)) {
            return false;
        }

        level.setBlock(
                top,
                SelectivepowersBlocks.CROWS_NEST.get().defaultBlockState(),
                Block.UPDATE_CLIENTS
        );

        if (level.getBlockEntity(top) instanceof NestBlockEntity be) {
            be.genLoot=true;
        }

        return true;
    }
}

