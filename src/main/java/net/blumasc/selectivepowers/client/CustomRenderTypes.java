package net.blumasc.selectivepowers.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderType;
public class CustomRenderTypes extends RenderType {

    private CustomRenderTypes(String name, VertexFormat fmt, VertexFormat.Mode mode,
                               int buf, boolean crumbling, boolean sort,
                               Runnable setup, Runnable clear) {
        super(name, fmt, mode, buf, crumbling, sort, setup, clear);
    }
    public static final RenderType REDSTONE_VISION = create(
            "redstone_vision",
            DefaultVertexFormat.POSITION_COLOR,
            VertexFormat.Mode.QUADS,
            1024,
            false,
            true,
            CompositeState.builder()
                    .setShaderState(POSITION_COLOR_SHADER)
                    .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                    .setDepthTestState(NO_DEPTH_TEST)
                    .setCullState(NO_CULL)
                    .setWriteMaskState(COLOR_DEPTH_WRITE)
                    .createCompositeState(false)
    );
}