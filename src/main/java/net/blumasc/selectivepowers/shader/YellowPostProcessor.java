package net.blumasc.selectivepowers.shader;

import com.google.gson.JsonSyntaxException;
import net.blumasc.blubasics.BluBasicsMod;
import net.blumasc.selectivepowers.SelectivePowers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

import java.io.IOException;

public class YellowPostProcessor {
    private static PostChain chain = null;

    public static void load() {
        dispose();
        Minecraft mc = Minecraft.getInstance();
        try {
            chain = new PostChain(
                    mc.getTextureManager(),
                    mc.getResourceManager(),
                    mc.getMainRenderTarget(),
                    ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "shaders/post/yellow_post.json")
            );
            chain.resize(mc.getWindow().getWidth(), mc.getWindow().getHeight());
        } catch (IOException | JsonSyntaxException e) {
            e.printStackTrace();
        }
    }

    public static void apply(float partialTick) {
        if (chain == null) return;
        Minecraft mc = Minecraft.getInstance();
        // Resize every frame to match window — cheap enough to do each tick
        chain.resize(mc.getWindow().getWidth(), mc.getWindow().getHeight());
        chain.process(partialTick);
    }

    public static void dispose() {
        if (chain != null) { chain.close(); chain = null; }
    }
}