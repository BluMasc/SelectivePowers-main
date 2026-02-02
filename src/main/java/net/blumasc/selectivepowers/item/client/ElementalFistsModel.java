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

public class ElementalFistsModel<T extends LivingEntity> extends EntityModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "elementalfistsmodel"), "main");
    public final ModelPart right_arm;
    public final ModelPart left_arm;

    public ElementalFistsModel(ModelPart root) {
        this.right_arm = root.getChild("right_arm");
        this.left_arm = root.getChild("left_arm");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition right_arm = partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(24, 14).addBox(-3.0F, 7.0F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(1.0F))
                .texOffs(28, 21).addBox(-4.2F, 4.0F, -4.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(28, 26).addBox(0.8F, 4.0F, 1.3F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(28, 10).addBox(-2.2F, 5.0F, -3.6F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(28, 31).addBox(-4.4F, 5.0F, 1.2F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(4, 33).addBox(-1.6F, 2.0F, -3.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(8, 33).addBox(-3.6F, 1.0F, -3.6F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 33).addBox(-3.8F, 2.0F, 1.7F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(12, 33).addBox(1.4F, 1.0F, 1.7F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(16, 33).addBox(1.4F, 4.0F, -3.6F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(20, 33).addBox(0.7F, 3.0F, -0.6F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(24, 33).addBox(-3.5F, 3.0F, -0.6F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(28, 0).addBox(-2.0F, 10.0F, -1.0F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.1F))
                .texOffs(28, 5).addBox(-3.0F, 10.2F, -2.0F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition left_arm = partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, 5.0F, -4.0F, 6.0F, 6.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 14).addBox(-2.0F, 11.0F, -3.0F, 6.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 21).addBox(4.0F, 5.0F, -3.0F, 1.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(14, 21).addBox(-3.0F, 5.0F, -3.0F, 1.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
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
