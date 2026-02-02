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

public class OctopusMushroomModel<T extends LivingEntity> extends EntityModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "octopusmushroommodel"), "main");
    public final ModelPart body;
    private final ModelPart tentacle;
    private final ModelPart tentacle2;
    private final ModelPart tentacle3;
    private final ModelPart tentacle4;

    public OctopusMushroomModel(ModelPart root) {
        this.body = root.getChild("body");
        this.tentacle = this.body.getChild("tentacle");
        this.tentacle2 = this.body.getChild("tentacle2");
        this.tentacle3 = this.body.getChild("tentacle3");
        this.tentacle4 = this.body.getChild("tentacle4");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r1 = body.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(26, 10).addBox(-1.0F, -1.0F, 0.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(-0.31F)), PartPose.offsetAndRotation(-6.0F, -1.0F, 1.0F, 0.0F, -0.48F, 0.0F));

        PartDefinition cube_r2 = body.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(10, 9).addBox(-3.0F, -1.0F, 0.0F, 5.0F, 1.0F, 1.0F, new CubeDeformation(-0.31F)), PartPose.offsetAndRotation(-6.0F, -2.0F, 3.0F, 0.0F, -0.3927F, 0.0F));

        PartDefinition cube_r3 = body.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(22, 8).addBox(-1.0F, -1.0F, 0.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(-0.31F)), PartPose.offsetAndRotation(-7.0F, -1.0F, -2.0F, 0.0F, 0.4363F, 0.0F));

        PartDefinition cube_r4 = body.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(6, 29).addBox(-2.0F, -1.0F, 0.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(-0.31F)), PartPose.offsetAndRotation(-8.0F, -1.0F, -1.0F, 0.0F, 1.5272F, 0.0F));

        PartDefinition cube_r5 = body.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(10, 7).addBox(-3.0F, -1.0F, 0.0F, 5.0F, 1.0F, 1.0F, new CubeDeformation(-0.31F)), PartPose.offsetAndRotation(-10.0F, -2.0F, -1.0F, 0.0F, 1.5272F, 0.0F));

        PartDefinition cube_r6 = body.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(10, 5).addBox(-3.0F, -1.0F, 0.0F, 5.0F, 1.0F, 1.0F, new CubeDeformation(-0.31F)), PartPose.offsetAndRotation(-7.0F, -2.0F, -4.0F, 0.0F, 0.4363F, 0.0F));

        PartDefinition tentacle = body.addOrReplaceChild("tentacle", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -3.0F, -6.0F, 2.0F, 2.0F, 3.0F, new CubeDeformation(-0.3F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r7 = tentacle.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(26, 12).addBox(-0.5F, 0.0F, -2.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, -2.0F, -6.0F, 0.7418F, 0.0F, 0.0F));

        PartDefinition cube_r8 = tentacle.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(20, 0).addBox(-1.0F, -1.5F, 0.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.4F)), PartPose.offsetAndRotation(-5.0F, -1.0F, -6.0F, 0.4363F, 0.0F, 0.0F));

        PartDefinition cube_r9 = tentacle.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(0, 20).addBox(-0.8F, -3.5F, -1.2F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.3F)), PartPose.offsetAndRotation(-5.0F, -1.0F, -1.0F, 1.1303F, 0.0795F, -0.1041F));

        PartDefinition cube_r10 = tentacle.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(18, 11).addBox(-0.8F, -2.5F, -1.5F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.3F)), PartPose.offsetAndRotation(-5.0F, -1.0F, -1.0F, 0.9558F, 0.0795F, -0.1041F));

        PartDefinition cube_r11 = tentacle.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(10, 11).addBox(-0.8F, -2.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(-0.3F)), PartPose.offsetAndRotation(-5.0F, 0.0F, -1.0F, 0.6504F, 0.0795F, -0.1041F));

        PartDefinition tentacle2 = body.addOrReplaceChild("tentacle2", CubeListBuilder.create().texOffs(0, 5).addBox(-1.0F, -3.0F, -6.0F, 2.0F, 2.0F, 3.0F, new CubeDeformation(-0.3F)), PartPose.offsetAndRotation(-5.0F, 0.0F, 0.0F, 0.0F, 1.0908F, 0.0F));

        PartDefinition cube_r12 = tentacle2.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(24, 27).addBox(-0.5F, 0.0F, -2.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.0F, -6.0F, 0.7418F, 0.0F, 0.0F));

        PartDefinition cube_r13 = tentacle2.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(22, 4).addBox(-1.0F, -1.5F, 0.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.4F)), PartPose.offsetAndRotation(0.0F, -1.0F, -6.0F, 0.4363F, 0.0F, 0.0F));

        PartDefinition cube_r14 = tentacle2.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(16, 21).addBox(-0.8F, -3.5F, -1.2F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.3F)), PartPose.offsetAndRotation(0.0F, -1.0F, -1.0F, 1.1303F, 0.0795F, -0.1041F));

        PartDefinition cube_r15 = tentacle2.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(8, 21).addBox(-0.8F, -2.5F, -1.5F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.3F)), PartPose.offsetAndRotation(0.0F, -1.0F, -1.0F, 0.9558F, 0.0795F, -0.1041F));

        PartDefinition cube_r16 = tentacle2.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(0, 15).addBox(-0.8F, -2.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(-0.3F)), PartPose.offsetAndRotation(0.0F, 0.0F, -1.0F, 0.6504F, 0.0795F, -0.1041F));

        PartDefinition tentacle3 = body.addOrReplaceChild("tentacle3", CubeListBuilder.create().texOffs(0, 10).addBox(-1.0F, -3.0F, -6.0F, 2.0F, 2.0F, 3.0F, new CubeDeformation(-0.3F)), PartPose.offsetAndRotation(-5.0F, 0.0F, 0.0F, 0.0F, 2.0508F, 0.0F));

        PartDefinition cube_r17 = tentacle3.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(0, 28).addBox(-0.5F, 0.0F, -2.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.0F, -6.0F, 0.7418F, 0.0F, 0.0F));

        PartDefinition cube_r18 = tentacle3.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(24, 19).addBox(-1.0F, -1.5F, 0.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.4F)), PartPose.offsetAndRotation(0.0F, -1.0F, -6.0F, 0.4363F, 0.0F, 0.0F));

        PartDefinition cube_r19 = tentacle3.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(24, 15).addBox(-0.8F, -3.5F, -1.2F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.3F)), PartPose.offsetAndRotation(0.0F, -1.0F, -1.0F, 1.1303F, 0.0795F, -0.1041F));

        PartDefinition cube_r20 = tentacle3.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(0, 24).addBox(-0.8F, -2.5F, -1.5F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.3F)), PartPose.offsetAndRotation(0.0F, -1.0F, -1.0F, 0.9558F, 0.0795F, -0.1041F));

        PartDefinition cube_r21 = tentacle3.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(8, 16).addBox(-0.8F, -2.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(-0.3F)), PartPose.offsetAndRotation(0.0F, 0.0F, -1.0F, 0.6504F, 0.0795F, -0.1041F));

        PartDefinition tentacle4 = body.addOrReplaceChild("tentacle4", CubeListBuilder.create().texOffs(10, 0).addBox(-1.0F, -3.0F, -6.0F, 2.0F, 2.0F, 3.0F, new CubeDeformation(-0.3F)), PartPose.offsetAndRotation(-5.0F, 0.0F, 0.0F, 0.0F, -3.0543F, 0.0F));

        PartDefinition cube_r22 = tentacle4.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(28, 0).addBox(-0.5F, 0.0F, -2.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.0F, -6.0F, 0.7418F, 0.0F, 0.0F));

        PartDefinition cube_r23 = tentacle4.addOrReplaceChild("cube_r23", CubeListBuilder.create().texOffs(16, 25).addBox(-1.0F, -1.5F, 0.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.4F)), PartPose.offsetAndRotation(0.0F, -1.0F, -6.0F, 0.4363F, 0.0F, 0.0F));

        PartDefinition cube_r24 = tentacle4.addOrReplaceChild("cube_r24", CubeListBuilder.create().texOffs(8, 25).addBox(-0.8F, -3.5F, -1.2F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.3F)), PartPose.offsetAndRotation(0.0F, -1.0F, -1.0F, 1.1303F, 0.0795F, -0.1041F));

        PartDefinition cube_r25 = tentacle4.addOrReplaceChild("cube_r25", CubeListBuilder.create().texOffs(24, 23).addBox(-0.8F, -2.5F, -1.5F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.3F)), PartPose.offsetAndRotation(0.0F, -1.0F, -1.0F, 0.9558F, 0.0795F, -0.1041F));

        PartDefinition cube_r26 = tentacle4.addOrReplaceChild("cube_r26", CubeListBuilder.create().texOffs(16, 16).addBox(-0.8F, -2.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(-0.3F)), PartPose.offsetAndRotation(0.0F, 0.0F, -1.0F, 0.6504F, 0.0795F, -0.1041F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        body.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }
}
