package net.blumasc.selectivepowers.events;

import net.blumasc.selectivepowers.SelectivePowers;
import net.blumasc.selectivepowers.block.entity.SelectivepowersBlockEntities;
import net.blumasc.selectivepowers.block.entity.renderer.ProtectionEffigyModel;
import net.blumasc.selectivepowers.entity.SelectivepowersEntities;
import net.blumasc.selectivepowers.entity.client.anchor.AnchorModel;
import net.blumasc.selectivepowers.entity.client.corruptingmask.CorruptingMaskModel;
import net.blumasc.selectivepowers.entity.client.crow.CrowModel;
import net.blumasc.selectivepowers.entity.client.lunarmaiden.LunarMaidenModel;
import net.blumasc.selectivepowers.entity.client.moonsquid.MoonsquidModel;
import net.blumasc.selectivepowers.entity.client.quetzal.QuetzalModel;
import net.blumasc.selectivepowers.entity.client.yellowfanatic.YellowFanaticModel;
import net.blumasc.selectivepowers.entity.client.yellowking.YellowKingModel;
import net.blumasc.selectivepowers.entity.custom.*;
import net.blumasc.selectivepowers.item.client.*;
import net.blumasc.selectivepowers.managers.StartAltarManager;
import net.blumasc.selectivepowers.potion.SelectivePowerPotions;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.items.wrapper.InvWrapper;

import java.util.Arrays;

import static net.blumasc.selectivepowers.item.SelectivepowersItems.*;

@EventBusSubscriber(modid = SelectivePowers.MODID)
public class SelectivepowersEventBusEvents {

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event)
    {
        event.registerLayerDefinition(CrowModel.LAYER_LOCATION, CrowModel::createBodyLayer);
        event.registerLayerDefinition(YellowKingModel.LAYER_LOCATION, YellowKingModel::createBodyLayer);
        event.registerLayerDefinition(YellowFanaticModel.LAYER_LOCATION, YellowFanaticModel::createBodyLayer);
        event.registerLayerDefinition(CorruptingMaskModel.LAYER_LOCATION, CorruptingMaskModel::createBodyLayer);
        event.registerLayerDefinition(LunarMaidenModel.LAYER_LOCATION, LunarMaidenModel::createBodyLayer);
        event.registerLayerDefinition(CrownCurioModel.LAYER_LOCATION, CrownCurioModel::createBodyLayer);
        event.registerLayerDefinition(LeafwalkerCurioModel.LAYER_LOCATION, LeafwalkerCurioModel::createBodyLayer);
        event.registerLayerDefinition(MoonPendantCurioModel.LAYER_LOCATION, MoonPendantCurioModel::createBodyLayer);
        event.registerLayerDefinition(ShelfMushroomModel.LAYER_LOCATION, ShelfMushroomModel::createBodyLayer);
        event.registerLayerDefinition(OctopusMushroomModel.LAYER_LOCATION, OctopusMushroomModel::createBodyLayer);
        event.registerLayerDefinition(DragonCurioModel.LAYER_LOCATION, DragonCurioModel::createBodyLayer);
        event.registerLayerDefinition(MaskCurioModel.LAYER_LOCATION, MaskCurioModel::createBodyLayer);
        event.registerLayerDefinition(QuetzalModel.LAYER_LOCATION, QuetzalModel::createBodyLayer);
        event.registerLayerDefinition(MoonsquidModel.LAYER_LOCATION, MoonsquidModel::createBodyLayer);
        event.registerLayerDefinition(ProtectionEffigyModel.LAYER_LOCATION, ProtectionEffigyModel::createBodyLayer);
        event.registerLayerDefinition(HipBookModel.LAYER_LOCATION, HipBookModel::createBodyLayer);
        event.registerLayerDefinition(StormCloudModel.LAYER_LOCATION, StormCloudModel::createBodyLayer);
        event.registerLayerDefinition(SkinModel.LAYER_LOCATION, SkinModel::createBodyLayer);
        event.registerLayerDefinition(BearPeltModel.LAYER_LOCATION, BearPeltModel::createBodyLayer);
        event.registerLayerDefinition(CircletModel.LAYER_LOCATION, CircletModel::createBodyLayer);
        event.registerLayerDefinition(DragonClawsModel.LAYER_LOCATION, DragonClawsModel::createBodyLayer);
        event.registerLayerDefinition(DragonHornsModel.LAYER_LOCATION, DragonHornsModel::createBodyLayer);
        event.registerLayerDefinition(DrinkHornModel.LAYER_LOCATION, DrinkHornModel::createBodyLayer);
        event.registerLayerDefinition(DustMaskModel.LAYER_LOCATION, DustMaskModel::createBodyLayer);
        event.registerLayerDefinition(ElementalFistsModel.LAYER_LOCATION, ElementalFistsModel::createBodyLayer);
        event.registerLayerDefinition(FaceTextureModel.LAYER_LOCATION, FaceTextureModel::createBodyLayer);
        event.registerLayerDefinition(HaloModel.LAYER_LOCATION, HaloModel::createBodyLayer);
        event.registerLayerDefinition(HardHatModel.LAYER_LOCATION, HardHatModel::createBodyLayer);
        event.registerLayerDefinition(RuneModel.LAYER_LOCATION, RuneModel::createBodyLayer);
        event.registerLayerDefinition(WardenHornsModel.LAYER_LOCATION, WardenHornsModel::createBodyLayer);
        event.registerLayerDefinition(WolfEarsModel.LAYER_LOCATION, WolfEarsModel::createBodyLayer);
        event.registerLayerDefinition(WolfTailModel.LAYER_LOCATION, WolfTailModel::createBodyLayer);
        event.registerLayerDefinition(DrillArmModel.LAYER_LOCATION, DrillArmModel::createBodyLayer);
        event.registerLayerDefinition(LeafPauldronsModel.LAYER_LOCATION, LeafPauldronsModel::createBodyLayer);
        event.registerLayerDefinition(AnchorModel.LAYER_LOCATION, AnchorModel::createBodyLayer);
        event.registerLayerDefinition(PirateHatModel.LAYER_LOCATION, PirateHatModel::createBodyLayer);
        event.registerLayerDefinition(NauticBeltModel.LAYER_LOCATION, NauticBeltModel::createBodyLayer);
        event.registerLayerDefinition(RedstoneVizerModel.LAYER_LOCATION, RedstoneVizerModel::createBodyLayer);
        event.registerLayerDefinition(ArmPistonModel.LAYER_LOCATION, ArmPistonModel::createBodyLayer);
        event.registerLayerDefinition(WorkOverallModel.LAYER_LOCATION, WorkOverallModel::createBodyLayer);
}

    @SubscribeEvent
    public static void registerAtributes(EntityAttributeCreationEvent event){
        event.put(SelectivepowersEntities.CROW.get(), CrowEntity.createAttributes().build());
        event.put(SelectivepowersEntities.YELLOW_KING.get(), YellowKingEntity.createAttributes().build());
        event.put(SelectivepowersEntities.YELLOW_KING_BOSS.get(), YellowKingBossEntity.createAttributes().build());
        event.put(SelectivepowersEntities.YELLOW_FANATIC.get(), YellowFanaticEntity.createAttributes().build());
        event.put(SelectivepowersEntities.CORRUPTING_MASK.get(), CorruptingMaskEntity.createAttributes().build());
        event.put(SelectivepowersEntities.LUNAR_MAIDEN.get(), LunarMaidenEntity.createAttributes().build());
        event.put(SelectivepowersEntities.MOON_SQUID.get(), MoonsquidEntity.createAttributes().build());
        event.put(SelectivepowersEntities.QUETZAL.get(), QuetzalEntity.createAttributes().build());
        event.put(SelectivepowersEntities.QUETZAL_YELLOW.get(), YellowQuetzalEntity.createAttributes().build());
    }
    @SubscribeEvent
    public static void registerSpawnPlacements(RegisterSpawnPlacementsEvent event){
        event.register(SelectivepowersEntities.CROW.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, Animal::checkAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(SelectivepowersEntities.MOON_SQUID.get(), SpawnPlacementTypes.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING, MoonsquidEntity::checkSquidSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(SelectivepowersEntities.QUETZAL.get(), SpawnPlacementTypes.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING, Monster::checkAnyLightMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
    }
    @SubscribeEvent
    public static void onBrewingRecipeRegister(RegisterBrewingRecipesEvent e)
    {
        PotionBrewing.Builder builder = e.getBuilder();
        builder.addMix(Potions.AWKWARD, CORRUPTION_SHARD.get(), SelectivePowerPotions.TRUTH_VISION_POTION);
    }
    @SubscribeEvent
    public static void onWorldLoad(ServerStartingEvent event){
        ServerLevel level = event.getServer().overworld();
        StartAltarManager am = StartAltarManager.get(level);
        if (am.isGenerated()) return;

        BlockPos spawnPos = level.getSharedSpawnPos();

        int structureSize = 47;
        int halfSize = structureSize / 2;

        int[] heights = new int[structureSize * structureSize];
        int index = 0;
        for (int dx = -halfSize; dx <= halfSize; dx++) {
            for (int dz = -halfSize; dz <= halfSize; dz++) {
                int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, spawnPos.getX() + dx, spawnPos.getZ() + dz);
                heights[index++] = y;
            }
        }

        Arrays.sort(heights);
        int medianY;
        int middle = heights.length / 2;
        if (heights.length % 2 == 0) {
            medianY = (heights[middle - 1] + heights[middle]) / 2;
        } else {
            medianY = heights[middle];
        }

        BlockPos adjustedPos = new BlockPos(spawnPos.getX() - halfSize, medianY, spawnPos.getZ() - halfSize);

        StructureTemplate template = level.getStructureManager()
                .getOrCreate(ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "basic_altar"));
        if (template == null) return;

        template.placeInWorld(
                level,
                adjustedPos,
                adjustedPos,
                new StructurePlaceSettings(),
                RandomSource.create(),
                3
        );

        am.setGenerated();
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                SelectivepowersBlockEntities.ITEM_SENTINEL_BE.get(),
                (be, side) -> new InvWrapper(be)
        );
    }

}
