package net.blumasc.selectivepowers.datagen;

import net.blumasc.selectivepowers.SelectivePowers;
import net.blumasc.selectivepowers.block.SelectivepowersBlocks;
import net.blumasc.selectivepowers.item.SelectivepowersItems;
import net.blumasc.selectivepowers.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.references.Blocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosTags;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends ItemTagsProvider {

    public ModItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, SelectivePowers.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(ItemTags.create(ResourceLocation.fromNamespaceAndPath("c","feathers")))
                .add(SelectivepowersItems.BURNING_FEATHER.get());
        tag(ItemTags.create(ResourceLocation.fromNamespaceAndPath("c","mushrooms")))
                .add(SelectivepowersItems.MOONCAP_MUSHROOM.get())
                .add(SelectivepowersBlocks.JUMP_MUSHROOM.get().asItem())
                .add(SelectivepowersBlocks.SPORE_MUSHROOM_BLOCK.get().asItem());
        tag(ItemTags.create(ResourceLocation.fromNamespaceAndPath("c","eggs")))
                .add(SelectivepowersItems.FLAMING_EGG.get());
        tag(ItemTags.create(ResourceLocation.fromNamespaceAndPath("minecraft","spears")))
                .add(SelectivepowersItems.MOONLIGHT_GLAIVE.get());

        tag(Tags.Items.TOOLS_SHIELD)
                .add(SelectivepowersItems.PERMAFROST_SHIELD.get())
                        .add(SelectivepowersItems.ICE_SHIELD.get());

        tag(ItemTags.ARROWS)
                .add(SelectivepowersItems.CORRUPTING_ARROW.get())
                .add(SelectivepowersItems.BEAM_ARROW.get());

        tag(ItemTags.ARMOR_ENCHANTABLE)
                .add(SelectivepowersItems.HORNED_HELMET.get());

        tag(ItemTags.HEAD_ARMOR_ENCHANTABLE)
                .add(SelectivepowersItems.HORNED_HELMET.get());

        tag(ItemTags.EQUIPPABLE_ENCHANTABLE)
                .add(SelectivepowersItems.HORNED_HELMET.get());

        tag(ItemTags.HEAD_ARMOR)
                .add(SelectivepowersItems.HORNED_HELMET.get());

        tag(ItemTags.VANISHING_ENCHANTABLE)
                .add(SelectivepowersItems.HORNED_HELMET.get());

        tag(ItemTags.FREEZE_IMMUNE_WEARABLES)
                .add(SelectivepowersItems.HORNED_HELMET.get());

        tag(ItemTags.DURABILITY_ENCHANTABLE)
                .add(SelectivepowersItems.SUN_SLICER.get())
                .add(SelectivepowersItems.MOONLIGHT_GLAIVE.get())
                .add(SelectivepowersItems.FLAMING_HAMMER.get())
                .add(SelectivepowersItems.HORNED_HELMET.get())
                .add(SelectivepowersItems.LEAFWALKER_CURIO.get())
                .add(SelectivepowersItems.DRAGON_SLEEVES.get());

        tag(ItemTags.WEAPON_ENCHANTABLE)
                .add(SelectivepowersItems.SUN_SLICER.get())
                .add(SelectivepowersItems.MOONLIGHT_GLAIVE.get())
                .add(SelectivepowersItems.FLAMING_HAMMER.get());

        tag(ItemTags.SWORD_ENCHANTABLE)
                .add(SelectivepowersItems.SUN_SLICER.get())
                .add(SelectivepowersItems.MOONLIGHT_GLAIVE.get());

        tag(ItemTags.MACE_ENCHANTABLE)
                .add(SelectivepowersItems.FLAMING_HAMMER.get());
        tag(ItemTags.SHOVELS)
                .add(SelectivepowersItems.PROSPECTORS_SHOVEL.get());

        tag(CuriosTags.HEAD)
                .add(SelectivepowersItems.FAKE_CROWN.get())
                .add(SelectivepowersItems.TRUE_CROWN.get());
        tag(CuriosTags.NECKLACE)
                .add(SelectivepowersItems.MOON_PENDANT.get());
        tag(CuriosTags.BODY)
                .add(SelectivepowersItems.LEAFWALKER_CURIO.get())
                .add(SelectivepowersItems.DRAGON_SLEEVES.get());

        tag(CuriosTags.CURIO)
                .add(SelectivepowersItems.SHELF_MUSHROOM.get())
                .add(SelectivepowersItems.OCTOPUS_MUSHROOM.get())
                .add(SelectivepowersItems.CORRUPTED_MASK.get())
                .add(SelectivepowersItems.MUSHROOM_SPORES.get())
                .add(SelectivepowersItems.ENCHANTING_RUNE.get())
                .add(SelectivepowersItems.POCKET_TESSLA_COIL.get())
                .add(SelectivepowersItems.CELESTIAL_RUNE.get())
                .add(SelectivepowersItems.DUALITY_PEARL.get())
                .add(SelectivepowersItems.BREATH_BAUBLE.get())
                .add(SelectivepowersItems.SCULK_TENDRIL.get())
                .add(SelectivepowersItems.HIP_BOOK.get())
                .add(SelectivepowersItems.STORM_CLOUD.get())
                .add(SelectivepowersItems.SCULK_MOSS.get())
                .add(SelectivepowersItems.MOSS_LAYER.get())
                .add(SelectivepowersItems.LIGHTNING_BALL.get())
                .add(SelectivepowersItems.EMBEDDED_CRYSTALS.get())
                .add(SelectivepowersItems.SPINE_TREE.get())
                .add(SelectivepowersItems.POLARBEAR_PELT.get())
                .add(SelectivepowersItems.FLOWER_CROWN.get())
                .add(SelectivepowersItems.ELEMENTAL_CIRCLET.get())
                .add(SelectivepowersItems.DRAGON_CLAWS.get())
                .add(SelectivepowersItems.DRAGON_HORNS.get())
                .add(SelectivepowersItems.HIP_HORN.get())
                .add(SelectivepowersItems.DUST_MASK.get())
                .add(SelectivepowersItems.SHOULDER_CROW.get())
                .add(SelectivepowersItems.ELEMENTAL_GLOVES.get())
                .add(SelectivepowersItems.FACE_PAINT.get())
                .add(SelectivepowersItems.GLOWING_EYES.get())
                .add(SelectivepowersItems.HALO.get())
                .add(SelectivepowersItems.MINERS_HAT.get())
                .add(SelectivepowersItems.RUNE.get())
                .add(SelectivepowersItems.WARDEN_HORNS.get())
                .add(SelectivepowersItems.WOLF_EARS.get())
                .add(SelectivepowersItems.WOLF_TAIL.get());

        tag(ModTags.Items.CROW_TAMING_ITEM)
                .add(Items.GOLD_NUGGET)
                .add(SelectivepowersItems.MOON_SACRIFICE.get())
                .add(SelectivepowersItems.SUN_SACRIFICE.get())
                .add(Items.GOLD_INGOT)
                .add(Items.RAW_GOLD);

        tag(ModTags.Items.CROW_BREEDING_ITEM)
                .add(Items.WHEAT)
                .add(Items.WHEAT_SEEDS)
                .add(Items.BEETROOT_SEEDS)
                .add(Items.PUMPKIN_SEEDS)
                .add(Items.PUMPKIN);

        tag(ModTags.Items.PACKWING_TAMING_ITEM)
                .add(Items.GLOW_BERRIES)
                .add(Items.GLOW_INK_SAC);

        tag(ModTags.Items.PACKWING_BREEDING_ITEM)
                .add(Items.SWEET_BERRIES)
                .add(Items.MELON_SLICE)
                .add(Items.APPLE);

        tag(ModTags.Items.SALAMANDER_FOOD)
                .add(Items.FIRE_CHARGE)
                .add(Items.MAGMA_BLOCK)
                .add(Items.BLAZE_POWDER)
                .add(Items.BLAZE_ROD)
                .add(Items.MAGMA_CREAM);

        tag(ModTags.Items.SOLAR_BEETLE_FOOD)
                .add(Items.QUARTZ)
                .add(SelectivepowersItems.SUN_SACRIFICE.get())
                .add(SelectivepowersItems.SUN_HORN.get());

        tag(ModTags.Items.ENCHANTABLE_DROP_EDITOR)
                .addTag(ItemTags.MINING_ENCHANTABLE)
                .addTag(ItemTags.FIRE_ASPECT_ENCHANTABLE);


    }
}
