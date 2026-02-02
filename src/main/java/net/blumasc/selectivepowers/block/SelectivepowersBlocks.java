package net.blumasc.selectivepowers.block;

import com.mojang.serialization.MapCodec;
import net.blumasc.selectivepowers.SelectivePowers;
import net.blumasc.selectivepowers.block.custom.*;
import net.blumasc.selectivepowers.item.SelectivepowersItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class SelectivepowersBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(SelectivePowers.MODID);

    public static  final DeferredBlock<Block> BLUMASC_PLUSH = registerBlock("blumasc_plush",
            () -> new PlushBlock(BlockBehaviour.Properties.of().noOcclusion().strength(0f).sound(SoundType.WOOL)));

    public static  final DeferredBlock<Block> RIKARASHI_PLUSH = registerBlock("rikarashi_plush",
            () -> new PlushBlock(BlockBehaviour.Properties.of().noOcclusion().strength(0f).sound(SoundType.WOOL)));

    public static  final DeferredBlock<Block> BLUBOTT_PLUSH = registerBlock("blubott_plush",
            () -> new PlushBlock(BlockBehaviour.Properties.of().noOcclusion().strength(0f).sound(SoundType.WOOL)));

    public static final DeferredBlock<Block> ALTAR = registerBlock("altar",
            () -> new AltarBlock(BlockBehaviour.Properties.of().noOcclusion()));

    public static  final DeferredBlock<Block> MOON_SACRIFICE_BLOCK = registerBlock("moon_sacrifice_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(2f).sound(SoundType.AMETHYST).mapColor(MapColor.COLOR_BLUE)));

    public static  final DeferredBlock<Block> INLET_MOON_SACRIFICE_BLOCK = registerBlock("inlet_moon_sacrifice_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(2f).sound(SoundType.STONE)));

    public static  final DeferredBlock<Block> SUN_SACRIFICE_BLOCK = registerBlock("sun_sacrifice_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(2f).sound(SoundType.AMETHYST).mapColor(MapColor.COLOR_YELLOW)));

    public static  final DeferredBlock<Block> INLET_SUN_SACRIFICE_BLOCK = registerBlock("inlet_sun_sacrifice_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(2f).sound(SoundType.STONE)));

    public static final DeferredBlock<Block> MOONCAP_CROP = BLOCKS.register("mooncap_crop",
            () -> new MooncapCropBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.WHEAT).mapColor(MapColor.COLOR_BLUE)));

    public static final DeferredBlock<Block> CROWS_NEST = registerBlock("crows_nest",
            () -> new NestBlock(BlockBehaviour.Properties.of().strength(0.1f).sound(SoundType.CROP).noOcclusion().mapColor(MapColor.WOOD)));

    public static final DeferredBlock<Block> SUN_LANTERN = registerBlock("sun_lantern",
            () -> new LanternBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.LANTERN)));
    public static final DeferredBlock<Block> MOON_LANTERN = registerBlock("moon_lantern",
            () -> new LanternBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.LANTERN)));
    public static final DeferredBlock<Block> DORMANT_ECHO_CRAB = registerBlock("dormant_echo_crab",
            () -> new DormantEchoCrabBlock(BlockBehaviour.Properties.of().sound(SoundType.SCULK).noOcclusion().randomTicks()));

    public static final DeferredBlock<Block> MOON_ROCK = registerBlock("moon_rock",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE).mapColor(MapColor.STONE)));

    public static final DeferredBlock<Block> MOON_DUST = registerBlock("moon_dust",
            () -> new SPFallingBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SAND).mapColor(MapColor.COLOR_LIGHT_GRAY)));

    public static final DeferredBlock<Block> MOON_GLASS = registerBlock("moon_glass",
            () -> new TransparentBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).mapColor(MapColor.STONE)));

    public static final DeferredBlock<Block> EARTH_GLASS = registerBlock("earth_glass",
            () -> new TransparentBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).mapColor(MapColor.COLOR_BLUE)));

    public static final DeferredBlock<Block> SMOOTH_MOON_ROCK = registerBlock("smooth_moon_rock",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE).mapColor(MapColor.STONE)));

    public static final DeferredBlock<Block> MOON_ROCK_BRICKS = registerBlock("moon_bricks",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE).mapColor(MapColor.STONE)));

    public static final DeferredBlock<Block> MOON_ROCK_TILES = registerBlock("moon_tiles",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE).mapColor(MapColor.STONE)));

    public static final DeferredBlock<Block> MOON_CAP_BLOCK = registerBlock("mooncap_block",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.NETHER_WART_BLOCK).mapColor(MapColor.COLOR_BLUE)));

    public static final DeferredBlock<StairBlock> SMOOTH_MOON_ROCK_STAIRS = registerBlock("smooth_moon_rock_staris",
            () -> new StairBlock(SelectivepowersBlocks.SMOOTH_MOON_ROCK.get().defaultBlockState(),
                    BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_BRICK_STAIRS).mapColor(MapColor.STONE)));
    public static final DeferredBlock<SlabBlock> SMOOTH_MOON_ROCK_SLAB = registerBlock("smooth_moon_rock_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_BRICK_SLAB).mapColor(MapColor.STONE)));
    public static final DeferredBlock<WallBlock> SMOOTH_MOON_ROCK_WALL = registerBlock("smooth_moon_rock_wall",
            () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_BRICK_WALL).mapColor(MapColor.STONE)));

    public static final DeferredBlock<StairBlock> MOON_ROCK_BRICKS_STAIRS = registerBlock("moon_bricks_staris",
            () -> new StairBlock(SelectivepowersBlocks.MOON_ROCK_BRICKS.get().defaultBlockState(),
                    BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_BRICK_STAIRS).mapColor(MapColor.STONE)));
    public static final DeferredBlock<SlabBlock> MOON_ROCK_BRICKS_SLAB = registerBlock("moon_bricks_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_BRICK_SLAB).mapColor(MapColor.STONE)));
    public static final DeferredBlock<WallBlock> MOON_ROCK_BRICKS_WALL = registerBlock("moon_bricks_wall",
            () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_BRICK_WALL).mapColor(MapColor.STONE)));

    public static final DeferredBlock<StairBlock> MOON_ROCK_TILES_STAIRS = registerBlock("moon_tiles_staris",
            () -> new StairBlock(SelectivepowersBlocks.MOON_ROCK_TILES.get().defaultBlockState(),
                    BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_BRICK_STAIRS).mapColor(MapColor.STONE)));
    public static final DeferredBlock<SlabBlock> MOON_ROCK_TILES_SLAB = registerBlock("moon_tiles_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_BRICK_SLAB).mapColor(MapColor.STONE)));
    public static final DeferredBlock<WallBlock> MOON_ROCK_TILES_WALL = registerBlock("moon_tiles_wall",
            () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_BRICK_WALL).mapColor(MapColor.STONE)));

    public static final DeferredBlock<Block> JUMP_MUSHROOM = registerBlock("jump_mushroom",
            () -> new JumpingMushroom(BlockBehaviour.Properties.ofFullCopy(Blocks.MUSHROOM_STEM).noCollission().noOcclusion().mapColor(MapColor.COLOR_LIGHT_GREEN)));

    public static final DeferredBlock<MagmaticBlock> SOLAR_BLOCK = registerBlock("solar_block",
            () -> new MagmaticBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.MAGMA_BLOCK)));

    public static final DeferredBlock<MagmaticBlock> M_SOLAR_BLOCK = registerBlock("m_solar_block",
            () -> new UltraMagmaticBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.MAGMA_BLOCK)));

    public static final DeferredBlock<Block> SOLAR_BRICKS = registerBlock("solar_bricks",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.NETHER_BRICKS).mapColor(MapColor.COLOR_YELLOW)));

    public static final DeferredBlock<Block> CRACKED_SOLAR_BRICKS = registerBlock("cracked_solar_bricks",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.NETHER_BRICKS).mapColor(MapColor.COLOR_YELLOW)));

    public static final DeferredBlock<Block> CHISLED_SOLAR_BRICKS = registerBlock("chisled_solar_bricks",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.NETHER_BRICKS).mapColor(MapColor.COLOR_YELLOW)));

    public static final DeferredBlock<WallBlock> SOLAR_BRICK_WALL = registerBlock("solar_brick_wall",
            () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_BRICK_WALL).mapColor(MapColor.COLOR_YELLOW)));

    public static final DeferredBlock<StairBlock> SOLAR_BRICK_STAIRS = registerBlock("solar_brick_staris",
            () -> new StairBlock(SelectivepowersBlocks.SOLAR_BRICKS.get().defaultBlockState(),
                    BlockBehaviour.Properties.ofFullCopy(Blocks.NETHER_BRICK_STAIRS).mapColor(MapColor.COLOR_YELLOW)));

    public static final DeferredBlock<FenceBlock> SOLAR_BRICK_FENCE = registerBlock("solar_brick_fence",
            () -> new FenceBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.NETHER_BRICK_FENCE).mapColor(MapColor.COLOR_YELLOW)));

    public static final DeferredBlock<SlabBlock> SOLAR_BRICK_SLAB = registerBlock("solar_brick_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.NETHER_BRICK_SLAB).mapColor(MapColor.COLOR_YELLOW)));

    public static final DeferredBlock<Block> OBSIDIAN_DUST = registerBlock("obsidian_sand",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.OBSIDIAN).sound(SoundType.GRAVEL)));

    public static final DeferredBlock<ThornsBlock> ROSE_VINES = registerBlock("rose_bush",
            () -> new ThornsBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COBWEB).mapColor(MapColor.COLOR_GREEN)));
    public static final DeferredBlock<DraconicBeaconBlock> DRACONIC_BEACON = registerBlock("draconic_beacon",
            () -> new DraconicBeaconBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.ENCHANTING_TABLE).noOcclusion().mapColor(MapColor.STONE)));

    public static final DeferredBlock<VoidBlock> VOID_BLOCK = registerBlock("void_block",
            () -> new VoidBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BLACK_WOOL)));

    public static final DeferredBlock<SporeMushroomBlock> SPORE_MUSHROOM_BLOCK = registerBlock("spore_mushroom",
            () -> new SporeMushroomBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.MUSHROOM_STEM).noOcclusion().mapColor(MapColor.COLOR_BLACK)));

    public static final DeferredBlock<ProtectionEffigyBlock> PROTECTION_EFFIGY_BLOCK = registerBlock("protection_effigy",
            () -> new ProtectionEffigyBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_BLOCK).noOcclusion().lightLevel((bs) -> 15).mapColor(MapColor.QUARTZ)));

    public static final DeferredBlock<GoldenGoatBlock> GOLDEN_GOAT = registerBlock("golden_goat",
            () -> new GoldenGoatBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GOLD_BLOCK).noOcclusion()));

    public static final DeferredBlock<TesslaCoilBlock> TESSLA_COIL = registerBlock("tessla_coil",
            () -> new TesslaCoilBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.LIGHTNING_ROD).noOcclusion()));

    public static final DeferredBlock<SacrificialAltarBlock> SACRIFICIAL_ALTAR_BLOCK = registerBlock("decrepit_sacrifical_altar",
            () -> new SacrificialAltarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.ANDESITE).noOcclusion().lightLevel((b) -> 3)));
    public static final DeferredBlock<TrapBlock> PITFALL_TRAP = registerBlock("pitfall_trap",
            () -> new TrapBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DIRT).noOcclusion()));

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block){
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block)
    {
        SelectivepowersItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus){
        BLOCKS.register(eventBus);
    }
}
