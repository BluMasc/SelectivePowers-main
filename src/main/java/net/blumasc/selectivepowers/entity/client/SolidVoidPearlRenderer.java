package net.blumasc.selectivepowers.entity.client;

import net.blumasc.selectivepowers.entity.custom.projectile.ThrownSolidVoidPearl;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class SolidVoidPearlRenderer extends ThrownItemRenderer<ThrownSolidVoidPearl> {

    public SolidVoidPearlRenderer(EntityRendererProvider.Context context) {
        super(context, 1.0F, true);
    }

    @Override
    public boolean shouldRender(ThrownSolidVoidPearl entity,
                                net.minecraft.client.renderer.culling.Frustum frustum,
                                double camX, double camY, double camZ) {

        Entity owner = entity.getOwner();
        Player localPlayer = Minecraft.getInstance().player;

        if (owner == null || localPlayer == null) {
            return false;
        }

        return owner == localPlayer;
    }
}
