package net.blumasc.selectivepowers.entity.client.crow;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import net.blumasc.selectivepowers.SelectivePowers;
import net.blumasc.selectivepowers.entity.CrowVariant;
import net.blumasc.selectivepowers.entity.custom.CrowEntity;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public class CrowRenderer extends MobRenderer<CrowEntity, CrowModel<CrowEntity>> {

    private static final Map<CrowVariant, ResourceLocation> LOCATION_BY_VARIANT=
            Util.make(Maps.newEnumMap(CrowVariant.class), map ->{
                map.put(CrowVariant.GRAY,
                        ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID,"textures/entity/crow/gray_variant.png"));
                map.put(CrowVariant.WHITE,
                        ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID,"textures/entity/crow/white_variant.png"));
                map.put(CrowVariant.BLUE,
                        ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID,"textures/entity/crow/blue_variant.png"));
            });
    private static final ResourceLocation PINK = ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID,"textures/entity/crow/pink_variant.png");
    private static final ResourceLocation NETT = ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID,"textures/entity/crow/nett_variant.png");
    public CrowRenderer(EntityRendererProvider.Context context) {
        super(context, new CrowModel<>(context.bakeLayer(CrowModel.LAYER_LOCATION)), 0.25f);

        this.addLayer(new CrowBeakItemLayer(this, context.getItemRenderer()));
    }

    @Override
    public ResourceLocation getTextureLocation(CrowEntity crowEntity) {
        if(crowEntity.hasCustomName()) {
            switch( crowEntity.getCustomName().getString().toLowerCase()){
                case "strawberry": return PINK;
                case "nett": return NETT;
            }
        }
        return LOCATION_BY_VARIANT.get(crowEntity.getVariant());
    }

    @Override
    public void render(CrowEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        if(entity.isBaby()){
            poseStack.scale(0.45f,0.45f,0.5f);

        }
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }


}
