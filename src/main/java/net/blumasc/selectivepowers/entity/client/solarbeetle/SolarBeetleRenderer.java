package net.blumasc.selectivepowers.entity.client.solarbeetle;

import com.mojang.blaze3d.vertex.PoseStack;
import net.blumasc.selectivepowers.SelectivePowers;
import net.blumasc.selectivepowers.entity.custom.SolarBeetleEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class SolarBeetleRenderer extends MobRenderer<SolarBeetleEntity, SolarBeetleModel<SolarBeetleEntity>> {
    public SolarBeetleRenderer(EntityRendererProvider.Context context) {
        super(context, new SolarBeetleModel<>(context.bakeLayer(SolarBeetleModel.LAYER_LOCATION)),0.4f);
    }

    @Override
    public ResourceLocation getTextureLocation(SolarBeetleEntity solarBeetleEntity) {
        if (solarBeetleEntity.getCharge()>=100) return ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "textures/entity/solar_beetle/texture_charged.png");
        return ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "textures/entity/solar_beetle/texture.png");
    }

    @Override
    public void render(SolarBeetleEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        if(entity.isBaby())
        {
            poseStack.scale(0.55f,0.55f,0.55f);
        }else{
            poseStack.scale(1f,1f,1f);
        }

        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}
