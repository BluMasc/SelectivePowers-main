package net.blumasc.selectivepowers.events;

import com.mojang.blaze3d.vertex.PoseStack;
import net.blumasc.selectivepowers.SelectivePowers;
import net.blumasc.selectivepowers.SelectivePowersClient;
import net.blumasc.selectivepowers.block.SelectivepowersBlocks;
import net.blumasc.selectivepowers.block.entity.renderer.TrapClientExtension;
import net.blumasc.selectivepowers.client.ClientPowerData;
import net.blumasc.selectivepowers.effect.SelectivepowersEffects;
import net.blumasc.selectivepowers.entity.client.lunarmaiden.LunarMaidenLayer;
import net.blumasc.selectivepowers.entity.client.moonsquid.PlayerMoonsquidLayer;
import net.blumasc.selectivepowers.entity.client.wings.DragonWingLayer;
import net.blumasc.selectivepowers.entity.client.yellowking.YellowKingLayer;
import net.blumasc.selectivepowers.entity.client.yellowking.YellowKingModel;
import net.blumasc.selectivepowers.network.ActivateAbilityPacket;
import net.blumasc.selectivepowers.network.ModNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RenderLivingEvent;
import net.neoforged.neoforge.client.event.RenderPlayerEvent;
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

    private static final Map<Integer, Boolean> ORIGINAL_GLOW = new HashMap<>();

    @SubscribeEvent
    public static void onRenderLivingPre(RenderLivingEvent.Pre<?, ?> event) {
        LivingEntity entity = event.getEntity();
        Minecraft mc = Minecraft.getInstance();

        if (mc.player == null) return;

        if (!entity.isInvisible()) return;
        if(ClientPowerData.truthOwner == null) return;
        if (!ClientPowerData.truthOwner.equals(mc.player.getUUID())) return;

        int id = entity.getId();

        ORIGINAL_GLOW.putIfAbsent(id, entity.isCurrentlyGlowing());

        entity.setGlowingTag(true);
    }

    @SubscribeEvent
    public static void onRenderLivingPost(RenderLivingEvent.Post<?, ?> event) {
        LivingEntity entity = event.getEntity();
        Minecraft mc = Minecraft.getInstance();

        if (mc.player == null) return;
        if(ClientPowerData.truthOwner == null) return;
        if (!ClientPowerData.truthOwner.equals(mc.player.getUUID())) return;

        int id = entity.getId();
        Boolean original = ORIGINAL_GLOW.remove(id);
        if (original != null) {
            entity.setGlowingTag(original);
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
    public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerBlock(
                new TrapClientExtension(),
                SelectivepowersBlocks.PITFALL_TRAP.get()
        );
    }

}