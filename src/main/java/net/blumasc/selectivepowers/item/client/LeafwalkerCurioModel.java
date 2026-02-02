package net.blumasc.selectivepowers.item.client;// Made with Blockbench 5.0.5
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

public class LeafwalkerCurioModel<T extends LivingEntity> extends EntityModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "leafwalkercuriomodel"), "main");
    public final ModelPart right_arm;

    public final ModelPart left_arm;

    public LeafwalkerCurioModel(ModelPart root) {

        this.right_arm = root.getChild("right_arm");
        this.left_arm = root.getChild("left_arm");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition right_arm = partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(24, 10).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(1.0F))
                .texOffs(0, 16).addBox(-7.0F, -5.0F, -2.0F, 6.0F, 4.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 34).addBox(-5.0F, -6.0F, -4.0F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(22, 34).addBox(-7.0F, -7.0F, 1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition left_arm = partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(24, 22).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(1.0F))
                .texOffs(24, 0).addBox(1.0F, -5.0F, -5.0F, 6.0F, 4.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 26).addBox(-1.0F, -7.0F, -1.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(12, 34).addBox(4.0F, -6.0F, -4.0F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        right_arm.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        left_arm.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }

    @Override
    public void setupAnim(T t, float v, float v1, float v2, float v3, float v4) {

    }
}