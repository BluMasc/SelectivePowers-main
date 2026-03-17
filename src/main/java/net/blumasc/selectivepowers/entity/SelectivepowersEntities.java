package net.blumasc.selectivepowers.entity;

import net.blumasc.selectivepowers.SelectivePowers;
import net.blumasc.selectivepowers.entity.custom.*;
import net.blumasc.selectivepowers.entity.custom.projectile.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class SelectivepowersEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, SelectivePowers.MODID);

    public static final Supplier<EntityType<CrowEntity>> CROW =
            ENTITY_TYPES.register("crow", () -> EntityType.Builder.of(CrowEntity::new, MobCategory.CREATURE).sized(0.75f, 0.35f).build("crow"));

    public static final Supplier<EntityType<YellowKingEntity>> YELLOW_KING =
            ENTITY_TYPES.register("yellow_king", () -> EntityType.Builder.of(YellowKingEntity::new, MobCategory.MONSTER).sized(1.2f, 2.8f).build("yellow_king"));

    public static final Supplier<EntityType<YellowKingBossEntity>> YELLOW_KING_BOSS =
            ENTITY_TYPES.register("yellow_king_boss", () -> EntityType.Builder.of(YellowKingBossEntity::new, MobCategory.MONSTER).sized(1.2f, 2.8f).build("yellow_king_boss"));

    public static final Supplier<EntityType<YellowFanaticEntity>> YELLOW_FANATIC =
            ENTITY_TYPES.register("yellow_fanatic", () -> EntityType.Builder.of(YellowFanaticEntity::new, MobCategory.MONSTER).sized(1.0f, 2.0f).build("yellow_fanatic"));

    public static final Supplier<EntityType<CorruptingMaskEntity>> CORRUPTING_MASK =
            ENTITY_TYPES.register("corrupting_mask", () -> EntityType.Builder.of(CorruptingMaskEntity::new, MobCategory.MONSTER).sized(0.6f, 0.6f).build("corrupting_mask"));

    public static final Supplier<EntityType<LunarMaidenEntity>> LUNAR_MAIDEN =
            ENTITY_TYPES.register("lunar_maiden", () -> EntityType.Builder.of(LunarMaidenEntity::new, MobCategory.AMBIENT).sized(1.5f,3f).build("lunar_maiden"));

    public static final Supplier<EntityType<RuneProjectileEntity>> RUNE_PROJECTILE =
            ENTITY_TYPES.register("rune_projectile", () -> EntityType.Builder.<RuneProjectileEntity>of(RuneProjectileEntity::new, MobCategory.MISC)
                    .sized(0.5f, 0.3f).build("rune_projectile"));

    public static final Supplier<EntityType<MagicCircleEntity>> MAGIC_CIRCLE =
            ENTITY_TYPES.register("magic_circle", () -> EntityType.Builder.<MagicCircleEntity>of(MagicCircleEntity::new, MobCategory.MISC)
                    .sized(3.0f, 0.1f).build("magic_circle"));

    public static final Supplier<EntityType<ThrownFlamingEggEntity>> FLAMING_EGG =
            ENTITY_TYPES.register("flaming_egg", () -> EntityType.Builder.<ThrownFlamingEggEntity>of(ThrownFlamingEggEntity::new, MobCategory.MISC)
                    .sized(0.3f,0.3f).build("flaming_egg"));

    public static final Supplier<EntityType<FlamingFeatherEntity>> FLAMING_FEATHER =
            ENTITY_TYPES.register("flaming_feather", () -> EntityType.Builder.<FlamingFeatherEntity>of(FlamingFeatherEntity::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f).clientTrackingRange(4).updateInterval(20).build("flaming_feather"));

    public static final Supplier<EntityType<CorruptingArrowEntity>> CORRUPTING_ARROW =
            ENTITY_TYPES.register("corrupting_arrow", () -> EntityType.Builder.<CorruptingArrowEntity>of(CorruptingArrowEntity::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f).clientTrackingRange(4).updateInterval(20).build("corrupting_arrow"));

    public static final Supplier<EntityType<LightBeamArrowEntity>> LIGHT_BEAM_ARROW =
            ENTITY_TYPES.register("light_beam_arrow", () -> EntityType.Builder.<LightBeamArrowEntity>of(LightBeamArrowEntity::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f).clientTrackingRange(4).updateInterval(20).build("light_beam_arrow"));


    public static final Supplier<EntityType<MoonsquidEntity>> MOON_SQUID =
            ENTITY_TYPES.register("moon_squid", () -> EntityType.Builder.of(MoonsquidEntity::new, MobCategory.AMBIENT)
                    .sized(0.4f, 0.5f).build("moon_squid"));

    public static final Supplier<EntityType<QuetzalEntity>> QUETZAL =
            ENTITY_TYPES.register("quetzal", () -> EntityType.Builder.of(QuetzalEntity::new, MobCategory.MONSTER)
                    .sized(0.7f, 0.4f).build("quetzal"));

    public static final Supplier<EntityType<YellowQuetzalEntity>> QUETZAL_YELLOW =
            ENTITY_TYPES.register("quetzal_yellow", () -> EntityType.Builder.of(YellowQuetzalEntity::new, MobCategory.MONSTER)
                    .sized(0.9f, 0.6f).build("quetzal_yellow"));

    public static final Supplier<EntityType<ThrownSolidVoidPearl>> SOLID_VOID_PEARL =
            ENTITY_TYPES.register("solid_void_pearl", () ->
                    EntityType.Builder.<ThrownSolidVoidPearl>of(ThrownSolidVoidPearl::new, MobCategory.MISC)
                            .sized(1.0F, 1.0F)
                            .clientTrackingRange(128)
                            .updateInterval(1)
                            .build("solid_void_pearl")
            );



    public static void register(IEventBus eventBus){
        ENTITY_TYPES.register(eventBus);
    }
}
