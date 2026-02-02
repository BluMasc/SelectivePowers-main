package net.blumasc.selectivepowers.entity.client.quetzal;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.blumasc.selectivepowers.SelectivePowers;
import net.blumasc.selectivepowers.entity.client.echocrab.EchoCrabAnimation;
import net.blumasc.selectivepowers.entity.custom.MoonsquidEntity;
import net.blumasc.selectivepowers.entity.custom.QuetzalEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class QuetzalModel<T extends QuetzalEntity> extends HierarchicalModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "quetzal"), "main");
    private final ModelPart root;
    private final ModelPart core;
    private final ModelPart segment1;
    private final ModelPart segment2;
    private final ModelPart segment3;
    private final ModelPart head;
    private final ModelPart jaw;
    private final ModelPart wing1;
    private final ModelPart wing2;
    private final ModelPart tailSegment1;
    private final ModelPart tailSegment2;
    private final ModelPart tailSegment3;
    private final ModelPart tailSegment4;
    private final ModelPart tail;
    private final ModelPart tailTip;

    public QuetzalModel(ModelPart root) {
        this.root = root.getChild("root");
        this.core = this.root.getChild("core");
        this.segment1 = this.core.getChild("segment1");
        this.segment2 = this.segment1.getChild("segment2");
        this.segment3 = this.segment2.getChild("segment3");
        this.head = this.segment3.getChild("head");
        this.jaw = this.head.getChild("jaw");
        this.wing1 = this.segment2.getChild("wing1");
        this.wing2 = this.segment2.getChild("wing2");
        this.tailSegment1 = this.core.getChild("tailSegment1");
        this.tailSegment2 = this.tailSegment1.getChild("tailSegment2");
        this.tailSegment3 = this.tailSegment2.getChild("tailSegment3");
        this.tailSegment4 = this.tailSegment3.getChild("tailSegment4");
        this.tail = this.tailSegment4.getChild("tail");
        this.tailTip = this.tail.getChild("tailTip");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition core = root.addOrReplaceChild("core", CubeListBuilder.create().texOffs(22, 25).addBox(0.0F, -4.0F, -2.0F, 0.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(12, 20).addBox(-1.25F, -2.25F, -2.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(22, 20).addBox(0.25F, -2.25F, -2.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 1.0F, 0.5236F, 0.0F, 0.0F));

        PartDefinition cube_r1 = core.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 20).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, -1.0F, 0.0F, 0.0F, -0.7854F));

        PartDefinition segment1 = core.addOrReplaceChild("segment1", CubeListBuilder.create().texOffs(22, 25).addBox(0.0F, -3.0F, -4.0F, 0.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(12, 20).addBox(-1.25F, -1.25F, -4.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(22, 20).addBox(0.25F, -1.25F, -4.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, -2.0F, -0.3054F, 0.0F, 0.0F));

        PartDefinition cube_r2 = segment1.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 20).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -3.0F, 0.0F, 0.0F, -0.7854F));

        PartDefinition segment2 = segment1.addOrReplaceChild("segment2", CubeListBuilder.create().texOffs(22, 25).addBox(0.0F, -3.0F, -4.0F, 0.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(12, 20).addBox(-1.25F, -1.25F, -4.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(22, 20).addBox(0.25F, -1.25F, -4.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -4.0F, -0.3491F, 0.0F, 0.0F));

        PartDefinition cube_r3 = segment2.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 20).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -3.0F, 0.0F, 0.0F, -0.7854F));

        PartDefinition segment3 = segment2.addOrReplaceChild("segment3", CubeListBuilder.create().texOffs(22, 25).addBox(0.0F, -3.0F, -4.0F, 0.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(12, 20).addBox(-1.25F, -1.25F, -4.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(22, 20).addBox(0.25F, -1.25F, -4.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -4.0F, -0.3491F, 0.0F, 0.0F));

        PartDefinition cube_r4 = segment3.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 20).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -3.0F, 0.0F, 0.0F, -0.7854F));

        PartDefinition head = segment3.addOrReplaceChild("head", CubeListBuilder.create().texOffs(12, 25).addBox(0.0F, -2.0F, -2.0F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(16, 30).addBox(1.0F, -3.5F, -2.0F, 0.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(10, 26).addBox(1.95F, 0.0F, -2.0F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(10, 28).addBox(0.05F, 0.0F, -2.0F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 0.0F, -5.0F, 0.8727F, 0.0F, 0.0F));

        PartDefinition cube_r5 = head.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(8, 30).addBox(-1.0F, -2.0F, -1.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, -1.0F, 1.0F, 0.0F, -0.2182F, 0.5236F));

        PartDefinition cube_r6 = head.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(0, 30).addBox(-2.0F, -2.0F, 0.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 0.0F, 0.0F, 0.2182F, -0.5236F));

        PartDefinition jaw = head.addOrReplaceChild("jaw", CubeListBuilder.create().texOffs(0, 26).addBox(-1.0F, 0.0F, -3.0F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 0.0F, 1.0F, 0.1745F, 0.0F, 0.0F));

        PartDefinition wing1 = segment2.addOrReplaceChild("wing1", CubeListBuilder.create().texOffs(0, 0).addBox(-21.0F, 0.0F, -5.0F, 21.0F, 0.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, -1.0F, -1.0F));

        PartDefinition wing2 = segment2.addOrReplaceChild("wing2", CubeListBuilder.create().texOffs(0, 10).addBox(0.0F, 0.0F, -5.0F, 21.0F, 0.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, -1.0F, -1.0F));

        PartDefinition tailSegment1 = core.addOrReplaceChild("tailSegment1", CubeListBuilder.create().texOffs(22, 25).addBox(0.0F, -3.0F, 0.0F, 0.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(12, 20).addBox(-1.25F, -1.25F, 0.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(22, 20).addBox(0.25F, -1.25F, 0.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 2.0F, -0.2618F, 0.0F, 0.0F));

        PartDefinition cube_r7 = tailSegment1.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(0, 20).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, 0.0F, 0.0F, -0.7854F));

        PartDefinition tailSegment2 = tailSegment1.addOrReplaceChild("tailSegment2", CubeListBuilder.create().texOffs(22, 25).addBox(0.0F, -3.0F, 1.0F, 0.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(12, 20).addBox(-1.25F, -1.25F, 1.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(22, 20).addBox(0.25F, -1.25F, 1.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 3.0F, -0.3491F, 0.0F, 0.0F));

        PartDefinition cube_r8 = tailSegment2.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(0, 20).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 2.0F, 0.0F, 0.0F, -0.7854F));

        PartDefinition tailSegment3 = tailSegment2.addOrReplaceChild("tailSegment3", CubeListBuilder.create().texOffs(22, 25).addBox(0.0F, -3.0F, 0.0F, 0.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(12, 20).addBox(-1.25F, -1.25F, 0.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(22, 20).addBox(0.25F, -1.25F, 0.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 5.0F, -0.3491F, 0.0F, 0.0F));

        PartDefinition cube_r9 = tailSegment3.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(0, 20).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, 0.0F, 0.0F, -0.7854F));

        PartDefinition tailSegment4 = tailSegment3.addOrReplaceChild("tailSegment4", CubeListBuilder.create().texOffs(22, 25).addBox(0.0F, -3.0F, 0.0F, 0.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(12, 20).addBox(-1.25F, -1.25F, 0.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(22, 20).addBox(0.25F, -1.25F, 0.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 4.0F, -0.0436F, 0.0F, 0.0F));

        PartDefinition cube_r10 = tailSegment4.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(0, 20).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, 0.0F, 0.0F, -0.7854F));

        PartDefinition tail = tailSegment4.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(24, 28).addBox(0.0F, -2.0F, 0.0F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 4.0F, 0.2182F, 0.0F, 0.0F));

        PartDefinition cube_r11 = tail.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(30, 25).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.7854F));

        PartDefinition tailTip = tail.addOrReplaceChild("tailTip", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 2.0F, 0.1745F, 0.0F, 0.0F));

        PartDefinition cube_r12 = tailTip.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(30, 29).addBox(-0.5F, -0.5F, -1.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, 0.0F, 0.0F, -0.7854F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }


    @Override
    public void setupAnim(QuetzalEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.applyHeadRotation(netHeadYaw, headPitch);
        this.animate(entity.shootAnimationState, QuetzalAnimation.shoot, ageInTicks, 1.5f);
        this.animate(entity.idleAnimationState, QuetzalAnimation.idle, ageInTicks, 1f);
        this.animate(entity.attackAnimationState, QuetzalAnimation.attackFlight, ageInTicks, 1F);
    }

    private void applyHeadRotation(float headYaw, float headPitch) {
        headYaw = Mth.clamp(headYaw, -30f, 30f);
        headPitch = Mth.clamp(headPitch, -25f, 45);

        this.head.yRot = headYaw * ((float)Math.PI / 180f);
        this.head.xRot = headPitch *  ((float)Math.PI / 180f);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        root.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }

    @Override
    public ModelPart root() {
        return root;
    }
}

