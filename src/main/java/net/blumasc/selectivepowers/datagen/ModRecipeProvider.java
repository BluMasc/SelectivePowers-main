package net.blumasc.selectivepowers.datagen;

import net.blumasc.selectivepowers.SelectivePowers;
import net.blumasc.selectivepowers.block.SelectivepowersBlocks;
import net.blumasc.selectivepowers.item.SelectivepowersItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, SelectivepowersBlocks.MOON_SACRIFICE_BLOCK.get())
                .pattern("MMM")
                .pattern("MMM")
                .pattern("MMM")
                .define('M', SelectivepowersItems.MOON_SACRIFICE)
                .unlockedBy("has_moon_sacrifice", has(SelectivepowersItems.MOON_SACRIFICE)).save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, SelectivepowersBlocks.INLET_MOON_SACRIFICE_BLOCK.get(), 4)
                .pattern("MSM")
                .pattern("SMS")
                .pattern("MSM")
                .define('S', SelectivepowersItems.MOON_SACRIFICE)
                .define('M', Blocks.SMOOTH_STONE)
                .unlockedBy("has_moon_sacrifice", has(SelectivepowersItems.MOON_SACRIFICE)).save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, SelectivepowersBlocks.SUN_SACRIFICE_BLOCK.get())
                .pattern("MMM")
                .pattern("MMM")
                .pattern("MMM")
                .define('M', SelectivepowersItems.SUN_SACRIFICE)
                .unlockedBy("has_sun_sacrifice", has(SelectivepowersItems.SUN_SACRIFICE)).save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, SelectivepowersBlocks.INLET_SUN_SACRIFICE_BLOCK.get(), 4)
                .pattern("MSM")
                .pattern("SMS")
                .pattern("MSM")
                .define('S', SelectivepowersItems.SUN_SACRIFICE)
                .define('M', Blocks.SMOOTH_STONE)
                .unlockedBy("has_sun_sacrifice", has(SelectivepowersItems.SUN_SACRIFICE)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, SelectivepowersBlocks.SUN_LANTERN.get(), 1)
                .pattern("MMM")
                .pattern("MSM")
                .pattern("MMM")
                .define('S', SelectivepowersItems.SUN_SACRIFICE)
                .define('M', Items.IRON_NUGGET)
                .unlockedBy("has_sun_sacrifice", has(SelectivepowersItems.SUN_SACRIFICE)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, SelectivepowersBlocks.MOON_LANTERN.get(), 1)
                .pattern("MMM")
                .pattern("MSM")
                .pattern("MMM")
                .define('S', SelectivepowersItems.MOON_SACRIFICE)
                .define('M', Items.IRON_NUGGET)
                .unlockedBy("has_moon_sacrifice", has(SelectivepowersItems.MOON_SACRIFICE)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, SelectivepowersItems.HORNED_HELMET.get())
                .pattern("MSM")
                .define('S', Items.LEATHER_HELMET)
                .define('M', SelectivepowersItems.BEETLE_HORN)
                .unlockedBy("has_beetle_horn", has(SelectivepowersItems.BEETLE_HORN)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SelectivepowersItems.CORRUPTED_MASK.get())
                .pattern("MM")
                .pattern("MM")
                .define('M', SelectivepowersItems.CORRUPTION_SHARD)
                .unlockedBy("has_corupted_shard", has(SelectivepowersItems.CORRUPTION_SHARD)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, SelectivepowersBlocks.MOON_ROCK_BRICKS.get(), 4)
                .pattern("MM")
                .pattern("MM")
                .define('M', SelectivepowersBlocks.MOON_ROCK)
                .unlockedBy("has_moon_rock", has(SelectivepowersBlocks.MOON_ROCK)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, SelectivepowersBlocks.MOON_ROCK_TILES.get(), 4)
                .pattern("MM")
                .pattern("MM")
                .define('M', SelectivepowersBlocks.SMOOTH_MOON_ROCK)
                .unlockedBy("has_smooth_moon_rock", has(SelectivepowersBlocks.SMOOTH_MOON_ROCK)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, SelectivepowersBlocks.SOLAR_BRICKS.get(), 4)
                .pattern("MM")
                .pattern("MM")
                .define('M', SelectivepowersItems.SOLAR_BRICK)
                .unlockedBy("has_solar_brick", has(SelectivepowersItems.SOLAR_BRICK)).save(recipeOutput);


        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, SelectivepowersBlocks.CHISLED_SOLAR_BRICKS.get())
                .pattern("M")
                .pattern("M")
                .define('M', SelectivepowersBlocks.SOLAR_BRICK_SLAB)
                .unlockedBy("has_solar_brick_slabs", has(SelectivepowersBlocks.SOLAR_BRICK_SLAB)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SelectivepowersItems.GOLDEN_IDOL.get(), 1)
                .pattern("NNN")
                .pattern("NSN")
                .pattern(" M ")
                .define('S', SelectivepowersItems.FLAMING_EGG)
                .define('M', Items.GOLD_INGOT)
                .define('N', Items.GOLD_NUGGET)
                .unlockedBy("has_flaming_egg", has(SelectivepowersItems.FLAMING_EGG)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, SelectivepowersItems.CORRUPTING_ARROW.get(),4)
                .pattern("C")
                .pattern("S")
                .pattern("F")
                .define('C', SelectivepowersItems.CORRUPTION_SHARD)
                .define('S', ItemTags.create(ResourceLocation.fromNamespaceAndPath("c","rods/wooden")))
                .define('F', SelectivepowersItems.BURNING_FEATHER)
                .unlockedBy("has_burning_feather", has(SelectivepowersItems.BURNING_FEATHER)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, Items.ARROW,4)
                .pattern("C")
                .pattern("S")
                .pattern("F")
                .define('C', Items.FLINT)
                .define('S', ItemTags.create(ResourceLocation.fromNamespaceAndPath("c","rods/wooden")))
                .define('F', ItemTags.create(ResourceLocation.fromNamespaceAndPath("c","feathers")))
                .unlockedBy("has_feather", has(ItemTags.create(ResourceLocation.fromNamespaceAndPath("c","feathers")))).save(recipeOutput, "selectivepowers:arrow_any_feather");

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, Items.BRUSH)
                .pattern("C")
                .pattern("S")
                .pattern("F")
                .define('C', ItemTags.create(ResourceLocation.fromNamespaceAndPath("c","feathers")))
                .define('S', ItemTags.create(ResourceLocation.fromNamespaceAndPath("c","ingots/copper")))
                .define('F', ItemTags.create(ResourceLocation.fromNamespaceAndPath("c","rods/wooden")))
                .unlockedBy("has_feather", has(ItemTags.create(ResourceLocation.fromNamespaceAndPath("c","feathers")))).save(recipeOutput, "selectivepowers:brush_any_feather");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, Items.WRITABLE_BOOK)
                .requires(Items.BOOK)
                .requires(Items.INK_SAC)
                .requires(ItemTags.create(ResourceLocation.fromNamespaceAndPath("c","feathers")))
                .unlockedBy("has_ink_sac", has(Items.INK_SAC))
                .save(recipeOutput, "selectivepowers:written_book_any_feather");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, SelectivepowersItems.MOON_SACRIFICE, 9)
                .requires(SelectivepowersBlocks.MOON_SACRIFICE_BLOCK)
                .unlockedBy("has_moon_sacrifice_block", has(SelectivepowersBlocks.MOON_SACRIFICE_BLOCK))
                .save(recipeOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, SelectivepowersItems.SUN_SACRIFICE, 9)
                .requires(SelectivepowersBlocks.SUN_SACRIFICE_BLOCK)
                .unlockedBy("has_sun_sacrifice_block", has(SelectivepowersBlocks.SUN_SACRIFICE_BLOCK))
                .save(recipeOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, SelectivepowersBlocks.BLUBOTT_PLUSH)
                .requires(Items.ARMOR_STAND)
                .requires(Items.CYAN_WOOL)
                .unlockedBy("has_armor_stand", has(Items.ARMOR_STAND))
                .save(recipeOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, SelectivepowersBlocks.BLUMASC_PLUSH)
                .requires(Items.ARMOR_STAND)
                .requires(Items.BLUE_WOOL)
                .unlockedBy("has_armor_stand", has(Items.ARMOR_STAND))
                .save(recipeOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, SelectivepowersBlocks.RIKARASHI_PLUSH)
                .requires(Items.ARMOR_STAND)
                .requires(Items.ORANGE_WOOL)
                .unlockedBy("has_armor_stand", has(Items.ARMOR_STAND))
                .save(recipeOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, SelectivepowersItems.BOUND_CONTRACT)
                .requires(Items.PAPER)
                .requires(ItemTags.create(ResourceLocation.fromNamespaceAndPath("c","dyes/blue")))
                .unlockedBy("has_armor_stand", has(Items.PAPER))
                .save(recipeOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, SelectivepowersItems.DENIAL_CONTRACT)
                .requires(SelectivepowersItems.BOUND_CONTRACT)
                .requires(ItemTags.create(ResourceLocation.fromNamespaceAndPath("c","dyes/red")))
                .requires(ItemTags.create(ResourceLocation.fromNamespaceAndPath("c","feathers")))
                .unlockedBy("has_armor_stand", has(SelectivepowersItems.BOUND_CONTRACT))
                .save(recipeOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.BLUE_DYE, 2)
                .requires(SelectivepowersItems.MOONCAP_MUSHROOM)
                .unlockedBy("has_mooncap_mushroom", has(SelectivepowersItems.MOONCAP_MUSHROOM))
                .save(recipeOutput, "selectivepowers:blue_from_mooncap_mushrooms");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.BLUE_DYE)
                .requires(SelectivepowersItems.MOONCAP_SEEDS)
                .unlockedBy("has_mooncap_seeds", has(SelectivepowersItems.MOONCAP_SEEDS))
                .save(recipeOutput, "selectivepowers:blue_from_mooncap_seeds");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.BONE_MEAL, 5)
                .requires(SelectivepowersItems.BEETLE_HORN)
                .unlockedBy("has_beetle_horn", has(SelectivepowersItems.BEETLE_HORN))
                .save(recipeOutput, "selectivepowers:bone_meal_from_beetle_horn");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.NETHERITE_SCRAP, 5)
                .requires(SelectivepowersItems.SALAMANDER_SCALES)
                .requires(Items.NETHERITE_INGOT)
                .unlockedBy("has_salamander_scales", has(SelectivepowersItems.SALAMANDER_SCALES))
                .save(recipeOutput, "selectivepowers:netherite_scrap_from_scales");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, SelectivepowersItems.MUSHROOM_SKEWER)
                .requires(SelectivepowersBlocks.JUMP_MUSHROOM.get())
                .requires(ItemTags.create(ResourceLocation.fromNamespaceAndPath("c","mushrooms")))
                .requires(SelectivepowersBlocks.JUMP_MUSHROOM.get())
                .requires(Items.STICK)
                .unlockedBy("has_jump_mushroom", has(SelectivepowersBlocks.JUMP_MUSHROOM.get()))
                .save(recipeOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.PAPER)
                .requires(SelectivepowersItems.YELLOW_RAMBLINGS)
                .unlockedBy("has_yellow_ramblings", has(SelectivepowersItems.YELLOW_RAMBLINGS))
                .save(recipeOutput, "selectivepowers:paper_from_ramblings");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.STICK, 8)
                .requires(SelectivepowersBlocks.CROWS_NEST)
                .unlockedBy("has_crows_nest", has(SelectivepowersBlocks.CROWS_NEST))
                .save(recipeOutput, "selectivepowers:sticks_from_nest");
        oreSmelting(recipeOutput, Collections.singletonList(SelectivepowersItems.MOONCAP_MUSHROOM), RecipeCategory.MISC, SelectivepowersItems.MOON_SACRIFICE.get(), 0.25f, 200, "moon_sacrifice");
        oreSmelting(recipeOutput, Collections.singletonList(SelectivepowersItems.SUN_HORN), RecipeCategory.MISC, SelectivepowersItems.SUN_SACRIFICE.get(), 0.25f, 200, "sun_sacrifice");
        oreSmelting(recipeOutput, Collections.singletonList(SelectivepowersItems.SALAMANDER_SCALES), RecipeCategory.MISC, Items.NETHERRACK, 0.25f, 200, "scales_to_netherrack");
        oreSmelting(recipeOutput, Collections.singletonList(SelectivepowersItems.CORRUPTION_SHARD), RecipeCategory.MISC, SelectivepowersItems.SOLAR_BRICK.get(), 0.25f, 200, "solar_brick");
        oreSmelting(recipeOutput, Collections.singletonList(SelectivepowersBlocks.SOLAR_BLOCK), RecipeCategory.MISC, SelectivepowersItems.SOLAR_BRICK.get(), 0.25f, 200, "solar_brick_from_block");
        oreSmelting(recipeOutput, Collections.singletonList(SelectivepowersBlocks.MOON_DUST), RecipeCategory.MISC, SelectivepowersBlocks.MOON_GLASS.get(), 0.25f, 200, "moon_glass");
        oreSmelting(recipeOutput, Collections.singletonList(SelectivepowersBlocks.MOON_ROCK), RecipeCategory.MISC, SelectivepowersBlocks.SMOOTH_MOON_ROCK.get(), 0.25f, 200, "moon_rock");
        oreSmelting(recipeOutput, Collections.singletonList(SelectivepowersBlocks.SOLAR_BRICKS), RecipeCategory.MISC, SelectivepowersBlocks.CRACKED_SOLAR_BRICKS.get(), 0.25f, 200, "cracked_solar_bricks");
        stairBuilder(SelectivepowersBlocks.MOON_ROCK_BRICKS_STAIRS.get(), Ingredient.of(SelectivepowersBlocks.MOON_ROCK_BRICKS))
                .unlockedBy("has_moonrock_bricks", has(SelectivepowersBlocks.MOON_ROCK_BRICKS))
                .save(recipeOutput);
        stairBuilder(SelectivepowersBlocks.MOON_ROCK_TILES_STAIRS.get(), Ingredient.of(SelectivepowersBlocks.MOON_ROCK_TILES))
                .unlockedBy("has_moonrock_tiles", has(SelectivepowersBlocks.MOON_ROCK_TILES))
                .save(recipeOutput);
        stairBuilder(SelectivepowersBlocks.SMOOTH_MOON_ROCK_STAIRS.get(), Ingredient.of(SelectivepowersBlocks.SMOOTH_MOON_ROCK))
                .unlockedBy("has_smooth_moonrock", has(SelectivepowersBlocks.SMOOTH_MOON_ROCK))
                .save(recipeOutput);
        slab(recipeOutput, RecipeCategory.BUILDING_BLOCKS, SelectivepowersBlocks.MOON_ROCK_BRICKS_SLAB.get(), SelectivepowersBlocks.MOON_ROCK_BRICKS.get());
        slab(recipeOutput, RecipeCategory.BUILDING_BLOCKS, SelectivepowersBlocks.MOON_ROCK_TILES_SLAB.get(), SelectivepowersBlocks.MOON_ROCK_TILES.get());
        slab(recipeOutput, RecipeCategory.BUILDING_BLOCKS, SelectivepowersBlocks.SMOOTH_MOON_ROCK_SLAB.get(), SelectivepowersBlocks.SMOOTH_MOON_ROCK.get());
        wall(recipeOutput, RecipeCategory.BUILDING_BLOCKS, SelectivepowersBlocks.MOON_ROCK_BRICKS_WALL.get(), SelectivepowersBlocks.MOON_ROCK_BRICKS.get());
        wall(recipeOutput, RecipeCategory.BUILDING_BLOCKS, SelectivepowersBlocks.MOON_ROCK_TILES_WALL.get(), SelectivepowersBlocks.MOON_ROCK_TILES.get());
        wall(recipeOutput, RecipeCategory.BUILDING_BLOCKS, SelectivepowersBlocks.SMOOTH_MOON_ROCK_WALL.get(), SelectivepowersBlocks.SMOOTH_MOON_ROCK.get());
        netherFenceBuilder(SelectivepowersBlocks.SOLAR_BRICK_FENCE.get(), Ingredient.of(SelectivepowersItems.SOLAR_BRICK))
                .unlockedBy("has_solar_bricks", has(SelectivepowersItems.SOLAR_BRICK))
                .save(recipeOutput);
        wall(recipeOutput, RecipeCategory.BUILDING_BLOCKS, SelectivepowersBlocks.SOLAR_BRICK_WALL.get(), SelectivepowersBlocks.SOLAR_BRICKS.get());
        stairBuilder(SelectivepowersBlocks.SOLAR_BRICK_STAIRS.get(), Ingredient.of(SelectivepowersBlocks.SOLAR_BRICKS))
                .unlockedBy("has_solar_bricks", has(SelectivepowersBlocks.SOLAR_BRICKS))
                .save(recipeOutput);
        slab(recipeOutput, RecipeCategory.BUILDING_BLOCKS, SelectivepowersBlocks.SOLAR_BRICK_SLAB.get(), SelectivepowersBlocks.SOLAR_BRICKS.get());

        stonecutterList(recipeOutput, RecipeCategory.BUILDING_BLOCKS, SelectivepowersBlocks.MOON_ROCK, List.of(
                SelectivepowersBlocks.MOON_ROCK_BRICKS,
                SelectivepowersBlocks.SMOOTH_MOON_ROCK,
                SelectivepowersBlocks.MOON_ROCK_TILES,
                SelectivepowersBlocks.MOON_ROCK_BRICKS_STAIRS,
                SelectivepowersBlocks.MOON_ROCK_BRICKS_WALL,
                SelectivepowersBlocks.SMOOTH_MOON_ROCK_STAIRS,
                SelectivepowersBlocks.SMOOTH_MOON_ROCK_WALL,
                SelectivepowersBlocks.MOON_ROCK_TILES_STAIRS,
                SelectivepowersBlocks.MOON_ROCK_TILES_WALL
        ));
        stonecutting(recipeOutput, RecipeCategory.BUILDING_BLOCKS, SelectivepowersBlocks.MOON_ROCK_BRICKS_SLAB, SelectivepowersBlocks.MOON_ROCK, 2);
        stonecutting(recipeOutput, RecipeCategory.BUILDING_BLOCKS, SelectivepowersBlocks.MOON_ROCK_TILES_SLAB, SelectivepowersBlocks.MOON_ROCK, 2);
        stonecutting(recipeOutput, RecipeCategory.BUILDING_BLOCKS, SelectivepowersBlocks.SMOOTH_MOON_ROCK_SLAB, SelectivepowersBlocks.MOON_ROCK, 2);
        stonecutterList(recipeOutput, RecipeCategory.BUILDING_BLOCKS, SelectivepowersBlocks.SMOOTH_MOON_ROCK, List.of(
                SelectivepowersBlocks.MOON_ROCK_TILES,
                SelectivepowersBlocks.SMOOTH_MOON_ROCK_STAIRS,
                SelectivepowersBlocks.SMOOTH_MOON_ROCK_WALL,
                SelectivepowersBlocks.MOON_ROCK_TILES_STAIRS,
                SelectivepowersBlocks.MOON_ROCK_TILES_WALL
        ));
        stonecutting(recipeOutput, RecipeCategory.BUILDING_BLOCKS, SelectivepowersBlocks.MOON_ROCK_TILES_SLAB, SelectivepowersBlocks.SMOOTH_MOON_ROCK, 2);
        stonecutting(recipeOutput, RecipeCategory.BUILDING_BLOCKS, SelectivepowersBlocks.SMOOTH_MOON_ROCK_SLAB, SelectivepowersBlocks.SMOOTH_MOON_ROCK, 2);
        stonecutterList(recipeOutput, RecipeCategory.BUILDING_BLOCKS, SelectivepowersBlocks.MOON_ROCK_BRICKS, List.of(
                SelectivepowersBlocks.MOON_ROCK_BRICKS_STAIRS,
                SelectivepowersBlocks.MOON_ROCK_BRICKS_WALL
        ));
        stonecutting(recipeOutput, RecipeCategory.BUILDING_BLOCKS, SelectivepowersBlocks.MOON_ROCK_BRICKS_SLAB, SelectivepowersBlocks.MOON_ROCK_BRICKS, 2);
        stonecutterList(recipeOutput, RecipeCategory.BUILDING_BLOCKS, SelectivepowersBlocks.MOON_ROCK_TILES, List.of(
                SelectivepowersBlocks.MOON_ROCK_TILES_STAIRS,
                SelectivepowersBlocks.MOON_ROCK_TILES_WALL
        ));
        stonecutting(recipeOutput, RecipeCategory.BUILDING_BLOCKS, SelectivepowersBlocks.MOON_ROCK_TILES_SLAB, SelectivepowersBlocks.MOON_ROCK_TILES, 2);
        stonecutterList(recipeOutput, RecipeCategory.BUILDING_BLOCKS, SelectivepowersItems.SOLAR_BRICK, List.of(
                SelectivepowersBlocks.SOLAR_BRICK_FENCE,
                SelectivepowersBlocks.SOLAR_BRICKS,
                SelectivepowersBlocks.SOLAR_BRICK_WALL,
                SelectivepowersBlocks.CHISLED_SOLAR_BRICKS,
                SelectivepowersBlocks.CRACKED_SOLAR_BRICKS
        ));
        stonecutting(recipeOutput, RecipeCategory.BUILDING_BLOCKS, SelectivepowersBlocks.SOLAR_BRICK_SLAB, SelectivepowersItems.SOLAR_BRICK, 2);
        stonecutterList(recipeOutput, RecipeCategory.BUILDING_BLOCKS, SelectivepowersBlocks.SOLAR_BRICKS, List.of(
                SelectivepowersBlocks.SOLAR_BRICK_FENCE,
                SelectivepowersBlocks.SOLAR_BRICK_WALL,
                SelectivepowersBlocks.CHISLED_SOLAR_BRICKS,
                SelectivepowersBlocks.CRACKED_SOLAR_BRICKS
        ));
        stonecutting(recipeOutput, RecipeCategory.BUILDING_BLOCKS, SelectivepowersBlocks.SOLAR_BRICK_SLAB, SelectivepowersBlocks.SOLAR_BRICKS, 2);
        stonecutting(recipeOutput, RecipeCategory.BUILDING_BLOCKS, SelectivepowersBlocks.EARTH_GLASS, SelectivepowersBlocks.MOON_GLASS);
        foodSmelting(recipeOutput, Collections.singletonList(SelectivepowersItems.MUSHROOM_SKEWER), RecipeCategory.FOOD, SelectivepowersItems.COOKED_MUSHROOM_SKEWER.get(), 0.25f, 200, "cooked_mushrrom_skewer");

    }
    protected static void stonecutterList(RecipeOutput recipeOutput, RecipeCategory recipeCategory, ItemLike input, List<ItemLike> outputs)
    {
        for (ItemLike e : outputs)
        {
            stonecutting(recipeOutput, recipeCategory, e, input);
        }
    }
    protected static void oreSmelting(RecipeOutput recipeOutput, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult,
                                      float pExperience, int pCookingTIme, String pGroup) {
        oreCooking(recipeOutput, RecipeSerializer.SMELTING_RECIPE, SmeltingRecipe::new, pIngredients, pCategory, pResult,
                pExperience, pCookingTIme, pGroup, "_from_smelting");
    }

    protected static void stonecutting(RecipeOutput recipeOutput, RecipeCategory recipeCategory, ItemLike output, ItemLike input){
        stonecutting(recipeOutput, recipeCategory, output, input, 1);
    }
    protected static void stonecutting(RecipeOutput recipeOutput, RecipeCategory category, ItemLike result, ItemLike material, int resultCount) {
        SingleItemRecipeBuilder recipeBuilder = SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[]{material}), category, result, resultCount).unlockedBy(getHasName(material), has(material));
        String recipeName = getConversionRecipeName(result, material);
        recipeBuilder.save(recipeOutput, SelectivePowers.MODID+":stonecutting/"+recipeName + "_stonecutting");
    }

    protected static void oreBlasting(RecipeOutput recipeOutput, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult,
                                      float pExperience, int pCookingTime, String pGroup) {
        oreCooking(recipeOutput, RecipeSerializer.BLASTING_RECIPE, BlastingRecipe::new, pIngredients, pCategory, pResult,
                pExperience, pCookingTime, pGroup, "_from_blasting");
    }

    protected static void oreSmoking(RecipeOutput recipeOutput, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult,
                                      float pExperience, int pCookingTime, String pGroup) {
        oreCooking(recipeOutput, RecipeSerializer.SMOKING_RECIPE, SmokingRecipe::new, pIngredients, pCategory, pResult,
                pExperience, pCookingTime, pGroup, "_from_smoking");
    }
    protected static void oreCampfire(RecipeOutput recipeOutput, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult,
                                     float pExperience, int pCookingTime, String pGroup) {
        oreCooking(recipeOutput, RecipeSerializer.CAMPFIRE_COOKING_RECIPE, CampfireCookingRecipe::new, pIngredients, pCategory, pResult,
                pExperience, pCookingTime, pGroup, "_from_campfire");
    }
    protected static void foodSmelting(RecipeOutput recipeOutput, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult,
                                      float pExperience, int pCookingTime, String pGroup) {
        oreSmelting(recipeOutput, pIngredients, pCategory, pResult, pExperience, pCookingTime, pGroup);
        oreSmoking(recipeOutput, pIngredients, pCategory, pResult, pExperience, pCookingTime/2, pGroup);
        oreCampfire(recipeOutput, pIngredients, pCategory, pResult, pExperience, pCookingTime*3, pGroup);
    }

    protected static <T extends AbstractCookingRecipe> void oreCooking(RecipeOutput recipeOutput, RecipeSerializer<T> pCookingSerializer, AbstractCookingRecipe.Factory<T> factory,
                                                                       List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup, String pRecipeName) {
        for(ItemLike itemlike : pIngredients) {
            SimpleCookingRecipeBuilder.generic(Ingredient.of(itemlike), pCategory, pResult, pExperience, pCookingTime, pCookingSerializer, factory).group(pGroup).unlockedBy(getHasName(itemlike), has(itemlike))
                    .save(recipeOutput, SelectivePowers.MODID + ":smelting/" + getItemName(pResult) + pRecipeName + "_" + getItemName(itemlike));
        }
    }

    protected static RecipeBuilder netherFenceBuilder(ItemLike fence, Ingredient material) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, fence, 6).define('W', material).pattern("WWW").pattern("WWW");
    }
}
