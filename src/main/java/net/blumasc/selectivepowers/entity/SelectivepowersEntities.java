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

    public static final Supplier<EntityType<PackwingEntity>> PACKWING =
            ENTITY_TYPES.register("packwing", () -> EntityType.Builder.of(PackwingEntity::new, MobCategory.AMBIENT).sized(0.75f, 0.35f).build("packwing"));

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

    public static final Supplier<EntityType<SalamanderEntity>> SALAMANDER =
            ENTITY_TYPES.register("salamander", () -> EntityType.Builder.of(SalamanderEntity::new, MobCategory.CREATURE).sized(1f,1f).build("salamander"));

    public static final Supplier<EntityType<SolarBeetleEntity>> SOLAR_BEETLE =
            ENTITY_TYPES.register("solar_beetle", () -> EntityType.Builder.of(SolarBeetleEntity::new, MobCategory.CREATURE).sized(0.8f,0.6f).build("solar_beetle"));

    public static final Supplier<EntityType<EchoCrabEntity>> ECHO_CRAB =
            ENTITY_TYPES.register("echo_crab", () -> EntityType.Builder.of(EchoCrabEntity::new, MobCategory.CREATURE).sized(0.8f,0.7f).build("echo_crab"));

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

    public static final Supplier<EntityType<WoodArrow>> WOOD_ARROW =
            ENTITY_TYPES.register("wood_arrow", () -> EntityType.Builder.<WoodArrow>of(WoodArrow::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f).clientTrackingRange(4).updateInterval(20).build("wood_arrow"));

    public static final Supplier<EntityType<LightningRodArrowEntity>> LIGHTNING_ROD_ARROW =
            ENTITY_TYPES.register("lightning_rod_arrow", () -> EntityType.Builder.<LightningRodArrowEntity>of(LightningRodArrowEntity::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f).clientTrackingRange(4).updateInterval(20).build("lightning_rod_arrow"));

    public static final Supplier<EntityType<LightBeamArrowEntity>> LIGHT_BEAM_ARROW =
            ENTITY_TYPES.register("light_beam_arrow", () -> EntityType.Builder.<LightBeamArrowEntity>of(LightBeamArrowEntity::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f).clientTrackingRange(4).updateInterval(20).build("light_beam_arrow"));

    public static final Supplier<EntityType<ShardProjectileEntity>> SHARD_PROJECTILE =
            ENTITY_TYPES.register("shard_projectile", () -> EntityType.Builder.<ShardProjectileEntity>of(ShardProjectileEntity::new, MobCategory.MISC)
                    .sized(0.3f, 0.3f).clientTrackingRange(4).updateInterval(20).build("shard_projectile"));


    public static final Supplier<EntityType<MoonsquidEntity>> MOON_SQUID =
            ENTITY_TYPES.register("moon_squid", () -> EntityType.Builder.of(MoonsquidEntity::new, MobCategory.AMBIENT)
                    .sized(0.4f, 0.5f).build("moon_squid"));

    public static final Supplier<EntityType<ChimeraEntity>> CHIMERA =
            ENTITY_TYPES.register("chimera", () -> EntityType.Builder.of(ChimeraEntity::new, MobCategory.AMBIENT)
                    .sized(1.0f, 1.2f).build("chimera"));

    public static final Supplier<EntityType<QuetzalEntity>> QUETZAL =
            ENTITY_TYPES.register("quetzal", () -> EntityType.Builder.of(QuetzalEntity::new, MobCategory.MONSTER)
                    .sized(0.7f, 0.4f).build("quetzal"));

    public static final Supplier<EntityType<YellowQuetzalEntity>> QUETZAL_YELLOW =
            ENTITY_TYPES.register("quetzal_yellow", () -> EntityType.Builder.of(YellowQuetzalEntity::new, MobCategory.MONSTER)
                    .sized(0.9f, 0.6f).build("quetzal_yellow"));

    public static final Supplier<EntityType<LightningArcEntity>> LIGHTNING_ARC =
            ENTITY_TYPES.register("lightning_arc", () ->
                    EntityType.Builder.<LightningArcEntity>of(LightningArcEntity::new, MobCategory.MISC)
                            .sized(0.1F, 0.1F)
                            .clientTrackingRange(64)
                            .updateInterval(1)
                            .build("lightning_arc"));

    public static final Supplier<EntityType<MeteoriteEntity>> METEOR =
            ENTITY_TYPES.register("meteor", () ->
                    EntityType.Builder.<MeteoriteEntity>of(MeteoriteEntity::new, MobCategory.MISC)
                            .sized(1.0F, 1.0F)
                            .clientTrackingRange(128)
                            .updateInterval(1)
                            .build("meteor")
            );

    public static final Supplier<EntityType<ElementalBallEntity>> ELEMENTAL_BALL =
            ENTITY_TYPES.register("elemental_ball", () ->
                    EntityType.Builder.<ElementalBallEntity>of(ElementalBallEntity::new, MobCategory.MISC)
                            .sized(1.0F, 1.0F)
                            .clientTrackingRange(128)
                            .updateInterval(1)
                            .build("elemental_ball")
            );

    public static final Supplier<EntityType<ThrownSolidVoidPearl>> SOLID_VOID_PEARL =
            ENTITY_TYPES.register("solid_void_pearl", () ->
                    EntityType.Builder.<ThrownSolidVoidPearl>of(ThrownSolidVoidPearl::new, MobCategory.MISC)
                            .sized(1.0F, 1.0F)
                            .clientTrackingRange(128)
                            .updateInterval(1)
                            .build("solid_void_pearl")
            );

    public static final Supplier<EntityType<PickaxeBoomerangEntity>> PICKAXE_BOOMERANG =
            ENTITY_TYPES.register("pickaxe_boomerang", () ->
                    EntityType.Builder.<PickaxeBoomerangEntity>of(PickaxeBoomerangEntity::new, MobCategory.MISC)
                            .sized(1.0F, 1.0F)
                            .clientTrackingRange(128)
                            .updateInterval(1)
                            .build("pickaxe_boomerang")
            );

    public static final Supplier<EntityType<SpikeEntity>> DRIPSTONE_SPIKE =
            ENTITY_TYPES.register("dripstone_spike", () ->
                    EntityType.Builder.<SpikeEntity>of(SpikeEntity::new, MobCategory.MISC)
                            .sized(1.0F, 1.0F)
                            .clientTrackingRange(128)
                            .updateInterval(1)
                            .build("dripstone_spike")
            );

    public static final Supplier<EntityType<WardenBeamProjectile>> WARDEN_BEAM =
            ENTITY_TYPES.register("warden_beam", () ->
                    EntityType.Builder.<WardenBeamProjectile>of(WardenBeamProjectile::new, MobCategory.MISC)
                            .sized(1.0F, 1.0F)
                            .clientTrackingRange(128)
                            .updateInterval(1)
                            .build("warden_beam")
            );

    public static final Supplier<EntityType<GrapeShotProjectile>> GRAPE_SHOT =
            ENTITY_TYPES.register("grape_shot", () ->
                    EntityType.Builder.<GrapeShotProjectile>of(GrapeShotProjectile::new, MobCategory.MISC)
                            .sized(1.0F, 1.0F)
                            .clientTrackingRange(128)
                            .updateInterval(1)
                            .build("grape_shot")
            );

    public static void register(IEventBus eventBus){
        ENTITY_TYPES.register(eventBus);
    }
}
