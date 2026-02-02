package net.blumasc.selectivepowers.item.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.blumasc.selectivepowers.SelectivePowers;
import net.blumasc.selectivepowers.item.SelectivepowersItems;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class ArmCrystalsModel<T extends LivingEntity> extends EntityModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "armcrystalsmodel"), "main");
    public final ModelPart right_arm;

    public ArmCrystalsModel(ModelPart root) {
        this.right_arm = root.getChild("right_arm");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition right_arm = partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r1 = right_arm.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(16, 13).addBox(-1.0F, -2.4F, -0.4F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(-3.0F, 11.0F, 2.0F, -0.0233F, -0.2608F, 0.0903F));

        PartDefinition cube_r2 = right_arm.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(12, 16).addBox(-1.5F, -1.5F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(-2.0F, 8.0F, 2.0F, -0.0873F, -0.0873F, 0.0F));

        PartDefinition cube_r3 = right_arm.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(8, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 9.0F, 2.0F, -0.0983F, -0.478F, 0.0453F));

        PartDefinition cube_r4 = right_arm.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 16).addBox(-0.7F, -2.2F, -0.3F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 5.0F, -2.0F, -0.0873F, 0.0F, -0.1745F));

        PartDefinition cube_r5 = right_arm.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(14, 10).addBox(-1.0F, -2.0F, -0.3F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 6.0F, -2.0F, -0.0289F, -0.2163F, 0.134F));

        PartDefinition cube_r6 = right_arm.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(8, 10).addBox(-1.0F, -2.0F, -0.6F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.2F)), PartPose.offsetAndRotation(-2.0F, 3.0F, -1.0F, -0.6939F, 0.084F, 0.1005F));

        PartDefinition crystal = right_arm.addOrReplaceChild("crystal", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 0.0F, 0.0F, -0.0184F, -0.0395F, 0.4803F));

        PartDefinition cube_r7 = crystal.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(16, 2).addBox(-1.0F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -1.0F, 0.0F, -0.1474F, -0.7872F, 0.0381F));

        PartDefinition cube_r8 = crystal.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(0, 12).addBox(-1.7F, -0.8F, -0.5F, 3.0F, 1.0F, 1.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(-1.0F, -1.0F, 0.0F, 0.2197F, -0.0594F, 0.5582F));

        PartDefinition cube_r9 = crystal.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(16, 0).addBox(-2.0F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(-1.0F, -1.0F, 0.0F, 0.3421F, -0.3662F, -0.2905F));

        PartDefinition cube_r10 = crystal.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(8, 4).addBox(-2.0F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -1.0F, 0.0F, -0.1062F, 0.2097F, -0.0889F));

        PartDefinition crystal2 = right_arm.addOrReplaceChild("crystal2", CubeListBuilder.create().texOffs(0, 4).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 4.0F, 1.0F, -1.7888F, 1.5519F, -1.3092F));

        PartDefinition cube_r11 = crystal2.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(6, 16).addBox(-1.0F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -1.0F, 0.0F, -0.1474F, -0.7872F, 0.0381F));

        PartDefinition cube_r12 = crystal2.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(0, 14).addBox(-1.7F, -0.8F, -0.5F, 3.0F, 1.0F, 1.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(-1.0F, -1.0F, 0.0F, 0.2197F, -0.0594F, 0.5582F));

        PartDefinition cube_r13 = crystal2.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(16, 4).addBox(-2.0F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(-1.0F, -1.0F, 0.0F, 0.3421F, -0.3662F, -0.2905F));

        PartDefinition cube_r14 = crystal2.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(8, 6).addBox(-2.0F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -1.0F, 0.0F, -0.1062F, 0.2097F, -0.0889F));

        PartDefinition crystal3 = right_arm.addOrReplaceChild("crystal3", CubeListBuilder.create().texOffs(0, 8).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 5.0F, -1.0F, -3.0304F, -0.5664F, -0.2404F));

        PartDefinition cube_r15 = crystal3.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(16, 8).addBox(-1.0F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -1.0F, 0.0F, -0.1474F, -0.7872F, 0.0381F));

        PartDefinition cube_r16 = crystal3.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(8, 14).addBox(-1.7F, -0.8F, -0.5F, 3.0F, 1.0F, 1.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(-1.0F, -1.0F, 0.0F, 0.2197F, -0.0594F, 0.5582F));

        PartDefinition cube_r17 = crystal3.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(16, 6).addBox(-2.0F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(-1.0F, -1.0F, 0.0F, 0.3421F, -0.3662F, -0.2905F));

        PartDefinition cube_r18 = crystal3.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(8, 8).addBox(-2.0F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -1.0F, 0.0F, -0.1062F, 0.2097F, -0.0889F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void setupAnim(LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        right_arm.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }
}
