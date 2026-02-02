package net.blumasc.selectivepowers.entity.client.corruptingmask;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.blumasc.selectivepowers.SelectivePowers;
import net.blumasc.selectivepowers.entity.custom.CorruptingMaskEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

public class CorruptingMaskModel <T extends CorruptingMaskEntity> extends HierarchicalModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "corruptingmask"), "main");
    private final ModelPart root;

    public CorruptingMaskModel(ModelPart root) {
        this.root = root.getChild("root");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -12.0F, -1.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 28.0F, 0.0F));

        PartDefinition leftMask = root.addOrReplaceChild("leftMask", CubeListBuilder.create().texOffs(0, 8).addBox(-2.0F, -4.0F, -2.0F, 4.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 16).addBox(-1.0F, 2.0F, -2.0F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(16, 26).addBox(0.0F, 4.0F, -2.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(22, 27).addBox(-2.0F, -5.0F, -2.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, -10.0F, 0.0F));

        PartDefinition rightMask = root.addOrReplaceChild("rightMask", CubeListBuilder.create().texOffs(10, 8).addBox(-2.0F, -4.0F, -2.0F, 4.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(16, 0).addBox(-2.0F, 2.0F, -2.0F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 27).addBox(-2.0F, 4.0F, -2.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(6, 28).addBox(0.0F, -5.0F, -2.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, -10.0F, 0.0F));

        PartDefinition coreTentacles = root.addOrReplaceChild("coreTentacles", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition top = coreTentacles.addOrReplaceChild("top", CubeListBuilder.create(), PartPose.offset(0.0F, -12.0F, 1.0F));

        PartDefinition cube_r1 = top.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(16, 18).addBox(-2.0F, -1.0F, -1.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 0.0F, 0.0F, -1.6199F, 1.309F, -1.3021F));

        PartDefinition cube_r2 = top.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(8, 18).addBox(-2.0F, -1.0F, -1.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -2.0F, -2.7056F, 0.0308F, -2.8293F));

        PartDefinition cube_r3 = top.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(16, 16).addBox(-2.0F, -1.0F, -1.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, -2.7261F, 0.1394F, -3.0685F));

        PartDefinition cube_r4 = top.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(8, 16).addBox(-2.0F, -1.0F, -1.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -2.5778F, 0.1197F, -2.4839F));

        PartDefinition cube_r5 = top.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(16, 5).addBox(-2.0F, -1.0F, -1.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, -1.7604F, 1.0001F, -1.2127F));

        PartDefinition cube_r6 = top.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(16, 3).addBox(-2.0F, -1.0F, -1.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3054F, -0.6109F, -0.48F));

        PartDefinition top2 = coreTentacles.addOrReplaceChild("top2", CubeListBuilder.create(), PartPose.offsetAndRotation(-2.0F, -10.0F, 1.0F, 0.0F, 0.0F, -1.6581F));

        PartDefinition cube_r7 = top2.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(20, 13).addBox(-2.0F, -1.0F, -1.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 0.0F, 0.0F, -1.6199F, 1.309F, -1.3021F));

        PartDefinition cube_r8 = top2.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(20, 11).addBox(-2.0F, -1.0F, -1.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -2.0F, -2.7056F, 0.0308F, -2.8293F));

        PartDefinition cube_r9 = top2.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(20, 9).addBox(-2.0F, -1.0F, -1.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, -2.7261F, 0.1394F, -3.0685F));

        PartDefinition cube_r10 = top2.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(8, 20).addBox(-2.0F, -1.0F, -1.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -2.5778F, 0.1197F, -2.4839F));

        PartDefinition cube_r11 = top2.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(20, 7).addBox(-2.0F, -1.0F, -1.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, -1.7604F, 1.0001F, -1.2127F));

        PartDefinition cube_r12 = top2.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(0, 19).addBox(-2.0F, -1.0F, -1.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3054F, -0.6109F, -0.48F));

        PartDefinition top3 = coreTentacles.addOrReplaceChild("top3", CubeListBuilder.create(), PartPose.offsetAndRotation(2.0F, -10.0F, 1.0F, 0.0F, 0.0F, 1.5708F));

        PartDefinition cube_r13 = top3.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(24, 0).addBox(-2.0F, -1.0F, -1.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 0.0F, 0.0F, -1.6199F, 1.309F, -1.3021F));

        PartDefinition cube_r14 = top3.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(0, 23).addBox(-2.0F, -1.0F, -1.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -2.0F, -2.7056F, 0.0308F, -2.8293F));

        PartDefinition cube_r15 = top3.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(16, 22).addBox(-2.0F, -1.0F, -1.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, -2.7261F, 0.1394F, -3.0685F));

        PartDefinition cube_r16 = top3.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(8, 22).addBox(-2.0F, -1.0F, -1.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -2.5778F, 0.1197F, -2.4839F));

        PartDefinition cube_r17 = top3.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(0, 21).addBox(-2.0F, -1.0F, -1.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, -1.7604F, 1.0001F, -1.2127F));

        PartDefinition cube_r18 = top3.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(16, 20).addBox(-2.0F, -1.0F, -1.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3054F, -0.6109F, -0.48F));

        PartDefinition top4 = coreTentacles.addOrReplaceChild("top4", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -8.0F, 1.0F, 0.0F, 0.0F, -3.1416F));

        PartDefinition cube_r19 = top4.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(24, 17).addBox(-2.0F, -1.0F, -1.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 0.0F, 0.0F, -1.6199F, 1.309F, -1.3021F));

        PartDefinition cube_r20 = top4.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(16, 24).addBox(-2.0F, -1.0F, -1.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -2.0F, -2.7056F, 0.0308F, -2.8293F));

        PartDefinition cube_r21 = top4.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(24, 15).addBox(-2.0F, -1.0F, -1.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, -2.7261F, 0.1394F, -3.0685F));

        PartDefinition cube_r22 = top4.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(8, 24).addBox(-2.0F, -1.0F, -1.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -2.5778F, 0.1197F, -2.4839F));

        PartDefinition cube_r23 = top4.addOrReplaceChild("cube_r23", CubeListBuilder.create().texOffs(24, 4).addBox(-2.0F, -1.0F, -1.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, -1.7604F, 1.0001F, -1.2127F));

        PartDefinition cube_r24 = top4.addOrReplaceChild("cube_r24", CubeListBuilder.create().texOffs(24, 2).addBox(-2.0F, -1.0F, -1.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3054F, -0.6109F, -0.48F));

        PartDefinition top5 = coreTentacles.addOrReplaceChild("top5", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -10.0F, 3.0F, -1.6144F, 0.0F, 0.0F));

        PartDefinition cube_r25 = top5.addOrReplaceChild("cube_r25", CubeListBuilder.create().texOffs(8, 26).addBox(-2.0F, -1.0F, -1.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 0.0F, 0.0F, -1.6199F, 1.309F, -1.3021F));

        PartDefinition cube_r26 = top5.addOrReplaceChild("cube_r26", CubeListBuilder.create().texOffs(24, 25).addBox(-2.0F, -1.0F, -1.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -2.0F, -2.7056F, 0.0308F, -2.8293F));

        PartDefinition cube_r27 = top5.addOrReplaceChild("cube_r27", CubeListBuilder.create().texOffs(0, 25).addBox(-2.0F, -1.0F, -1.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, -2.7261F, 0.1394F, -3.0685F));

        PartDefinition cube_r28 = top5.addOrReplaceChild("cube_r28", CubeListBuilder.create().texOffs(24, 23).addBox(-2.0F, -1.0F, -1.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -2.5778F, 0.1197F, -2.4839F));

        PartDefinition cube_r29 = top5.addOrReplaceChild("cube_r29", CubeListBuilder.create().texOffs(24, 21).addBox(-2.0F, -1.0F, -1.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, -1.7604F, 1.0001F, -1.2127F));

        PartDefinition cube_r30 = top5.addOrReplaceChild("cube_r30", CubeListBuilder.create().texOffs(24, 19).addBox(-2.0F, -1.0F, -1.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3054F, -0.6109F, -0.48F));

        PartDefinition tentacle = root.addOrReplaceChild("tentacle", CubeListBuilder.create(), PartPose.offset(0.5F, -13.0F, 1.0F));

        PartDefinition cube_r31 = tentacle.addOrReplaceChild("cube_r31", CubeListBuilder.create().texOffs(4, 30).addBox(-1.0F, -2.5F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 1.0F, 1.0F, 0.0F, 0.0F, 0.3927F));

        PartDefinition topTentacle = tentacle.addOrReplaceChild("topTentacle", CubeListBuilder.create(), PartPose.offset(0.5F, -1.0F, 0.0F));

        PartDefinition cube_r32 = topTentacle.addOrReplaceChild("cube_r32", CubeListBuilder.create().texOffs(28, 6).addBox(0.0F, -3.0F, -1.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, 0.4349F, 0.0368F, -0.0791F));

        PartDefinition tentacle2 = root.addOrReplaceChild("tentacle2", CubeListBuilder.create(), PartPose.offsetAndRotation(1.5F, -13.0F, 1.0F, -3.0893F, 0.544F, -2.2917F));

        PartDefinition cube_r33 = tentacle2.addOrReplaceChild("cube_r33", CubeListBuilder.create().texOffs(8, 30).addBox(-1.0F, -2.5F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 1.0F, 1.0F, 0.0F, 0.0F, 0.3927F));

        PartDefinition topTentacle2 = tentacle2.addOrReplaceChild("topTentacle2", CubeListBuilder.create(), PartPose.offset(0.5F, -1.0F, 0.0F));

        PartDefinition cube_r34 = topTentacle2.addOrReplaceChild("cube_r34", CubeListBuilder.create().texOffs(28, 10).addBox(0.0F, -3.0F, -1.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, 0.4349F, 0.0368F, -0.0791F));

        PartDefinition tentacle3 = root.addOrReplaceChild("tentacle3", CubeListBuilder.create(), PartPose.offsetAndRotation(2.5F, -11.0F, 3.0F, -0.5609F, -0.8612F, 0.1762F));

        PartDefinition cube_r35 = tentacle3.addOrReplaceChild("cube_r35", CubeListBuilder.create().texOffs(28, 31).addBox(-1.0F, -2.5F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 1.0F, 1.0F, 0.0F, 0.0F, 0.3927F));

        PartDefinition topTentacle3 = tentacle3.addOrReplaceChild("topTentacle3", CubeListBuilder.create(), PartPose.offset(0.5F, -1.0F, 0.0F));

        PartDefinition cube_r36 = topTentacle3.addOrReplaceChild("cube_r36", CubeListBuilder.create().texOffs(12, 28).addBox(0.0F, -3.0F, -1.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, 0.4349F, 0.0368F, -0.0791F));

        PartDefinition tentacle4 = root.addOrReplaceChild("tentacle4", CubeListBuilder.create(), PartPose.offsetAndRotation(2.5F, -11.0F, 1.0F, -0.4498F, 0.2368F, 0.629F));

        PartDefinition cube_r37 = tentacle4.addOrReplaceChild("cube_r37", CubeListBuilder.create().texOffs(32, 0).addBox(-1.0F, -2.5F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 1.0F, 1.0F, 0.0F, 0.0F, 0.3927F));

        PartDefinition topTentacle4 = tentacle4.addOrReplaceChild("topTentacle4", CubeListBuilder.create(), PartPose.offset(0.5F, -1.0F, 0.0F));

        PartDefinition cube_r38 = topTentacle4.addOrReplaceChild("cube_r38", CubeListBuilder.create().texOffs(16, 28).addBox(0.0F, -3.0F, -1.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, 0.4349F, 0.0368F, -0.0791F));

        PartDefinition tentacle5 = root.addOrReplaceChild("tentacle5", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.5F, -11.0F, 4.0F, -0.8413F, -0.4366F, 0.4419F));

        PartDefinition cube_r39 = tentacle5.addOrReplaceChild("cube_r39", CubeListBuilder.create().texOffs(32, 3).addBox(-1.0F, -2.5F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 1.0F, 1.0F, 0.0F, 0.0F, 0.3927F));

        PartDefinition topTentacle5 = tentacle5.addOrReplaceChild("topTentacle5", CubeListBuilder.create(), PartPose.offset(0.5F, -1.0F, 0.0F));

        PartDefinition cube_r40 = topTentacle5.addOrReplaceChild("cube_r40", CubeListBuilder.create().texOffs(28, 27).addBox(0.0F, -3.0F, -1.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, 0.4349F, 0.0368F, -0.0791F));

        PartDefinition tentacle6 = root.addOrReplaceChild("tentacle6", CubeListBuilder.create(), PartPose.offsetAndRotation(-2.5F, -12.0F, 3.0F, -0.4756F, 0.9965F, -0.8503F));

        PartDefinition cube_r41 = tentacle6.addOrReplaceChild("cube_r41", CubeListBuilder.create().texOffs(32, 6).addBox(-1.0F, -2.5F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 1.0F, 1.0F, 0.0F, 0.0F, 0.3927F));

        PartDefinition topTentacle6 = tentacle6.addOrReplaceChild("topTentacle6", CubeListBuilder.create(), PartPose.offset(0.5F, -1.0F, 0.0F));

        PartDefinition cube_r42 = topTentacle6.addOrReplaceChild("cube_r42", CubeListBuilder.create().texOffs(0, 29).addBox(0.0F, -3.0F, -1.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, 0.4349F, 0.0368F, -0.0791F));

        PartDefinition tentacle7 = root.addOrReplaceChild("tentacle7", CubeListBuilder.create(), PartPose.offsetAndRotation(-3.5F, -12.0F, 1.0F, -2.6616F, 1.1781F, 3.1416F));

        PartDefinition cube_r43 = tentacle7.addOrReplaceChild("cube_r43", CubeListBuilder.create().texOffs(32, 9).addBox(-1.0F, -2.5F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 1.0F, 1.0F, 0.0F, 0.0F, 0.3927F));

        PartDefinition topTentacle7 = tentacle7.addOrReplaceChild("topTentacle7", CubeListBuilder.create(), PartPose.offset(0.5F, -1.0F, 0.0F));

        PartDefinition cube_r44 = topTentacle7.addOrReplaceChild("cube_r44", CubeListBuilder.create().texOffs(20, 29).addBox(0.0F, -3.0F, -1.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, 0.4349F, 0.0368F, -0.0791F));

        PartDefinition tentacle8 = root.addOrReplaceChild("tentacle8", CubeListBuilder.create(), PartPose.offsetAndRotation(-3.5F, -11.0F, 1.0F, 2.7591F, -0.7624F, 2.7501F));

        PartDefinition cube_r45 = tentacle8.addOrReplaceChild("cube_r45", CubeListBuilder.create().texOffs(12, 32).addBox(-1.0F, -2.5F, -2.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 2.0F, 1.0F, 0.0F, 0.0F, 0.3927F));

        PartDefinition topTentacle8 = tentacle8.addOrReplaceChild("topTentacle8", CubeListBuilder.create(), PartPose.offset(0.5F, -1.0F, 0.0F));

        PartDefinition cube_r46 = topTentacle8.addOrReplaceChild("cube_r46", CubeListBuilder.create().texOffs(24, 29).addBox(0.0F, -3.0F, -2.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, 0.4349F, 0.0368F, -0.0791F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(CorruptingMaskEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.animate(entity.attackAnimationState, CorruptingMaskAnimation.attack, ageInTicks, 1f);
        this.animate(entity.idleAnimationState, CorruptingMaskAnimation.idle, ageInTicks, 1f);
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
