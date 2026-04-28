package net.blumasc.selectivepowers.structureprocessor;

import com.mojang.serialization.MapCodec;
import net.blumasc.selectivepowers.block.SelectivepowersBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StructureCleanupProcessor extends StructureProcessor {

    public static final MapCodec<StructureCleanupProcessor> CODEC = MapCodec.unit(new StructureCleanupProcessor());

    private static final ResourceLocation ALTAR_RL = ResourceLocation.fromNamespaceAndPath(
            "selectivepowers", "decrepit_sacrificial_altar"
    );

    @Override
    protected StructureProcessorType<?> getType() {
        return ModProcessors.CLEANUP_PROCESSOR.get();
    }

    @Override
    public List<StructureTemplate.StructureBlockInfo> finalizeProcessing(
            ServerLevelAccessor level,
            BlockPos offset,
            BlockPos pos,
            List<StructureTemplate.StructureBlockInfo> originalBlockInfos,
            List<StructureTemplate.StructureBlockInfo> processedBlockInfos,
            StructurePlaceSettings settings
    ) {
        Map<BlockPos, StructureTemplate.StructureBlockInfo> blockMap = new HashMap<>();
        for (var info : processedBlockInfos) {
            blockMap.put(info.pos(), info);
        }

        List<StructureTemplate.StructureBlockInfo> result = new ArrayList<>(processedBlockInfos);
        for (int i = 0; i < result.size(); i++) {
            var info = result.get(i);
            BlockState state = info.state();

            if (state.getBlock() instanceof VineBlock) {
                BlockState cleaned = cleanVine(state, info.pos(), blockMap, level);
                if (cleaned == null) {
                    result.set(i, new StructureTemplate.StructureBlockInfo(
                            info.pos(), Blocks.AIR.defaultBlockState(), null));
                } else if (cleaned != state) {
                    result.set(i, new StructureTemplate.StructureBlockInfo(
                            info.pos(), cleaned, null));
                }

            } else if (state.is(Blocks.MOSS_CARPET)) {
                BlockPos belowPos = info.pos().below();
                if (!isSolidTop(belowPos, blockMap, level)) {
                    result.set(i, new StructureTemplate.StructureBlockInfo(
                            info.pos(), Blocks.AIR.defaultBlockState(), null));
                }
            }
        }
        blockMap.clear();
        for (var info : result) {
            blockMap.put(info.pos(), info);
        }

        Block altarBlock = SelectivepowersBlocks.SACRIFICIAL_ALTAR_BLOCK.get();
        List<StructureTemplate.StructureBlockInfo> altarFixes = new ArrayList<>();

        for (var info : result) {
            if (info.state().is(altarBlock)) {
                BlockPos belowPos = info.pos().below();
                var belowInfo = blockMap.get(belowPos);

                if (belowInfo == null || belowInfo.state().isAir()) {
                    altarFixes.add(new StructureTemplate.StructureBlockInfo(
                            belowPos, Blocks.ANDESITE.defaultBlockState(), null));
                }
            }
        }
        for (var fix : altarFixes) {
            boolean replaced = false;
            for (int i = 0; i < result.size(); i++) {
                if (result.get(i).pos().equals(fix.pos())) {
                    result.set(i, fix);
                    replaced = true;
                    break;
                }
            }
            if (!replaced) {
                result.add(fix);
            }
        }

        return result;
    }
    private BlockState cleanVine(BlockState state, BlockPos pos,
                                 Map<BlockPos, StructureTemplate.StructureBlockInfo> blockMap, ServerLevelAccessor level) {

        boolean north = state.getValue(VineBlock.NORTH) && isSolidFace(pos.north(), Direction.SOUTH, blockMap, level);
        boolean south = state.getValue(VineBlock.SOUTH) && isSolidFace(pos.south(), Direction.NORTH, blockMap, level);
        boolean east  = state.getValue(VineBlock.EAST)  && isSolidFace(pos.east(),  Direction.WEST,  blockMap, level);
        boolean west  = state.getValue(VineBlock.WEST)  && isSolidFace(pos.west(),  Direction.EAST,  blockMap, level);
        boolean up    = state.getValue(VineBlock.UP)    && isSolidFace(pos.above(), Direction.DOWN,  blockMap, level);

        if (!north && !south && !east && !west && !up) {
            return null;
        }

        return state
                .setValue(VineBlock.NORTH, north)
                .setValue(VineBlock.SOUTH, south)
                .setValue(VineBlock.EAST,  east)
                .setValue(VineBlock.WEST,  west)
                .setValue(VineBlock.UP,    up);
    }

    private boolean isSolidFace(BlockPos pos, Direction face,
                                Map<BlockPos, StructureTemplate.StructureBlockInfo> blockMap, ServerLevelAccessor level) {

        var info = blockMap.get(pos);
        if (info != null) {
            return info.state().isFaceSturdy(level, pos, face);
        }
        return level.getBlockState(pos).isFaceSturdy(level, pos, face);
    }
    private boolean isSolidTop(BlockPos pos,
                               Map<BlockPos, StructureTemplate.StructureBlockInfo> blockMap, ServerLevelAccessor level) {
        return isSolidFace(pos, Direction.UP, blockMap, level);
    }
}