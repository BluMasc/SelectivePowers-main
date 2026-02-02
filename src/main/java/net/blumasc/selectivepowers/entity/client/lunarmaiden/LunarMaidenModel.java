package net.blumasc.selectivepowers.entity.client.lunarmaiden;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.blumasc.selectivepowers.SelectivePowers;
import net.blumasc.selectivepowers.entity.custom.LunarMaidenEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class LunarMaidenModel<T extends LunarMaidenEntity> extends HierarchicalModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "lunarmaiden"), "main");
    private final ModelPart Base;
    public final ModelPart Head;

    public LunarMaidenModel(ModelPart root) {
        this.Base = root.getChild("Base");
        ModelPart person = this.Base.getChild("Person");
        ModelPart waist = person.getChild("Waist");
        this.Head = waist.getChild("Head");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition Base = partdefinition.addOrReplaceChild("Base", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition bg = Base.addOrReplaceChild("bg", CubeListBuilder.create().texOffs(0, 0).addBox(-24.0F, -53.0F, 9.0F, 48.0F, 48.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition Person = Base.addOrReplaceChild("Person", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition Waist = Person.addOrReplaceChild("Waist", CubeListBuilder.create(), PartPose.offset(0.0F, -23.0F, 0.0F));

        PartDefinition Head = Waist.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(0, 48).addBox(-5.0F, -10.0F, -5.0F, 10.0F, 10.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(0, 88).addBox(-5.0F, -10.0F, -5.0F, 10.0F, 10.0F, 10.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, -14.0F, 0.0F));

        PartDefinition moons = Head.addOrReplaceChild("moons", CubeListBuilder.create().texOffs(72, 48).addBox(-1.0F, -1.0F, -8.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(72, 52).addBox(-6.0F, -2.0F, -6.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(72, 56).addBox(-8.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(72, 60).addBox(-6.0F, -2.0F, 4.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(72, 64).addBox(-1.0F, -1.0F, 6.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(76, 68).addBox(4.0F, -2.0F, 4.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(76, 72).addBox(6.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(76, 76).addBox(4.0F, -2.0F, -7.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -13.0F, 0.0F));

        PartDefinition Body = Waist.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(0, 68).addBox(-5.0F, -2.0F, -3.0F, 10.0F, 14.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -12.0F, 0.0F));

        PartDefinition RightArm = Waist.addOrReplaceChild("RightArm", CubeListBuilder.create().texOffs(32, 69).addBox(-3.0F, -4.0F, -3.0F, 5.0F, 14.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-7.0F, -10.0F, 0.0F));

        PartDefinition LeftArm = Waist.addOrReplaceChild("LeftArm", CubeListBuilder.create().texOffs(54, 69).addBox(-2.0F, -4.0F, -3.0F, 5.0F, 14.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(7.0F, -10.0F, 0.0F));

        PartDefinition dress = Person.addOrReplaceChild("dress", CubeListBuilder.create().texOffs(40, 48).addBox(-5.0F, -1.0F, -3.0F, 10.0F, 15.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -23.0F, 0.0F, 0.2618F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(@NotNull LunarMaidenEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.applyHeadRotation(entity, netHeadYaw,headPitch);
        this.animate(entity.idleAnimationState, LunarMaidenAnimation.idle, ageInTicks, 1f);
        this.animate(entity.blessAnimationState, LunarMaidenAnimation.bless, ageInTicks, 1F);
    }

    private void applyHeadRotation(LunarMaidenEntity entity, float headYaw, float headPitch) {
        Minecraft mc = Minecraft.getInstance();

        Vec3 cameraPos = mc.gameRenderer.getMainCamera().getPosition();

        double dy = cameraPos.y - (entity.getY() + entity.getBbHeight() * 0.75);

        double dx = cameraPos.x - entity.getX();
        double dz = cameraPos.z - entity.getZ();

        double horizontal = Math.sqrt(dx * dx + dz * dz);

        // ✅ Proper head pitch only
        float pitch = -(float) -Math.atan2(dy, horizontal);

        this.Head.xRot = -pitch;
    }

    @Override
    public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        Base.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }

    @Override
    public @NotNull ModelPart root() {
        return Base;
    }
}
