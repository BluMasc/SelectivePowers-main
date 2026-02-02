package net.blumasc.selectivepowers.entity.client.packwing;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.blumasc.selectivepowers.entity.custom.PackwingEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class PackwingTailItemLayer extends RenderLayer<PackwingEntity, PackwingModel<PackwingEntity>> {

    private final ItemRenderer itemRenderer;

    public PackwingTailItemLayer(RenderLayerParent<PackwingEntity, PackwingModel<PackwingEntity>> parent,
                                 ItemRenderer itemRenderer) {
        super(parent);
        this.itemRenderer = itemRenderer;
    }

    @Override
    public void render(PoseStack poseStack,
                       MultiBufferSource buffer,
                       int packedLight,
                       PackwingEntity Packwing,
                       float limbSwing,
                       float limbSwingAmount,
                       float partialTicks,
                       float ageInTicks,
                       float netHeadYaw,
                       float headPitch) {

        ItemStack item = Packwing.getMainHandItem();
        if (item.isEmpty())
            return;

        poseStack.pushPose();

        PackwingModel<?> model = this.getParentModel();

        model.root().translateAndRotate(poseStack);
        model.getTail().translateAndRotate(poseStack);
        model.getMiddleSegment().translateAndRotate(poseStack);

        poseStack.translate(0.0F, 0.0F, 0.1F);
        poseStack.scale(0.5F, 0.5F, 0.5F);

        poseStack.mulPose(Axis.XP.rotationDegrees(90f));

        itemRenderer.renderStatic(
                item,
                ItemDisplayContext.GROUND,
                packedLight,
                OverlayTexture.NO_OVERLAY,
                poseStack,
                buffer,
                Packwing.level(),
                Packwing.getId()
        );

        poseStack.popPose();
    }
}
