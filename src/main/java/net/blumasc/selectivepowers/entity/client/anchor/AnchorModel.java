package net.blumasc.selectivepowers.entity.client.anchor;// Made with Blockbench 5.1.3
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
import net.minecraft.world.entity.Entity;

public class AnchorModel<T extends Entity> extends EntityModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "anchor_model"), "main");
	private final ModelPart bone;

	public AnchorModel(ModelPart root) {
		this.bone = root.getChild("bone");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(8, 0).addBox(-12.0F, -1.5F, 7.0F, 8.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(20, 20).addBox(-2.3F, -6.8F, 7.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 22).addBox(-14.7F, -6.8F, 7.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-9.0F, -15.5F, 7.0F, 2.0F, 15.0F, 2.0F, new CubeDeformation(-0.2F))
		.texOffs(8, 3).addBox(-12.5F, -11.5F, 7.5F, 9.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(12, 19).addBox(-5.0F, -12.0F, 7.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.3F))
		.texOffs(20, 16).addBox(-13.0F, -12.0F, 7.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.3F))
		.texOffs(8, 16).addBox(-10.0F, -15.5F, 7.0F, 4.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 19).addBox(-10.0F, -18.5F, 7.0F, 4.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(12, 23).addBox(-7.0F, -17.5F, 7.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(24, 5).addBox(-10.0F, -17.5F, 7.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(8.0F, 24.0F, -8.0F));

		PartDefinition cube_r1 = bone.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(22, 10).addBox(0.0F, -1.0F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(-0.01F))
		.texOffs(6, 22).addBox(12.4F, -1.0F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(-14.7F, -7.5F, 8.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition cube_r2 = bone.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(8, 13).addBox(-4.0F, -1.0F, -1.0F, 5.0F, 1.0F, 2.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(-12.0F, -1.2F, 8.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition cube_r3 = bone.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(8, 10).addBox(-1.0F, -1.0F, -1.0F, 5.0F, 1.0F, 2.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(-4.0F, -1.2F, 8.0F, 0.0F, 0.0F, -0.7854F));

		PartDefinition cube_r4 = bone.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(8, 5).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, 0.0F, 8.0F, 0.0F, -0.7854F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
		bone.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
	}
}