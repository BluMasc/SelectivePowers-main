package net.blumasc.selectivepowers.entity.client.salamander;

import com.mojang.blaze3d.vertex.PoseStack;
import net.blumasc.selectivepowers.SelectivePowers;
import net.blumasc.selectivepowers.entity.custom.SalamanderEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class SalamanderRenderer extends MobRenderer<SalamanderEntity, SalamanderModel<SalamanderEntity>> {
    public SalamanderRenderer(EntityRendererProvider.Context context) {
        super(context, new SalamanderModel<>(context.bakeLayer(SalamanderModel.LAYER_LOCATION)), 0.6f);
    }

    @Override
    public ResourceLocation getTextureLocation(SalamanderEntity salamanderEntity) {
        return ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "textures/entity/salamander/texture.png");
    }

    @Override
    public void render(SalamanderEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        if(entity.isBaby())
        {
            poseStack.scale(0.55f,0.55f,0.55f);
        }else{
            poseStack.scale(1f,1f,1f);
        }

        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}
