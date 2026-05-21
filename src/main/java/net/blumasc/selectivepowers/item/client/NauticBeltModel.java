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

public class NauticBeltModel<T extends LivingEntity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "nauticbeltmodel"), "main");
	public final ModelPart body;

	public NauticBeltModel(ModelPart root) {
		this.body = root.getChild("body");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, 11.0F, -4.0F, 12.0F, 3.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(0, 22).addBox(-1.0F, 12.5F, -4.5F, 8.0F, 2.0F, 9.0F, new CubeDeformation(0.0F))
		.texOffs(32, 33).addBox(-7.0F, 12.5F, -3.5F, 6.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(0, 33).addBox(-6.0F, 14.0F, -4.5F, 7.0F, 2.0F, 9.0F, new CubeDeformation(-0.01F))
		.texOffs(34, 22).addBox(1.0F, 14.0F, -4.0F, 5.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(40, 15).addBox(-1.0F, 10.0F, -4.4F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 11).addBox(-6.0F, 9.5F, -4.0F, 12.0F, 3.0F, 8.0F, new CubeDeformation(-0.2F))
		.texOffs(40, 0).addBox(-12.0F, 7.0F, 1.0F, 7.0F, 7.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(40, 7).addBox(5.0F, 5.0F, -2.0F, 4.0F, 8.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(0, 44).addBox(-6.0F, 16.0F, -4.5F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(27, 43).addBox(0.0F, 16.0F, -1.0F, 6.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

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