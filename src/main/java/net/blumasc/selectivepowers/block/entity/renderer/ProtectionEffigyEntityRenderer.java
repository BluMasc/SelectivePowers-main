package net.blumasc.selectivepowers.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.blumasc.selectivepowers.SelectivePowers;
import net.blumasc.selectivepowers.block.entity.ProtectionEffigyBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class ProtectionEffigyEntityRenderer implements BlockEntityRenderer<ProtectionEffigyBlockEntity> {

    private final ProtectionEffigyModel<Entity> model;

    public ProtectionEffigyEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.model = new ProtectionEffigyModel<>(context.bakeLayer(ProtectionEffigyModel.LAYER_LOCATION));
    }

    @Override
    public void render(ProtectionEffigyBlockEntity entity, float partialTick, PoseStack poseStack,
                       MultiBufferSource buffer, int light, int overlay) {

        poseStack.pushPose();
        poseStack.translate(0.5, 1.0, 0.5);

        float ageInTicks = entity.getLevel() != null ? entity.getLevel().getGameTime() + partialTick : partialTick;

        model.setupAnim(null, 0, 0, ageInTicks, 0, 0);

        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityTranslucent(
                ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "textures/block/protection_effigy.png")));
        model.renderToBuffer(poseStack, vertexConsumer, light, overlay, 0xffffffff);

        poseStack.popPose();
    }
}


