package net.blumasc.selectivepowers.entity.custom;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public class YellowKingEntity extends Monster {
    public final AnimationState attackAnimationState = new AnimationState();
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState summonAnimationState = new AnimationState();

    public final AnimationState glitch0AnimationState = new AnimationState();
    public final AnimationState glitch1AnimationState = new AnimationState();
    public final AnimationState glitch2AnimationState = new AnimationState();
    public final AnimationState glitch3AnimationState = new AnimationState();
    public final AnimationState glitch4AnimationState = new AnimationState();
    public final AnimationState glitch5AnimationState = new AnimationState();
    public final AnimationState glitch6AnimationState = new AnimationState();
    public YellowKingEntity(EntityType<? extends YellowKingEntity> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, (double)0.0F)
                .add(Attributes.FOLLOW_RANGE, (double)12.0F)
                .add(Attributes.MAX_HEALTH, (double)24.0F);
    }

    private void triggerRandomGlitch() {
        int r = this.getRandom().nextInt(7);

        switch (r) {
            case 0 -> glitch1AnimationState.start(this.tickCount);
            case 1 -> glitch2AnimationState.start(this.tickCount);
            case 2 -> glitch3AnimationState.start(this.tickCount);
            case 3 -> glitch4AnimationState.start(this.tickCount);
            case 4 -> glitch5AnimationState.start(this.tickCount);
            case 5 -> glitch6AnimationState.start(this.tickCount);
            case 6 -> glitch0AnimationState.start(this.tickCount);
        }
    }



    private void setupAnimationState()
    {
        idleAnimationState.startIfStopped(this.tickCount);

        if(this.getRandom().nextInt(25)==1)
        {
            triggerRandomGlitch();
        }

    }

    public void tickAnimationStates() {
        idleAnimationState.startIfStopped(this.tickCount);

        if (this.getRandom().nextInt(25) == 1) {
            triggerRandomGlitch();
        }
    }

    @Override
    public void tick() {
        super.tick();

        if(this.level().isClientSide())
        {
            setupAnimationState();
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
    }
}
