package net.blumasc.selectivepowers.entity.client.yellowfanatic;

import net.blumasc.selectivepowers.SelectivePowers;
import net.blumasc.selectivepowers.entity.custom.YellowFanaticEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class YellowFanaticRenderer extends MobRenderer<YellowFanaticEntity, YellowFanaticModel<YellowFanaticEntity>> {
    public YellowFanaticRenderer(EntityRendererProvider.Context context) {
        super(context, new YellowFanaticModel<>(context.bakeLayer(YellowFanaticModel.LAYER_LOCATION)), 0.5f);
        this.addLayer(new YellowFanaticHandItemLayer(this, context.getItemRenderer()));
    }

    @Override
    public ResourceLocation getTextureLocation(YellowFanaticEntity yellowFanaticEntity) {
        return ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "textures/entity/yellow_fanatic/texture.png");
    }
}
