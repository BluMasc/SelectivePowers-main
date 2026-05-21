package net.blumasc.selectivepowers.item.client;// Made with Blockbench 5.1.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


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

public class PirateHatModel<T extends LivingEntity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "piratehat"), "main");
	public final ModelPart head;

	public PirateHatModel(ModelPart root) {
		this.head = root.getChild("head");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 10).addBox(-9.0F, -9.5F, -1.0F, 18.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-10.0F, -11.0F, -1.0F, 20.0F, 2.0F, 8.0F, new CubeDeformation(-0.01F))
		.texOffs(0, 18).addBox(-9.0F, -14.0F, 1.0F, 18.0F, 3.0F, 3.0F, new CubeDeformation(-0.01F))
		.texOffs(0, 37).addBox(-8.0F, -13.0F, 4.0F, 16.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(30, 40).addBox(-7.0F, -12.0F, 5.0F, 14.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(50, 13).addBox(10.0F, -11.0F, 1.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(34, 51).addBox(-11.0F, -11.0F, 1.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(34, 37).addBox(-8.0F, -11.0F, 7.0F, 16.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(8, 52).addBox(9.0F, -12.0F, 2.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(52, 23).addBox(-10.0F, -12.0F, 2.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 50).addBox(-4.0F, -12.0F, 6.0F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 40).addBox(-6.0F, -16.0F, 1.0F, 12.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 52).addBox(-1.0F, -16.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(50, 10).addBox(-3.0F, -16.0F, 0.0F, 6.0F, 2.0F, 1.0F, new CubeDeformation(0.02F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r1 = head.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(44, 51).addBox(-1.0F, -3.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -9.0F, -7.0F, 0.0F, -0.7854F, 0.0F));

		PartDefinition cube_r2 = head.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(48, 47).addBox(-8.0F, -3.0F, -1.0F, 9.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -13.0F, -2.0F, -0.3927F, 0.6109F, 0.0F));

		PartDefinition cube_r3 = head.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(26, 47).addBox(-1.0F, -3.0F, -1.0F, 9.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -13.0F, -2.0F, -0.3927F, -0.6109F, 0.0F));

		PartDefinition cube_r4 = head.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(30, 42).addBox(-9.0F, -3.0F, -1.0F, 11.0F, 3.0F, 2.0F, new CubeDeformation(0.02F)), PartPose.offsetAndRotation(-1.0F, -11.0F, -3.0F, 0.0F, 0.6109F, 0.0F));

		PartDefinition cube_r5 = head.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(42, 18).addBox(-2.0F, -3.0F, -1.0F, 11.0F, 3.0F, 2.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(1.0F, -11.0F, -3.0F, 0.0F, -0.6109F, 0.0F));

		PartDefinition cube_r6 = head.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(26, 24).addBox(-2.0F, -3.0F, -1.0F, 3.0F, 3.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -9.0F, -5.0F, 0.0F, -0.8727F, 0.0F));

		PartDefinition cube_r7 = head.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(0, 24).addBox(-1.0F, -3.0F, -1.0F, 3.0F, 3.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -9.0F, -5.0F, 0.0F, 0.8727F, 0.0F));

		PartDefinition cube_r8 = head.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(26, 51).addBox(-2.0F, -7.0F, -1.0F, 3.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.25F, -9.0F, -6.0F, -0.0873F, 0.48F, 0.0F));

		PartDefinition cube_r9 = head.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(18, 50).addBox(-1.0F, -7.0F, -1.0F, 3.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.25F, -9.0F, -6.0F, -0.0873F, -0.48F, 0.0F));

		PartDefinition cube_r10 = head.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(0, 47).addBox(-8.0F, -1.0F, -1.0F, 11.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, -8.5F, -6.0F, 0.0F, 0.7854F, 0.0F));

		PartDefinition cube_r11 = head.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(0, 44).addBox(-3.0F, -1.0F, -1.0F, 11.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, -8.5F, -6.0F, 0.0F, -0.7854F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
		head.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
	}
}