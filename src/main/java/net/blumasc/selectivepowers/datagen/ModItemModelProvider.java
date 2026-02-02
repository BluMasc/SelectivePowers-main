package net.blumasc.selectivepowers.datagen;

import net.blumasc.selectivepowers.SelectivePowers;
import net.blumasc.selectivepowers.block.SelectivepowersBlocks;
import net.blumasc.selectivepowers.item.SelectivepowersItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, SelectivePowers.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(SelectivepowersItems.BOUND_CONTRACT.get());
        basicItem(SelectivepowersItems.DENIAL_CONTRACT.get());
        basicItem(SelectivepowersItems.GOLDEN_IDOL.get());
        basicItem(SelectivepowersItems.BEETLE_HORN.get());
        basicItem(SelectivepowersItems.MOON_SACRIFICE.get());
        basicItem(SelectivepowersItems.MOONCAP_MUSHROOM.get());
        basicItem(SelectivepowersItems.SUN_HORN.get());
        basicItem(SelectivepowersItems.SUN_SACRIFICE.get());
        basicItem(SelectivepowersItems.SOULGEM.get());
        basicItem(SelectivepowersItems.BLESSED_IDOL.get());
        basicItem(SelectivepowersItems.MOONLIGHT_JELLY.get());
        basicItem(SelectivepowersItems.MOONCAP_SEEDS.get());
        basicItem(SelectivepowersItems.FAKE_CROWN.get());
        basicItem(SelectivepowersItems.TRUE_CROWN.get());
        basicItem(SelectivepowersItems.MOON_PENDANT.get());
        basicItem(SelectivepowersItems.SALAMANDER_GOO.get());
        basicItem(SelectivepowersItems.SALAMANDER_SCALES.get());
        basicItem(SelectivepowersItems.HORNED_HELMET.get());
        basicItem(SelectivepowersItems.CORRUPTION_SHARD.get());
        basicItem(SelectivepowersItems.YELLOW_RAMBLINGS.get());
        basicItem(SelectivepowersItems.LIGHT_RUNE.get());
        basicItem(SelectivepowersItems.CORRUPTED_MASK.get());
        basicItem(SelectivepowersItems.FLAMING_EGG.get());
        basicItem(SelectivepowersItems.MOON_TAKOYAKI.get());
        basicItem(SelectivepowersItems.BURNING_FEATHER.get());
        basicItem(SelectivepowersItems.CORRUPTING_ARROW.get());
        basicItem(SelectivepowersItems.BEAM_ARROW.get());
        basicItem(SelectivepowersItems.SOLAR_BRICK.get());
        basicItem(SelectivepowersItems.RAGE_COOKIE.get());
        basicItem(SelectivepowersItems.LIGHTNING_IN_A_BOTTLE.get());
        basicItem(SelectivepowersItems.COOKED_MUSHROOM_SKEWER.get());
        basicItem(SelectivepowersItems.MUSHROOM_SKEWER.get());
        basicItem(SelectivepowersItems.BATTLE_FOR_ETERNITY_MUSIC_DISC.get());
        basicItem(SelectivepowersItems.CROW_SPAWN.get());
        basicItem(SelectivepowersItems.YELLOW_KING_SPAWN.get());
        basicItem(SelectivepowersItems.LUNAR_MAIDEN_SPAWN.get());
        basicItem(SelectivepowersItems.CHIMERA_CORE.get());
        basicItem(SelectivepowersItems.SPINE_TREE.get());
        basicItem(SelectivepowersItems.POLARBEAR_PELT.get());
        basicItem(SelectivepowersItems.ELEMENTAL_CIRCLET.get());
        basicItem(SelectivepowersItems.FLOWER_CROWN.get());
        basicItem(SelectivepowersItems.LEAFWALKER_CURIO.get());
        basicItem(SelectivepowersItems.SOLID_VOID.get());
        basicItem(SelectivepowersItems.VOID_PEARL.get());
        basicItem(SelectivepowersItems.DRAGON_CLAWS.get());
        basicItem(SelectivepowersItems.DRAGON_HORNS.get());
        basicItem(SelectivepowersItems.HIP_HORN.get());
        basicItem(SelectivepowersItems.DRAGON_SLEEVES.get());
        basicItem(SelectivepowersItems.DRINKING_HORN.get());
        basicItem(SelectivepowersItems.LORE_SCROLL.get());
        basicItem(SelectivepowersItems.SHELF_MUSHROOM.get());
        basicItem(SelectivepowersItems.HIP_BOOK.get());
        basicItem(SelectivepowersItems.STORM_CLOUD.get());
        basicItem(SelectivepowersItems.SCULK_MOSS.get());
        basicItem(SelectivepowersItems.MOSS_LAYER.get());
        basicItem(SelectivepowersItems.LIGHTNING_BALL.get());
        basicItem(SelectivepowersItems.EMBEDDED_CRYSTALS.get());
        basicItem(SelectivepowersItems.OCTOPUS_MUSHROOM.get());
        basicItem(SelectivepowersItems.MUSHROOM_SPORES.get());
        basicItem(SelectivepowersItems.ENCHANTING_RUNE.get());
        basicItem(SelectivepowersItems.POCKET_TESSLA_COIL.get());
        basicItem(SelectivepowersItems.CELESTIAL_RUNE.get());
        basicItem(SelectivepowersItems.DUALITY_PEARL.get());
        basicItem(SelectivepowersItems.BREATH_BAUBLE.get());
        basicItem(SelectivepowersItems.SCULK_TENDRIL.get());
        basicItem(SelectivepowersItems.DUST_MASK.get());
        basicItem(SelectivepowersItems.SHOULDER_CROW.get());
        basicItem(SelectivepowersItems.WARDEN_HORNS.get());
        basicItem(SelectivepowersItems.WOLF_EARS.get());
        basicItem(SelectivepowersItems.WOLF_TAIL.get());
        basicItem(SelectivepowersItems.MINERS_HAT.get());
        basicItem(SelectivepowersItems.RUNE.get());
        basicItem(SelectivepowersItems.ELEMENTAL_GLOVES.get());
        basicItem(SelectivepowersItems.GLOWING_EYES.get());
        basicItem(SelectivepowersItems.FACE_PAINT.get());
        basicItem(SelectivepowersItems.HALO.get());
        basicItem(SelectivepowersBlocks.PITFALL_TRAP.asItem());
        handheldItem(SelectivepowersItems.PROSPECTORS_SHOVEL.get());
        handheldItem(SelectivepowersItems.SUN_SLICER.get());
        spawnEggItem(SelectivepowersItems.SOLAR_BEETLE_SPAWN_EGG);
        spawnEggItem(SelectivepowersItems.SALAMANDER_SPAWN_EGG);
        spawnEggItem(SelectivepowersItems.CROW_SPAWN_EGG);
        spawnEggItem(SelectivepowersItems.PACKWING_SPAWN_EGG);
        spawnEggItem(SelectivepowersItems.CORRUPTING_MASK_SPAWN_EGG);
        spawnEggItem(SelectivepowersItems.YELLOW_FANATIC_SPAWN_EGG);
        spawnEggItem(SelectivepowersItems.YELLOW_KING_SPAWN_EGG);
        spawnEggItem(SelectivepowersItems.ECHO_CRAB_SPAWN_EGG);
        spawnEggItem(SelectivepowersItems.QUETZAL_SPAWN_EGG);
        spawnEggItem(SelectivepowersItems.YELLOW_QUETZAL_SPAWN_EGG);
        spawnEggItem(SelectivepowersItems.MOON_SQUID_SPAWN_EGG);
        spawnEggItem(SelectivepowersItems.LUNAR_MAIDEN_SPAWN_EGG);

        wallItem(SelectivepowersBlocks.SMOOTH_MOON_ROCK_WALL, SelectivepowersBlocks.SMOOTH_MOON_ROCK);
        wallItem(SelectivepowersBlocks.MOON_ROCK_BRICKS_WALL, SelectivepowersBlocks.MOON_ROCK_BRICKS);
        wallItem(SelectivepowersBlocks.MOON_ROCK_TILES_WALL, SelectivepowersBlocks.MOON_ROCK_TILES);
        wallItem(SelectivepowersBlocks.SOLAR_BRICK_WALL, SelectivepowersBlocks.SOLAR_BRICKS);

        fenceItem(SelectivepowersBlocks.SOLAR_BRICK_FENCE, SelectivepowersBlocks.SOLAR_BRICKS);

    }

    private ItemModelBuilder handheldItem(DeferredItem<?> item) {
        return withExistingParent(item.getId().getPath(),
                ResourceLocation.parse("item/handheld")).texture("layer0",
                ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID,"item/" + item.getId().getPath()));
    }

    private ItemModelBuilder spawnEggItem(DeferredItem<?> item){
        return withExistingParent(item.getId().getPath(), mcLoc("item/template_spawn_egg"));
    }

    public void wallItem(DeferredBlock<?> block, DeferredBlock<Block> baseBlock) {
        this.withExistingParent(block.getId().getPath(), mcLoc("block/wall_inventory"))
                .texture("wall",  ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID,
                        "block/" + baseBlock.getId().getPath()));
    }

    public void fenceItem(DeferredBlock<?> block, DeferredBlock<Block> baseBlock) {
        this.withExistingParent(block.getId().getPath(), mcLoc("block/fence_inventory"))
                .texture("texture",  ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID,
                        "block/" + baseBlock.getId().getPath()));
    }
}
