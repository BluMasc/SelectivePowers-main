package net.blumasc.selectivepowers.entity.client.salamander;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.blumasc.selectivepowers.SelectivePowers;
import net.blumasc.selectivepowers.entity.custom.SalamanderEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class SalamanderModel<T extends SalamanderEntity> extends HierarchicalModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "salamander"), "main");
    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart lowerJaw;
    private final ModelPart Sombrero;
    private final ModelPart tail;
    private final ModelPart segment1;
    private final ModelPart segment2;
    private final ModelPart segment3;
    private final ModelPart leftArm;
    private final ModelPart leftGrabber;
    private final ModelPart maracaLeft;
    private final ModelPart rightArm;
    private final ModelPart leftGrabber2;
    private final ModelPart maracaRight;
    private final ModelPart leftleg;
    private final ModelPart upperLeft;
    private final ModelPart lowerLeft;
    private final ModelPart rightLeg;
    private final ModelPart upperRight;
    private final ModelPart loverRight;

    public SalamanderModel(ModelPart root) {
        this.root = root.getChild("root");
        this.body = this.root.getChild("body");
        this.head = this.body.getChild("head");
        this.lowerJaw = this.head.getChild("lowerJaw");
        this.Sombrero = this.head.getChild("Sombrero");
        this.tail = this.body.getChild("tail");
        this.segment1 = this.tail.getChild("segment1");
        this.segment2 = this.segment1.getChild("segment2");
        this.segment3 = this.segment2.getChild("segment3");
        this.leftArm = this.body.getChild("leftArm");
        this.leftGrabber = this.leftArm.getChild("leftGrabber");
        this.maracaLeft = this.leftGrabber.getChild("maracaLeft");
        this.rightArm = this.body.getChild("rightArm");
        this.leftGrabber2 = this.rightArm.getChild("leftGrabber2");
        this.maracaRight = this.leftGrabber2.getChild("maracaRight");
        this.leftleg = this.root.getChild("leftleg");
        this.upperLeft = this.leftleg.getChild("upperLeft");
        this.lowerLeft = this.upperLeft.getChild("lowerLeft");
        this.rightLeg = this.root.getChild("rightLeg");
        this.upperRight = this.rightLeg.getChild("upperRight");
        this.loverRight = this.upperRight.getChild("loverRight");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(-0.5F, -10.0F, 7.0F));

        PartDefinition cube_r1 = body.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 17).addBox(-3.0F, -4.0F, -1.0F, 5.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 2.0F, -3.0F, -0.48F, 0.0F, 0.0F));

        PartDefinition cube_r2 = body.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -6.0F, -7.0F, 7.0F, 6.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 1.0F, -7.0F, -0.5236F, 0.0F, 0.0F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(22, 25).addBox(-2.0F, -2.0F, -5.5F, 4.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -6.0F, -11.0F));

        PartDefinition cube_r3 = head.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(50, 50).addBox(-2.5F, -0.5F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, -1.0F, 0.0F, -0.7974F, 0.1536F, -0.1555F));

        PartDefinition cube_r4 = head.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(50, 47).addBox(-0.5F, -0.5F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, -1.0F, 0.0F, -0.8027F, -0.1841F, 0.1872F));

        PartDefinition cube_r5 = head.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(44, 21).addBox(-2.5F, -3.5F, 0.0F, 5.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

        PartDefinition lowerJaw = head.addOrReplaceChild("lowerJaw", CubeListBuilder.create().texOffs(22, 17).addBox(-2.5F, 0.0F, -5.0F, 5.0F, 2.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(48, 41).addBox(-2.5F, -2.0F, -5.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(54, 6).addBox(1.5F, -2.0F, -5.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(14, 45).addBox(2.5F, -2.0F, -5.0F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(18, 27).addBox(-2.5F, -2.0F, -5.0F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, -1.0F));

        PartDefinition Sombrero = head.addOrReplaceChild("Sombrero", CubeListBuilder.create().texOffs(-6, 58).addBox(-3.0F, -2.1F, -5.0F, 6.0F, 0.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(19, 58).addBox(-1.0F, -4.0F, -3.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create(), PartPose.offset(0.0F, 2.0F, 2.0F));

        PartDefinition segment1 = tail.addOrReplaceChild("segment1", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0F, 0.0F));

        PartDefinition cube_r6 = segment1.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(48, 34).addBox(-0.5F, -2.0F, -2.0F, 0.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 0.0F, 2.0F, -0.48F, 0.0F, 0.0F));

        PartDefinition cube_r7 = segment1.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(36, 0).addBox(-2.0F, -2.0F, -3.0F, 3.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 1.0F, 1.0F, -0.48F, 0.0F, 0.0F));

        PartDefinition segment2 = segment1.addOrReplaceChild("segment2", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 2.0F, 4.0F, 0.4363F, 0.0F, 0.0F));

        PartDefinition cube_r8 = segment2.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(0, 50).addBox(-0.5F, -1.5F, -2.0F, 0.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 0.0F, 2.0F, -0.6981F, 0.0F, 0.0F));

        PartDefinition cube_r9 = segment2.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(36, 8).addBox(-1.5F, -2.5F, -3.0F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 2.0F, 1.0F, -0.6981F, 0.0F, 0.0F));

        PartDefinition segment3 = segment2.addOrReplaceChild("segment3", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 1.0F, 5.0F, 0.3491F, 0.0F, 0.0F));

        PartDefinition cube_r10 = segment3.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(38, 43).addBox(-0.5F, -1.5F, -2.0F, 0.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 2.0F, 1.0F, -0.6981F, 0.0F, 0.0F));

        PartDefinition cube_r11 = segment3.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(0, 27).addBox(-1.0F, -2.5F, -3.0F, 1.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 4.0F, -1.0F, -0.6981F, 0.0F, 0.0F));

        PartDefinition leftArm = body.addOrReplaceChild("leftArm", CubeListBuilder.create(), PartPose.offset(3.5F, -4.0F, -9.0F));

        PartDefinition cube_r12 = leftArm.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(18, 43).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 8.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 1.0F, -1.0F, 0.0436F, 0.0F, 0.0F));

        PartDefinition leftGrabber = leftArm.addOrReplaceChild("leftGrabber", CubeListBuilder.create().texOffs(18, 34).addBox(-1.5F, -1.0F, -6.0F, 2.0F, 2.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(54, 0).addBox(-1.0F, -1.5F, -7.0F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(38, 51).addBox(0.0F, -0.5F, -7.0F, 1.0F, 0.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(44, 51).addBox(0.0F, 0.5F, -7.0F, 1.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 7.0F, 0.0F, 0.48F, 0.48F, 0.0F));

        PartDefinition maracaLeft = leftGrabber.addOrReplaceChild("maracaLeft", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -1.0F, -7.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 7).addBox(-1.5F, -3.0F, -7.5F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition rightArm = body.addOrReplaceChild("rightArm", CubeListBuilder.create(), PartPose.offset(-3.5F, -4.0F, -9.0F));

        PartDefinition cube_r13 = rightArm.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(28, 43).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 8.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 1.0F, -1.0F, 0.0436F, 0.0F, 0.0F));

        PartDefinition leftGrabber2 = rightArm.addOrReplaceChild("leftGrabber2", CubeListBuilder.create().texOffs(0, 36).addBox(-0.5F, -1.0F, -6.0F, 2.0F, 2.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(54, 3).addBox(1.0F, -1.5F, -7.0F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(52, 8).addBox(-1.0F, -0.5F, -7.0F, 1.0F, 0.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(52, 10).addBox(-1.0F, 0.5F, -7.0F, 1.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 7.0F, 0.0F, 0.48F, -0.48F, 0.0F));

        PartDefinition maracaRight = leftGrabber2.addOrReplaceChild("maracaRight", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -1.0F, -7.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 7).addBox(-0.5F, -3.0F, -7.5F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition leftleg = root.addOrReplaceChild("leftleg", CubeListBuilder.create(), PartPose.offset(1.0F, -9.0F, 6.0F));

        PartDefinition upperLeft = leftleg.addOrReplaceChild("upperLeft", CubeListBuilder.create(), PartPose.offset(-1.0F, 9.0F, -6.0F));

        PartDefinition cube_r14 = upperLeft.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(36, 34).addBox(-1.0F, -2.0F, -2.0F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, -8.0F, 7.0F, -0.3927F, 0.0F, 0.0F));

        PartDefinition lowerLeft = upperLeft.addOrReplaceChild("lowerLeft", CubeListBuilder.create().texOffs(10, 50).addBox(-0.5F, 0.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(44, 16).addBox(-1.0F, 4.0F, -3.0F, 3.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(18, 31).addBox(1.0F, 4.0F, -4.5F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(52, 12).addBox(0.0F, 4.0F, -4.5F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(38, 53).addBox(0.5F, 4.0F, 0.5F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, -5.0F, 5.0F));

        PartDefinition rightLeg = root.addOrReplaceChild("rightLeg", CubeListBuilder.create(), PartPose.offset(-2.0F, -9.0F, 6.0F));

        PartDefinition upperRight = rightLeg.addOrReplaceChild("upperRight", CubeListBuilder.create(), PartPose.offset(-4.0F, 9.0F, -6.0F));

        PartDefinition cube_r15 = upperRight.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(42, 25).addBox(-1.0F, -2.0F, -2.0F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, -8.0F, 7.0F, -0.3927F, 0.0F, 0.0F));

        PartDefinition loverRight = upperRight.addOrReplaceChild("loverRight", CubeListBuilder.create().texOffs(50, 41).addBox(-1.5F, 0.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 45).addBox(-2.0F, 4.0F, -3.0F, 3.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(42, 53).addBox(0.0F, 4.0F, -4.5F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(46, 53).addBox(-1.0F, 4.0F, -4.5F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(50, 53).addBox(-0.5F, 4.0F, 0.5F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, -5.0F, 5.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(SalamanderEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.applyHeadRotation(netHeadYaw,headPitch);

        if(!entity.danceAnimation.isStarted()){
            maracaLeft.xScale = 0;
            maracaLeft.yScale = 0;
            maracaLeft.zScale = 0;
            maracaRight.xScale = 0;
            maracaRight.zScale = 0;
            maracaRight.yScale = 0;
            Sombrero.yScale = 0;
            Sombrero.xScale = 0;
            Sombrero.zScale = 0;
        }else{
            maracaLeft.xScale = 1;
            maracaLeft.yScale = 1;
            maracaLeft.zScale = 1;
            maracaRight.xScale = 1;
            maracaRight.zScale = 1;
            maracaRight.yScale = 1;
            Sombrero.yScale = 1;
            Sombrero.xScale = 1;
            Sombrero.zScale = 1;
        }
        this.animate(entity.danceAnimation, SalamanderAnimation.salamanderDance, ageInTicks, 1f);
            this.animate(entity.idleAnimationState, SalamanderAnimation.idle, ageInTicks, 1f);
            this.animate(entity.infusionAnimationState, SalamanderAnimation.infusion, ageInTicks, 1f);
        this.animateWalk(SalamanderAnimation.walking, limbSwing, limbSwingAmount, 8f, 2.5f);
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
