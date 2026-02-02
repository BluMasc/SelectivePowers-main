package net.blumasc.selectivepowers.worldgen.features;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.blumasc.selectivepowers.SelectivePowers;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

import java.util.Objects;

public class LunarSkyEffects extends DimensionSpecialEffects {

    private static final ResourceLocation EARTH = ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "textures/sky/earth.png");

    public LunarSkyEffects() {
        super(
                Float.NaN,
                true,
                SkyType.NONE,
                false,
                false
        );
    }

    @Override
    public @NotNull Vec3 getBrightnessDependentFogColor(@NotNull Vec3 vec3, float v) {
        return Vec3.ZERO;
    }

    @Override
    public boolean renderSky(@NotNull ClientLevel level, int ticks, float partialTick, @NotNull Matrix4f modelViewMatrix, @NotNull Camera camera, @NotNull Matrix4f projectionMatrix, boolean isFoggy, @NotNull Runnable setupFog) {

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setProjectionMatrix(projectionMatrix, VertexSorting.DISTANCE_TO_ORIGIN);
        RenderSystem.getModelViewStack().pushMatrix();
        RenderSystem.getModelViewStack().mul(modelViewMatrix);
        var matrix = new Matrix4f();
        matrix.mul(Axis.YP.rotationDegrees(90).get(matrix));
        matrix.mul(Axis.XP.rotationDegrees(30).get(matrix));
        RenderSystem.getModelViewStack().mul(matrix);
        RenderSystem.applyModelViewMatrix();

        drawCelestialQuad();

        RenderSystem.getModelViewStack().popMatrix();
        RenderSystem.applyModelViewMatrix();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();

        return true;
    }

    private void drawCelestialQuad() {
        Tesselator tess = Tesselator.getInstance();
        BufferBuilder buf = tess.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

        float size = 100.0F;

        float y = 450.0F;

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, EARTH);


        buf.addVertex(-size, y, -size).setUv(0, 0);
        buf.addVertex( size, y, -size).setUv(1, 0);
        buf.addVertex( size, y,  size).setUv(1, 1);
        buf.addVertex(-size, y,  size).setUv(0, 1);

        BufferUploader.drawWithShader(Objects.requireNonNull(buf.build()));
    }


    @Override
    public boolean isFoggyAt(int i, int i1) {
        return false;
    }
}
