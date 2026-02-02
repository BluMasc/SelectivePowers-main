package net.blumasc.selectivepowers.entity.client.yellowfanatic;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.blumasc.selectivepowers.entity.custom.YellowFanaticEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class YellowFanaticHandItemLayer extends RenderLayer<YellowFanaticEntity, YellowFanaticModel<YellowFanaticEntity>> {

    private final ItemRenderer itemRenderer;

    public YellowFanaticHandItemLayer(RenderLayerParent<YellowFanaticEntity, YellowFanaticModel<YellowFanaticEntity>> renderer, ItemRenderer itemRenderer) {
        super(renderer);
        this.itemRenderer = itemRenderer;
    }

    @Override
    public void render(PoseStack poseStack,
                       MultiBufferSource buffer,
                       int packedLight,
                       YellowFanaticEntity yellowFanatic,
                       float limbSwing,
                       float limbSwingAmount,
                       float partialTicks,
                       float ageInTicks,
                       float netHeadYaw,
                       float headPitch) {
        ItemStack item = new ItemStack(Items.GOLDEN_SWORD, 1);

        poseStack.pushPose();

        YellowFanaticModel<?> model = this.getParentModel();

        model.root().translateAndRotate(poseStack);
        model.getWaist().translateAndRotate(poseStack);
        model.getBody().translateAndRotate(poseStack);
        model.getRightArm().translateAndRotate(poseStack);
        model.getRightItem().translateAndRotate(poseStack);


        poseStack.scale(0.8F, 0.8F, 0.8F);

        poseStack.mulPose(Axis.YP.rotationDegrees(-90f));
        poseStack.mulPose(Axis.ZP.rotationDegrees(-45f));

        itemRenderer.renderStatic(
                item,
                ItemDisplayContext.GROUND,
                packedLight,
                OverlayTexture.NO_OVERLAY,
                poseStack,
                buffer,
                yellowFanatic.level(),
                yellowFanatic.getId()
        );

        poseStack.popPose();
    }
}
