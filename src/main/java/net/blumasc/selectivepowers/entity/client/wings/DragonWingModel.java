package net.blumasc.selectivepowers.entity.client.wings;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.blumasc.selectivepowers.SelectivePowers;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class DragonWingModel<T extends LivingEntity> extends EntityModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "dragonwingmodel"), "main");
    private final ModelPart body;
    private final ModelPart wing;
    private final ModelPart wingtip;
    private final ModelPart wing1;
    private final ModelPart wingtip1;

    public DragonWingModel(ModelPart root) {
        this.body = root.getChild("body");
        this.wing = this.body.getChild("wing");
        this.wingtip = this.wing.getChild("wingtip");
        this.wing1 = this.body.getChild("wing1");
        this.wingtip1 = this.wing1.getChild("wingtip1");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition wing = body.addOrReplaceChild("wing", CubeListBuilder.create().texOffs(0, 33).addBox(-24.0F, -1.0F, -4.0F, 24.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(0, 17).addBox(-24.0F, 2.0F, 0.0F, 25.0F, 0.0F, 16.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(-1.0F, 5.0F, 4.0F, -1.5708F, 0.1745F, 0.1745F));

        PartDefinition wingtip = wing.addOrReplaceChild("wingtip", CubeListBuilder.create().texOffs(0, 43).addBox(-23.0F, -1.0F, -1.0F, 25.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-24.0F, 1.0F, 2.0F, 25.0F, 0.0F, 17.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(-24.0F, 1.0F, -2.0F, 0.0F, 0.0F, -0.3491F));

        PartDefinition wing1 = body.addOrReplaceChild("wing1", CubeListBuilder.create().texOffs(0, 33).addBox(0.0F, -1.0F, -4.0F, 24.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(0, 17).mirror().addBox(-1.0F, 2.0F, 0.0F, 25.0F, 0.0F, 16.0F, new CubeDeformation(0.01F)).mirror(false), PartPose.offsetAndRotation(1.0F, 5.0F, 4.0F, -1.5708F, -0.1745F, -0.1745F));

        PartDefinition wingtip1 = wing1.addOrReplaceChild("wingtip1", CubeListBuilder.create().texOffs(0, 43).addBox(-2.0F, -1.0F, -1.0F, 25.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).mirror().addBox(-1.0F, 1.0F, 2.0F, 25.0F, 0.0F, 17.0F, new CubeDeformation(0.01F)).mirror(false), PartPose.offsetAndRotation(24.0F, 1.0F, -2.0F, 0.0F, 0.0F, 0.3491F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        body.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }

    public void applyWingAnimation(float time, boolean flying) {
        if (flying) {
            // Flying animation (based on your Keyframe values)
            wing.zRot = (float) Math.toRadians(20);
            wing1.zRot = (float) Math.toRadians(-20);
            // wing rotates on Y axis: 0 -> 35 -> 0 -> -17.5 -> 0
            float wingY = oscillate(time, new float[]{0, 35, 0, -17.5f, 0});
            float wingtipZ = oscillate(time, new float[]{0, -10, 25, -35, 0});
            float wing1Y = oscillate(time, new float[]{0, -35, 0, 17.5f, 0});
            float wingtip1Z = oscillate(time, new float[]{0, 10, -25, 35, 0});

            wing.yRot = (float) Math.toRadians(wingY);
            wingtip.zRot = (float) Math.toRadians(wingtipZ);
            wing1.yRot = (float) Math.toRadians(wing1Y);
            wingtip1.zRot = (float) Math.toRadians(wingtip1Z);

        } else {
            float wingY = oscillate(time, new float[]{30, 31, 30, 29, 30});
            float wingtipZ = oscillate(time, new float[]{-80, -85, -90, -85, -80});
            float wing1Y = oscillate(time, new float[]{-30, -31, -30, -29, -30});
            float wingtip1Z = oscillate(time, new float[]{80, 85, 90, 85, 80});

            wing.zRot = (float) Math.toRadians(wingY);
            wingtip.zRot = (float) Math.toRadians(wingtipZ);
            wing1.zRot = (float) Math.toRadians(wing1Y);
            wingtip1.zRot = (float) Math.toRadians(wingtip1Z);
        }
    }

    private float oscillate(float time, float[] keyframes) {
        int count = keyframes.length;
        float segment = 1f / (count - 1);
        float t = time % 1f;
        int idx = (int) (t / segment);
        int nextIdx = (idx + 1) % count;
        float localT = (t - idx * segment) / segment;
        return keyframes[idx] + (keyframes[nextIdx] - keyframes[idx]) * localT;
    }

}
