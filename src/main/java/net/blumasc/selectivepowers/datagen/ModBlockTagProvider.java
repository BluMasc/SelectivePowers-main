package net.blumasc.selectivepowers.datagen;

import net.blumasc.selectivepowers.SelectivePowers;
import net.blumasc.selectivepowers.block.SelectivepowersBlocks;
import net.blumasc.selectivepowers.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends BlockTagsProvider {
    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, SelectivePowers.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(BlockTags.ANIMALS_SPAWNABLE_ON)
                .add(SelectivepowersBlocks.MOON_DUST.get());
        tag(BlockTags.MINEABLE_WITH_SHOVEL)
                .add(SelectivepowersBlocks.MOON_DUST.get())
                .add(SelectivepowersBlocks.OBSIDIAN_DUST.get());
        tag(BlockTags.MINEABLE_WITH_HOE)
                .add(SelectivepowersBlocks.MOON_CAP_BLOCK.get());
        tag(BlockTags.MINEABLE_WITH_AXE)
                .add(SelectivepowersBlocks.ROSE_VINES.get());
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(SelectivepowersBlocks.ALTAR.get())
                .add(SelectivepowersBlocks.MOON_SACRIFICE_BLOCK.get())
                .add(SelectivepowersBlocks.INLET_MOON_SACRIFICE_BLOCK.get())
                .add(SelectivepowersBlocks.SUN_SACRIFICE_BLOCK.get())
                .add(SelectivepowersBlocks.INLET_SUN_SACRIFICE_BLOCK.get())
                .add(SelectivepowersBlocks.MOON_ROCK.get())
                .add(SelectivepowersBlocks.MOON_ROCK_BRICKS_WALL.get())
                .add(SelectivepowersBlocks.MOON_ROCK_TILES_WALL.get())
                .add(SelectivepowersBlocks.SMOOTH_MOON_ROCK_WALL.get())
                .add(SelectivepowersBlocks.SMOOTH_MOON_ROCK.get())
                .add(SelectivepowersBlocks.SMOOTH_MOON_ROCK_SLAB.get())
                .add(SelectivepowersBlocks.SMOOTH_MOON_ROCK_STAIRS.get())
                .add(SelectivepowersBlocks.MOON_ROCK_TILES.get())
                .add(SelectivepowersBlocks.MOON_ROCK_TILES_SLAB.get())
                .add(SelectivepowersBlocks.MOON_ROCK_TILES_STAIRS.get())
                .add(SelectivepowersBlocks.MOON_ROCK_BRICKS.get())
                .add(SelectivepowersBlocks.MOON_ROCK_BRICKS_SLAB.get())
                .add(SelectivepowersBlocks.MOON_ROCK_BRICKS_STAIRS.get())
                .add(SelectivepowersBlocks.CHISLED_SOLAR_BRICKS.get())
                .add(SelectivepowersBlocks.CRACKED_SOLAR_BRICKS.get())
                .add(SelectivepowersBlocks.SOLAR_BRICKS.get())
                .add(SelectivepowersBlocks.SOLAR_BRICK_FENCE.get())
                .add(SelectivepowersBlocks.SOLAR_BRICK_WALL.get())
                .add(SelectivepowersBlocks.SOLAR_BRICK_STAIRS.get())
                .add(SelectivepowersBlocks.SOLAR_BRICK_SLAB.get())
                .add(SelectivepowersBlocks.DRACONIC_BEACON.get())
                .add(SelectivepowersBlocks.GOLDEN_GOAT.get())
                .add(SelectivepowersBlocks.SACRIFICIAL_ALTAR_BLOCK.get());
        tag(BlockTags.WALLS)
                .add(SelectivepowersBlocks.MOON_ROCK_BRICKS_WALL.get())
                .add(SelectivepowersBlocks.MOON_ROCK_TILES_WALL.get())
                .add(SelectivepowersBlocks.SMOOTH_MOON_ROCK_WALL.get())
                .add(SelectivepowersBlocks.SOLAR_BRICK_WALL.get());
        tag(BlockTags.FENCES)
                .add(SelectivepowersBlocks.SOLAR_BRICK_FENCE.get());
        tag(BlockTags.NEEDS_DIAMOND_TOOL)
                .add(SelectivepowersBlocks.ALTAR.get())
                .add(SelectivepowersBlocks.OBSIDIAN_DUST.get())
                .add(SelectivepowersBlocks.DRACONIC_BEACON.get());
        tag(BlockTags.NEEDS_STONE_TOOL)
                .add(SelectivepowersBlocks.MOON_ROCK.get())
                .add(SelectivepowersBlocks.MOON_ROCK_BRICKS_WALL.get())
                .add(SelectivepowersBlocks.MOON_ROCK_TILES_WALL.get())
                .add(SelectivepowersBlocks.SMOOTH_MOON_ROCK_WALL.get())
                .add(SelectivepowersBlocks.SMOOTH_MOON_ROCK.get())
                .add(SelectivepowersBlocks.SMOOTH_MOON_ROCK_SLAB.get())
                .add(SelectivepowersBlocks.SMOOTH_MOON_ROCK_STAIRS.get())
                .add(SelectivepowersBlocks.MOON_ROCK_TILES.get())
                .add(SelectivepowersBlocks.MOON_ROCK_TILES_SLAB.get())
                .add(SelectivepowersBlocks.MOON_ROCK_TILES_STAIRS.get())
                .add(SelectivepowersBlocks.MOON_ROCK_BRICKS.get())
                .add(SelectivepowersBlocks.MOON_ROCK_BRICKS_SLAB.get())
                .add(SelectivepowersBlocks.MOON_ROCK_BRICKS_STAIRS.get())
                .add(SelectivepowersBlocks.SACRIFICIAL_ALTAR_BLOCK.get());
        tag(BlockTags.NEEDS_IRON_TOOL)
                .add(SelectivepowersBlocks.GOLDEN_GOAT.get());
        tag(ModTags.Blocks.CROW_PERCHABLE)
                .add(SelectivepowersBlocks.CROWS_NEST.get())
                .addTag(BlockTags.FENCES);
        tag(ModTags.Blocks.LAVA_CORE)
                .add(Blocks.LAVA);
        tag(ModTags.Blocks.ICE_CORE)
                .add(Blocks.POWDER_SNOW);
        tag(BlockTags.create(ResourceLocation.fromNamespaceAndPath("c","storage_blocks")))
                .add(SelectivepowersBlocks.MOON_SACRIFICE_BLOCK.get())
                .add(SelectivepowersBlocks.SUN_SACRIFICE_BLOCK.get());
        tag(BlockTags.create(ResourceLocation.fromNamespaceAndPath("c","glass_blocks")))
                .add(SelectivepowersBlocks.EARTH_GLASS.get())
                .add(SelectivepowersBlocks.MOON_GLASS.get());
        tag(BlockTags.IMPERMEABLE)
                .add(SelectivepowersBlocks.EARTH_GLASS.get())
                .add(SelectivepowersBlocks.MOON_GLASS.get());
        tag(BlockTags.INFINIBURN_END)
                .add(SelectivepowersBlocks.SOLAR_BLOCK.get())
                .add(SelectivepowersBlocks.M_SOLAR_BLOCK.get());
        tag(BlockTags.INFINIBURN_NETHER)
                .add(SelectivepowersBlocks.SOLAR_BLOCK.get())
                .add(SelectivepowersBlocks.M_SOLAR_BLOCK.get());
        tag(BlockTags.INFINIBURN_OVERWORLD)
                .add(SelectivepowersBlocks.SOLAR_BLOCK.get())
                .add(SelectivepowersBlocks.M_SOLAR_BLOCK.get());
        tag(BlockTags.FLOWERS)
                .add(SelectivepowersBlocks.ROSE_VINES.get());
    }
}
