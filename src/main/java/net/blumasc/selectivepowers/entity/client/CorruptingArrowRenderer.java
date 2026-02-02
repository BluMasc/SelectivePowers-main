package net.blumasc.selectivepowers.entity.client;

import net.blumasc.selectivepowers.SelectivePowers;
import net.blumasc.selectivepowers.entity.custom.projectile.CorruptingArrowEntity;
import net.blumasc.selectivepowers.entity.custom.projectile.FlamingFeatherEntity;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class CorruptingArrowRenderer extends ArrowRenderer<CorruptingArrowEntity> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "textures/entity/corrupting_arrow/texture.png");

    public CorruptingArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(CorruptingArrowEntity entity) {
        return TEXTURE;
    }
}
