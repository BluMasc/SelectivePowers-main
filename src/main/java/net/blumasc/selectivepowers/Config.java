package net.blumasc.selectivepowers;

import java.util.List;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.ModConfigSpec;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();


    public static final ModConfigSpec.ConfigValue<List<? extends String>> EFFECT_STRINGS_POISON = BUILDER
            .comment("A list of poison effects.")
            .defineListAllowEmpty("effects_poison",
                    List.of("minecraft:nausea", "minecraft:poison","minecraft:hunger","selectivepowers:hallucination"),
                    () -> "",
                    Config::validateEffect);

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
