package net.blumasc.selectivepowers.entity.client.moonsquid;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.blumasc.selectivepowers.SelectivePowers;
import net.blumasc.selectivepowers.entity.client.quetzal.QuetzalAnimation;
import net.blumasc.selectivepowers.entity.custom.MoonsquidEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.phys.Vec3;

public class MoonsquidModel<T extends MoonsquidEntity> extends HierarchicalModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "moonsquid"), "main");
    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart ear1;
    private final ModelPart ear2;
    private final ModelPart legs;
    private final ModelPart leg1;
    private final ModelPart top1;
    private final ModelPart leg2;
    private final ModelPart top2;
    private final ModelPart leg3;
    private final ModelPart top3;
    private final ModelPart leg4;
    private final ModelPart top4;
    private final ModelPart legs2;
    private final ModelPart leg5;
    private final ModelPart top5;
    private final ModelPart leg6;
    private final ModelPart top6;
    private final ModelPart leg7;
    private final ModelPart top7;
    private final ModelPart leg8;
    private final ModelPart top8;

    public MoonsquidModel(ModelPart root) {
        this.root = root.getChild("root");
        this.head = this.root.getChild("head");
        this.ear1 = this.head.getChild("ear1");
        this.ear2 = this.head.getChild("ear2");
        this.legs = this.root.getChild("legs");
        this.leg1 = this.legs.getChild("leg1");
        this.top1 = this.leg1.getChild("top1");
        this.leg2 = this.legs.getChild("leg2");
        this.top2 = this.leg2.getChild("top2");
        this.leg3 = this.legs.getChild("leg3");
        this.top3 = this.leg3.getChild("top3");
        this.leg4 = this.legs.getChild("leg4");
        this.top4 = this.leg4.getChild("top4");
        this.legs2 = this.root.getChild("legs2");
        this.leg5 = this.legs2.getChild("leg5");
        this.top5 = this.leg5.getChild("top5");
        this.leg6 = this.legs2.getChild("leg6");
        this.top6 = this.leg6.getChild("top6");
        this.leg7 = this.legs2.getChild("leg7");
        this.top7 = this.leg7.getChild("top7");
        this.leg8 = this.legs2.getChild("leg8");
        this.top8 = this.leg8.getChild("top8");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -9.0F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition ear1 = head.addOrReplaceChild("ear1", CubeListBuilder.create().texOffs(0, 9).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, -9.0F, 0.0F));

        PartDefinition ear2 = head.addOrReplaceChild("ear2", CubeListBuilder.create().texOffs(6, 9).addBox(-2.0F, -1.0F, 0.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, -9.0F, 0.0F));

        PartDefinition legs = root.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition leg1 = legs.addOrReplaceChild("leg1", CubeListBuilder.create().texOffs(12, 9).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 19).addBox(-1.0F, 0.0F, 1.05F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.0F, -2.0F));

        PartDefinition top1 = leg1.addOrReplaceChild("top1", CubeListBuilder.create().texOffs(6, 16).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(10, 16).addBox(-0.5F, 0.0F, 1.05F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, 0.0F));

        PartDefinition leg2 = legs.addOrReplaceChild("leg2", CubeListBuilder.create().texOffs(12, 12).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(4, 19).addBox(-1.0F, 0.0F, 1.05F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -4.0F, 2.0F, 0.0F, 3.1416F, 0.0F));

        PartDefinition top2 = leg2.addOrReplaceChild("top2", CubeListBuilder.create().texOffs(16, 6).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(8, 19).addBox(-0.5F, 0.0F, 1.05F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, 0.0F));

        PartDefinition leg3 = legs.addOrReplaceChild("leg3", CubeListBuilder.create().texOffs(0, 13).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(20, 6).addBox(-1.0F, 0.0F, 1.05F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, -4.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition top3 = leg3.addOrReplaceChild("top3", CubeListBuilder.create().texOffs(18, 9).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(20, 21).addBox(-0.5F, 0.0F, 1.05F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, 0.0F));

        PartDefinition leg4 = legs.addOrReplaceChild("leg4", CubeListBuilder.create().texOffs(6, 13).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 21).addBox(-1.0F, 0.0F, 1.05F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, -4.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

        PartDefinition top4 = leg4.addOrReplaceChild("top4", CubeListBuilder.create().texOffs(10, 18).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(22, 0).addBox(-0.5F, 0.0F, 1.05F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, 0.0F));

        PartDefinition legs2 = root.addOrReplaceChild("legs2", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition leg5 = legs2.addOrReplaceChild("leg5", CubeListBuilder.create().texOffs(12, 15).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(4, 21).addBox(-1.0F, 0.0F, 1.05F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.0F, -2.0F));

        PartDefinition top5 = leg5.addOrReplaceChild("top5", CubeListBuilder.create().texOffs(18, 12).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(22, 2).addBox(-0.5F, 0.0F, 1.05F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, 0.0F));

        PartDefinition leg6 = legs2.addOrReplaceChild("leg6", CubeListBuilder.create().texOffs(0, 16).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(8, 21).addBox(-1.0F, 0.0F, 1.05F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -4.0F, 2.0F, 0.0F, 3.1416F, 0.0F));

        PartDefinition top6 = leg6.addOrReplaceChild("top6", CubeListBuilder.create().texOffs(14, 18).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(22, 4).addBox(-0.5F, 0.0F, 1.05F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, 0.0F));

        PartDefinition leg7 = legs2.addOrReplaceChild("leg7", CubeListBuilder.create().texOffs(16, 0).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(12, 21).addBox(-1.0F, 0.0F, 1.05F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, -4.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition top7 = leg7.addOrReplaceChild("top7", CubeListBuilder.create().texOffs(18, 15).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(22, 8).addBox(-0.5F, 0.0F, 1.05F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, 0.0F));

        PartDefinition leg8 = legs2.addOrReplaceChild("leg8", CubeListBuilder.create().texOffs(16, 3).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(16, 21).addBox(-1.0F, 0.0F, 1.05F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, -4.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

        PartDefinition top8 = leg8.addOrReplaceChild("top8", CubeListBuilder.create().texOffs(18, 18).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(22, 10).addBox(-0.5F, 0.0F, 1.05F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void setupAnim(MoonsquidEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);

        this.animate(entity.swirlAnimationState, MoonsquidAnimation.swirl, ageInTicks, 1f);
        this.animate(entity.idleAnimationState, MoonsquidAnimation.idle, ageInTicks, 1f);
        this.animateWalk(MoonsquidAnimation.walk, limbSwing, limbSwingAmount, 2.0f, 1.0f);
        this.animate(entity.sitAnimationState, MoonsquidAnimation.sitting, ageInTicks, 1f);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        root.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }

    public void renderOnShoulder(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay,
                                 float limbSwing, float limbSwingAmount, float netHeadYaw, float headPitch, int ageInTicks) {
        this.root().getAllParts().forEach(ModelPart::resetPose);

        AnimationState dummySit = new AnimationState();
        dummySit.startIfStopped(ageInTicks);

        this.animate(dummySit, MoonsquidAnimation.sitting, ageInTicks, 1f);

        float sway = Mth.sin(ageInTicks * 0.1F) * 0.02F;
        root.xRot += sway;

        root.render(poseStack, buffer, packedLight, packedOverlay);
    }

    @Override
    public ModelPart root() {
        return root;
    }
}
