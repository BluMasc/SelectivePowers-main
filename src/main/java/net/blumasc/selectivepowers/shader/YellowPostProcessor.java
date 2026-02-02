package net.blumasc.selectivepowers.shader;

import net.blumasc.selectivepowers.SelectivePowers;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
import team.lodestar.lodestone.systems.postprocess.PostProcessor;

public class YellowPostProcessor extends PostProcessor {
    public static final YellowPostProcessor INSTANCE = new YellowPostProcessor();


    static {
        INSTANCE.setActive(false);
    }

    @Override
    public ResourceLocation getPostChainLocation() {
        return ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "yellow_post");
    }

    @Override
    public void beforeProcess(Matrix4f viewModelMatrix) {

    }

    @Override
    public void afterProcess() {

    }
}