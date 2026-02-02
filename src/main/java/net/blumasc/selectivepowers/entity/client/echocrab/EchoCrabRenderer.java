package net.blumasc.selectivepowers.entity.client.echocrab;

import net.blumasc.selectivepowers.SelectivePowers;
import net.blumasc.selectivepowers.entity.custom.EchoCrabEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class EchoCrabRenderer extends MobRenderer<EchoCrabEntity, EchoCrabModel<EchoCrabEntity>> {

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID,"textures/entity/echo_crab/texture.png");
    public EchoCrabRenderer(EntityRendererProvider.Context context) {
        super(context, new EchoCrabModel<>(context.bakeLayer(EchoCrabModel.LAYER_LOCATION)), 0.4f);
    }

    @Override
    public ResourceLocation getTextureLocation(EchoCrabEntity echoCrabEntity) {
        return TEXTURE;
    }
}
