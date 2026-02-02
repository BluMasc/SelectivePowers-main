package net.blumasc.selectivepowers.entity.client.yellowking;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.blumasc.selectivepowers.PowerManager;
import net.blumasc.selectivepowers.SelectivePowers;
import net.blumasc.selectivepowers.client.ClientPowerData;
import net.blumasc.selectivepowers.effect.SelectivepowersEffects;
import net.blumasc.selectivepowers.entity.SelectivepowersEntities;
import net.blumasc.selectivepowers.entity.client.lunarmaiden.LunarMaidenModel;
import net.blumasc.selectivepowers.entity.custom.LunarMaidenEntity;
import net.blumasc.selectivepowers.entity.custom.YellowKingEntity;
import net.blumasc.selectivepowers.item.SelectivepowersItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.Map;
import java.util.UUID;

public class YellowKingLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "textures/entity/yellow_king/texture.png");

    private final YellowKingModel<YellowKingEntity> model;

    private YellowKingEntity fake;

    public YellowKingLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> parent,
                            EntityRendererProvider.Context ctx) {
        super(parent);
        this.model = new YellowKingModel<>(
                ctx.bakeLayer(YellowKingModel.LAYER_LOCATION)
        );
    }
    @Override
    public void render(
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            AbstractClientPlayer player,
            float limbSwing,
            float limbSwingAmount,
            float partialTicks,
            float ageInTicks,
            float netHeadYaw,
            float headPitch
    ) {
       if (!shouldRenderSunForm(player)) return;

       poseStack.pushPose();
        poseStack.scale(1.0f,1.0f,-1.0f);


        YellowKingEntity faker = createFakeEntity(player);

        faker.tickCount = player.tickCount;

        faker.tickAnimationStates();

        faker.yHeadRot = player.yHeadRot;

        model.setupAnim(faker, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        VertexConsumer vc = buffer.getBuffer(model.renderType(TEXTURE));
        model.renderToBuffer(poseStack, vc, packedLight, OverlayTexture.NO_OVERLAY, 0xFFFFFF);

        poseStack.popPose();
    }

    private YellowKingEntity createFakeEntity(AbstractClientPlayer player) {
        Minecraft mc = Minecraft.getInstance();
        Level level = mc.level;

        if (level == null) return null;

        if(fake == null) {
            fake = new YellowKingEntity(
                    SelectivepowersEntities.YELLOW_KING.get(),
                    level
            );
            fake.attackAnimationState.startIfStopped(player.tickCount);
        }

        fake.setPos(player.getX(), player.getY(), player.getZ());
        fake.setYRot(player.getYRot());
        fake.setXRot(player.getXRot());
        fake.yBodyRot = player.yBodyRot;
        fake.yHeadRot = player.yHeadRot;
        fake.idleAnimationState.startIfStopped(player.tickCount);


        return fake;
    }

    public static boolean shouldRenderSunForm(AbstractClientPlayer renderedPlayer) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return false;

        AbstractClientPlayer viewingPlayer = mc.player;

        boolean hasFullTruthPower =  ClientPowerData.truthOwner != null && (ClientPowerData.truthOwner.equals(viewingPlayer.getUUID())) && (ClientPowerData.truthLevel.equals(PowerManager.PowerLevel.ASCENDED));

        boolean hasPotionEffect = viewingPlayer.hasEffect(SelectivepowersEffects.TRUTH_VISION_EFFECT);

        boolean wearingHelmet = isWearingCurioHeadItem(renderedPlayer, SelectivepowersItems.TRUE_CROWN.get());

        boolean isYellowOwner =
                ClientPowerData.yellowOwner != null &&
                        ClientPowerData.yellowOwner.equals(renderedPlayer.getUUID());

        return isYellowOwner && (hasFullTruthPower || hasPotionEffect || wearingHelmet);
    }
    public static boolean isWearingCurioHeadItem(AbstractClientPlayer player, Item expectedItem) {
        return CuriosApi.getCuriosInventory(player)
                .flatMap(curiosInventory -> curiosInventory.getStacksHandler("head"))
                .map(handler -> {
                    for (int slot = 0; slot < handler.getSlots(); slot++) {
                        ItemStack stack = handler.getStacks().getStackInSlot(slot);
                        if (!stack.isEmpty() && stack.is(expectedItem)) {
                            return true;
                        }
                    }
                    return false;
                })
                .orElse(false);
    }

}
