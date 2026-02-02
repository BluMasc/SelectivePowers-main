package net.blumasc.selectivepowers.entity.client.corruptingmask;

import net.blumasc.selectivepowers.SelectivePowers;
import net.blumasc.selectivepowers.entity.custom.CorruptingMaskEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class CorruptingMaskRenderer extends MobRenderer<CorruptingMaskEntity, CorruptingMaskModel<CorruptingMaskEntity>> {
    public CorruptingMaskRenderer(EntityRendererProvider.Context context) {
        super(context, new CorruptingMaskModel<>(context.bakeLayer(CorruptingMaskModel.LAYER_LOCATION)), 0.4f);
    }

    @Override
    public ResourceLocation getTextureLocation(CorruptingMaskEntity corruptingMaskEntity) {
        return ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "textures/entity/corrupting_mask/texture.png");
    }
}
