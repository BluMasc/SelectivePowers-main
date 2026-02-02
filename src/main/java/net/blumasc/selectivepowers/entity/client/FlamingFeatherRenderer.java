package net.blumasc.selectivepowers.entity.client;

import net.blumasc.selectivepowers.SelectivePowers;
import net.blumasc.selectivepowers.entity.custom.projectile.FlamingFeatherEntity;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class FlamingFeatherRenderer extends ArrowRenderer<FlamingFeatherEntity> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "textures/entity/flaming_feather/texture.png");

    public FlamingFeatherRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(FlamingFeatherEntity entity) {
        return TEXTURE;
    }
}
