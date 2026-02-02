package net.blumasc.selectivepowers.events;

import net.blumasc.selectivepowers.Config;
import net.blumasc.selectivepowers.PowerManager;
import net.blumasc.selectivepowers.block.SelectivepowersBlocks;
import net.blumasc.selectivepowers.block.custom.JumpingMushroom;
import net.blumasc.selectivepowers.block.entity.VoidBlockEntity;
import net.blumasc.selectivepowers.effect.SelectivepowersEffects;
import net.blumasc.selectivepowers.entity.CircleVariant;
import net.blumasc.selectivepowers.entity.SelectivepowersEntities;
import net.blumasc.selectivepowers.entity.custom.projectile.ElementalBallEntity;
import net.blumasc.selectivepowers.entity.custom.projectile.MagicCircleEntity;
import net.blumasc.selectivepowers.entity.custom.projectile.MeteoriteEntity;
import net.blumasc.selectivepowers.item.SelectivepowersItems;
import net.blumasc.selectivepowers.managers.SunBattleManager;
import net.blumasc.selectivepowers.sound.SelectivepowersSounds;
import net.blumasc.selectivepowers.worldgen.ModDimensions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.DragonFireball;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MaceItem;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static net.blumasc.selectivepowers.item.custom.LightningInABottleItem.castChainLightning;

public class PowerActiveUse {
    public static void activateAbility(Player p, boolean ult)
    {
        if(!(p.level() instanceof ServerLevel sl)) return;
        PowerManager pm = PowerManager.get(sl);
        if(pm.getPowerLevelOfPlayer(p.getUUID()).equals(PowerManager.PowerLevel.FREE)) return;
        if(pm.getPowerLevelOfPlayer(p.getUUID()).equals(PowerManager.PowerLevel.BOUND)) return;
        if(ult && pm.getPowerLevelOfPlayer(p.getUUID()).equals(PowerManager.PowerLevel.AWOKEN)) return;
        if(!playerHasItem(p, ult? SelectivepowersItems.BLESSED_IDOL.get():SelectivepowersItems.GOLDEN_IDOL.get())) return;
        if(actuallyUseAbility(p, sl, pm.getPowerOfPlayer(p.getUUID()), ult))
        {
            sl.playSound(p, p.getOnPos(), ult?SelectivepowersSounds.ULT.get():SelectivepowersSounds.ABILITY.get(), p.getSoundSource());
            if(!p.hasInfiniteMaterials()) {
                removeItem(p, ult ? SelectivepowersItems.BLESSED_IDOL.get() : SelectivepowersItems.GOLDEN_IDOL.get());
            }
        }

    }


    private static boolean actuallyUseAbility(Player p, ServerLevel sl, String powerOfPlayer, boolean ult) {
        if(powerOfPlayer.equals(PowerManager.ROCK_POWER))
        {
            return useRockPower(p,sl,ult);
        }else if(powerOfPlayer.equals(PowerManager.DRAGON_POWER))
        {
            return useDragonPower(p,sl,ult);
        }else if(powerOfPlayer.equals(PowerManager.STORM_POWER))
        {
            return useStormPower(p,sl,ult);
        }else if(powerOfPlayer.equals(PowerManager.FORREST_POWER))
        {
            return useForrestPower(p,sl,ult);
        }else if(powerOfPlayer.equals(PowerManager.MUSHROOM_POWER))
        {
            return useMushroomPower(p,sl,ult);
        }else if(powerOfPlayer.equals(PowerManager.TRUTH_POWER))
        {
            return useTruthPower(p,sl,ult);
        }else if(powerOfPlayer.equals(PowerManager.YELLOW_POWER))
        {
            return useYellowPower(p,sl,ult);
        }else if(powerOfPlayer.equals(PowerManager.RAGE_POWER))
        {
            return useRagePower(p,sl,ult);
        }else if(powerOfPlayer.equals(PowerManager.LIGHT_POWER))
        {
            return useLightPower(p,sl,ult);
        }else if(powerOfPlayer.equals(PowerManager.ELEMENTAL_POWER))
        {
            return useElementalPower(p,sl,ult);
        }else if(powerOfPlayer.equals(PowerManager.DARK_POWER))
        {
            return useDarkPower(p,sl,ult);
        }else if(powerOfPlayer.equals(PowerManager.ANIMAL_POWER))
        {
            return useAnimalPower(p,sl,ult);
        }else if(powerOfPlayer.equals(PowerManager.MOON_POWER))
        {
            return useMoonPower(p,sl,ult);
        }
        return false;
    }

    private static boolean useYellowPower(Player p, ServerLevel sl, boolean ult) {
        if(ult){
            LivingEntity e = getEntityPlayerIsLookingAt(p,64);
            if(e!=null && e instanceof ServerPlayer pl)
            {
                SunBattleManager sbm = SunBattleManager.get(sl);
                sbm.startBattle(p,pl,sl);
                return true;
            }
        }else
        {
            spawnYellowPets(p,sl,1);
            return true;
        }
        return false;
    }

    private static boolean useMoonPower(Player p, ServerLevel sl, boolean ult) {

        if(ult){
            LivingEntity e = getEntityPlayerIsLookingAt(p,64);
            if(e == null)
            {
                e=p;
            }
            if (e.level().dimension() == ModDimensions.LUNAR_DIM_LEVEL){
                return false;
            }
            e.addEffect(new MobEffectInstance(SelectivepowersEffects.MOON_BOUND_EFFECT, 2400, 0));
            return true;
        }else
        {
            BlockPos target = getLookTarget(p,64);
            if(target!=null) {
                MagicCircleEntity circle =
                        new MagicCircleEntity(SelectivepowersEntities.MAGIC_CIRCLE.get(), sl, CircleVariant.MOON, 20, 40, 2);
                circle.moveTo(target.getBottomCenter());
                return sl.addFreshEntity(circle);

            }
        }
        return false;
    }

    private static boolean useAnimalPower(Player p, ServerLevel sl, boolean ult) {
        if(ult){
            spawnTamedPets(p,sl,10);
        }else
        {
            healTamedPets(p,32);
        }
        return true;
    }

    private static boolean useDarkPower(Player p, ServerLevel sl, boolean ult) {
        PowerManager pm = PowerManager.get(sl);
        PowerManager.PlayerProgress progress = pm.getProgress(p.getUUID());
        if(ult){
            progress.ultTimer = 1200;

        }else
        {
            replaceLightBlocks(p,sl,16,600);
            progress.abilityTimer = 600;
        }
        pm.setDirty();
        return true;
    }

    private static boolean useElementalPower(Player p, ServerLevel sl, boolean ult) {
        if(ult){
            shootIceBallBurst(p);
        }else
        {
            PowerManager pm = PowerManager.get(sl);
            PowerManager.PlayerProgress progress = pm.getProgress(p.getUUID());
            progress.abilityTimer = 12000;
            pm.setDirty();
            giveItemsWithEnchantments(p);
        }
        return true;
    }

    private static boolean useLightPower(Player p, ServerLevel sl, boolean ult) {

        if(ult){
            PowerManager pm = PowerManager.get(sl);
            PowerManager.PlayerProgress progress = pm.getProgress(p.getUUID());
            progress.ultTimer = 300;
            pm.setDirty();
        }else
        {
            removeUndeadInRange(p, 32);
        }
        return true;
    }

    private static boolean useRagePower(Player p, ServerLevel sl, boolean ult) {
        PowerManager pm = PowerManager.get(sl);
        PowerManager.PlayerProgress progress = pm.getProgress(p.getUUID());
        if(ult){
            progress.ultTimer = 600;
            p.addEffect(new MobEffectInstance(SelectivepowersEffects.RAGE_EFFECT, 600, 0));
        }else
        {
            progress.abilityTimer = 2400;

        }
        pm.setDirty();
        return true;
    }

    private static boolean useTruthPower(Player p, ServerLevel sl, boolean ult) {

        if(ult){
            return false;
        }else
        {
            applyEffectToAllAround(p, 32, MobEffects.GLOWING, 600, 0);
            return true;
        }
    }

    private static boolean useMushroomPower(Player p, ServerLevel sl, boolean ult) {
        if(ult){
            applyRandomEffects(p,32, Config.EFFECT_STRINGS_POISON.get());
            return true;
        }else
        {
            BlockPos target = getLookTarget(p,64);
            if(target==null) {
                return sl.setBlock(p.getOnPos().above(), SelectivepowersBlocks.JUMP_MUSHROOM.get().defaultBlockState().setValue(JumpingMushroom.SPAWNED, true), 3);
            }else{
                return sl.setBlock(target, SelectivepowersBlocks.JUMP_MUSHROOM.get().defaultBlockState().setValue(JumpingMushroom.SPAWNED, true), 3);
            }
        }
    }

    private static boolean useForrestPower(Player p, ServerLevel sl, boolean ult) {

        if(ult){
            PowerManager pm = PowerManager.get(sl);
            PowerManager.PlayerProgress progress = pm.getProgress(p.getUUID());
            progress.ultTimer = 2400;
            pm.setDirty();
            return true;
        }else
        {
            BlockPos target = getLookTarget(p,64);
            if(target!=null) {
                placeBall(sl, target, 4, SelectivepowersBlocks.ROSE_VINES.get());
                return true;
            }
        }
        return false;
    }

    private static boolean useStormPower(Player p, ServerLevel sl, boolean ult) {
        if(ult){
            if(p instanceof ServerPlayer sp)
            {
                sl.setWeatherParameters(0,12000, true, true);
                shootLightning(sp, 30);
                return true;
            }
        }else
        {
            castChainLightning(p, 64, 3.0f, 10, 16.0d);
            return true;
        }
        return false;
    }

    private static boolean useDragonPower(Player p, ServerLevel sl, boolean ult) {
        if(ult){
            RandomSource random = sl.getRandom();
            Vec3 eyePos = p.getEyePosition();

            for (int i = 0; i < 10; i++) {

                Vec3 direction = new Vec3(
                        random.nextDouble() * 2.0 - 1.0,
                        random.nextDouble() * 2.0 - 1.0,
                        random.nextDouble() * 2.0 - 1.0
                ).normalize();

                DragonFireball fireball = new DragonFireball(
                        sl,
                        p,
                        direction
                );
                fireball.setPos(
                        eyePos.x + direction.x,
                        eyePos.y + direction.y,
                        eyePos.z + direction.z
                );

                sl.addFreshEntity(fireball);
            }
        }else
        {
            PowerManager pm = PowerManager.get(sl);
            PowerManager.PlayerProgress progress = pm.getProgress(p.getUUID());
            progress.abilityTimer = 12000;
            pm.setDirty();
        }
        return true;
    }

    private static boolean useRockPower(Player p, ServerLevel sl, boolean ult) {
        if(ult){
            BlockPos lookTarget = getLookTarget(p,128);
            if(lookTarget!=null) {
                MeteoriteEntity me = new MeteoriteEntity(sl);
                me.setPos(lookTarget.getX(), lookTarget.getY()+128, lookTarget.getZ());
                return sl.addFreshEntity(me);
            }
        }else
        {
            placeCubeShell(sl, p, 2, SelectivepowersBlocks.OBSIDIAN_DUST.get());
            return true;
        }
        return false;
    }

    public static void replaceLightBlocks(Player player, ServerLevel world,int radius, int timer) {

        BlockPos playerPos = player.blockPosition();

        int startX = playerPos.getX() - radius;
        int endX = playerPos.getX() + radius;
        int startY = Math.max(0, playerPos.getY() - radius);
        int endY = Math.min(world.getMaxBuildHeight(), playerPos.getY() + radius);
        int startZ = playerPos.getZ() - radius;
        int endZ = playerPos.getZ() + radius;

        for (int x = startX; x <= endX; x++) {
            for (int y = startY; y <= endY; y++) {
                for (int z = startZ; z <= endZ; z++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    BlockState state = world.getBlockState(pos);

                    if (state.isAir()) continue;

                    BlockEntity tile = world.getBlockEntity(pos);
                    if (tile != null) continue;

                    if (state.getLightEmission() > 0) {
                        world.setBlock(pos, SelectivepowersBlocks.VOID_BLOCK.get().defaultBlockState(), 3);
                        BlockEntity vtile = world.getBlockEntity(pos);
                        if (vtile instanceof VoidBlockEntity timedTile) {
                            timedTile.setOriginalBlock(state, timer);
                        }
                    }
                }
            }
        }
    }

    public static void shootIceBallBurst(Player player) {
        if (!(player.level() instanceof ServerLevel level)) return;
        boolean version = player.getRandom().nextBoolean();
        Vec3 eyePos = player.getEyePosition(1.0f);
        Vec3 lookVec = player.getLookAngle();

        spawnIceBall(level, player, eyePos, lookVec, version);
        version = !version;

        for (int i = 1; i <= 3; i++) {
            double angleDeg = i * 90;
            Vec3 rotated = rotateVectorHorizontally(lookVec, angleDeg);
            spawnIceBall(level, player, eyePos, rotated, version);
            version = !version;
        }
    }

    private static void spawnIceBall(ServerLevel level, Player player, Vec3 pos, Vec3 direction, boolean version) {

        ElementalBallEntity ice = new ElementalBallEntity(level, player, version);
        ice.setPos(pos.x, pos.y, pos.z);

        ice.shoot(direction.x, direction.y, direction.z, 1.5f, 0.05f);
        ice.setOwner(player);

        level.addFreshEntity(ice);
    }

    private static Vec3 rotateVectorHorizontally(Vec3 vec, double degrees) {
        double radians = Math.toRadians(degrees);
        double cos = Math.cos(radians);
        double sin = Math.sin(radians);

        double x = vec.x * cos - vec.z * sin;
        double z = vec.x * sin + vec.z * cos;
        return new Vec3(x, vec.y, z);
    }


    public static LivingEntity getEntityPlayerIsLookingAt(Player player, double distance) {
        Vec3 eyePos = player.getEyePosition(1.0f);
        Vec3 lookVec = player.getViewVector(1.0f);
        Vec3 endVec = eyePos.add(lookVec.scale(distance));

        AABB searchBox = player.getBoundingBox().expandTowards(lookVec.scale(distance)).inflate(1.0);

        List<Entity> entities = player.level().getEntities(player, searchBox, e -> e instanceof LivingEntity && e.isPickable());

        LivingEntity closest = null;
        double closestDistSq = distance * distance;

        for (Entity e : entities) {
            AABB eBox = e.getBoundingBox().inflate(0.3);
            Vec3 hit = eBox.clip(eyePos, endVec).orElse(null);
            if (hit != null) {
                double distSq = eyePos.distanceToSqr(hit);
                if (distSq < closestDistSq) {
                    closest = (LivingEntity) e;
                    closestDistSq = distSq;
                }
            }
        }

        return closest;
    }

    public static void spawnTamedPets(Player player, ServerLevel sl, int total) {
        if (!(player.level() instanceof ServerLevel level)) return;

        for (int i = 0; i < total; i++) {
            boolean spawnWolf = player.getRandom().nextBoolean();
            TamableAnimal mob;

            if (spawnWolf) {
                mob = EntityType.WOLF.create(sl);
            } else {
                mob = SelectivepowersEntities.CROW.get().create(sl);
            }

            if (mob == null) continue;


            mob.setPos(player.position().x, player.position().y, player.position().z);
            mob.tame(player);
            level.addFreshEntity(mob);
        }
    }

    public static void spawnYellowPets(Player player, ServerLevel sl, int total) {
        if (!(player.level() instanceof ServerLevel level)) return;

        for (int i = 0; i < total; i++) {
            int enityType = player.getRandom().nextInt(3);
            LivingEntity mob;

            switch (enityType)
            {
                case 0: mob = SelectivepowersEntities.QUETZAL_YELLOW.get().create(sl);
                case 1: mob = SelectivepowersEntities.CORRUPTING_MASK.get().create(sl);
                case 2: mob = SelectivepowersEntities.YELLOW_FANATIC.get().create(sl);
                default: mob = SelectivepowersEntities.QUETZAL_YELLOW.get().create(sl);
            }


            if (mob == null) continue;


            mob.setPos(player.position().x, player.position().y, player.position().z);
            level.addFreshEntity(mob);
        }
    }

    public static void healTamedPets(Player player, double radius) {
        if (!(player.level() instanceof ServerLevel level)) return;

        List<Entity> entities = level.getEntities(player, player.getBoundingBox().inflate(radius));

        for (Entity entity : entities) {
            if (!(entity instanceof TamableAnimal tameable)) continue;
            if (!tameable.isOwnedBy(player)) continue;
            tameable.setHealth(tameable.getMaxHealth());

        }
    }

    public static void removeUndeadInRange(Player player, double radius) {
        if (!(player.level() instanceof ServerLevel level)) return;

        for (Entity entity : level.getEntities(player, player.getBoundingBox().inflate(radius))) {
            if (!(entity instanceof LivingEntity living)) continue;
            if (living == player) continue;

            if (living.getType().is(EntityTypeTags.SENSITIVE_TO_SMITE)) {
                living.remove(Entity.RemovalReason.DISCARDED);
            }
        }
    }

    public static void applyEffectToAllAround(Player player, double radius, Holder<MobEffect> effectHolder, int duration, int amplifier) {
        if (!(player.level() instanceof ServerLevel level)) return;

        for (Entity entity : level.getEntities(player, player.getBoundingBox().inflate(radius))) {
            if (!(entity instanceof LivingEntity target)) continue;
            if (target == player) continue;

            target.addEffect(new MobEffectInstance(effectHolder, duration, amplifier));
        }
    }

    public static void applyRandomEffects(Player player, double radius, List<? extends String> effectNames) {
        if (!(player.level() instanceof ServerLevel level)) return;

        RandomSource random = player.getRandom();
        BlockPos center = player.blockPosition();
        List<Entity> entities = level.getEntities(player, player.getBoundingBox().inflate(radius));

        for (Entity entity : entities) {
            if (!(entity instanceof LivingEntity target)) continue;
            if (target == player) continue;

            int effectsCount = 1 + random.nextInt(3);

            for (int i = 0; i < effectsCount; i++) {
                String effectString = effectNames.get(random.nextInt(effectNames.size()));
                ResourceLocation effectId = ResourceLocation.tryParse(effectString);
                if (effectId == null) continue;

                Holder<MobEffect> effectHolder = BuiltInRegistries.MOB_EFFECT.getHolder(effectId).orElse(null);
                if (effectHolder == null) continue;

                int duration = 30 + random.nextInt(80);
                int amplifier = random.nextInt(2);

                target.addEffect(new MobEffectInstance(effectHolder, duration, amplifier));
            }
        }
    }

    public static void shootLightning(ServerPlayer player, double radius) {
        if (!(player.level() instanceof ServerLevel level)) return;

        BlockPos center = player.blockPosition();

        List<Entity> entities = level.getEntities(player, player.getBoundingBox().inflate(radius));

        for (Entity entity : entities) {
            if (!(entity instanceof LivingEntity target)) continue;
            if (target == player) continue;

            LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(level);
            if (bolt != null) {
                bolt.moveTo(Vec3.atCenterOf(target.blockPosition()));
                bolt.setCause(player);
                level.addFreshEntity(bolt);
            }

            if (player.getRandom().nextInt(5) < 3) {
                MobEffectInstance paralyze = new MobEffectInstance(SelectivepowersEffects.PARALYZE_EFFECT, 1200, 0);
                target.addEffect(paralyze);
            }
        }
    }

    public static void placeBall(ServerLevel level, BlockPos center, double radius, Block block) {
        BlockState state = block.defaultBlockState();
        double sqRadius = radius * radius;

        int minX = center.getX() - (int) radius;
        int maxX = center.getX() + (int) radius;
        int minY = center.getY() - (int) radius;
        int maxY = center.getY() + (int) radius;
        int minZ = center.getZ() - (int) radius;
        int maxZ = center.getZ() + (int) radius;

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    BlockPos pos = new BlockPos(x, y, z);

                    double dx = x - center.getX();
                    double dy = y - center.getY();
                    double dz = z - center.getZ();
                    if (dx * dx + dy * dy + dz * dz > sqRadius) continue;

                    if (!level.getBlockState(pos).canBeReplaced()) continue;

                    level.setBlock(pos, state, 3);
                }
            }
        }
    }

    public static void placeCubeShell(ServerLevel level, Player player, int radius, Block block) {
        BlockPos center = player.blockPosition();

        int minX = center.getX() - radius;
        int maxX = center.getX() + radius;
        int minY = center.getY() - radius;
        int maxY = center.getY() + radius;
        int minZ = center.getZ() - radius;
        int maxZ = center.getZ() + radius;

        BlockState state = block.defaultBlockState();

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {

                    boolean isShell =
                            x == minX || x == maxX ||
                                    y == minY || y == maxY ||
                                    z == minZ || z == maxZ;

                    if (!isShell) continue;

                    BlockPos pos = new BlockPos(x, y, z);

                    BlockEntity be = level.getBlockEntity(pos);
                    if (be != null) continue;

                    BlockState existing = level.getBlockState(pos);

                    if (!existing.canBeReplaced()) continue;

                    level.setBlock(pos, state, Block.UPDATE_ALL);
                }
            }
        }
    }

    public static BlockPos getLookTarget(Player player, double maxDistance) {
        Level world = player.level();
        Vec3 eyePos = player.getEyePosition(1.0F);
        Vec3 lookVec = player.getViewVector(1.0F);
        Vec3 reachVec = eyePos.add(lookVec.scale(maxDistance));

        EntityHitResult entityHit = rayTraceEntities(player, eyePos, reachVec, maxDistance);
        if (entityHit != null) {
            Entity entity = entityHit.getEntity();
            return entity.getOnPos();
        }
        HitResult blockHit = world.clip(new net.minecraft.world.level.ClipContext(
                eyePos,
                reachVec,
                net.minecraft.world.level.ClipContext.Block.COLLIDER,
                net.minecraft.world.level.ClipContext.Fluid.NONE,
                player
        ));

        if (blockHit.getType() == HitResult.Type.BLOCK) {
            return ((BlockHitResult) blockHit).getBlockPos().above();
        }


        return null;
    }

    private static EntityHitResult rayTraceEntities(Player player, Vec3 start, Vec3 end, double distance) {
        Level world = player.level();
        EntityHitResult closest = null;
        double closestDistance = distance;

        for (Entity entity : world.getEntities(player, player.getBoundingBox().expandTowards(player.getViewVector(1.0F).scale(distance)).inflate(1.0))) {
            if (entity.isSpectator() || !entity.isPickable()) continue;

            Optional<Vec3> optionalHit = entity.getBoundingBox().clip(start, end);
            if (optionalHit.isPresent()) {
                Vec3 hitLocation = optionalHit.get();
                double d = start.distanceToSqr(hitLocation);
                if (d < closestDistance * closestDistance) {
                    closestDistance = Math.sqrt(d);
                    closest = new EntityHitResult(entity, hitLocation);
                }
            }
        }
        return closest;
    }

    public static boolean playerHasItem(Player player, Item item) {
        for (ItemStack stack : player.getInventory().items) {
            if (stack.getItem() == item && !stack.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public static boolean removeItem(Player player, Item item) {
        for (int i = 0; i < player.getInventory().items.size(); i++) {
            ItemStack stack = player.getInventory().items.get(i);
            if (stack.getItem() == item && !stack.isEmpty()) {
                if (stack.getCount() > 1) {
                    stack.shrink(1);
                } else {
                    player.getInventory().items.set(i, ItemStack.EMPTY);
                }
                player.getInventory().setChanged();
                return true;
            }
        }
        return false;
    }
    public static void giveItemsWithEnchantments(Player player) {
        Level level = player.level();

        ItemStack oldMain = player.getMainHandItem().copy();
        ItemStack oldOff = player.getOffhandItem().copy();

        ItemStack mainEnchantSource = findSimilarItem(player, MaceItem.class, oldMain, oldOff);
        ItemStack offEnchantSource = findSimilarItem(player, ShieldItem.class, oldMain, oldOff);

        ItemStack mainStack = new ItemStack(SelectivepowersItems.FLAMING_HAMMER.asItem());
        ItemStack offStack = new ItemStack(SelectivepowersItems.PERMAFROST_SHIELD.asItem());

        if (mainEnchantSource != null) {
            EnchantmentHelper.setEnchantments(
                    mainStack, EnchantmentHelper.getEnchantmentsForCrafting(mainEnchantSource)
            );
        }
        if (offEnchantSource != null) {
            EnchantmentHelper.setEnchantments(
                    offStack, EnchantmentHelper.getEnchantmentsForCrafting(offEnchantSource)
            );
        }
        swapItem(player, mainStack, oldMain, true);
        swapItem(player, offStack, oldOff, false);
    }

    private static void swapItem(Player player, ItemStack newItem, ItemStack oldItem, boolean mainHand) {
        if (mainHand) {
            player.setItemInHand(net.minecraft.world.InteractionHand.MAIN_HAND, newItem);
        } else {
            player.setItemInHand(net.minecraft.world.InteractionHand.OFF_HAND, newItem);
        }

        if (!oldItem.isEmpty()) {
            boolean added = player.getInventory().add(oldItem);
            if (!added) {
                player.drop(oldItem, false);
            }
        }
    }
    private static ItemStack findSimilarItem(Player player,Class<? extends Item> clazz, ItemStack oldMain, ItemStack oldOff) {
        if (clazz.isInstance(oldMain.getItem())) return oldMain;
        if (clazz.isInstance(oldOff.getItem())) return oldOff;

        NonNullList<ItemStack> inv = player.getInventory().items;
        for (int i = 0; i < 9; i++) {
            ItemStack stack = inv.get(i);
            if (clazz.isInstance(stack.getItem())) return stack;
        }
        for (int i = 9; i < inv.size(); i++) {
            ItemStack stack = inv.get(i);
            if (clazz.isInstance(stack.getItem())) return stack;
        }
        return null;
    }


}
