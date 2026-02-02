package net.blumasc.selectivepowers.entity.client.packwing;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import net.blumasc.selectivepowers.SelectivePowers;
import net.blumasc.selectivepowers.entity.PackwingVariant;
import net.blumasc.selectivepowers.entity.custom.PackwingEntity;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public class PackwingRenderer extends MobRenderer<PackwingEntity, PackwingModel<PackwingEntity>> {

    private static final Map<PackwingVariant, ResourceLocation> LOCATION_BY_VARIANT=
            Util.make(Maps.newEnumMap(PackwingVariant.class), map ->{
                map.put(PackwingVariant.BLUE,
                        ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID,"textures/entity/packwing/blue_packwing.png"));
                map.put(PackwingVariant.PINK,
                        ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID,"textures/entity/packwing/pink_packwing.png"));
                map.put(PackwingVariant.CYAN,
                        ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID,"textures/entity/packwing/cyan_packwing.png"));
                map.put(PackwingVariant.LIME,
                        ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID,"textures/entity/packwing/lime_packwing.png"));
                map.put(PackwingVariant.RED,
                        ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID,"textures/entity/packwing/red_packwing.png"));
            });
    public PackwingRenderer(EntityRendererProvider.Context context) {
        super(context, new PackwingModel<>(context.bakeLayer(PackwingModel.LAYER_LOCATION)), 0.3f);
        this.addLayer(new PackwingTailItemLayer(this, context.getItemRenderer()));
    }

    @Override
    public ResourceLocation getTextureLocation(PackwingEntity packwingEntity) {
        return LOCATION_BY_VARIANT.get(packwingEntity.getVariant());
    }

    @Override
    public void render(PackwingEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        if(!entity.isBaby()){
            poseStack.scale(1.45f,1.45f,1.5f);
        }
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}
