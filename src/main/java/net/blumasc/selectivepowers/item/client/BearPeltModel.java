package net.blumasc.selectivepowers.item.client;

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

public class BearPeltModel<T extends LivingEntity> extends EntityModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "bearpeltmodel"), "main");
    public final ModelPart hat;
    public final ModelPart body;
    public final ModelPart right_arm;
    public final ModelPart left_arm;

    public BearPeltModel(ModelPart root) {
        this.hat = root.getChild("hat");
        this.body = root.getChild("body");
        this.right_arm = root.getChild("right_arm");
        this.left_arm = root.getChild("left_arm");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition hat = partdefinition.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition head = hat.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-5.5F, -2.0F, -4.0F, 11.0F, 6.0F, 12.0F, new CubeDeformation(0.01F))
                .texOffs(0, 44).addBox(-2.5F, 1.0F, -6.0F, 5.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(40, 33).addBox(-6.5F, -3.0F, -1.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(42, 25).addBox(4.5F, -3.0F, -1.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -9.0F, -2.0F));

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 44).addBox(1.5F, -1.0F, -4.0F, 4.0F, 10.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(26, 44).addBox(-5.5F, -1.0F, -4.0F, 4.0F, 10.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 18).addBox(-6.0F, -2.0F, -4.0F, 12.0F, 1.0F, 9.0F, new CubeDeformation(-0.01F))
                .texOffs(0, 28).addBox(-9.0F, -2.0F, 3.0F, 18.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(40, 28).addBox(-7.0F, 5.0F, 3.0F, 14.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 37).addBox(-8.0F, -4.0F, 4.0F, 16.0F, 5.0F, 2.0F, new CubeDeformation(-0.1F))
                .texOffs(36, 37).addBox(-7.0F, -6.0F, 4.0F, 14.0F, 3.0F, 2.0F, new CubeDeformation(-0.2F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition right_arm = partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(42, 18).addBox(-3.0F, -3.0F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(1.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition left_arm = partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(36, 42).addBox(-1.0F, -3.0F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(1.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        hat.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        body.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        right_arm.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        left_arm.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }
}
