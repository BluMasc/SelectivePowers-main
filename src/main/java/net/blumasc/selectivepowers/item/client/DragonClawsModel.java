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

public class DragonClawsModel<T extends LivingEntity> extends EntityModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "dragonclawsmodel"), "main");
    public final ModelPart right_arm;
    public final ModelPart left_arm;

    public DragonClawsModel(ModelPart root) {
        this.right_arm = root.getChild("right_arm");
        this.left_arm = root.getChild("left_arm");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition right_arm = partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(0, 8).addBox(-4.0F, 9.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.3F))
                .texOffs(8, 0).addBox(-4.0F, 9.0F, 1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.3F))
                .texOffs(0, 4).addBox(-4.0F, 9.0F, -3.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.3F))
                .texOffs(8, 10).addBox(-3.5F, 10.6F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.1F))
                .texOffs(0, 12).addBox(-3.5F, 10.6F, 1.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.1F))
                .texOffs(8, 7).addBox(-3.5F, 10.6F, -2.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.1F))
                .texOffs(8, 4).addBox(0.5F, 10.6F, -1.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.1F))
                .texOffs(0, 0).addBox(0.0F, 9.0F, -2.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.3F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition left_arm = partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-2.0F, 9.0F, -2.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.3F)).mirror(false)
                .texOffs(0, 4).mirror().addBox(2.0F, 9.0F, -3.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.3F)).mirror(false)
                .texOffs(0, 8).mirror().addBox(2.0F, 9.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.3F)).mirror(false)
                .texOffs(8, 4).mirror().addBox(-1.5F, 10.6F, -1.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.1F)).mirror(false)
                .texOffs(8, 7).mirror().addBox(2.5F, 10.6F, -2.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.1F)).mirror(false)
                .texOffs(8, 10).mirror().addBox(2.5F, 10.6F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.1F)).mirror(false)
                .texOffs(0, 12).mirror().addBox(2.5F, 10.6F, 1.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(-0.1F)).mirror(false)
                .texOffs(8, 0).mirror().addBox(2.0F, 9.0F, 1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.3F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 16, 16);
    }

    @Override
    public void setupAnim(LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        right_arm.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        left_arm.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }
}
