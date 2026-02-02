package net.blumasc.selectivepowers.item;

import net.blumasc.selectivepowers.SelectivePowers;
import net.blumasc.selectivepowers.block.SelectivepowersBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class SelectivepowersCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, SelectivePowers.MODID);

    public static final Supplier<CreativeModeTab> SELECTIVE_POWERS_TAB = CREATIVE_MODE_TAB.register("selective_powers_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(SelectivepowersItems.TRUE_CROWN.get())).title(Component.translatable("itemGroup.selectivepowers"))
                    .displayItems((itemDisplayParameters, output) -> {
                        //Deal Items
                      output.accept(SelectivepowersItems.SOULGEM);
                      output.accept(SelectivepowersItems.DENIAL_CONTRACT);
                      output.accept(SelectivepowersItems.BOUND_CONTRACT);
                      output.accept(SelectivepowersItems.GOLDEN_IDOL);
                      output.accept(SelectivepowersItems.BLESSED_IDOL);
                      output.accept(SelectivepowersItems.MOON_SACRIFICE);
                      output.accept(SelectivepowersItems.SUN_SACRIFICE);
                      //Misc Items
                        output.accept(SelectivepowersItems.SUN_SLICER);
                        output.accept(SelectivepowersItems.MOONLIGHT_GLAIVE);
                        output.accept(SelectivepowersItems.ICE_SHIELD);
                        output.accept(SelectivepowersItems.FLAMING_HAMMER);
                        output.accept(SelectivepowersItems.TRUE_CROWN);
                        output.accept(SelectivepowersItems.FAKE_CROWN);
                        output.accept(SelectivepowersItems.MOON_PENDANT);
                        output.accept(SelectivepowersItems.HORNED_HELMET);
                        output.accept(SelectivepowersItems.BEETLE_HORN);
                        output.accept(SelectivepowersItems.SUN_HORN);
                        output.accept(SelectivepowersItems.MOONCAP_SEEDS);
                        output.accept(SelectivepowersItems.MOONCAP_MUSHROOM);
                        output.accept(SelectivepowersItems.MOONLIGHT_JELLY);
                        output.accept(SelectivepowersItems.SALAMANDER_SCALES);
                        output.accept(SelectivepowersItems.CORRUPTION_SHARD);
                        output.accept(SelectivepowersItems.YELLOW_RAMBLINGS);
                        output.accept(SelectivepowersItems.FLAMING_EGG);
                        output.accept(SelectivepowersItems.BURNING_FEATHER);
                        output.accept(SelectivepowersItems.MOON_TAKOYAKI);
                        output.accept(SelectivepowersItems.CORRUPTING_ARROW);
                        output.accept(SelectivepowersItems.BEAM_ARROW);
                        output.accept(SelectivepowersItems.SOLAR_BRICK);
                        output.accept(SelectivepowersItems.RAGE_COOKIE);
                        output.accept(SelectivepowersItems.LIGHTNING_IN_A_BOTTLE);
                        output.accept(SelectivepowersItems.MUSHROOM_SKEWER);
                        output.accept(SelectivepowersItems.COOKED_MUSHROOM_SKEWER);
                        output.accept(SelectivepowersItems.BATTLE_FOR_ETERNITY_MUSIC_DISC);
                        output.accept(SelectivepowersItems.CHIMERA_CORE);
                        output.accept(SelectivepowersItems.LEAFWALKER_CURIO);
                        output.accept(SelectivepowersItems.SOLID_VOID);
                        output.accept(SelectivepowersItems.DRAGON_SLEEVES);
                        output.accept(SelectivepowersItems.PROSPECTORS_SHOVEL);
                        output.accept(SelectivepowersItems.ELEMENTAL_GUN);
                        output.accept(SelectivepowersItems.DRINKING_HORN);
                      output.accept(SelectivepowersItems.LORE_SCROLL);
                      //Deco Curios
                      output.accept(SelectivepowersItems.CORRUPTED_MASK);
                        output.accept(SelectivepowersItems.SHOULDER_CROW);
                        output.accept(SelectivepowersItems.WOLF_EARS);
                        output.accept(SelectivepowersItems.WOLF_TAIL);
                      output.accept(SelectivepowersItems.POLARBEAR_PELT);
                        output.accept(SelectivepowersItems.HIP_HORN);
                        output.accept(SelectivepowersItems.FACE_PAINT);
                      output.accept(SelectivepowersItems.SHELF_MUSHROOM);
                      output.accept(SelectivepowersItems.OCTOPUS_MUSHROOM);
                      output.accept(SelectivepowersItems.MUSHROOM_SPORES);
                      output.accept(SelectivepowersItems.HIP_BOOK);
                        output.accept(SelectivepowersItems.GLOWING_EYES);
                      output.accept(SelectivepowersItems.ENCHANTING_RUNE);
                      output.accept(SelectivepowersItems.STORM_CLOUD);
                      output.accept(SelectivepowersItems.LIGHTNING_BALL);
                      output.accept(SelectivepowersItems.POCKET_TESSLA_COIL);
                        output.accept(SelectivepowersItems.HALO);
                        output.accept(SelectivepowersItems.RUNE);
                      output.accept(SelectivepowersItems.CELESTIAL_RUNE);
                      output.accept(SelectivepowersItems.MOSS_LAYER);
                        output.accept(SelectivepowersItems.SPINE_TREE);
                        output.accept(SelectivepowersItems.FLOWER_CROWN);
                      output.accept(SelectivepowersItems.EMBEDDED_CRYSTALS);
                        output.accept(SelectivepowersItems.MINERS_HAT);
                        output.accept(SelectivepowersItems.DUST_MASK);
                      output.accept(SelectivepowersItems.DUALITY_PEARL);
                        output.accept(SelectivepowersItems.ELEMENTAL_GLOVES);
                      output.accept(SelectivepowersItems.ELEMENTAL_CIRCLET);
                        output.accept(SelectivepowersItems.DRAGON_CLAWS);
                        output.accept(SelectivepowersItems.DRAGON_HORNS);
                      output.accept(SelectivepowersItems.BREATH_BAUBLE);
                      output.accept(SelectivepowersItems.SCULK_MOSS);
                        output.accept(SelectivepowersItems.WARDEN_HORNS);
                      output.accept(SelectivepowersItems.SCULK_TENDRIL);
                      //Spawn Eggs
                        output.accept(SelectivepowersItems.CROW_SPAWN_EGG);
                        output.accept(SelectivepowersItems.PACKWING_SPAWN_EGG);
                        output.accept(SelectivepowersItems.SOLAR_BEETLE_SPAWN_EGG);
                        output.accept(SelectivepowersItems.SALAMANDER_SPAWN_EGG);
                        output.accept(SelectivepowersItems.ECHO_CRAB_SPAWN_EGG);
                        output.accept(SelectivepowersItems.MOON_SQUID_SPAWN_EGG);
                        output.accept(SelectivepowersItems.QUETZAL_SPAWN_EGG);
                        output.accept(SelectivepowersItems.YELLOW_QUETZAL_SPAWN_EGG);
                        output.accept(SelectivepowersItems.YELLOW_FANATIC_SPAWN_EGG);
                        output.accept(SelectivepowersItems.CORRUPTING_MASK_SPAWN_EGG);
                        output.accept(SelectivepowersItems.YELLOW_KING_SPAWN_EGG);
                        //Blocks
                        output.accept(SelectivepowersBlocks.ALTAR);
                        output.accept(SelectivepowersBlocks.MOON_SACRIFICE_BLOCK);
                        output.accept(SelectivepowersBlocks.SUN_SACRIFICE_BLOCK);
                        output.accept(SelectivepowersBlocks.INLET_MOON_SACRIFICE_BLOCK);
                        output.accept(SelectivepowersBlocks.INLET_SUN_SACRIFICE_BLOCK);
                        output.accept(SelectivepowersBlocks.MOON_LANTERN);
                        output.accept(SelectivepowersBlocks.SUN_LANTERN);
                        output.accept(SelectivepowersBlocks.CROWS_NEST);
                        output.accept(SelectivepowersBlocks.MOON_CAP_BLOCK);
                        output.accept(SelectivepowersBlocks.DORMANT_ECHO_CRAB);
                        output.accept(SelectivepowersBlocks.JUMP_MUSHROOM);
                        output.accept(SelectivepowersBlocks.MOON_DUST);
                        output.accept(SelectivepowersBlocks.MOON_GLASS);
                        output.accept(SelectivepowersBlocks.MOON_ROCK);
                        output.accept(SelectivepowersBlocks.SMOOTH_MOON_ROCK);
                        output.accept(SelectivepowersBlocks.SMOOTH_MOON_ROCK_SLAB);
                        output.accept(SelectivepowersBlocks.SMOOTH_MOON_ROCK_STAIRS);
                        output.accept(SelectivepowersBlocks.SMOOTH_MOON_ROCK_WALL);
                        output.accept(SelectivepowersBlocks.MOON_ROCK_BRICKS);
                        output.accept(SelectivepowersBlocks.MOON_ROCK_BRICKS_SLAB);
                        output.accept(SelectivepowersBlocks.MOON_ROCK_BRICKS_STAIRS);
                        output.accept(SelectivepowersBlocks.MOON_ROCK_BRICKS_WALL);
                        output.accept(SelectivepowersBlocks.MOON_ROCK_TILES);
                        output.accept(SelectivepowersBlocks.MOON_ROCK_TILES_SLAB);
                        output.accept(SelectivepowersBlocks.MOON_ROCK_TILES_STAIRS);
                        output.accept(SelectivepowersBlocks.MOON_ROCK_TILES_WALL);
                        output.accept(SelectivepowersBlocks.SOLAR_BLOCK);
                        output.accept(SelectivepowersBlocks.SOLAR_BRICKS);
                        output.accept(SelectivepowersBlocks.CRACKED_SOLAR_BRICKS);
                        output.accept(SelectivepowersBlocks.CHISLED_SOLAR_BRICKS);
                        output.accept(SelectivepowersBlocks.SOLAR_BRICK_FENCE);
                        output.accept(SelectivepowersBlocks.SOLAR_BRICK_WALL);
                        output.accept(SelectivepowersBlocks.SOLAR_BRICK_STAIRS);
                        output.accept(SelectivepowersBlocks.SOLAR_BRICK_SLAB);
                        output.accept(SelectivepowersBlocks.OBSIDIAN_DUST);
                        output.accept(SelectivepowersBlocks.ROSE_VINES);
                        output.accept(SelectivepowersBlocks.DRACONIC_BEACON);
                        output.accept(SelectivepowersBlocks.SPORE_MUSHROOM_BLOCK);
                        output.accept(SelectivepowersBlocks.PROTECTION_EFFIGY_BLOCK);
                        output.accept(SelectivepowersBlocks.GOLDEN_GOAT);
                        output.accept(SelectivepowersBlocks.TESSLA_COIL);
                        output.accept(SelectivepowersBlocks.SACRIFICIAL_ALTAR_BLOCK);
                        output.accept(SelectivepowersBlocks.PITFALL_TRAP);
                        //Funny Stuff
                        output.accept(SelectivepowersBlocks.BLUMASC_PLUSH);
                        output.accept(SelectivepowersBlocks.RIKARASHI_PLUSH);
                        output.accept(SelectivepowersBlocks.BLUBOTT_PLUSH);
                    }).build());

    public static void register(IEventBus eventBus)
    {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
