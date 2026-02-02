package net.blumasc.selectivepowers.entity.client.echocrab;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.blumasc.selectivepowers.SelectivePowers;
import net.blumasc.selectivepowers.entity.custom.EchoCrabEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class EchoCrabModel  <T extends EchoCrabEntity> extends HierarchicalModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "echocrab"), "main");
    private final ModelPart root;
    private final ModelPart pupil1;
    private final ModelPart pupil2;
    private final ModelPart crystal;

    public EchoCrabModel(ModelPart root) {
        this.root = root.getChild("root");
        ModelPart body = this.root.getChild("body");
        ModelPart pincer1 = body.getChild("pincer1");
        ModelPart front1 = pincer1.getChild("front1");
        ModelPart eye1 = body.getChild("eye1");
        this.pupil1 = eye1.getChild("pupil1");
        ModelPart eye2 = body.getChild("eye2");
        this.pupil2 = eye2.getChild("pupil2");
        this.crystal = body.getChild("crystal");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -5.0F, -5.0F, 10.0F, 4.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(0, 14).addBox(-4.0F, -6.0F, -4.0F, 8.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(20, 23).addBox(-4.0F, -4.0F, -6.0F, 8.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r1 = body.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 23).addBox(-2.5F, -11.0F, -2.5F, 5.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition mandible2 = body.addOrReplaceChild("mandible2", CubeListBuilder.create().texOffs(24, 40).addBox(-0.5F, -0.5F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.5F, -2.0F, -5.5F));

        PartDefinition mandible1 = body.addOrReplaceChild("mandible1", CubeListBuilder.create().texOffs(20, 40).addBox(-1.5F, -0.5F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, -2.0F, -5.5F));

        PartDefinition pincer1 = body.addOrReplaceChild("pincer1", CubeListBuilder.create().texOffs(32, 14).addBox(-1.0F, -1.0F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(-0.01F))
                .texOffs(40, 0).addBox(-4.0F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, -3.0F, -4.0F));

        PartDefinition front1 = pincer1.addOrReplaceChild("front1", CubeListBuilder.create().texOffs(20, 26).addBox(-0.5F, -0.5F, -4.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.01F))
                .texOffs(20, 31).addBox(-1.0F, -0.5F, -6.0F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.5F, 0.0F, 0.0F));

        PartDefinition top1 = front1.addOrReplaceChild("top1", CubeListBuilder.create().texOffs(0, 32).addBox(-1.0F, -1.5F, -3.0F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 0.0F, -3.0F));

        PartDefinition eye1 = body.addOrReplaceChild("eye1", CubeListBuilder.create().texOffs(16, 37).addBox(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(2.5F, -4.0F, -5.5F));

        PartDefinition pupil1 = eye1.addOrReplaceChild("pupil1", CubeListBuilder.create().texOffs(40, 11).addBox(0.0F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.2F)), PartPose.offset(-0.5F, -3.0F, 0.0F));

        PartDefinition eye2 = body.addOrReplaceChild("eye2", CubeListBuilder.create().texOffs(40, 4).addBox(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.5F, -4.0F, -5.5F));

        PartDefinition pupil2 = eye2.addOrReplaceChild("pupil2", CubeListBuilder.create().texOffs(40, 8).addBox(-1.0F, -2.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.2F)), PartPose.offset(0.5F, -3.0F, 0.0F));

        PartDefinition pincer2 = body.addOrReplaceChild("pincer2", CubeListBuilder.create().texOffs(32, 18).addBox(-2.0F, -1.0F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(-0.01F))
                .texOffs(40, 2).addBox(1.0F, -0.5F, -0.5F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, -3.0F, -4.0F));

        PartDefinition front2 = pincer2.addOrReplaceChild("front2", CubeListBuilder.create().texOffs(30, 26).addBox(-0.5F, -0.5F, -4.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.01F))
                .texOffs(30, 31).addBox(-1.0F, -0.5F, -6.0F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(3.5F, 0.0F, 0.0F));

        PartDefinition top2 = front2.addOrReplaceChild("top2", CubeListBuilder.create().texOffs(10, 32).addBox(-1.0F, -1.5F, -3.0F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 0.0F, -3.0F));

        PartDefinition crystal = body.addOrReplaceChild("crystal", CubeListBuilder.create().texOffs(48, 0).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(51, 9).addBox(0.0F, -5.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -6.0F, 0.0F, 0.0F, 1.0908F, 0.0F));

        PartDefinition leg1 = root.addOrReplaceChild("leg1", CubeListBuilder.create().texOffs(20, 36).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, -1.0F, -3.0F));

        PartDefinition leg2 = root.addOrReplaceChild("leg2", CubeListBuilder.create().texOffs(28, 36).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, -1.0F, 3.0F));

        PartDefinition leg3 = root.addOrReplaceChild("leg3", CubeListBuilder.create().texOffs(36, 36).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, -1.0F, 0.0F));

        PartDefinition leg4 = root.addOrReplaceChild("leg4", CubeListBuilder.create().texOffs(0, 37).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, -1.0F, -3.0F));

        PartDefinition leg5 = root.addOrReplaceChild("leg5", CubeListBuilder.create().texOffs(8, 37).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, -1.0F, 3.0F));

        PartDefinition leg6 = root.addOrReplaceChild("leg6", CubeListBuilder.create().texOffs(38, 22).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, -1.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(EchoCrabEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        crystal.yScale = entity.hasGem()/100.0f;
        crystal.xScale = entity.hasGem()/100.0f;
        crystal.zScale = entity.hasGem()/100.0f;
        this.applyHeadRotation(netHeadYaw, headPitch);
        this.animate(entity.snapAnimationState, EchoCrabAnimation.pancerSnip, ageInTicks, 1f);
        this.animateWalk(EchoCrabAnimation.walking, limbSwing, limbSwingAmount, 2f, 2.5f);
        this.animate(entity.idleAnimationState, EchoCrabAnimation.idle, ageInTicks, 1f);
    }

    private void applyHeadRotation(float headYaw, float headPitch) {
        headYaw = Mth.clamp(headYaw, -30f, 30f);
        headPitch = Mth.clamp(headPitch, -25f, 45);

        this.pupil1.yRot = headYaw * ((float)Math.PI / 180f);
        this.pupil1.xRot = headPitch *  ((float)Math.PI / 180f);
        this.pupil2.yRot = headYaw * ((float)Math.PI / 180f);
        this.pupil2.xRot = headPitch *  ((float)Math.PI / 180f);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        root.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }

    @Override
    public @NotNull ModelPart root() {
        return root;
    }
}
