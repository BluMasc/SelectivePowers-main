package net.blumasc.selectivepowers.entity.client.packwing;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.blumasc.selectivepowers.SelectivePowers;
import net.blumasc.selectivepowers.entity.custom.PackwingEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

public class PackwingModel<T extends PackwingEntity> extends HierarchicalModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "packwing"), "main");
    private final ModelPart body;
    private final ModelPart tail;
    private final ModelPart segment2;

    public PackwingModel(ModelPart root) {
        this.body = root.getChild("Body");
        this.tail = this.body.getChild("tail");
        this.segment2 = this.tail.getChild("segment2");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition Body = partdefinition.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -3.0F, -5.0F, 4.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition Nose = Body.addOrReplaceChild("Nose", CubeListBuilder.create().texOffs(0, 19).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -5.0F));

        PartDefinition Wing1 = Body.addOrReplaceChild("Wing1", CubeListBuilder.create().texOffs(0, 9).addBox(-8.0F, 0.0F, -1.0F, 6.0F, 0.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, -4.0F));

        PartDefinition Wing2 = Body.addOrReplaceChild("Wing2", CubeListBuilder.create().texOffs(0, 14).addBox(2.0F, 0.0F, -1.0F, 6.0F, 0.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, -4.0F));

        PartDefinition ears = Body.addOrReplaceChild("ears", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r1 = ears.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(14, 19).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, -3.0F, -3.0F, 0.0F, -0.1309F, 0.0F));

        PartDefinition cube_r2 = ears.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(18, 19).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, -3.0F, -3.0F, 0.0F, 0.1309F, 0.0F));

        PartDefinition tail = Body.addOrReplaceChild("tail", CubeListBuilder.create(), PartPose.offset(-0.25F, -1.0F, -3.5F));

        PartDefinition segment3 = tail.addOrReplaceChild("segment3", CubeListBuilder.create().texOffs(8, 19).addBox(-0.5F, -1.0F, 1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.25F, 0.0F, 7.5F));

        PartDefinition segment2 = tail.addOrReplaceChild("segment2", CubeListBuilder.create().texOffs(8, 19).addBox(-0.5F, -1.0F, 1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.25F, 0.0F, 5.5F));

        PartDefinition segment1 = tail.addOrReplaceChild("segment1", CubeListBuilder.create().texOffs(8, 19).addBox(-0.5F, -1.0F, 1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.25F, 0.0F, 3.5F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void setupAnim(PackwingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.animate(entity.flyAnimationState, PackwingAnimation.flying, ageInTicks, 1f);
        this.animate(entity.idleAnimationState, PackwingAnimation.idle_ground, ageInTicks, 1f);
        this.animate(entity.grabAnimationState, PackwingAnimation.holding_item, ageInTicks, 2f);
        this.animate(entity.dropAnimationState, PackwingAnimation.dropping_item, ageInTicks, 2f);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        body.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }

    @Override
    public ModelPart root() {
        return body;
    }

    public ModelPart getTail() {
        return tail;
    }

    public ModelPart getMiddleSegment() {
        return segment2;
    }
}
