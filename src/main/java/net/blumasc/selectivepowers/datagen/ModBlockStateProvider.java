package net.blumasc.selectivepowers.datagen;

import net.blumasc.selectivepowers.SelectivePowers;
import net.blumasc.selectivepowers.block.SelectivepowersBlocks;
import net.blumasc.selectivepowers.block.custom.MooncapCropBlock;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

import java.util.function.Function;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, SelectivePowers.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(SelectivepowersBlocks.MOON_SACRIFICE_BLOCK);
        blockWithItem(SelectivepowersBlocks.SUN_SACRIFICE_BLOCK);
        blockWithItem(SelectivepowersBlocks.INLET_MOON_SACRIFICE_BLOCK);
        blockWithItem(SelectivepowersBlocks.INLET_SUN_SACRIFICE_BLOCK);
        blockWithItem(SelectivepowersBlocks.MOON_DUST);
        blockWithItem(SelectivepowersBlocks.MOON_ROCK);
        blockWithItem(SelectivepowersBlocks.MOON_GLASS);
        blockWithItem(SelectivepowersBlocks.EARTH_GLASS);
        blockWithItem(SelectivepowersBlocks.MOON_CAP_BLOCK);
        blockWithItem(SelectivepowersBlocks.SMOOTH_MOON_ROCK);
        blockWithItem(SelectivepowersBlocks.MOON_ROCK_BRICKS);
        blockWithItem(SelectivepowersBlocks.MOON_ROCK_TILES);
        blockWithItem(SelectivepowersBlocks.SOLAR_BLOCK);
        blockWithItem(SelectivepowersBlocks.M_SOLAR_BLOCK);
        blockWithItem(SelectivepowersBlocks.VOID_BLOCK);
        stairsBlock(SelectivepowersBlocks.SMOOTH_MOON_ROCK_STAIRS.get(), blockTexture(SelectivepowersBlocks.SMOOTH_MOON_ROCK.get()));
        blockItem(SelectivepowersBlocks.SMOOTH_MOON_ROCK_STAIRS);
        stairsBlock(SelectivepowersBlocks.MOON_ROCK_BRICKS_STAIRS.get(), blockTexture(SelectivepowersBlocks.MOON_ROCK_BRICKS.get()));
        blockItem(SelectivepowersBlocks.MOON_ROCK_BRICKS_STAIRS);
        stairsBlock(SelectivepowersBlocks.MOON_ROCK_TILES_STAIRS.get(), blockTexture(SelectivepowersBlocks.MOON_ROCK_TILES.get()));
        blockItem(SelectivepowersBlocks.MOON_ROCK_TILES_STAIRS);
        slabBlock(SelectivepowersBlocks.SMOOTH_MOON_ROCK_SLAB.get(), blockTexture(SelectivepowersBlocks.SMOOTH_MOON_ROCK.get()), blockTexture(SelectivepowersBlocks.SMOOTH_MOON_ROCK.get()));
        blockItem(SelectivepowersBlocks.SMOOTH_MOON_ROCK_SLAB);
        slabBlock(SelectivepowersBlocks.MOON_ROCK_BRICKS_SLAB.get(), blockTexture(SelectivepowersBlocks.MOON_ROCK_BRICKS.get()), blockTexture(SelectivepowersBlocks.MOON_ROCK_BRICKS.get()));
        blockItem(SelectivepowersBlocks.MOON_ROCK_BRICKS_SLAB);
        slabBlock(SelectivepowersBlocks.MOON_ROCK_TILES_SLAB.get(), blockTexture(SelectivepowersBlocks.MOON_ROCK_TILES.get()), blockTexture(SelectivepowersBlocks.MOON_ROCK_TILES.get()));
        blockItem(SelectivepowersBlocks.MOON_ROCK_TILES_SLAB);
        wallBlock(SelectivepowersBlocks.SMOOTH_MOON_ROCK_WALL.get(), blockTexture(SelectivepowersBlocks.SMOOTH_MOON_ROCK.get()));
        wallBlock(SelectivepowersBlocks.MOON_ROCK_TILES_WALL.get(), blockTexture(SelectivepowersBlocks.MOON_ROCK_TILES.get()));
        wallBlock(SelectivepowersBlocks.MOON_ROCK_BRICKS_WALL.get(), blockTexture(SelectivepowersBlocks.MOON_ROCK_BRICKS.get()));
        blockWithItem(SelectivepowersBlocks.SOLAR_BRICKS);
        blockWithItem(SelectivepowersBlocks.CHISLED_SOLAR_BRICKS);
        blockWithItem(SelectivepowersBlocks.CRACKED_SOLAR_BRICKS);
        stairsBlock(SelectivepowersBlocks.SOLAR_BRICK_STAIRS.get(), blockTexture(SelectivepowersBlocks.SOLAR_BRICKS.get()));
        blockItem(SelectivepowersBlocks.SOLAR_BRICK_STAIRS);
        wallBlock(SelectivepowersBlocks.SOLAR_BRICK_WALL.get(), blockTexture(SelectivepowersBlocks.SOLAR_BRICKS.get()));
        fenceBlock(SelectivepowersBlocks.SOLAR_BRICK_FENCE.get(), blockTexture(SelectivepowersBlocks.SOLAR_BRICKS.get()));
        slabBlock(SelectivepowersBlocks.SOLAR_BRICK_SLAB.get(), blockTexture(SelectivepowersBlocks.SOLAR_BRICKS.get()), blockTexture(SelectivepowersBlocks.SOLAR_BRICKS.get()));
        blockItem(SelectivepowersBlocks.SOLAR_BRICK_SLAB);
        blockWithItem(SelectivepowersBlocks.OBSIDIAN_DUST);
        simpleBlock(SelectivepowersBlocks.ROSE_VINES.get(),
                models().cubeAll("rose_bush", modLoc("block/rose_bush"))
                        .renderType("cutout"));
        blockItem(SelectivepowersBlocks.ROSE_VINES);

        makeCrop((CropBlock) SelectivepowersBlocks.MOONCAP_CROP.get(), "mooncap_crop_stage", "crop_mooncap_");

    }

    private void blockWithItem(DeferredBlock<?> deferredBlock){
        simpleBlockWithItem(deferredBlock.get(), cubeAll(deferredBlock.get()));
    }

    public void makeCrop(CropBlock block, String modelName, String textureName) {
        Function<BlockState, ConfiguredModel[]> function = state -> states(state, block, modelName, textureName);

        getVariantBuilder(block).forAllStates(function);
    }

    private ConfiguredModel[] states(BlockState state, CropBlock block, String modelName, String textureName) {
        ConfiguredModel[] models = new ConfiguredModel[1];
        models[0] = new ConfiguredModel(models().crop(modelName + state.getValue(((MooncapCropBlock) block).getAgeProperty()),
                ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "block/" + textureName + state.getValue(((MooncapCropBlock) block).getAgeProperty()))).renderType("cutout"));

        return models;
    }
    private void blockItem(DeferredBlock<?> deferredBlock)
    {
        simpleBlockItem(deferredBlock.get(), new ModelFile.UncheckedModelFile("selectivepowers:block/"+deferredBlock.getId().getPath()));
    }

    private void blockItem(DeferredBlock<?> deferredBlock, String apendix)
    {
        simpleBlockItem(deferredBlock.get(), new ModelFile.UncheckedModelFile("selectivepowers:block/"+deferredBlock.getId().getPath()+apendix));
    }
}
