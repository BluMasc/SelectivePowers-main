package net.blumasc.selectivepowers.entity.client;

import net.blumasc.selectivepowers.SelectivePowers;
import net.blumasc.selectivepowers.entity.custom.projectile.CorruptingArrowEntity;
import net.blumasc.selectivepowers.entity.custom.projectile.LightBeamArrowEntity;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class BeamArrowRenderer extends ArrowRenderer<LightBeamArrowEntity> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "textures/entity/beam_arrow/texture.png");

    public BeamArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(LightBeamArrowEntity entity) {
        return TEXTURE;
    }
}
