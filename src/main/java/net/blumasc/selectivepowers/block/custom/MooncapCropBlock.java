package net.blumasc.selectivepowers.block.custom;

import net.blumasc.selectivepowers.item.SelectivepowersItems;
import net.blumasc.selectivepowers.particles.custom.WispParticleOption;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.CommonHooks;

import java.awt.*;
import java.util.function.Supplier;

public class MooncapCropBlock extends CropBlock {
    public static final int MAX_AGE = 5;
    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 5);
    private static final VoxelShape[] SHAPE_BY_AGE= new VoxelShape[]{
            Block.box((double)0.0F, (double)0.0F, (double)0.0F, (double)16.0F, (double)4.0F, (double)16.0F),
            Block.box((double)0.0F, (double)0.0F, (double)0.0F, (double)16.0F, (double)6.0F, (double)16.0F),
            Block.box((double)0.0F, (double)0.0F, (double)0.0F, (double)16.0F, (double)8.0F, (double)16.0F),
            Block.box((double)0.0F, (double)0.0F, (double)0.0F, (double)16.0F, (double)11.0F, (double)16.0F),
            Block.box((double)0.0F, (double)0.0F, (double)0.0F, (double)16.0F, (double)13.0F, (double)16.0F),
            Block.box((double)0.0F, (double)0.0F, (double)0.0F, (double)16.0F, (double)15.0F, (double)16.0F)};
    public MooncapCropBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE_BY_AGE[state.getValue(AGE)];
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return SelectivepowersItems.MOONCAP_SEEDS;
    }

    @Override
    public IntegerProperty getAgeProperty() {
        return AGE;
    }

    @Override
    public int getMaxAge() {
        return MAX_AGE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return level.getBlockState(pos).isSolid();
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        long time = level.getDayTime() % 24000;
        return time >= 13000 && time <= 23000;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        long time = level.getDayTime() % 24000;
        if (time >= 13000 && time <= 23000) {
            int age = this.getAge(state);
            int maxAge = this.getMaxAge();
            int newAge = Math.min(age + 1, maxAge);
            if(newAge == 5)
            {
                level.scheduleTick(pos, this, 20);
            }
            level.setBlock(pos, this.getStateForAge(newAge), 2);
        }
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (level.isAreaLoaded(pos, 1)) {

            long time = level.getDayTime() % 24000;
            if (time < 13000 || time > 23000) return;

            if (level.getBrightness(LightLayer.SKY, pos) > 7) {
                int i = this.getAge(state);
                if (i < this.getMaxAge()) {
                    float f = getGrowthSpeed(state, level, pos);
                    if (CommonHooks.canCropGrow(level, pos, state, random.nextInt((int)(25.0F / f) + 1) == 0)) {
                        level.setBlock(pos, this.getStateForAge(i + 1), 2);
                        if(i+1 ==5){
                            level.scheduleTick(pos, this, 20);
                        }
                        CommonHooks.fireCropGrowPost(level, pos, state);
                    }
                }
            }
            if(this.getAge(state)==5){
                level.scheduleTick(pos, this, 20);
            }

        }
    }



    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if(this.getAge(state)==5 ){
            if(level.isDay()) {
                level.setBlock(pos, this.getStateForAge(4), 2);
                CommonHooks.fireCropGrowPost(level, pos, state);
            }else {
                level.scheduleTick(pos, this, 20);
            }
        }
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        int i = this.getAge(state);
        if (i == this.getMaxAge()) {
            spawnParticle(level, pos.getX(), pos.getY(), pos.getZ());
        }
        super.animateTick(state, level, pos, random);
    }

    public static void spawnParticle(Level level, double x, double y, double z) {
        RandomSource rand = level.getRandom();
        if(rand.nextDouble()<0.1) {
            double ox = (rand.nextDouble() - 0.5) * 0.5;
            double oy = rand.nextDouble();
            double oz = (rand.nextDouble() - 0.5) * 0.5;
            spawnParticle(level, x+ox, y+oy, z+oz, new Color(0x90d5ff), new Color(0xd7e5f0), 0, 0.01f, 0);
        }
    }

    public static void spawnParticle(Level level, double x, double y, double z,
                                     Color startingColor, Color endingColor,
                                     double moveX, double moveY, double moveZ) {
        if (!(level instanceof ClientLevel)) return; // client-only

        WispParticleOption options = new WispParticleOption(
                startingColor.getRed()   / 255f, startingColor.getGreen() / 255f, startingColor.getBlue() / 255f,
                endingColor.getRed()     / 255f, endingColor.getGreen()   / 255f, endingColor.getBlue()   / 255f
        );

        level.addParticle(options, x, y, z, moveX, moveY, moveZ);
    }
}
