package net.blumasc.selectivepowers;

import java.util.List;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.ModConfigSpec;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.ConfigValue<List<? extends String>> ITEM_STRINGS_NATURE = BUILDER
            .comment("A list of plant items.")
            .defineListAllowEmpty("items_nature",
                    List.of("minecraft:sugar_cane", "#minecraft:flowers"),
                    () -> "",
                    Config::validateItemOrTag);

    public static final ModConfigSpec.ConfigValue<List<? extends String>> ITEM_STRINGS_MUSHROOM = BUILDER
            .comment("A list of mushroom items.")
            .defineListAllowEmpty("items_mushroom",
                    List.of("minecraft:crimson_fungus",
                            "minecraft:warped_fungus",
                            "minecraft:shroomlight",
                            "minecraft:mushroom_stem",
                            "minecraft:brown_mushroom_block",
                            "minecraft:red_mushroom_block",
                            "#c:mushrooms",
                            "selectivepowers:mooncap_seeds",
                            "selectivepowers:jump_mushroom",
                            "selectivepowers:mushroom_skewer",
                            "selectivepowers:cooked_mushroom_skewer"),
                    () -> "",
                    Config::validateItemOrTag);

    public static final ModConfigSpec.ConfigValue<List<? extends String>> MOB_STRINGS_DRAGON = BUILDER
            .comment("A list of dragon entities.")
            .defineListAllowEmpty("mobs_dragon",
                    List.of("minecraft:ender_dragon"),
                    () -> "",
                    Config::validateEntity);

    public static final ModConfigSpec.ConfigValue<List<? extends String>> MOB_STRINGS_VILLAGER = BUILDER
            .comment("A list of villager entities.")
            .defineListAllowEmpty("mobs_villager",
                    List.of("minecraft:villager","minecraft:piglin","minecraft:witch","minecraft:piglin_brute","minecraft:evoker","minecraft:pillager","minecraft:illusioner","minecraft:shulker","minecraft:vindicator","minecraft:wandering_trader"),
                    () -> "",
                    Config::validateEntity);

    public static final ModConfigSpec.ConfigValue<List<? extends String>> MOB_STRINGS_ELEMENTAL_ATTACK = BUILDER
            .comment("A list of elemental projectile entities.")
            .defineListAllowEmpty("mobs_elemental_projectiles",
                    List.of("minecraft:fireball",
                            "minecraft:small_fireball",
                            "minecraft:snowball",
                            "minecraft:wind_charge",
                            "minecraft:breeze_wind_charge"),
                    () -> "",
                    Config::validateEntity);

    public static final ModConfigSpec.ConfigValue<List<? extends String>> EFFECT_STRINGS_POISON = BUILDER
            .comment("A list of poison effects.")
            .defineListAllowEmpty("effects_poison",
                    List.of("minecraft:nausea", "minecraft:poison","minecraft:hunger","selectivepowers:hallucination"),
                    () -> "",
                    Config::validateEffect);

    public static final ModConfigSpec.ConfigValue<List<? extends String>> BLOCK_STRINGS_ORE = BUILDER
            .comment("A list of ore Blocks.")
            .defineListAllowEmpty("ore_blocks",
                    List.of(  "minecraft:iron_ore",
                            "minecraft:gold_ore",
                            "minecraft:diamond_ore",
                            "minecraft:lapis_ore",
                            "minecraft:redstone_ore",
                            "minecraft:emerald_ore",
                            "minecraft:copper_ore",
                            "minecraft:deepslate_iron_ore",
                            "minecraft:deepslate_gold_ore",
                            "minecraft:deepslate_diamond_ore",
                            "minecraft:deepslate_lapis_ore",
                            "minecraft:deepslate_redstone_ore",
                            "minecraft:deepslate_emerald_ore",
                            "minecraft:deepslate_copper_ore",
                            "minecraft:nether_gold_ore",
                            "minecraft:nether_quartz_ore",
                            "minecraft:ancient_debris"),
                    () -> "",
                    Config::validateBlock);

    public static final ModConfigSpec.ConfigValue<List<? extends String>> MOB_STRINGS_LITTLE_BUGS = BUILDER
            .comment("A list of bug entities.")
            .defineListAllowEmpty("mobs_bug",
                    List.of("minecraft:silverfish","minecraft:endermite"),
                    () -> "",
                    Config::validateEntity);

    public static final ModConfigSpec.ConfigValue<String> YELLOW_KING = BUILDER
            .comment("The Player name for the yellow king.")
            .define("yellow_player",
                    "YellowKing");

    public static final ModConfigSpec.ConfigValue<String> LUNAR_MAIDEN = BUILDER
            .comment("The Player name for the lunar maiden.")
            .define("lunar_maiden",
                    "LunarMaiden");

    static final ModConfigSpec SPEC = BUILDER.build();

    private static boolean validateItemOrTag(Object obj) {
        if (!(obj instanceof String s)) return false;

        if (s.startsWith("#")) {
            return true;
        }

        return BuiltInRegistries.ITEM.containsKey(ResourceLocation.parse(s)) || BuiltInRegistries.BLOCK.containsKey(ResourceLocation.parse(s));
    }

    private static boolean validateBlock(Object obj) {
        if (!(obj instanceof String s)) return false;

        return BuiltInRegistries.BLOCK.containsKey(ResourceLocation.parse(s));
    }

    private static boolean validateEntity(Object obj) {
        if (!(obj instanceof String s)) return false;


        return BuiltInRegistries.ENTITY_TYPE.containsKey(ResourceLocation.parse(s));
    }

    private static boolean validateEffect(Object obj) {
        if (!(obj instanceof String s)) return false;


        return BuiltInRegistries.MOB_EFFECT.containsKey(ResourceLocation.parse(s));
    }
}
