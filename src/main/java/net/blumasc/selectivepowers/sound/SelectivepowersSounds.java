package net.blumasc.selectivepowers.sound;

import net.blumasc.selectivepowers.SelectivePowers;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.JukeboxSong;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class SelectivepowersSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENT =
            DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, SelectivePowers.MODID);

    public static final Supplier<SoundEvent> SQUISH_PLUSHY =registerSoundEvent("squish_plushy");
    public static final Supplier<SoundEvent> BOUNCE =registerSoundEvent("bounce");
    public static final Supplier<SoundEvent> WIND =registerSoundEvent("wind");
    public static final Supplier<SoundEvent> SPARKLE =registerSoundEvent("sparkle");
    public static final Supplier<SoundEvent> CROW = registerSoundEvent("crow");
    public static final Supplier<SoundEvent> CROW_DEATH = registerSoundEvent("crow_death");
    public static final Supplier<SoundEvent> CROW_HURT = registerSoundEvent("crow_hurt");
    public static final Supplier<SoundEvent> PACKWING = registerSoundEvent("packwing");
    public static final Supplier<SoundEvent> PACKWING_DEATH = registerSoundEvent("packwing_death");
    public static final Supplier<SoundEvent> PACKWING_HURT = registerSoundEvent("packwing_hurt");
    public static final Supplier<SoundEvent> YELLOW_WHISPERS = registerSoundEvent("yellow_whispers");
    public static final Supplier<SoundEvent> MAGICAL_SUCCESS = registerSoundEvent("magical_success");
    public static final Supplier<SoundEvent> SOLAR_BUG = registerSoundEvent("solar_bug");
    public static final Supplier<SoundEvent> SOLAR_BUG_DEATH = registerSoundEvent("solar_bug_death");
    public static final Supplier<SoundEvent> SOLAR_BUG_HURT = registerSoundEvent("solar_bug_hurt");
    public static final Supplier<SoundEvent> SOLAR_BUG_SCUTTLE = registerSoundEvent("solar_bug_scuttle");
    public static final Supplier<SoundEvent> SALAMANDER = registerSoundEvent("salamander");
    public static final Supplier<SoundEvent> SALAMANDER_DEATH = registerSoundEvent("salamander_death");
    public static final Supplier<SoundEvent> SALAMANDER_HURT = registerSoundEvent("salamander_hurt");
    public static final Supplier<SoundEvent> SALAMANDER_BURN = registerSoundEvent("salamander_burn");
    public static final Supplier<SoundEvent> SALAMANDER_BURN_START = registerSoundEvent("salamander_burn_start");
    public static final Supplier<SoundEvent> SALAMANDER_BURN_END = registerSoundEvent("salamander_burn_end");
    public static final Supplier<SoundEvent> THE_AXOLOTL_SONG = registerSoundEvent("the_axolotl_song");
    public static final Supplier<SoundEvent> FANATIC_PRAYING = registerSoundEvent("fanatic_praying");
    public static final Supplier<SoundEvent> BLAST = registerSoundEvent("blast");
    public static final Supplier<SoundEvent> MOON_SQUID = registerSoundEvent("moon_squid");
    public static final Supplier<SoundEvent> MOON_SQUID_HURT = registerSoundEvent("moon_squid_hurt");
    public static final Supplier<SoundEvent> MOON_SQUID_DEATH = registerSoundEvent("moon_squid_death");
    public static final Supplier<SoundEvent> QUETZAL_HISS = registerSoundEvent("quetzal_hiss");
    public static final Supplier<SoundEvent> QUETZAL_HIT = registerSoundEvent("quetzal_hit");
    public static final Supplier<SoundEvent> ELECTRIC = registerSoundEvent("electric");
    public static final Supplier<SoundEvent> METEOR = registerSoundEvent("meteor");
    public static final Supplier<SoundEvent> ABILITY = registerSoundEvent("ability");
    public static final Supplier<SoundEvent> ULT = registerSoundEvent("ult");
    public static final Supplier<SoundEvent> LUNAR_LADY = registerSoundEvent("lunar_lady");
    public static final Supplier<SoundEvent> BATTLE_FOR_ETERNITY = registerSoundEvent("battle_for_eternity");
    public static final Supplier<SoundEvent> SHOOT = registerSoundEvent("shoot");
    public static final Supplier<SoundEvent> ROPE = registerSoundEvent("rope");
    public static final Supplier<SoundEvent> PICKERANG = registerSoundEvent("pickerang");
    public static final Supplier<SoundEvent> GRAPE_SHOT = registerSoundEvent("grape_shot");

    public static final ResourceKey<JukeboxSong> BATTLE_FOR_ETERNITY_KEY = createSong("battle_for_eternity");

    private static ResourceKey<JukeboxSong> createSong(String name){
        return ResourceKey.create(Registries.JUKEBOX_SONG, ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, name));
    }

    private static Supplier<SoundEvent> registerSoundEvent(String name){
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, name);
        return SOUND_EVENT.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENT.register(eventBus);
    }
}
