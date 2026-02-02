package net.blumasc.selectivepowers.item;

import net.blumasc.selectivepowers.SelectivePowers;
import net.blumasc.selectivepowers.block.SelectivepowersBlocks;
import net.blumasc.selectivepowers.entity.SelectivepowersEntities;
import net.blumasc.selectivepowers.item.custom.*;
import net.blumasc.selectivepowers.sound.SelectivepowersSounds;
import net.minecraft.world.item.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class SelectivepowersItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(SelectivePowers.MODID);

    public static final DeferredItem<Item> SOULGEM = ITEMS.register("soulgem",
            () -> new FreeSoulItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> DENIAL_CONTRACT = ITEMS.register("denial_contract",
            () -> new DealDenialItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> BOUND_CONTRACT = ITEMS.register("bound_contract",
            () -> new DealStatusItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> CROW_SPAWN_EGG = ITEMS.register("crow_spawn_egg",
            () -> new DeferredSpawnEggItem(SelectivepowersEntities.CROW, 0x030303, 0xfafafa, new Item.Properties()));

    public static final DeferredItem<Item> PACKWING_SPAWN_EGG = ITEMS.register("packwing_spawn_egg",
            () -> new DeferredSpawnEggItem(SelectivepowersEntities.PACKWING, 0xffffff, 0xfafafa, new Item.Properties()));

    public static final DeferredItem<Item> YELLOW_KING_SPAWN_EGG = ITEMS.register("yellow_king_spawn_egg",
            () -> new DeferredSpawnEggItem(SelectivepowersEntities.YELLOW_KING_BOSS, 0xffd700, 0xddd618, new Item.Properties()));

    public static final DeferredItem<Item> YELLOW_FANATIC_SPAWN_EGG = ITEMS.register("yellow_fanatic_spawn_egg",
            () -> new DeferredSpawnEggItem(SelectivepowersEntities.YELLOW_FANATIC, 0xffd700, 0x080808, new Item.Properties()));

    public static final DeferredItem<Item> CORRUPTING_MASK_SPAWN_EGG = ITEMS.register("corrupting_mask_spawn_egg",
            () -> new DeferredSpawnEggItem(SelectivepowersEntities.CORRUPTING_MASK, 0xffd700, 0xfdfdfd, new Item.Properties()));

    public static final DeferredItem<Item> GOLDEN_IDOL = ITEMS.register("golden_idol",
            () -> new Item(new Item.Properties().stacksTo(8)));

    public static final DeferredItem<Item> BLESSED_IDOL = ITEMS.register("blessed_idol",
            () -> new GlintItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> BEETLE_HORN = ITEMS.register("beetle_horn",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> SUN_HORN= ITEMS.register("sun_horn",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> MOON_SACRIFICE= ITEMS.register("moon_sacrifice",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> SUN_SACRIFICE= ITEMS.register("sun_sacrifice",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> MOONCAP_MUSHROOM= ITEMS.register("mooncap_mushroom",
            () -> new Item(new Item.Properties().food(SelectiveFoodProperties.MOONCAP_MUSHROOM)));

    public static final DeferredItem<Item> MOONLIGHT_JELLY = ITEMS.register("moonlight_jelly",
            () -> new MoonlightJellyItem(new Item.Properties().food(SelectiveFoodProperties.MOONLIGHT_JELLY).stacksTo(8)));

    public static final DeferredItem<Item> MOONCAP_SEEDS = ITEMS.register("mooncap_seeds",
    ()-> new ItemNameBlockItem(SelectivepowersBlocks.MOONCAP_CROP.get(), new Item.Properties()));

    public static  final DeferredItem<SwordItem> SUN_SLICER = ITEMS.register("sun_slicer",
            () -> new SunSlicerItem(new Item.Properties().attributes(SwordItem.createAttributes(Tiers.NETHERITE, 3, -2.4F))));

    public static final DeferredItem<SwordItem> MOONLIGHT_GLAIVE = ITEMS.register("moonlight_glaive",
            () -> new MoonGlaiveItem(new Item.Properties().attributes(SwordItem.createAttributes(Tiers.NETHERITE, 3, -2.4F))));

    public static final DeferredItem<Item> SOLAR_BEETLE_SPAWN_EGG = ITEMS.register("solar_beetle_spawn_egg",
            () -> new DeferredSpawnEggItem(SelectivepowersEntities.SOLAR_BEETLE, 0x721011, 0xfff123, new Item.Properties()));

    public static final DeferredItem<Item> SALAMANDER_SPAWN_EGG = ITEMS.register("salamander_spawn_egg",
            () -> new DeferredSpawnEggItem(SelectivepowersEntities.SALAMANDER, 0xb67c3b, 0xff6c2c, new Item.Properties()));

    public static final DeferredItem<Item> ECHO_CRAB_SPAWN_EGG = ITEMS.register("echo_crab_spawn_egg",
            () -> new DeferredSpawnEggItem(SelectivepowersEntities.ECHO_CRAB, 0x101b21, 0x33d8de, new Item.Properties()));

    public static final DeferredItem<Item> MOON_SQUID_SPAWN_EGG = ITEMS.register("moon_squid_spawn_egg",
            () -> new DeferredSpawnEggItem(SelectivepowersEntities.MOON_SQUID, 0x334697, 0x9caefa, new Item.Properties()));

    public static final DeferredItem<Item> QUETZAL_SPAWN_EGG = ITEMS.register("quetzal_spawn_egg",
            () -> new DeferredSpawnEggItem(SelectivepowersEntities.QUETZAL, 0x6d1919, 0x824d21, new Item.Properties()));

    public static final DeferredItem<Item> YELLOW_QUETZAL_SPAWN_EGG = ITEMS.register("yellow_quetzal_spawn_egg",
            () -> new DeferredSpawnEggItem(SelectivepowersEntities.QUETZAL_YELLOW, 0x8f6b17, 0x824d21, new Item.Properties()));

    public static final DeferredItem<Item> LUNAR_MAIDEN_SPAWN_EGG = ITEMS.register("lunar_maiden_spawn_egg",
            () -> new DeferredSpawnEggItem(SelectivepowersEntities.LUNAR_MAIDEN, 0xc0c0c0, 0xadd8e6, new Item.Properties()));


    public static final DeferredItem<Item> FAKE_CROWN = ITEMS.register("crown",
            () -> new CurioItem());
    public static final DeferredItem<Item> TRUE_CROWN = ITEMS.register("true_crown",
            () -> new CurioItem());
    public static final DeferredItem<Item> MOON_PENDANT = ITEMS.register("moon_pendant",
            () -> new CurioItem());
    public static final DeferredItem<Item> CORRUPTED_MASK = ITEMS.register("mask",
            () -> new CurioItem());
    public static final DeferredItem<Item> SALAMANDER_GOO = ITEMS.register("salamander_goo",
            () -> new SalamanderGooItem(new Item.Properties()));
    public static final DeferredItem<Item> SALAMANDER_SCALES = ITEMS.register("salamander_scales",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> HORNED_HELMET = ITEMS.register("horned_helmet",
    () -> new HornHelmetArmorItem());
    public static final DeferredItem<Item> CORRUPTION_SHARD = ITEMS.register("corruption_shards",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> YELLOW_RAMBLINGS = ITEMS.register("yellow_ramblings",
            () -> new PageItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> LIGHT_RUNE = ITEMS.register("light_rune",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> FLAMING_EGG = ITEMS.register("flaming_egg",
            () -> new FlamingEggItem(new Item.Properties().stacksTo(16)));

    public static final DeferredItem<Item> MOON_TAKOYAKI= ITEMS.register("moon_takoyaki",
            () -> new Item(new Item.Properties().food(SelectiveFoodProperties.LUNAR_TAKOYAKI)));

    public static final DeferredItem<Item> BURNING_FEATHER = ITEMS.register("burning_feather",
            () -> new BurningFeatherItem(new Item.Properties()));
    public static final DeferredItem<Item> CORRUPTING_ARROW = ITEMS.register("corrupting_arrow",
            () -> new CorruptingArrowItem(new Item.Properties()));
    public static final DeferredItem<Item> BEAM_ARROW = ITEMS.register("beam_arrow",
            () -> new LightBeamArrowItem(new Item.Properties()));

    public static final DeferredItem<Item> SOLAR_BRICK = ITEMS.register("solar_brick",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> ICE_SHIELD = ITEMS.register("ice_shield",
            () -> new FrostShieldItem(new Item.Properties().durability(336)
                    .stacksTo(1)));
    public static final DeferredItem<Item> PERMAFROST_SHIELD = ITEMS.register("permafrost_shield",
            () -> new FrostShieldItem(new Item.Properties().durability(750)
                    .stacksTo(1), true));

    public static final DeferredItem<Item> FLAMING_HAMMER = ITEMS.register("flaming_hammer",
            () -> new FlamingHammer(new Item.Properties().durability(1000).stacksTo(1).attributes(MaceItem.createAttributes())));

    public static final DeferredItem<Item> RAGE_COOKIE= ITEMS.register("rage_cookie",
            () -> new Item(new Item.Properties().food(SelectiveFoodProperties.RAGE_COOKIE)));

    public static final DeferredItem<Item> LIGHTNING_IN_A_BOTTLE= ITEMS.register("lightning_bottle",
            () -> new LightningInABottleItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> MUSHROOM_SKEWER= ITEMS.register("mushroom_skewer",
            () -> new SkewerItem(new Item.Properties().food(SelectiveFoodProperties.MUSHROOM_SKEWER)));

    public static final DeferredItem<Item> COOKED_MUSHROOM_SKEWER= ITEMS.register("cooked_mushroom_skewer",
            () -> new SkewerItem(new Item.Properties().food(SelectiveFoodProperties.COOKED_MUSHROOM_SKEWER)));

    public static final DeferredItem<Item> BATTLE_FOR_ETERNITY_MUSIC_DISC = ITEMS.register("battle_for_eternity_music_disc",
            () -> new Item(new Item.Properties().jukeboxPlayable(SelectivepowersSounds.BATTLE_FOR_ETERNITY_KEY).stacksTo(1)));

    public static final DeferredItem<Item> CROW_SPAWN = ITEMS.register("crow",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> YELLOW_KING_SPAWN = ITEMS.register("yellow_king",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> LUNAR_MAIDEN_SPAWN = ITEMS.register("lunar_maiden",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> CHIMERA_CORE = ITEMS.register("chimera_core",
            () -> new ChimeraSummoningItem(new Item.Properties().stacksTo(16)));

    public static final DeferredItem<Item> LEAFWALKER_CURIO = ITEMS.register("leafwalker_curio",
            () -> new LeafwalkerCurioItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> SOLID_VOID = ITEMS.register("solid_void",
            () -> new SolidVoidItem(new Item.Properties().durability(32).stacksTo(1)));

    public static final DeferredItem<Item> VOID_PEARL = ITEMS.register("void_pearl",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> DRAGON_SLEEVES = ITEMS.register("dragon_sleeves",
            () -> new DamagableCurioItem(new Item.Properties().durability(1024)));

    public static final DeferredItem<ShovelItem> PROSPECTORS_SHOVEL = ITEMS.register("prospectors_shovel",
            () -> new ProspectorsShovelItem(new Item.Properties().attributes(ShovelItem.createAttributes(Tiers.NETHERITE , 1.5F, -3.0F))));

    public static final DeferredItem<Item> ELEMENTAL_GUN = ITEMS.register("elemental_gun",
            () -> new ElementalGunItem(new Item.Properties().stacksTo(1).durability(128)));

    public static final DeferredItem<Item> DRINKING_HORN = ITEMS.register("drinking_horn",
            () -> new DrinkingHornItem(new Item.Properties().stacksTo(1).food(SelectiveFoodProperties.DRINKING_HORN)));

    public static final DeferredItem<Item> LORE_SCROLL = ITEMS.register("lore_scroll",
            () -> new LoreScrollItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<CurioItem> SHELF_MUSHROOM = ITEMS.register("shelf_mushroom",
            () -> new CurioItem());

    public static final DeferredItem<CurioItem> OCTOPUS_MUSHROOM = ITEMS.register("octopus_mushroom",
            () -> new CurioItem());

    public static final DeferredItem<CurioItem> MUSHROOM_SPORES = ITEMS.register("mushroom_spores",
            () -> new CurioItem());

    public static final DeferredItem<CurioItem> ENCHANTING_RUNE = ITEMS.register("enchanting_rune",
            () -> new CurioItem());

    public static final DeferredItem<CurioItem> POCKET_TESSLA_COIL = ITEMS.register("pocket_tessla_coil",
            () -> new CurioItem());

    public static final DeferredItem<CurioItem> CELESTIAL_RUNE = ITEMS.register("celestial_rune",
            () -> new CurioItem());

    public static final DeferredItem<CurioItem> DUALITY_PEARL = ITEMS.register("duality_pearl",
            () -> new CurioItem());

    public static final DeferredItem<CurioItem> BREATH_BAUBLE = ITEMS.register("breath_bauble",
            () -> new CurioItem());

    public static final DeferredItem<CurioItem> SCULK_TENDRIL = ITEMS.register("sculk_tendril",
            () -> new CurioItem());

    public static final DeferredItem<CurioItem> HIP_BOOK = ITEMS.register("hip_book",
            () -> new CurioItem());

    public static final DeferredItem<CurioItem> STORM_CLOUD = ITEMS.register("storm_cloud",
            () -> new CurioItem());

    public static final DeferredItem<CurioItem> SCULK_MOSS = ITEMS.register("sculk_moss",
            () -> new CurioItem());

    public static final DeferredItem<CurioItem> MOSS_LAYER = ITEMS.register("moss_layer",
            () -> new CurioItem());

    public static final DeferredItem<CurioItem> LIGHTNING_BALL = ITEMS.register("lightning_ball",
            () -> new CurioItem());

    public static final DeferredItem<CurioItem> EMBEDDED_CRYSTALS = ITEMS.register("embedded_crystals",
            () -> new CurioItem());

    public static final DeferredItem<CurioItem> SPINE_TREE = ITEMS.register("spine_tree",
            () -> new CurioItem());

    public static final DeferredItem<CurioItem> POLARBEAR_PELT = ITEMS.register("polarbear_pelt",
            () -> new CurioItem());

    public static final DeferredItem<CurioItem> FLOWER_CROWN = ITEMS.register("flower_crown",
            () -> new CurioItem());

    public static final DeferredItem<CurioItem> ELEMENTAL_CIRCLET = ITEMS.register("elemental_circlet",
            () -> new CurioItem());

    public static final DeferredItem<CurioItem> DRAGON_CLAWS = ITEMS.register("dragon_claws",
            () -> new CurioItem());

    public static final DeferredItem<CurioItem> DRAGON_HORNS = ITEMS.register("dragon_horns",
            () -> new CurioItem());

    public static final DeferredItem<CurioItem> HIP_HORN = ITEMS.register("hip_horn",
            () -> new CurioItem());

    public static final DeferredItem<CurioItem> DUST_MASK = ITEMS.register("dust_mask",
            () -> new CurioItem());

    public static final DeferredItem<CurioItem> SHOULDER_CROW = ITEMS.register("shoulder_crow",
            () -> new CurioItem());

    public static final DeferredItem<CurioItem> ELEMENTAL_GLOVES = ITEMS.register("elemental_gloves",
            () -> new CurioItem());

    public static final DeferredItem<CurioItem> GLOWING_EYES = ITEMS.register("glowing_eyes",
            () -> new CurioItem());

    public static final DeferredItem<CurioItem> FACE_PAINT = ITEMS.register("face_paint",
            () -> new CurioItem());

    public static final DeferredItem<CurioItem> HALO = ITEMS.register("halo",
            () -> new CurioItem());

    public static final DeferredItem<CurioItem> MINERS_HAT = ITEMS.register("miners_hat",
            () -> new CurioItem());

    public static final DeferredItem<CurioItem> RUNE = ITEMS.register("rune",
            () -> new CurioItem());

    public static final DeferredItem<CurioItem> WARDEN_HORNS = ITEMS.register("warden_horns",
            () -> new CurioItem());

    public static final DeferredItem<CurioItem> WOLF_EARS = ITEMS.register("wolf_ears",
            () -> new CurioItem());

    public static final DeferredItem<CurioItem> WOLF_TAIL = ITEMS.register("wolf_tail",
            () -> new CurioItem());

    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
