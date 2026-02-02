package net.blumasc.selectivepowers.enchantment;

import com.mojang.serialization.MapCodec;
import net.blumasc.selectivepowers.SelectivePowers;
import net.blumasc.selectivepowers.enchantment.custom.SplatterEnchantmentEffect;
import net.blumasc.selectivepowers.enchantment.custom.ThinkingCapEnchantmentEffect;
import net.blumasc.selectivepowers.util.ModTags;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.item.enchantment.effects.EnchantmentAttributeEffect;
import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect;
import net.neoforged.neoforge.common.Tags;

import java.util.UUID;

public class ModEnchantments {

    public static final ResourceKey<Enchantment> SPLATTER = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "splatter"));

    public static final ResourceKey<Enchantment> HUNGRY = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "hungry"));

    public static final ResourceKey<Enchantment> THINKING_CAP = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "thinking_cap"));

    public static final ResourceKey<Enchantment> DIVINER = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "diviner"));

    public static final ResourceKey<Enchantment> SCULKING = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "sculking"));

    public static final ResourceKey<Enchantment> SQUID_ENCOUNTER = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "squid_encounter"));

    public static final ResourceKey<Enchantment> BULWARK = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "bulwark"));

    public static final ResourceKey<Enchantment> RUN_AWAY = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "run_away"));

    public static final ResourceKey<Enchantment> LAST_STAND = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "last_stand"));

    public static final ResourceKey<Enchantment> EVOKING = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "evoking"));

    public static final ResourceKey<Enchantment> DASH = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "dash"));

    public static final ResourceKey<Enchantment> ENDER_POCKETS = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "ender_pockets"));

    public static final ResourceKey<Enchantment> ENDURANCE = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "endurance"));

    public static final ResourceKey<Enchantment> CHARGE = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "charge"));

    public static final ResourceKey<Enchantment> COLLECTING_POCKETS = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "collecting_pockets"));

    public static final ResourceKey<Enchantment> BUOYANT = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "buoyant"));

    public static final ResourceKey<Enchantment> CLOUD_STEP = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "cloud_step"));

    public static final ResourceKey<Enchantment> SHRINK = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "shrink"));

    public static final ResourceKey<Enchantment> REVERBERATING = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "reverberating"));

    public static final ResourceKey<Enchantment> SCORCH = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "scorch"));

    public static final ResourceKey<Enchantment> MAGNETIC = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "magnetic"));

    public static final ResourceKey<Enchantment> SOUL_REND = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "soul_rend"));

    public static final ResourceKey<Enchantment> SNEAK_ATTACK = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "sneak_attack"));

    public static final ResourceKey<Enchantment> BREACHING = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "breaching"));

    public static final ResourceKey<Enchantment> FRENZY = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "frenzy"));

    public static final ResourceKey<Enchantment> SOUL_RETRIEVER = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "soul_retriever"));

    public static final ResourceKey<Enchantment> REAPING = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "reaping"));

    public static final ResourceKey<Enchantment> LIFE_LEACH = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "life_leach"));

    public static final ResourceKey<Enchantment> POCKET_SAND = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "pocket_sand"));

    public static final ResourceKey<Enchantment> TRAP_DIGGER = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "trap_digger"));

    public static final ResourceKey<Enchantment> PICKERANG = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "pickerang"));

    public static final ResourceKey<Enchantment> EARTHEN_SPIKE = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "earthen_spike"));

    public static final ResourceKey<Enchantment> DEQUIPING = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "dequiping"));

    public static final ResourceKey<Enchantment> GRAVITY_IMPLOSION = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "gravity_implosion"));

    public static final ResourceKey<Enchantment> ROPED = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "roped"));

    public static final ResourceKey<Enchantment> SUNRAY = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "sunray"));

    public static final ResourceKey<Enchantment> PINNING = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "pinning"));

    public static final ResourceKey<Enchantment> TRAILING = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "trailing"));

    public static final ResourceKey<Enchantment> VOLLEY = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "volley"));

    public static final ResourceKey<Enchantment> LIVING_WOOD = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "living_wood"));

    public static final ResourceKey<Enchantment> BULLET_TIME = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "bullet_time"));

    public static final ResourceKey<Enchantment> ECHOING = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "echoing"));

    public static final ResourceKey<Enchantment> SHARING = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "sharing"));

    public static final ResourceKey<Enchantment> GALVANIZING = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "galvanizing"));

    public static final ResourceKey<Enchantment> INFESTED = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "infested"));

    public static final ResourceKey<Enchantment> GRAPE_SHOT = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "grape_shot"));

    public static final ResourceKey<Enchantment> YOINKING = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "yoinking"));

    public static final ResourceKey<Enchantment> FLASH_FREEZE = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "flash_freeze"));

    public static final ResourceKey<Enchantment> BARBED_HOOK = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "barbed_hook"));

    public static final ResourceKey<Enchantment> FORK = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "fork"));

    public static final ResourceKey<Enchantment> POSEIDON = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "poseidon"));

    public static final ResourceKey<Enchantment> DEVILS_TOOL = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "devils_tool"));

    public static final ResourceKey<Enchantment> BOUNCY = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "bouncy"));

    public static final ResourceKey<Enchantment> DEVOURING = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "devouring"));

    public static final ResourceKey<Enchantment> CONTINGENCY = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "contingeny"));

    public static final ResourceKey<Enchantment> IMMORTALITY = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "immortality"));

    public static final ResourceKey<Enchantment> PHEROMONE_STINGER = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "pheromone_stinger"));

    public static void bootstrap(BootstrapContext<Enchantment> context) {
        var enchantments = context.lookup(Registries.ENCHANTMENT);
        var items = context.lookup(Registries.ITEM);

        register(context, SPLATTER, Enchantment.enchantment(Enchantment.definition(
                        items.getOrThrow(ItemTags.WEAPON_ENCHANTABLE),
                        items.getOrThrow(ItemTags.SWORD_ENCHANTABLE),
                        5,
                        5,
                        Enchantment.dynamicCost(2, 2),
                        Enchantment.dynamicCost(25, 7),
                        2,
                        EquipmentSlotGroup.HAND))
                .exclusiveWith(enchantments.getOrThrow(ModTags.EnchantmentTypes.SWORD_MUTUAL_EXCLUSIVE)));

        register(context, HUNGRY, Enchantment.enchantment(Enchantment.definition(
                        items.getOrThrow(ItemTags.HEAD_ARMOR_ENCHANTABLE),
                        items.getOrThrow(ItemTags.HEAD_ARMOR_ENCHANTABLE),
                        5,
                        1,
                        Enchantment.dynamicCost(5, 3),
                        Enchantment.dynamicCost(25, 7),
                        2,
                        EquipmentSlotGroup.HEAD))
                .exclusiveWith(enchantments.getOrThrow(ModTags.EnchantmentTypes.HEAD_SENSE)));

        register(context, THINKING_CAP, Enchantment.enchantment(Enchantment.definition(
                        items.getOrThrow(ItemTags.HEAD_ARMOR_ENCHANTABLE),
                        items.getOrThrow(ItemTags.HEAD_ARMOR_ENCHANTABLE),
                        5,
                        5,
                        Enchantment.dynamicCost(5, 3),
                        Enchantment.dynamicCost(25, 7),
                        2,
                        EquipmentSlotGroup.HEAD))
                .exclusiveWith(enchantments.getOrThrow(ModTags.EnchantmentTypes.MIND_POWER))
                .withEffect(EnchantmentEffectComponents.BLOCK_EXPERIENCE,
                        new ThinkingCapEnchantmentEffect())
                .withEffect(EnchantmentEffectComponents.MOB_EXPERIENCE,
                        new ThinkingCapEnchantmentEffect()));

        register(context, DIVINER, Enchantment.enchantment(Enchantment.definition(
                        items.getOrThrow(ItemTags.HEAD_ARMOR_ENCHANTABLE),
                        items.getOrThrow(ItemTags.HEAD_ARMOR_ENCHANTABLE),
                        5,
                        5,
                        Enchantment.dynamicCost(7, 3),
                        Enchantment.dynamicCost(25, 7),
                        2,
                        EquipmentSlotGroup.HEAD))
                .exclusiveWith(enchantments.getOrThrow(ModTags.EnchantmentTypes.HEAD_SENSE)));

        register(context, SCULKING, Enchantment.enchantment(Enchantment.definition(
                        items.getOrThrow(ItemTags.HEAD_ARMOR_ENCHANTABLE),
                        items.getOrThrow(ItemTags.HEAD_ARMOR_ENCHANTABLE),
                        5,
                        3,
                        Enchantment.dynamicCost(7, 3),
                        Enchantment.dynamicCost(25, 7),
                        2,
                        EquipmentSlotGroup.HEAD))
                .exclusiveWith(enchantments.getOrThrow(ModTags.EnchantmentTypes.HEAD_SENSE)));

        register(context, SQUID_ENCOUNTER, Enchantment.enchantment(Enchantment.definition(
                        items.getOrThrow(ItemTags.CHEST_ARMOR_ENCHANTABLE),
                        items.getOrThrow(ItemTags.CHEST_ARMOR_ENCHANTABLE),
                        5,
                        5,
                        Enchantment.dynamicCost(7, 3),
                        Enchantment.dynamicCost(25, 7),
                        2,
                        EquipmentSlotGroup.CHEST))
                .exclusiveWith(enchantments.getOrThrow(ModTags.EnchantmentTypes.CHEST_EXCLUSIVE)));

        register(context, BULWARK, Enchantment.enchantment(Enchantment.definition(
                        items.getOrThrow(ItemTags.CHEST_ARMOR_ENCHANTABLE),
                        items.getOrThrow(ItemTags.CHEST_ARMOR_ENCHANTABLE),
                        5,
                        5,
                        Enchantment.dynamicCost(7, 3),
                        Enchantment.dynamicCost(25, 7),
                        2,
                        EquipmentSlotGroup.CHEST))
                .withEffect(
                        EnchantmentEffectComponents.ATTRIBUTES,
                        new EnchantmentAttributeEffect(
                                ResourceLocation.fromNamespaceAndPath("selectivepowers", "bulwark_knockback"),
                                Attributes.KNOCKBACK_RESISTANCE,
                                LevelBasedValue.perLevel(0.15F),
                                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                        )
                )
                .withEffect(
                        EnchantmentEffectComponents.ATTRIBUTES,
                        new EnchantmentAttributeEffect(
                                ResourceLocation.fromNamespaceAndPath("selectivepowers", "bulwark_speed"),
                                Attributes.MOVEMENT_SPEED,
                                LevelBasedValue.perLevel(-0.10F),
                                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                        )
                ));

        register(context, RUN_AWAY, Enchantment.enchantment(Enchantment.definition(
                        items.getOrThrow(ItemTags.CHEST_ARMOR_ENCHANTABLE),
                        items.getOrThrow(ItemTags.CHEST_ARMOR_ENCHANTABLE),
                        5,
                        3,
                        Enchantment.dynamicCost(7, 3),
                        Enchantment.dynamicCost(25, 7),
                        2,
                        EquipmentSlotGroup.CHEST))
                .exclusiveWith(enchantments.getOrThrow(ModTags.EnchantmentTypes.CHEST_EXCLUSIVE)));

        register(context, LAST_STAND, Enchantment.enchantment(Enchantment.definition(
                        items.getOrThrow(ItemTags.CHEST_ARMOR_ENCHANTABLE),
                        items.getOrThrow(ItemTags.CHEST_ARMOR_ENCHANTABLE),
                        5,
                        1,
                        Enchantment.dynamicCost(7, 3),
                        Enchantment.dynamicCost(25, 7),
                        2,
                        EquipmentSlotGroup.CHEST))
                .exclusiveWith(enchantments.getOrThrow(ModTags.EnchantmentTypes.CHEST_EXCLUSIVE)));

        register(context, EVOKING, Enchantment.enchantment(Enchantment.definition(
                        items.getOrThrow(ItemTags.LEG_ARMOR_ENCHANTABLE),
                        items.getOrThrow(ItemTags.LEG_ARMOR_ENCHANTABLE),
                        5,
                        3,
                        Enchantment.dynamicCost(7, 3),
                        Enchantment.dynamicCost(25, 7),
                        2,
                        EquipmentSlotGroup.LEGS))
                .exclusiveWith(enchantments.getOrThrow(ModTags.EnchantmentTypes.LEG_MOVING)));

        register(context, DASH, Enchantment.enchantment(Enchantment.definition(
                        items.getOrThrow(ItemTags.LEG_ARMOR_ENCHANTABLE),
                        items.getOrThrow(ItemTags.LEG_ARMOR_ENCHANTABLE),
                        5,
                        3,
                        Enchantment.dynamicCost(7, 3),
                        Enchantment.dynamicCost(25, 7),
                        2,
                        EquipmentSlotGroup.LEGS))
                .exclusiveWith(enchantments.getOrThrow(ModTags.EnchantmentTypes.LEG_MOVING)));

        register(context, ENDURANCE, Enchantment.enchantment(Enchantment.definition(
                        items.getOrThrow(ItemTags.LEG_ARMOR_ENCHANTABLE),
                        items.getOrThrow(ItemTags.LEG_ARMOR_ENCHANTABLE),
                        5,
                        1,
                        Enchantment.dynamicCost(7, 3),
                        Enchantment.dynamicCost(25, 7),
                        2,
                        EquipmentSlotGroup.LEGS))
                .exclusiveWith(enchantments.getOrThrow(ModTags.EnchantmentTypes.LEG_MOVING)));

        register(context, CHARGE, Enchantment.enchantment(Enchantment.definition(
                        items.getOrThrow(ItemTags.LEG_ARMOR_ENCHANTABLE),
                        items.getOrThrow(ItemTags.LEG_ARMOR_ENCHANTABLE),
                        5,
                        3,
                        Enchantment.dynamicCost(7, 3),
                        Enchantment.dynamicCost(25, 7),
                        2,
                        EquipmentSlotGroup.LEGS))
                .exclusiveWith(enchantments.getOrThrow(ModTags.EnchantmentTypes.LEG_MOVING)));

        register(context, COLLECTING_POCKETS, Enchantment.enchantment(Enchantment.definition(
                        items.getOrThrow(ItemTags.LEG_ARMOR_ENCHANTABLE),
                        items.getOrThrow(ItemTags.LEG_ARMOR_ENCHANTABLE),
                        5,
                        3,
                        Enchantment.dynamicCost(7, 3),
                        Enchantment.dynamicCost(25, 7),
                        2,
                        EquipmentSlotGroup.LEGS))
                .exclusiveWith(enchantments.getOrThrow(ModTags.EnchantmentTypes.POCKETS)));

        register(context, BUOYANT, Enchantment.enchantment(Enchantment.definition(
                        items.getOrThrow(ItemTags.FOOT_ARMOR_ENCHANTABLE),
                        items.getOrThrow(ItemTags.FOOT_ARMOR_ENCHANTABLE),
                        5,
                        1,
                        Enchantment.dynamicCost(7, 3),
                        Enchantment.dynamicCost(25, 7),
                        2,
                        EquipmentSlotGroup.FEET))
                .exclusiveWith(enchantments.getOrThrow(ModTags.EnchantmentTypes.FEET_EXCLUSIVE)));

        register(context, CLOUD_STEP, Enchantment.enchantment(Enchantment.definition(
                        items.getOrThrow(ItemTags.FOOT_ARMOR_ENCHANTABLE),
                        items.getOrThrow(ItemTags.FOOT_ARMOR_ENCHANTABLE),
                        5,
                        2,
                        Enchantment.dynamicCost(7, 3),
                        Enchantment.dynamicCost(25, 7),
                        2,
                        EquipmentSlotGroup.FEET))
                .exclusiveWith(enchantments.getOrThrow(ModTags.EnchantmentTypes.FEET_EXCLUSIVE)));

        register(context, SHRINK, Enchantment.enchantment(Enchantment.definition(
                        items.getOrThrow(ItemTags.FOOT_ARMOR_ENCHANTABLE),
                        items.getOrThrow(ItemTags.FOOT_ARMOR_ENCHANTABLE),
                        5,
                        2,
                        Enchantment.dynamicCost(7, 3),
                        Enchantment.dynamicCost(25, 7),
                        2,
                        EquipmentSlotGroup.FEET))
                .exclusiveWith(enchantments.getOrThrow(ModTags.EnchantmentTypes.FEET_EXCLUSIVE)));

        register(context, REVERBERATING, Enchantment.enchantment(Enchantment.definition(
                        items.getOrThrow(ItemTags.MINING_ENCHANTABLE),
                        items.getOrThrow(ItemTags.MINING_ENCHANTABLE),
                        5,
                        5,
                        Enchantment.dynamicCost(7, 3),
                        Enchantment.dynamicCost(25, 7),
                        2,
                        EquipmentSlotGroup.HAND))
                .exclusiveWith(enchantments.getOrThrow(ModTags.EnchantmentTypes.VEIN_MINER_INCOMPATIBLE)));

        register(context, SCORCH, Enchantment.enchantment(Enchantment.definition(
                        items.getOrThrow(ModTags.Items.ENCHANTABLE_DROP_EDITOR),
                        items.getOrThrow(ModTags.Items.ENCHANTABLE_DROP_EDITOR),
                        5,
                        1,
                        Enchantment.dynamicCost(7, 3),
                        Enchantment.dynamicCost(25, 7),
                        2,
                        EquipmentSlotGroup.HAND))
                .exclusiveWith(enchantments.getOrThrow(ModTags.EnchantmentTypes.SMELT_SILK)));

        register(context, MAGNETIC, Enchantment.enchantment(Enchantment.definition(
                        items.getOrThrow(ModTags.Items.ENCHANTABLE_DROP_EDITOR),
                        items.getOrThrow(ModTags.Items.ENCHANTABLE_DROP_EDITOR),
                        5,
                        1,
                        Enchantment.dynamicCost(7, 3),
                        Enchantment.dynamicCost(25, 7),
                        2,
                        EquipmentSlotGroup.HAND))
                .exclusiveWith(enchantments.getOrThrow(ModTags.EnchantmentTypes.MOLTEN_MAGNETS)));

        register(context, SOUL_REND, Enchantment.enchantment(Enchantment.definition(
                        items.getOrThrow(ItemTags.SWORD_ENCHANTABLE),
                        items.getOrThrow(ItemTags.SWORD_ENCHANTABLE),
                        5,
                        3,
                        Enchantment.dynamicCost(7, 3),
                        Enchantment.dynamicCost(25, 7),
                        2,
                        EquipmentSlotGroup.HAND))
                .exclusiveWith(enchantments.getOrThrow(ModTags.EnchantmentTypes.SWORD_MUTUAL_EXCLUSIVE)));

        register(context, SNEAK_ATTACK, Enchantment.enchantment(Enchantment.definition(
                        items.getOrThrow(ItemTags.SWORD_ENCHANTABLE),
                        items.getOrThrow(ItemTags.SWORD_ENCHANTABLE),
                        5,
                        4,
                        Enchantment.dynamicCost(7, 3),
                        Enchantment.dynamicCost(25, 7),
                        2,
                        EquipmentSlotGroup.HAND))
                .exclusiveWith(enchantments.getOrThrow(ModTags.EnchantmentTypes.SWORD_MUTUAL_EXCLUSIVE)));

        register(context, BREACHING, Enchantment.enchantment(Enchantment.definition(
                        items.getOrThrow(ItemTags.AXES),
                        items.getOrThrow(ItemTags.AXES),
                        5,
                        3,
                        Enchantment.dynamicCost(7, 3),
                        Enchantment.dynamicCost(25, 7),
                        2,
                        EquipmentSlotGroup.HAND))
                .exclusiveWith(enchantments.getOrThrow(ModTags.EnchantmentTypes.AXE_MUTUAL_EXCLUSIVE)));

        register(context, FRENZY, Enchantment.enchantment(Enchantment.definition(
                        items.getOrThrow(ItemTags.AXES),
                        items.getOrThrow(ItemTags.AXES),
                        5,
                        3,
                        Enchantment.dynamicCost(7, 3),
                        Enchantment.dynamicCost(25, 7),
                        2,
                        EquipmentSlotGroup.HAND))
                .exclusiveWith(enchantments.getOrThrow(ModTags.EnchantmentTypes.AXE_MUTUAL_EXCLUSIVE)));

        register(context, SOUL_RETRIEVER, Enchantment.enchantment(Enchantment.definition(
                        items.getOrThrow(ItemTags.AXES),
                        items.getOrThrow(ItemTags.AXES),
                        5,
                        1,
                        Enchantment.dynamicCost(7, 3),
                        Enchantment.dynamicCost(25, 7),
                        2,
                        EquipmentSlotGroup.HAND))
                .exclusiveWith(enchantments.getOrThrow(ModTags.EnchantmentTypes.AXE_MUTUAL_EXCLUSIVE)));

        register(context, REAPING, Enchantment.enchantment(Enchantment.definition(
                        items.getOrThrow(ItemTags.HOES),
                        items.getOrThrow(ItemTags.HOES),
                        5,
                        3,
                        Enchantment.dynamicCost(7, 3),
                        Enchantment.dynamicCost(25, 7),
                        2,
                        EquipmentSlotGroup.HAND))
                .exclusiveWith(enchantments.getOrThrow(ModTags.EnchantmentTypes.HOE_MUTUAL_EXCLUSIVE)));

        register(context, LIFE_LEACH, Enchantment.enchantment(Enchantment.definition(
                        items.getOrThrow(ItemTags.HOES),
                        items.getOrThrow(ItemTags.HOES),
                        5,
                        3,
                        Enchantment.dynamicCost(7, 3),
                        Enchantment.dynamicCost(25, 7),
                        2,
                        EquipmentSlotGroup.HAND))
                .exclusiveWith(enchantments.getOrThrow(ModTags.EnchantmentTypes.HOE_MUTUAL_EXCLUSIVE)));

        register(context, POCKET_SAND, Enchantment.enchantment(Enchantment.definition(
                        items.getOrThrow(ItemTags.SHOVELS),
                        items.getOrThrow(ItemTags.SHOVELS),
                        5,
                        3,
                        Enchantment.dynamicCost(7, 3),
                        Enchantment.dynamicCost(25, 7),
                        2,
                        EquipmentSlotGroup.HAND))
                .exclusiveWith(enchantments.getOrThrow(ModTags.EnchantmentTypes.SHOVEL_MUTUAL_EXCLUSIVE)));

        register(context, TRAP_DIGGER, Enchantment.enchantment(Enchantment.definition(
                        items.getOrThrow(ItemTags.SHOVELS),
                        items.getOrThrow(ItemTags.SHOVELS),
                        5,
                        3,
                        Enchantment.dynamicCost(7, 3),
                        Enchantment.dynamicCost(25, 7),
                        2,
                        EquipmentSlotGroup.HAND))
                .exclusiveWith(enchantments.getOrThrow(ModTags.EnchantmentTypes.SHOVEL_MUTUAL_EXCLUSIVE)));

        register(context, PICKERANG, Enchantment.enchantment(Enchantment.definition(
                        items.getOrThrow(ItemTags.PICKAXES),
                        items.getOrThrow(ItemTags.PICKAXES),
                        5,
                        5,
                        Enchantment.dynamicCost(7, 3),
                        Enchantment.dynamicCost(25, 7),
                        2,
                        EquipmentSlotGroup.HAND))
                .exclusiveWith(enchantments.getOrThrow(ModTags.EnchantmentTypes.PICKERANG_EXCLUSIVE)));

        register(context, EARTHEN_SPIKE, Enchantment.enchantment(Enchantment.definition(
                        items.getOrThrow(ItemTags.PICKAXES),
                        items.getOrThrow(ItemTags.PICKAXES),
                        5,
                        3,
                        Enchantment.dynamicCost(7, 3),
                        Enchantment.dynamicCost(25, 7),
                        2,
                        EquipmentSlotGroup.HAND)));

        register(context, DEQUIPING, Enchantment.enchantment(Enchantment.definition(
                        items.getOrThrow(ItemTags.MACE_ENCHANTABLE),
                        items.getOrThrow(ItemTags.MACE_ENCHANTABLE),
                        5,
                        3,
                        Enchantment.dynamicCost(7, 3),
                        Enchantment.dynamicCost(25, 7),
                        2,
                        EquipmentSlotGroup.HAND))
                .exclusiveWith(enchantments.getOrThrow(ModTags.EnchantmentTypes.DEQUIPING_EXCLUSIVE)));

        register(context, GRAVITY_IMPLOSION, Enchantment.enchantment(Enchantment.definition(
                        items.getOrThrow(ItemTags.MACE_ENCHANTABLE),
                        items.getOrThrow(ItemTags.MACE_ENCHANTABLE),
                        5,
                        5,
                        Enchantment.dynamicCost(7, 3),
                        Enchantment.dynamicCost(25, 7),
                        2,
                        EquipmentSlotGroup.HAND))
                .exclusiveWith(enchantments.getOrThrow(ModTags.EnchantmentTypes.MACE_LAND_EFFECT)));

        register(context, ROPED, Enchantment.enchantment(Enchantment.definition(
                        items.getOrThrow(ItemTags.BOW_ENCHANTABLE),
                        items.getOrThrow(ItemTags.BOW_ENCHANTABLE),
                        5,
                        4,
                        Enchantment.dynamicCost(7, 3),
                        Enchantment.dynamicCost(25, 7),
                        2,
                        EquipmentSlotGroup.HAND))
                .exclusiveWith(enchantments.getOrThrow(ModTags.EnchantmentTypes.ARROW_HIT_EFFECT)));

        register(context, SUNRAY, Enchantment.enchantment(Enchantment.definition(
                        items.getOrThrow(ItemTags.BOW_ENCHANTABLE),
                        items.getOrThrow(ItemTags.BOW_ENCHANTABLE),
                        5,
                        3,
                        Enchantment.dynamicCost(7, 3),
                        Enchantment.dynamicCost(25, 7),
                        2,
                        EquipmentSlotGroup.HAND))
                .exclusiveWith(enchantments.getOrThrow(ModTags.EnchantmentTypes.ARROW_SHOOT_EFFECT)));

        register(context, PINNING, Enchantment.enchantment(Enchantment.definition(
                        items.getOrThrow(ItemTags.BOW_ENCHANTABLE),
                        items.getOrThrow(ItemTags.BOW_ENCHANTABLE),
                        5,
                        3,
                        Enchantment.dynamicCost(7, 3),
                        Enchantment.dynamicCost(25, 7),
                        2,
                        EquipmentSlotGroup.HAND))
                .exclusiveWith(enchantments.getOrThrow(ModTags.EnchantmentTypes.ARROW_HIT_EFFECT)));

        register(context, TRAILING, Enchantment.enchantment(Enchantment.definition(
                        items.getOrThrow(ItemTags.BOW_ENCHANTABLE),
                        items.getOrThrow(ItemTags.BOW_ENCHANTABLE),
                        5,
                        3,
                        Enchantment.dynamicCost(7, 3),
                        Enchantment.dynamicCost(25, 7),
                        2,
                        EquipmentSlotGroup.HAND))
                .exclusiveWith(enchantments.getOrThrow(ModTags.EnchantmentTypes.ARROW_SHOOT_EFFECT)));

        register(context, VOLLEY, Enchantment.enchantment(Enchantment.definition(
                        items.getOrThrow(ItemTags.BOW_ENCHANTABLE),
                        items.getOrThrow(ItemTags.BOW_ENCHANTABLE),
                        5,
                        4,
                        Enchantment.dynamicCost(7, 3),
                        Enchantment.dynamicCost(25, 7),
                        2,
                        EquipmentSlotGroup.HAND))
                .exclusiveWith(enchantments.getOrThrow(ModTags.EnchantmentTypes.ARROW_SHOOT_EFFECT)));

        register(context, LIVING_WOOD, Enchantment.enchantment(Enchantment.definition(
                        items.getOrThrow(ItemTags.BOW_ENCHANTABLE),
                        items.getOrThrow(ItemTags.BOW_ENCHANTABLE),
                        5,
                        1,
                        Enchantment.dynamicCost(7, 3),
                        Enchantment.dynamicCost(25, 7),
                        2,
                        EquipmentSlotGroup.HAND)));

        register(context, BULLET_TIME, Enchantment.enchantment(Enchantment.definition(
                        items.getOrThrow(ItemTags.BOW_ENCHANTABLE),
                        items.getOrThrow(ItemTags.BOW_ENCHANTABLE),
                        5,
                        3,
                        Enchantment.dynamicCost(7, 3),
                        Enchantment.dynamicCost(25, 7),
                        2,
                        EquipmentSlotGroup.HAND))
                .exclusiveWith(enchantments.getOrThrow(ModTags.EnchantmentTypes.ARROW_SHOOT_EFFECT)));

        register(context, ECHOING, Enchantment.enchantment(Enchantment.definition(
                        items.getOrThrow(ItemTags.CROSSBOW_ENCHANTABLE),
                        items.getOrThrow(ItemTags.CROSSBOW_ENCHANTABLE),
                        5,
                        1,
                        Enchantment.dynamicCost(7, 3),
                        Enchantment.dynamicCost(25, 7),
                        2,
                        EquipmentSlotGroup.HAND))
                .exclusiveWith(enchantments.getOrThrow(ModTags.EnchantmentTypes.CROSSBOW_EXCLUSIVE)));

        register(context, SHARING, Enchantment.enchantment(Enchantment.definition(
                        items.getOrThrow(ItemTags.CROSSBOW_ENCHANTABLE),
                        items.getOrThrow(ItemTags.CROSSBOW_ENCHANTABLE),
                        5,
                        5,
                        Enchantment.dynamicCost(7, 3),
                        Enchantment.dynamicCost(25, 7),
                        2,
                        EquipmentSlotGroup.HAND))
                .exclusiveWith(enchantments.getOrThrow(ModTags.EnchantmentTypes.CROSSBOW_EXCLUSIVE)));

        register(context, GALVANIZING, Enchantment.enchantment(Enchantment.definition(
                        items.getOrThrow(ItemTags.CROSSBOW_ENCHANTABLE),
                        items.getOrThrow(ItemTags.CROSSBOW_ENCHANTABLE),
                        5,
                        5,
                        Enchantment.dynamicCost(7, 3),
                        Enchantment.dynamicCost(25, 7),
                        2,
                        EquipmentSlotGroup.HAND))
                .exclusiveWith(enchantments.getOrThrow(ModTags.EnchantmentTypes.CROSSBOW_EXCLUSIVE)));

        register(context, INFESTED, Enchantment.enchantment(Enchantment.definition(
                        items.getOrThrow(ItemTags.CROSSBOW_ENCHANTABLE),
                        items.getOrThrow(ItemTags.CROSSBOW_ENCHANTABLE),
                        5,
                        1,
                        Enchantment.dynamicCost(7, 3),
                        Enchantment.dynamicCost(25, 7),
                        2,
                        EquipmentSlotGroup.HAND))
                .exclusiveWith(enchantments.getOrThrow(ModTags.EnchantmentTypes.CROSSBOW_EXCLUSIVE)));

        register(context, GRAPE_SHOT, Enchantment.enchantment(Enchantment.definition(
                        items.getOrThrow(ItemTags.CROSSBOW_ENCHANTABLE),
                        items.getOrThrow(ItemTags.CROSSBOW_ENCHANTABLE),
                        5,
                        3,
                        Enchantment.dynamicCost(7, 3),
                        Enchantment.dynamicCost(25, 7),
                        2,
                        EquipmentSlotGroup.HAND))
                .exclusiveWith(enchantments.getOrThrow(ModTags.EnchantmentTypes.CROSSBOW_EXCLUSIVE)));

        register(context, YOINKING, Enchantment.enchantment(Enchantment.definition(
                        items.getOrThrow(ItemTags.FISHING_ENCHANTABLE),
                        items.getOrThrow(ItemTags.FISHING_ENCHANTABLE),
                        5,
                        3,
                        Enchantment.dynamicCost(7, 3),
                        Enchantment.dynamicCost(25, 7),
                        2,
                        EquipmentSlotGroup.HAND))
                .exclusiveWith(enchantments.getOrThrow(ModTags.EnchantmentTypes.FISHING_ROD_HOOK)));

        register(context, FLASH_FREEZE, Enchantment.enchantment(Enchantment.definition(
                        items.getOrThrow(ItemTags.FISHING_ENCHANTABLE),
                        items.getOrThrow(ItemTags.FISHING_ENCHANTABLE),
                        5,
                        1,
                        Enchantment.dynamicCost(7, 3),
                        Enchantment.dynamicCost(25, 7),
                        2,
                        EquipmentSlotGroup.HAND))
                .exclusiveWith(enchantments.getOrThrow(ModTags.EnchantmentTypes.FISHING_ROD_HOOK)));

        register(context, BARBED_HOOK, Enchantment.enchantment(Enchantment.definition(
                        items.getOrThrow(ItemTags.FISHING_ENCHANTABLE),
                        items.getOrThrow(ItemTags.FISHING_ENCHANTABLE),
                        5,
                        1,
                        Enchantment.dynamicCost(7, 3),
                        Enchantment.dynamicCost(25, 7),
                        2,
                        EquipmentSlotGroup.HAND))
                .exclusiveWith(enchantments.getOrThrow(ModTags.EnchantmentTypes.FISHING_ROD_HOOK)));

        register(context, FORK, Enchantment.enchantment(Enchantment.definition(
                        items.getOrThrow(ItemTags.TRIDENT_ENCHANTABLE),
                        items.getOrThrow(ItemTags.TRIDENT_ENCHANTABLE),
                        5,
                        1,
                        Enchantment.dynamicCost(7, 3),
                        Enchantment.dynamicCost(25, 7),
                        2,
                        EquipmentSlotGroup.HAND))
                .exclusiveWith(enchantments.getOrThrow(ModTags.EnchantmentTypes.FORK_EXCLUSIVE)));

        register(context, POSEIDON, Enchantment.enchantment(Enchantment.definition(
                        items.getOrThrow(ItemTags.TRIDENT_ENCHANTABLE),
                        items.getOrThrow(ItemTags.TRIDENT_ENCHANTABLE),
                        5,
                        1,
                        Enchantment.dynamicCost(7, 3),
                        Enchantment.dynamicCost(25, 7),
                        2,
                        EquipmentSlotGroup.HAND))
                .exclusiveWith(enchantments.getOrThrow(ModTags.EnchantmentTypes.CHANNELING_EXCLUSIVE)));

        register(context, DEVILS_TOOL, Enchantment.enchantment(Enchantment.definition(
                        items.getOrThrow(ItemTags.TRIDENT_ENCHANTABLE),
                        items.getOrThrow(ItemTags.TRIDENT_ENCHANTABLE),
                        5,
                        1,
                        Enchantment.dynamicCost(7, 3),
                        Enchantment.dynamicCost(25, 7),
                        2,
                        EquipmentSlotGroup.HAND))
                .exclusiveWith(enchantments.getOrThrow(ModTags.EnchantmentTypes.CHANNELING_EXCLUSIVE)));

        register(context, BOUNCY, Enchantment.enchantment(Enchantment.definition(
                        items.getOrThrow(Tags.Items.TOOLS_SHIELD),
                        items.getOrThrow(Tags.Items.TOOLS_SHIELD),
                        5,
                        1,
                        Enchantment.dynamicCost(7, 3),
                        Enchantment.dynamicCost(25, 7),
                        2,
                        EquipmentSlotGroup.HAND))
                .exclusiveWith(enchantments.getOrThrow(ModTags.EnchantmentTypes.SHIELD_EXCLUSIVE)));

        register(context, DEVOURING, Enchantment.enchantment(Enchantment.definition(
                        items.getOrThrow(Tags.Items.TOOLS_SHIELD),
                        items.getOrThrow(Tags.Items.TOOLS_SHIELD),
                        5,
                        5,
                        Enchantment.dynamicCost(7, 3),
                        Enchantment.dynamicCost(25, 7),
                        2,
                        EquipmentSlotGroup.HAND))
                .exclusiveWith(enchantments.getOrThrow(ModTags.EnchantmentTypes.SHIELD_EXCLUSIVE)));

        register(context, CONTINGENCY, Enchantment.enchantment(Enchantment.definition(
                        items.getOrThrow(Tags.Items.TOOLS_SHIELD),
                        items.getOrThrow(Tags.Items.TOOLS_SHIELD),
                        5,
                        1,
                        Enchantment.dynamicCost(7, 3),
                        Enchantment.dynamicCost(25, 7),
                        2,
                        EquipmentSlotGroup.HAND))
                .exclusiveWith(enchantments.getOrThrow(ModTags.EnchantmentTypes.SHIELD_EXCLUSIVE)));

        register(context, IMMORTALITY, Enchantment.enchantment(Enchantment.definition(
                        items.getOrThrow(Tags.Items.TOOLS_SHIELD),
                        items.getOrThrow(Tags.Items.TOOLS_SHIELD),
                        5,
                        1,
                        Enchantment.dynamicCost(7, 3),
                        Enchantment.dynamicCost(25, 7),
                        2,
                        EquipmentSlotGroup.HAND))
                .exclusiveWith(enchantments.getOrThrow(ModTags.EnchantmentTypes.SHIELD_EXCLUSIVE)));

        register(context, PHEROMONE_STINGER, Enchantment.enchantment(Enchantment.definition(
                        items.getOrThrow(ItemTags.create(ResourceLocation.fromNamespaceAndPath("minecraft","spears"))),
                        items.getOrThrow(ItemTags.create(ResourceLocation.fromNamespaceAndPath("minecraft","spears"))),
                        5,
                        3,
                        Enchantment.dynamicCost(7, 3),
                        Enchantment.dynamicCost(25, 7),
                        2,
                        EquipmentSlotGroup.HAND)));

    }


    private static Holder<Enchantment> register(BootstrapContext<Enchantment> registry, ResourceKey<Enchantment> key,
                                   Enchantment.Builder builder) {
        return registry.register(key, builder.build(key.location()));
    }
}
