package net.blumasc.selectivepowers.entity.client;

import net.blumasc.selectivepowers.SelectivePowers;
import net.blumasc.selectivepowers.entity.custom.projectile.CorruptingArrowEntity;
import net.blumasc.selectivepowers.entity.custom.projectile.LightningRodArrowEntity;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class LightningRodArrowRenderer extends ArrowRenderer<LightningRodArrowEntity> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "textures/entity/lightning_rod_arrow/texture.png");

    public LightningRodArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(LightningRodArrowEntity entity) {
        return TEXTURE;
    }
}
