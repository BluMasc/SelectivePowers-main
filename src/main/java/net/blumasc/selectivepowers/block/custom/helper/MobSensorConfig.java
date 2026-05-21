package net.blumasc.selectivepowers.block.custom.helper;

import net.blumasc.selectivepowers.Config;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MobSensorConfig {

    private static Map<EntityType<?>, Set<Block>> mappings = new HashMap<>();

    public static void reload() {
        mappings.clear();
        for (String entry : Config.MOB_BLOCK_MAPPINGS.get()) {
            String[] parts = entry.split("=");
            if (parts.length != 2) continue;

            String entityId = parts[0].trim();
            String[] blockIds = parts[1].split(",");

            EntityType<?> entityType = BuiltInRegistries.ENTITY_TYPE
                .get(ResourceLocation.parse(entityId));
            if (entityType == null) continue;

            Set<Block> blocks = new HashSet<>();
            for (String blockId : blockIds) {
                Block block = BuiltInRegistries.BLOCK
                    .get(ResourceLocation.parse(blockId.trim()));
                if (block != null) blocks.add(block);
            }

            if (!blocks.isEmpty()) {
                mappings.put(entityType, blocks);
            }
        }
    }

    public static boolean blockMatchesEntity(Block block, EntityType<?> entityType) {
        if(mappings.isEmpty()){
            reload();
        }
        Set<Block> validBlocks = mappings.get(entityType);
        return validBlocks != null && validBlocks.contains(block);
    }

    public static boolean hasAnyMapping(Block block) {
        return mappings.values().stream().anyMatch(blocks -> blocks.contains(block));
    }
}