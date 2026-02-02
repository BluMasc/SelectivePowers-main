package net.blumasc.selectivepowers.entity.client.moonsquid;

import net.blumasc.selectivepowers.SelectivePowers;
import net.blumasc.selectivepowers.entity.custom.MoonsquidEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class MoonsquidRenderer extends MobRenderer<MoonsquidEntity, MoonsquidModel<MoonsquidEntity>>{

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID,"textures/entity/moon_squid/texture.png");

    public MoonsquidRenderer(EntityRendererProvider.Context context) {
        super(context, new MoonsquidModel<>(context.bakeLayer(MoonsquidModel.LAYER_LOCATION)), 0.2f);
    }

    @Override
    public ResourceLocation getTextureLocation(MoonsquidEntity moonsquidEntity) {
        return TEXTURE;
    }
    public static ResourceLocation getStaticTextureLocation() {
        return TEXTURE;
    }
}