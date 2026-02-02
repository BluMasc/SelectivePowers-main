package net.blumasc.selectivepowers.entity.client.quetzal;

import net.blumasc.selectivepowers.SelectivePowers;
import net.blumasc.selectivepowers.entity.custom.QuetzalEntity;
import net.blumasc.selectivepowers.entity.custom.YellowQuetzalEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class QuetzalRenderer extends MobRenderer<QuetzalEntity, QuetzalModel<QuetzalEntity>> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID,"textures/entity/flamewing_snake/texture.png");
    private static final ResourceLocation TEXTURE_YELLOW = ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID,"textures/entity/flamewing_snake/texture_yellow.png");

    public QuetzalRenderer(EntityRendererProvider.Context context) {
        super(context, new QuetzalModel<>(context.bakeLayer(QuetzalModel.LAYER_LOCATION)), 0.4f);
    }

    @Override
    public ResourceLocation getTextureLocation(QuetzalEntity quetzalEntity) {
        if(quetzalEntity instanceof YellowQuetzalEntity) return TEXTURE_YELLOW;
        return TEXTURE;
    }
}
