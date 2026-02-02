package net.blumasc.selectivepowers.entity.client.crow;

// Made with Blockbench 5.0.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.blumasc.selectivepowers.SelectivePowers;
import net.blumasc.selectivepowers.entity.custom.CrowEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class CrowModel<T extends CrowEntity> extends HierarchicalModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "crow"), "main");
    private final ModelPart body;
    private final ModelPart head;

    public ModelPart getHead() {
        return head;
    }

    public CrowModel(ModelPart root) {
        this.body = root.getChild("body");
        this.head = this.body.getChild("head");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 18.0F, 0.0F));

        PartDefinition body_r1 = body.addOrReplaceChild("body_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -1.5F, -4.0F, 4.0F, 3.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.5F, 0.0F, -0.3927F, 0.0F, 0.0F));

        PartDefinition wing1 = body.addOrReplaceChild("wing1", CubeListBuilder.create(), PartPose.offset(2.0F, 0.0F, -2.0F));

        PartDefinition wing1_r1 = wing1.addOrReplaceChild("wing1_r1", CubeListBuilder.create().texOffs(18, 11).addBox(-0.5F, -3.0F, -1.5F, 1.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 1.4771F, 1.13F, 0.9163F, 0.0F, 0.0F));

        PartDefinition wing2 = body.addOrReplaceChild("wing2", CubeListBuilder.create(), PartPose.offset(-2.0F, 0.0F, -2.0F));

        PartDefinition wing2_r1 = wing2.addOrReplaceChild("wing2_r1", CubeListBuilder.create().texOffs(24, 5).addBox(-0.5F, -3.0F, -1.5F, 1.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 1.863F, 0.9344F, 0.9163F, 0.0F, 0.0F));

        PartDefinition leg1 = body.addOrReplaceChild("leg1", CubeListBuilder.create().texOffs(22, 20).addBox(0.0F, 3.0F, 0.0F, 2.0F, 0.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(24, 0).addBox(0.0F, 0.0F, 2.0F, 1.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, 3.0F, 1.0F));

        PartDefinition leg2 = body.addOrReplaceChild("leg2", CubeListBuilder.create().texOffs(22, 22).addBox(-2.0F, 3.0F, 0.0F, 2.0F, 0.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(24, 0).addBox(-1.0F, 0.0F, 2.0F, 1.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, 3.0F, 1.0F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 18).addBox(-1.5F, -2.25F, -2.0F, 3.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(14, 20).addBox(-0.5F, -1.25F, -4.0F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -4.0F));

        PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(0, 11).addBox(-1.5F, 2.75F, 0.0F, 3.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, 4.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void setupAnim(CrowEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.applyHeadRotation(netHeadYaw, headPitch);
        this.animate(entity.flyAnimationState, CrowAnimation.flying, ageInTicks, 1f);
        this.animateWalk(CrowAnimation.walking, limbSwing, limbSwingAmount, 2f, 2.5f);
        this.animate(entity.idleAnimationState, CrowAnimation.idle, ageInTicks, 1f);
    }

    private void applyHeadRotation(float headYaw, float headPitch) {
        headYaw = Mth.clamp(headYaw, -30f, 30f);
        headPitch = Mth.clamp(headPitch, -25f, 45);

        this.head.yRot = headYaw * ((float)Math.PI / 180f);
        this.head.xRot = headPitch *  ((float)Math.PI / 180f);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        body.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }

    @Override
    public ModelPart root() {
        return body;
    }
}
