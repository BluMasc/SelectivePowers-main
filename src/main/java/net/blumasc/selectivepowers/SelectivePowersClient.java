package net.blumasc.selectivepowers;

import com.mojang.blaze3d.platform.InputConstants;
import net.blumasc.selectivepowers.block.SelectivepowersBlocks;
import net.blumasc.selectivepowers.block.entity.SelectivepowersBlockEntities;
import net.blumasc.selectivepowers.block.entity.renderer.*;
import net.blumasc.selectivepowers.enchantment.ModEnchantments;
import net.blumasc.selectivepowers.entity.SelectivepowersEntities;
import net.blumasc.selectivepowers.entity.client.*;
import net.blumasc.selectivepowers.entity.client.chimera.ChimeraRenderer;
import net.blumasc.selectivepowers.entity.client.corruptingmask.CorruptingMaskRenderer;
import net.blumasc.selectivepowers.entity.client.crow.CrowRenderer;
import net.blumasc.selectivepowers.entity.client.echocrab.EchoCrabRenderer;
import net.blumasc.selectivepowers.entity.client.lunarmaiden.LunarMaidenRenderer;
import net.blumasc.selectivepowers.entity.client.moonsquid.MoonsquidRenderer;
import net.blumasc.selectivepowers.entity.client.packwing.PackwingRenderer;
import net.blumasc.selectivepowers.entity.client.quetzal.QuetzalRenderer;
import net.blumasc.selectivepowers.entity.client.salamander.SalamanderRenderer;
import net.blumasc.selectivepowers.entity.client.solarbeetle.SolarBeetleRenderer;
import net.blumasc.selectivepowers.entity.client.yellowfanatic.YellowFanaticRenderer;
import net.blumasc.selectivepowers.entity.client.yellowking.YellowKingRenderer;
import net.blumasc.selectivepowers.item.SelectivepowersItems;
import net.blumasc.selectivepowers.item.client.*;
import net.blumasc.selectivepowers.item.client.gui.LoreScrollScreen;
import net.blumasc.selectivepowers.item.client.gui.ModMenuTypes;
import net.blumasc.selectivepowers.shader.ShadowPostProcessor;
import net.blumasc.selectivepowers.shader.YellowPostProcessor;
import net.blumasc.selectivepowers.worldgen.features.LunarSkyEffects;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterDimensionSpecialEffectsEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.util.Lazy;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
import team.lodestar.lodestone.systems.postprocess.PostProcessHandler;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@Mod(value = SelectivePowers.MODID, dist = Dist.CLIENT)
// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
@EventBusSubscriber(modid = SelectivePowers.MODID, value = Dist.CLIENT)
public class SelectivePowersClient {
    public SelectivePowersClient(ModContainer container) {
        // Allows NeoForge to create a config screen for this mod's configs.
        // The config screen is accessed by going to the Mods screen > clicking on your mod > clicking on config.
        // Do not forget to add translations for your config options to the en_us.json file.
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }
    public static MoonPendantCurioModel<net.minecraft.world.entity.LivingEntity> MOON_PENDANT_MODEL;
    private static final ResourceLocation BLOCKING_PROPERTY_RESLOC = ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "blocking");
    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        // Some client setup code
        EntityRenderers.register(SelectivepowersEntities.CROW.get(), CrowRenderer::new);
        EntityRenderers.register(SelectivepowersEntities.PACKWING.get(), PackwingRenderer::new);
        EntityRenderers.register(SelectivepowersEntities.YELLOW_KING.get(), YellowKingRenderer::new);
        EntityRenderers.register(SelectivepowersEntities.YELLOW_KING_BOSS.get(), YellowKingRenderer::new);
        EntityRenderers.register(SelectivepowersEntities.YELLOW_FANATIC.get(), YellowFanaticRenderer::new);
        EntityRenderers.register(SelectivepowersEntities.CORRUPTING_MASK.get(), CorruptingMaskRenderer::new);
        EntityRenderers.register(SelectivepowersEntities.LUNAR_MAIDEN.get(), LunarMaidenRenderer::new);
        EntityRenderers.register(SelectivepowersEntities.SALAMANDER.get(), SalamanderRenderer::new);
        EntityRenderers.register(SelectivepowersEntities.SOLAR_BEETLE.get(), SolarBeetleRenderer::new);
        EntityRenderers.register(SelectivepowersEntities.ECHO_CRAB.get(), EchoCrabRenderer::new);
        EntityRenderers.register(SelectivepowersEntities.QUETZAL.get(), QuetzalRenderer::new);
        EntityRenderers.register(SelectivepowersEntities.QUETZAL_YELLOW.get(), QuetzalRenderer::new);
        EntityRenderers.register(SelectivepowersEntities.MOON_SQUID.get(), MoonsquidRenderer::new);
        EntityRenderers.register(SelectivepowersEntities.RUNE_PROJECTILE.get(), RuneProjectileRenderer::new);
        EntityRenderers.register(SelectivepowersEntities.MAGIC_CIRCLE.get(), MagicCircleRenderer::new);
        EntityRenderers.register(SelectivepowersEntities.FLAMING_EGG.get(), ThrownItemRenderer::new);
        EntityRenderers.register(SelectivepowersEntities.FLAMING_FEATHER.get(), FlamingFeatherRenderer::new);
        EntityRenderers.register(SelectivepowersEntities.CORRUPTING_ARROW.get(), CorruptingArrowRenderer::new);
        EntityRenderers.register(SelectivepowersEntities.LIGHT_BEAM_ARROW.get(), BeamArrowRenderer::new);
        EntityRenderers.register(SelectivepowersEntities.WOOD_ARROW.get(), WoodArrowRenderer::new);
        EntityRenderers.register(SelectivepowersEntities.LIGHTNING_ROD_ARROW.get(), LightningRodArrowRenderer::new);
        EntityRenderers.register(SelectivepowersEntities.SHARD_PROJECTILE.get(), ShardProjectileRenderer::new);
        EntityRenderers.register(SelectivepowersEntities.LIGHTNING_ARC.get(), LightningArcRenderer::new);
        EntityRenderers.register(SelectivepowersEntities.METEOR.get(), MeteoriteRenderer::new);
        EntityRenderers.register(SelectivepowersEntities.ELEMENTAL_BALL.get(), ElementalBallRenderer::new);
        EntityRenderers.register(SelectivepowersEntities.CHIMERA.get(), ChimeraRenderer::new);
        EntityRenderers.register(SelectivepowersEntities.SOLID_VOID_PEARL.get(), SolidVoidPearlRenderer::new);
        EntityRenderers.register(SelectivepowersEntities.PICKAXE_BOOMERANG.get(), PickaxeBoomerangRenderer::new);
        EntityRenderers.register(SelectivepowersEntities.DRIPSTONE_SPIKE.get(), SpikeEntityRenderer::new);
        EntityRenderers.register(SelectivepowersEntities.WARDEN_BEAM.get(), ProjectileEmptyRenderer::new);
        EntityRenderers.register(SelectivepowersEntities.GRAPE_SHOT.get(), ProjectileEmptyRenderer::new);


        CuriosRendererRegistry.register(SelectivepowersItems.TRUE_CROWN.get(), CrownCurioRenderer::new);
        CuriosRendererRegistry.register(SelectivepowersItems.FAKE_CROWN.get(), CrownCurioRenderer::new);
        CuriosRendererRegistry.register(SelectivepowersItems.MOON_PENDANT.get(), MoonPendantCurioRenderer::new);
        CuriosRendererRegistry.register(SelectivepowersItems.CORRUPTED_MASK.get(), MaskCurioRenderer::new);
        CuriosRendererRegistry.register(SelectivepowersItems.LEAFWALKER_CURIO.get(), LeafwalkerCurioRenderer::new);
        CuriosRendererRegistry.register(SelectivepowersItems.DRAGON_SLEEVES.get(), DragonCurioRenderer::new);
        CuriosRendererRegistry.register(SelectivepowersItems.SHELF_MUSHROOM.get(), ShelfMushroomRenderer::new);
        CuriosRendererRegistry.register(SelectivepowersItems.OCTOPUS_MUSHROOM.get(), OctopusMushroomRenderer::new);
        CuriosRendererRegistry.register(SelectivepowersItems.MUSHROOM_SPORES.get(), SporeParticleRenderer::new);
        CuriosRendererRegistry.register(SelectivepowersItems.ENCHANTING_RUNE.get(), EnchantingParticleRenderer::new);
        CuriosRendererRegistry.register(SelectivepowersItems.POCKET_TESSLA_COIL.get(), LightningSparkleParticleRenderer::new);
        CuriosRendererRegistry.register(SelectivepowersItems.CELESTIAL_RUNE.get(), EndRodParticleRenderer::new);
        CuriosRendererRegistry.register(SelectivepowersItems.DUALITY_PEARL.get(), FireIceParticleRenderer::new);
        CuriosRendererRegistry.register(SelectivepowersItems.BREATH_BAUBLE.get(), EnderParticleRenderer::new);
        CuriosRendererRegistry.register(SelectivepowersItems.SCULK_TENDRIL.get(), VoidParticleRenderer::new);
        CuriosRendererRegistry.register(SelectivepowersItems.HIP_BOOK.get(), HipBookRenderer::new);
        CuriosRendererRegistry.register(SelectivepowersItems.STORM_CLOUD.get(), StormCloudRenderer::new);
        CuriosRendererRegistry.register(SelectivepowersItems.SCULK_MOSS.get(), SkinRenderer::new);
        CuriosRendererRegistry.register(SelectivepowersItems.MOSS_LAYER.get(), SkinRenderer::new);
        CuriosRendererRegistry.register(SelectivepowersItems.LIGHTNING_BALL.get(), SkinRenderer::new);
        CuriosRendererRegistry.register(SelectivepowersItems.EMBEDDED_CRYSTALS.get(), ArmCrystalsRenderer::new);
        CuriosRendererRegistry.register(SelectivepowersItems.SPINE_TREE.get(), BackTreeRenderer::new);
        CuriosRendererRegistry.register(SelectivepowersItems.POLARBEAR_PELT.get(), BearPeltRenderer::new);
        CuriosRendererRegistry.register(SelectivepowersItems.ELEMENTAL_CIRCLET.get(), CircletRenderer::new);
        CuriosRendererRegistry.register(SelectivepowersItems.FLOWER_CROWN.get(), CircletRenderer::new);
        CuriosRendererRegistry.register(SelectivepowersItems.DRAGON_CLAWS.get(), DragonClawsRenderer::new);
        CuriosRendererRegistry.register(SelectivepowersItems.DRAGON_HORNS.get(), DragonHornsRenderer::new);
        CuriosRendererRegistry.register(SelectivepowersItems.HIP_HORN.get(), DrinkHornRenderer::new);
        CuriosRendererRegistry.register(SelectivepowersItems.DUST_MASK.get(), DustMaskRenderer::new);
        CuriosRendererRegistry.register(SelectivepowersItems.SHOULDER_CROW.get(), ShoulderCrowRenderer::new);
        CuriosRendererRegistry.register(SelectivepowersItems.ELEMENTAL_GLOVES.get(), ElementalFistsRenderer::new);
        CuriosRendererRegistry.register(SelectivepowersItems.GLOWING_EYES.get(), FaceTextureRenderer::new);
        CuriosRendererRegistry.register(SelectivepowersItems.FACE_PAINT.get(), FaceTextureRenderer::new);
        CuriosRendererRegistry.register(SelectivepowersItems.HALO.get(), HaloRenderer::new);
        CuriosRendererRegistry.register(SelectivepowersItems.MINERS_HAT.get(), HardHatRenderer::new);
        CuriosRendererRegistry.register(SelectivepowersItems.RUNE.get(), RuneRenderer::new);
        CuriosRendererRegistry.register(SelectivepowersItems.WARDEN_HORNS.get(), WardenHornsRenderer::new);
        CuriosRendererRegistry.register(SelectivepowersItems.WOLF_EARS.get(), WolfEarsRenderer::new);
        CuriosRendererRegistry.register(SelectivepowersItems.WOLF_TAIL.get(), WolfTailRenderer::new);


        event.enqueueWork(() -> {
            ItemBlockRenderTypes.setRenderLayer(
                    SelectivepowersBlocks.MOON_GLASS.get(),
                    RenderType.translucent()
            );
        });

        event.enqueueWork(() -> {
            ItemBlockRenderTypes.setRenderLayer(
                    SelectivepowersBlocks.EARTH_GLASS.get(),
                    RenderType.translucent()
            );
        });
        event.enqueueWork(() -> {
            ItemProperties.register(SelectivepowersItems.ICE_SHIELD.get(), BLOCKING_PROPERTY_RESLOC, ($itemStack, $level, $entity, $seed) ->
                    $entity != null && $entity.isUsingItem() && $entity.getUseItem() == $itemStack ? 1.0F : 0.0F);
        });

        PostProcessHandler.addInstance(ShadowPostProcessor.INSTANCE);
        PostProcessHandler.addInstance(YellowPostProcessor.INSTANCE);

        ItemColors itemColors = Minecraft.getInstance().getItemColors();

        itemColors.register((stack, tintIndex) -> {
            if (tintIndex != 0) {
                return -1;
            }

            Holder<Enchantment> devilsTool = getDevilsToolHolder();
            if (devilsTool == null) {
                return -1;
            }

            ItemEnchantments enchants = stack.get(DataComponents.ENCHANTMENTS);
            if (enchants != null && enchants.getLevel(devilsTool) > 0) {
                return 0xFFF04040;
            }

            return -1;
        }, Items.TRIDENT);

    }

    @Nullable
    private static Holder<Enchantment> getDevilsToolHolder() {
        var mc = Minecraft.getInstance();

        if (mc.getConnection() == null) return null;

        return mc.getConnection()
                .registryAccess()
                .lookupOrThrow(Registries.ENCHANTMENT)
                .get(ModEnchantments.DEVILS_TOOL)
                .orElse(null);
    }

    public static final Lazy<KeyMapping> NORMAL_ABILITY = Lazy.of(() -> new KeyMapping(
            "key.selectivepowers.normal_ability",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_R,
            "key.categories.selectivepowers"
    ));
    public static final Lazy<KeyMapping> ULT_ABILITY = Lazy.of(() ->new KeyMapping(
            "key.selectivepowers.ult_ability",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_U,
            "key.categories.selectivepowers"
    ));

    @SubscribeEvent // on the mod event bus only on the physical client
    public static void registerBindings(RegisterKeyMappingsEvent event) {
        event.register(NORMAL_ABILITY.get());
        event.register(ULT_ABILITY.get());
    }

    @SubscribeEvent
    public static void registerBER(EntityRenderersEvent.RegisterRenderers event){
        event.registerBlockEntityRenderer(SelectivepowersBlockEntities.ALTAR_BE.get(), AltarBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(SelectivepowersBlockEntities.NEST_BE.get(), NestBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(SelectivepowersBlockEntities.PROTECTION_EFFIGY_BE.get(), ProtectionEffigyEntityRenderer::new);
        event.registerBlockEntityRenderer(SelectivepowersBlockEntities.SAC_ALTAR_BE.get(), SacrificeAltarBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(SelectivepowersBlockEntities.PITFALL_TRAP_BE.get(), TrapBlockEntityRenderer::new);
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenuTypes.LORE_SCROLL_MENU.get(), LoreScrollScreen::new);
    }

    @SubscribeEvent
    public static void registerSky(RegisterDimensionSpecialEffectsEvent event){
        event.register(
                ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "lunar"),
                new LunarSkyEffects()
        );
    }

}
