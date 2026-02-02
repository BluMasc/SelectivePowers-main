package net.blumasc.selectivepowers.entity.client;

import net.blumasc.selectivepowers.SelectivePowers;
import net.blumasc.selectivepowers.entity.custom.projectile.LightBeamArrowEntity;
import net.blumasc.selectivepowers.entity.custom.projectile.WoodArrow;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class WoodArrowRenderer extends ArrowRenderer<WoodArrow> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "textures/entity/wood_arrow/texture.png");

    public WoodArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(WoodArrow entity) {
        return TEXTURE;
    }
}
