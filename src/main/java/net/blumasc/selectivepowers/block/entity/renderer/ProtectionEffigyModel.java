package net.blumasc.selectivepowers.block.entity.renderer;// Made with Blockbench 5.0.7
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

public class ProtectionEffigyModel<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "protection_effigy"), "main");
	private final ModelPart core;
	private final ModelPart ring1;
	private final ModelPart ring2;
	private final ModelPart ring3;

	public ProtectionEffigyModel(ModelPart root) {
		this.core = root.getChild("core");
		this.ring1 = this.core.getChild("ring1");
		this.ring2 = this.core.getChild("ring2");
		this.ring3 = this.core.getChild("ring3");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition core = partdefinition.addOrReplaceChild("core", CubeListBuilder.create().texOffs(0, 54).addBox(-2.0F, -10.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));

		PartDefinition ring1 = core.addOrReplaceChild("ring1", CubeListBuilder.create().texOffs(0, 42).addBox(-8.0F, -1.0F, -8.0F, 16.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(36, 42).addBox(-8.0F, -1.0F, 6.0F, 16.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(6.0F, -1.0F, -6.0F, 2.0F, 2.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(0, 14).addBox(-8.0F, -1.0F, -6.0F, 2.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -8.0F, 0.0F));

		PartDefinition ring2 = core.addOrReplaceChild("ring2", CubeListBuilder.create().texOffs(0, 46).addBox(-8.0F, -1.0F, -8.0F, 16.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(36, 46).addBox(-8.0F, -1.0F, 6.0F, 16.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 28).addBox(6.0F, -1.0F, -6.0F, 2.0F, 2.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(28, 0).addBox(-8.0F, -1.0F, -6.0F, 2.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, 1.5708F, 0.0F, 0.0F));

		PartDefinition ring3 = core.addOrReplaceChild("ring3", CubeListBuilder.create().texOffs(0, 50).addBox(-8.0F, -1.0F, -8.0F, 16.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(36, 50).addBox(-8.0F, -1.0F, 6.0F, 16.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(28, 14).addBox(6.0F, -1.0F, -6.0F, 2.0F, 2.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(28, 28).addBox(-8.0F, -1.0F, -6.0F, 2.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, 0.0F, 0.0F, -1.5708F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		ring1.xScale = 0.5f;
		ring1.yScale = 0.5f;
		ring1.zScale = 0.5f;

		ring2.xScale = 0.75f;
		ring2.yScale = 0.75f;
		ring2.zScale = 0.75f;

		float degToRad = (float) Math.PI / 180f;


		ring1.xRot = ageInTicks * degToRad * 20f;
		ring1.yRot = ageInTicks * degToRad * 15f;
		ring1.zRot = ageInTicks * degToRad * 10f;

		ring2.xRot = ageInTicks * degToRad * 10f;
		ring2.yRot = ageInTicks * degToRad * 20f;
		ring2.zRot = ageInTicks * degToRad * 5f;

		ring3.xRot = ageInTicks * degToRad * 5f;
		ring3.yRot = ageInTicks * degToRad * 10f;
		ring3.zRot = ageInTicks * degToRad * 20f;
	}


	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
		core.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
	}
}