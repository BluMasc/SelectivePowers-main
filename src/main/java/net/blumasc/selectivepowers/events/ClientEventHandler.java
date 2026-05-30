package net.blumasc.selectivepowers.events;

import com.mojang.blaze3d.vertex.PoseStack;
import net.blumasc.blubasics.item.client.InWorld3dBakedModel;
import net.blumasc.selectivepowers.SelectivePowers;
import net.blumasc.selectivepowers.SelectivePowersClient;
import net.blumasc.selectivepowers.block.SelectivepowersBlocks;
import net.blumasc.selectivepowers.client.ClientPowerData;
import net.blumasc.selectivepowers.effect.SelectivepowersEffects;
import net.blumasc.selectivepowers.entity.client.lunarmaiden.LunarMaidenLayer;
import net.blumasc.selectivepowers.entity.client.moonsquid.PlayerMoonsquidLayer;
import net.blumasc.selectivepowers.entity.client.wings.DragonWingLayer;
import net.blumasc.selectivepowers.entity.client.yellowking.YellowKingLayer;
import net.blumasc.selectivepowers.entity.client.yellowking.YellowKingModel;
import net.blumasc.selectivepowers.network.ActivateAbilityPacket;
import net.blumasc.selectivepowers.network.LeftClickPayload;
import net.blumasc.selectivepowers.network.ModNetworking;
import net.blumasc.selectivepowers.particles.SelectivePowersParticles;
import net.blumasc.selectivepowers.particles.custom.WispParticle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@EventBusSubscriber(modid = SelectivePowers.MODID, value = Dist.CLIENT)
public class ClientEventHandler {

    private static final Random RANDOM = new Random();

    public static LunarMaidenLayer moonLayer;
    public static YellowKingLayer yellowLayer;
    @SubscribeEvent
    public static void onAddLayers(EntityRenderersEvent.AddLayers event) {
        EntityModelSet models = event.getEntityModels();

        for (PlayerSkin.Model skin : event.getSkins()) {
            PlayerRenderer renderer = event.getSkin(skin);


            if (renderer != null) {
                renderer.addLayer(
                        new PlayerMoonsquidLayer<>(renderer, models)
                );
                moonLayer = new LunarMaidenLayer(renderer, event.getContext());
                renderer.addLayer(moonLayer);
                yellowLayer = new YellowKingLayer(renderer, event.getContext());
                renderer.addLayer(yellowLayer);
                renderer.addLayer(new DragonWingLayer(renderer));
            }


        }
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        if (SelectivePowersClient.NORMAL_ABILITY.get().consumeClick()) {
            PacketDistributor.sendToServer(new ActivateAbilityPacket(false));
        }
        if (SelectivePowersClient.ULT_ABILITY.get().consumeClick()) {
            PacketDistributor.sendToServer(new ActivateAbilityPacket(true));
        }
    }

    @SubscribeEvent
    public static void onRenderLiving(RenderLivingEvent.Pre<?, ?> event) {
        LivingEntity entity = event.getEntity();

        if (!entity.hasEffect(SelectivepowersEffects.PARALYZE_EFFECT))
            return;

        PoseStack poseStack = event.getPoseStack();

        int amp = entity.getEffect(SelectivepowersEffects.PARALYZE_EFFECT).getAmplifier() + 1;
        float strength = 0.015f * amp;

        float x = (RANDOM.nextFloat() - 0.5f) * strength;
        float z = (RANDOM.nextFloat() - 0.5f) * strength;

        float yaw = (RANDOM.nextFloat() - 0.5f) * 2.0f * amp;
        entity.yHeadRot += yaw;
        entity.yBodyRot += yaw * 0.5f;


        poseStack.translate(x, 0.0f, z);
    }

    @SubscribeEvent
    public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(SelectivePowersParticles.WISP.get(), spriteSet -> (options, level, x, y, z, mx, my, mz) ->
                new WispParticle(level, x, y, z, mx, my, mz,
                        options.rStart, options.gStart, options.bStart,
                        options.rEnd,   options.gEnd,   options.bEnd)
                {{
                    pickSprite(spriteSet); // picks a texture from your particle's JSON
                }}
        );
    }
    @SubscribeEvent
    public static void onGlaveModelBake(ModelEvent.ModifyBakingResult event) {
        ModelResourceLocation inventoryLoc = ModelResourceLocation.inventory(
                ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "moonlight_glaive"));
        ModelResourceLocation handLoc = ModelResourceLocation.inventory(
                ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "moon_glaive_3d"));

        BakedModel inventoryModel = event.getModels().get(inventoryLoc);
        BakedModel handModel = event.getModels().get(handLoc);

        if (inventoryModel != null && handModel != null) {
            event.getModels().put(inventoryLoc, new InWorld3dBakedModel(inventoryModel, handModel));
        }
    }
    @SubscribeEvent
    public static void onSlicerModelBake(ModelEvent.ModifyBakingResult event) {
        ModelResourceLocation inventoryLoc = ModelResourceLocation.inventory(
                ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "sun_slicer"));
        ModelResourceLocation handLoc = ModelResourceLocation.inventory(
                ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "sun_slicer_3d"));

        BakedModel inventoryModel = event.getModels().get(inventoryLoc);
        BakedModel handModel = event.getModels().get(handLoc);

        if (inventoryModel != null && handModel != null) {
            event.getModels().put(inventoryLoc, new InWorld3dBakedModel(inventoryModel, handModel));
        }
    }

    @SubscribeEvent
    public static void onHammerModelBake(ModelEvent.ModifyBakingResult event) {
        ModelResourceLocation inventoryLoc = ModelResourceLocation.inventory(
                ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "flaming_hammer"));
        ModelResourceLocation handLoc = ModelResourceLocation.inventory(
                ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "flaming_hammer_3d"));

        BakedModel inventoryModel = event.getModels().get(inventoryLoc);
        BakedModel handModel = event.getModels().get(handLoc);

        if (inventoryModel != null && handModel != null) {
            event.getModels().put(inventoryLoc, new InWorld3dBakedModel(inventoryModel, handModel));
        }
    }

    @SubscribeEvent
    public static void onLauncherModelBake(ModelEvent.ModifyBakingResult event) {
        ModelResourceLocation inventoryLoc = ModelResourceLocation.inventory(
                ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "elemental_gun"));
        ModelResourceLocation handLoc = ModelResourceLocation.inventory(
                ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "elemental_gun_3d"));

        BakedModel inventoryModel = event.getModels().get(inventoryLoc);
        BakedModel handModel = event.getModels().get(handLoc);

        if (inventoryModel != null && handModel != null) {
            event.getModels().put(inventoryLoc, new InWorld3dBakedModel(inventoryModel, handModel));
        }
    }

    @SubscribeEvent
    public static void onAnchorModelBake(ModelEvent.ModifyBakingResult event) {
        ModelResourceLocation inventoryLoc = ModelResourceLocation.inventory(
                ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "anchor"));
        ModelResourceLocation handLoc = ModelResourceLocation.inventory(
                ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "anchor_3d"));

        BakedModel inventoryModel = event.getModels().get(inventoryLoc);
        BakedModel handModel = event.getModels().get(handLoc);

        if (inventoryModel != null && handModel != null) {
            event.getModels().put(inventoryLoc, new InWorld3dBakedModel(inventoryModel, handModel));
        }
    }

    private static boolean wasUseDown = false;
    @SubscribeEvent
    public static void onClientRedstoneTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();

        if (mc.player == null) return;

        boolean isUseDown = mc.options.keyUse.isDown();
        if (isUseDown && !wasUseDown) {
            PacketDistributor.sendToServer(new LeftClickPayload());
        }

        wasUseDown = isUseDown;
    }

}