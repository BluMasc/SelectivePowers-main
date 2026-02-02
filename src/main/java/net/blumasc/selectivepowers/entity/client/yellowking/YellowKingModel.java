package net.blumasc.selectivepowers.entity.client.yellowking;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.blumasc.selectivepowers.SelectivePowers;
import net.blumasc.selectivepowers.entity.custom.YellowKingEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public class YellowKingModel<T extends YellowKingEntity> extends HierarchicalModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "yellow_king"), "main");
    private final ModelPart root;
    private final ModelPart waist;
    public final ModelPart body;
    private final ModelPart head;
    private final ModelPart hat;
    private final ModelPart leftArm;
    private final ModelPart rightArm;
    private final ModelPart backTentacles;
    private final ModelPart rightTop;
    private final ModelPart rightTopBase;
    private final ModelPart rightTopMiddle;
    private final ModelPart rightTopEnd;
    private final ModelPart leftTop;
    private final ModelPart leftTopBase;
    private final ModelPart leftTopMiddle;
    private final ModelPart leftTopEnd;
    private final ModelPart leftBottom;
    private final ModelPart leftBottomBase;
    private final ModelPart leftBottomMiddle;
    private final ModelPart leftBottomEnd;
    private final ModelPart rightBottom;
    private final ModelPart rightBottomBase;
    private final ModelPart rightBottomMiddle;
    private final ModelPart rightBottomEnd;
    private final ModelPart legs;
    private final ModelPart tentacles;

    public YellowKingModel(ModelPart root) {
        this.root = root.getChild("root");
        this.waist = this.root.getChild("waist");
        this.body = this.waist.getChild("body");
        this.head = this.body.getChild("head");
        this.hat = this.head.getChild("hat");
        this.leftArm = this.body.getChild("leftArm");
        this.rightArm = this.body.getChild("rightArm");
        this.backTentacles = this.body.getChild("backTentacles");
        this.rightTop = this.backTentacles.getChild("rightTop");
        this.rightTopBase = this.rightTop.getChild("rightTopBase");
        this.rightTopMiddle = this.rightTopBase.getChild("rightTopMiddle");
        this.rightTopEnd = this.rightTopMiddle.getChild("rightTopEnd");
        this.leftTop = this.backTentacles.getChild("leftTop");
        this.leftTopBase = this.leftTop.getChild("leftTopBase");
        this.leftTopMiddle = this.leftTopBase.getChild("leftTopMiddle");
        this.leftTopEnd = this.leftTopMiddle.getChild("leftTopEnd");
        this.leftBottom = this.backTentacles.getChild("leftBottom");
        this.leftBottomBase = this.leftBottom.getChild("leftBottomBase");
        this.leftBottomMiddle = this.leftBottomBase.getChild("leftBottomMiddle");
        this.leftBottomEnd = this.leftBottomMiddle.getChild("leftBottomEnd");
        this.rightBottom = this.backTentacles.getChild("rightBottom");
        this.rightBottomBase = this.rightBottom.getChild("rightBottomBase");
        this.rightBottomMiddle = this.rightBottomBase.getChild("rightBottomMiddle");
        this.rightBottomEnd = this.rightBottomMiddle.getChild("rightBottomEnd");
        this.legs = this.root.getChild("legs");
        this.tentacles = this.root.getChild("tentacles");

        this.backTentacles.getAllParts().forEach(part -> {
            part.xScale = 0f;
            part.yScale = 0f;
            part.zScale = 0f;
        });
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 21.0F, 0.0F));

        PartDefinition waist = root.addOrReplaceChild("waist", CubeListBuilder.create(), PartPose.offset(0.0F, -12.0F, 0.0F));

        PartDefinition body = waist.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 79).addBox(-7.0F, -5.0F, -4.0F, 14.0F, 15.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -12.0F, 0.0F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(42, 80).addBox(-5.0F, -15.0F, -5.0F, 10.0F, 10.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition hat = head.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(82, 80).addBox(-5.0F, -15.0F, -5.0F, 10.0F, 10.0F, 10.0F, new CubeDeformation(0.5F))
                .texOffs(122, 93).addBox(-5.0F, -21.0F, 5.0F, 10.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition leftArm = body.addOrReplaceChild("leftArm", CubeListBuilder.create().texOffs(100, 115).addBox(-1.0F, -2.0F, -3.0F, 4.0F, 18.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(8.0F, -3.0F, 0.0F));

        PartDefinition rightArm = body.addOrReplaceChild("rightArm", CubeListBuilder.create().texOffs(0, 116).addBox(-3.0F, -2.0F, -3.0F, 4.0F, 18.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-8.0F, -3.0F, 0.0F));

        PartDefinition backTentacles = body.addOrReplaceChild("backTentacles", CubeListBuilder.create().texOffs(0,0).addBox(0f, 0f, 0f, 0f, 0f, 0f), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition rightTop = backTentacles.addOrReplaceChild("rightTop", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition rightTopBase = rightTop.addOrReplaceChild("rightTopBase", CubeListBuilder.create().texOffs(118, 115).addBox(2.0F, -2.0F, -13.0F, 3.0F, 3.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition rightTopMiddle = rightTopBase.addOrReplaceChild("rightTopMiddle", CubeListBuilder.create().texOffs(64, 123).addBox(-1.5F, -1.5F, -9.0F, 2.0F, 2.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 0.0F, -13.0F));

        PartDefinition rightTopEnd = rightTopMiddle.addOrReplaceChild("rightTopEnd", CubeListBuilder.create().texOffs(42, 100).addBox(0.0F, -1.0F, -12.0F, 1.0F, 1.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, 0.0F, -9.0F));

        PartDefinition leftTop = backTentacles.addOrReplaceChild("leftTop", CubeListBuilder.create(), PartPose.offset(-7.0F, 0.0F, 0.0F));

        PartDefinition leftTopBase = leftTop.addOrReplaceChild("leftTopBase", CubeListBuilder.create().texOffs(118, 115).addBox(2.0F, -2.0F, -13.0F, 3.0F, 3.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition leftTopMiddle = leftTopBase.addOrReplaceChild("leftTopMiddle", CubeListBuilder.create().texOffs(124, 56).addBox(-0.5F, -1.5F, -9.0F, 2.0F, 2.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 0.0F, -13.0F));

        PartDefinition leftTopEnd = leftTopMiddle.addOrReplaceChild("leftTopEnd", CubeListBuilder.create().texOffs(72, 100).addBox(-1.0F, -1.0F, -12.0F, 1.0F, 1.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, 0.0F, -9.0F));

        PartDefinition leftBottom = backTentacles.addOrReplaceChild("leftBottom", CubeListBuilder.create(), PartPose.offset(-7.0F, 6.0F, 0.0F));

        PartDefinition leftBottomBase = leftBottom.addOrReplaceChild("leftBottomBase", CubeListBuilder.create().texOffs(120, 34).addBox(2.0F, -2.0F, -13.0F, 3.0F, 3.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition leftBottomMiddle = leftBottomBase.addOrReplaceChild("leftBottomMiddle", CubeListBuilder.create().texOffs(124, 68).addBox(-0.5F, -1.5F, -9.0F, 2.0F, 2.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 0.0F, -13.0F));

        PartDefinition leftBottomEnd = leftBottomMiddle.addOrReplaceChild("leftBottomEnd", CubeListBuilder.create().texOffs(0, 101).addBox(-1.0F, -1.0F, -12.0F, 1.0F, 1.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, 0.0F, -9.0F));

        PartDefinition rightBottom = backTentacles.addOrReplaceChild("rightBottom", CubeListBuilder.create(), PartPose.offset(0.0F, 6.0F, 0.0F));

        PartDefinition rightBottomBase = rightBottom.addOrReplaceChild("rightBottomBase", CubeListBuilder.create().texOffs(122, 80).addBox(2.0F, -2.0F, -13.0F, 3.0F, 3.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition rightBottomMiddle = rightBottomBase.addOrReplaceChild("rightBottomMiddle", CubeListBuilder.create().texOffs(18, 125).addBox(-1.5F, -1.5F, -9.0F, 2.0F, 2.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 0.0F, -13.0F));

        PartDefinition rightBottomEnd = rightBottomMiddle.addOrReplaceChild("rightBottomEnd", CubeListBuilder.create().texOffs(102, 100).addBox(0.0F, -1.0F, -12.0F, 1.0F, 1.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, 0.0F, -9.0F));

        PartDefinition legs = root.addOrReplaceChild("legs", CubeListBuilder.create().texOffs(76, 57).addBox(-8.0F, -17.0F, -4.0F, 16.0F, 15.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(84, 34).addBox(-8.0F, -12.0F, -6.0F, 16.0F, 12.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(30, 115).addBox(-8.0F, -9.0F, -7.0F, 16.0F, 9.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(64, 115).addBox(-8.0F, -6.0F, -9.0F, 16.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(84, 48).addBox(-8.0F, -3.0F, -14.0F, 16.0F, 3.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(0, 57).addBox(-9.0F, -2.0F, -15.0F, 18.0F, 2.0F, 20.0F, new CubeDeformation(0.0F))
                .texOffs(0, 34).addBox(-10.0F, -1.0F, -16.0F, 20.0F, 1.0F, 22.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 3.0F, 0.0F));

        PartDefinition tentacles = root.addOrReplaceChild("tentacles", CubeListBuilder.create().texOffs(0, 0).addBox(-19.0F, 0.0F, -21.0F, 34.0F, 0.0F, 34.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 3.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 256, 256);
    }

    @Override
    public void setupAnim(YellowKingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.animate(entity.attackAnimationState, YellowKingAnimation.attack, ageInTicks, 1.5f);
        this.animate(entity.idleAnimationState, YellowKingAnimation.idle, ageInTicks, 1f);
        this.animate(entity.summonAnimationState, YellowKingAnimation.summon, ageInTicks, 1f);

        this.animate(entity.glitch0AnimationState, YellowKingAnimation.bodyPort, ageInTicks, 1f);
        this.animate(entity.glitch1AnimationState, YellowKingAnimation.armPort, ageInTicks, 1f);
        this.animate(entity.glitch2AnimationState, YellowKingAnimation.blink, ageInTicks, 1f);
        this.animate(entity.glitch3AnimationState, YellowKingAnimation.tentacleBlink, ageInTicks, 1f);
        this.animate(entity.glitch4AnimationState, YellowKingAnimation.large, ageInTicks, 1f);
        this.animate(entity.glitch5AnimationState, YellowKingAnimation.headless, ageInTicks, 1f);
        this.animate(entity.glitch6AnimationState, YellowKingAnimation.headPort, ageInTicks, 1f);
        this.applyHeadRotation(entity, netHeadYaw, headPitch);
    }

    private void applyHeadRotation(YellowKingEntity entity, float headYaw, float headPitch) {
        Minecraft mc = Minecraft.getInstance();

        if (mc.gameRenderer == null) return;

        Vec3 cameraPos = mc.gameRenderer.getMainCamera().getPosition();

        double dy = cameraPos.y - (entity.getY() + entity.getBbHeight() * 0.75);

        double dx = cameraPos.x - entity.getX();
        double dz = cameraPos.z - entity.getZ();

        double horizontal = Math.sqrt(dx * dx + dz * dz);

        this.head.xRot = -(float) -Math.atan2(dy, horizontal);
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
