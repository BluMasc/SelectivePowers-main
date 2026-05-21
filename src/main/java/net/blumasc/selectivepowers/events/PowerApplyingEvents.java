package net.blumasc.selectivepowers.events;

import net.blumasc.selectivepowers.Config;
import net.blumasc.selectivepowers.PowerManager;
import net.blumasc.selectivepowers.SelectivePowers;
import net.blumasc.selectivepowers.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityStruckByLightningEvent;
import net.neoforged.neoforge.event.entity.living.AnimalTameEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.entity.player.*;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.*;

import static net.blumasc.selectivepowers.events.SupportEvents.*;

@EventBusSubscriber(modid = SelectivePowers.MODID)
public class PowerApplyingEvents {

    @SubscribeEvent
    public static void onItemPickup(ItemEntityPickupEvent.Post event) {
        Player player = event.getPlayer();
        if(player.isCreative() || player.isSpectator()) return;
        PowerManager pm = PowerManager.get(event.getPlayer().getServer().overworld());
        System.out.println(pm.getPowerOfPlayer(player.getUUID()));
        if(!pm.doesPlayerHaveAnyPower(player.getUUID())){
            System.out.println("Check");
            if(pm.isPowerFree(PowerManager.FORREST_POWER)) {
                int uniqueFlowers = countUniqueItems(player, ModTags.Items.PLANT_ITEMS);

                if (uniqueFlowers >= 10) {
                    pm.assignPower(PowerManager.FORREST_POWER,player);
                    player.sendSystemMessage(
                            Component.translatable("selectivepowers.messages.offer."+PowerManager.FORREST_POWER, Component.translatable("selectivepowers.name."+PowerManager.FORREST_POWER))
                    );
                }
            }
            if(pm.isPowerFree(PowerManager.MUSHROOM_POWER)) {
                int numberMushrooms = countTotalItems(player, ModTags.Items.MUSHROOM_ITEMS);
                System.out.println(numberMushrooms);
                if (numberMushrooms >= 64) {
                    pm.assignPower(PowerManager.MUSHROOM_POWER,player);
                    player.sendSystemMessage(
                            Component.translatable("selectivepowers.messages.offer."+PowerManager.MUSHROOM_POWER, Component.translatable("selectivepowers.name."+PowerManager.MUSHROOM_POWER))
                    );
                }
            }}
    }

    @SubscribeEvent
    public static void onOpenContainer(PlayerContainerEvent.Open event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if(player.isCreative() || player.isSpectator()) return;

        PowerManager pm = PowerManager.get((ServerLevel) player.level());
        if (pm.doesPlayerHaveAnyPower(player.getUUID())) return;

        if (pm.isPowerFree(PowerManager.RAGE_POWER)) {

            if (!(event.getContainer() instanceof ChestMenu)) return;
            List<Entity> villagers = player.level().getEntitiesOfClass(Entity.class, player.getBoundingBox().inflate(10), mob -> mob.getType().is(ModTags.EntityTypes.VILLAGER_LIKE));
            if (!villagers.isEmpty()) {
                pm.assignPower(PowerManager.RAGE_POWER, player);
                player.sendSystemMessage(
                        Component.translatable("selectivepowers.messages.offer."+PowerManager.RAGE_POWER, Component.translatable("selectivepowers.name."+PowerManager.RAGE_POWER))
                );
                pm.setDirty();
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerHitByLightning(EntityStruckByLightningEvent event)
    {
        if (event.getEntity().level().isClientSide()) return;
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if(player.isCreative() || player.isSpectator()) return;

        PowerManager pm = PowerManager.get((ServerLevel) player.level());
        if (pm.doesPlayerHaveAnyPower(player.getUUID())) return;
        if (pm.isPowerFree(PowerManager.STORM_POWER)){
            pm.assignPower(PowerManager.STORM_POWER, player);
            player.sendSystemMessage(
                    Component.translatable("selectivepowers.messages.offer."+PowerManager.STORM_POWER, Component.translatable("selectivepowers.name."+PowerManager.STORM_POWER))
            );
            pm.setDirty();
        }
    }

    @SubscribeEvent
    public static void onCraft(PlayerEvent.ItemCraftedEvent event)
    {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if(player.isCreative() || player.isSpectator()) return;
        PowerManager pm = PowerManager.get((ServerLevel) player.level());
        if (pm.doesPlayerHaveAnyPower(player.getUUID())) return;
        PowerManager.PlayerProgress progress = pm.getProgress(player.getUUID());
        if (pm.isPowerFree(PowerManager.TRUTH_POWER)){
            ItemStack result = event.getCrafting();
            if (result.getItem() != Items.BOOK) return;
            progress.booksCrafted += result.getCount();
            if(progress.booksCrafted>=64) {
                pm.assignPower(PowerManager.TRUTH_POWER, player);
                player.sendSystemMessage(
                        Component.translatable("selectivepowers.messages.offer." + PowerManager.TRUTH_POWER, Component.translatable("selectivepowers.name."+PowerManager.TRUTH_POWER))
                );
                pm.syncToAll((ServerLevel) player.level());
            }
            pm.setDirty();
        }
    }

    @SubscribeEvent
    public static void onOreMined(BlockEvent.BreakEvent event) {
        if (!(event.getPlayer() instanceof ServerPlayer serverPlayer)) return;
        if(serverPlayer.isCreative() || serverPlayer.isSpectator()) return;

        PowerManager pm = PowerManager.get((ServerLevel) serverPlayer.level());
        if (pm.doesPlayerHaveAnyPower(serverPlayer.getUUID())) return;
        if (pm.isPowerFree(PowerManager.ROCK_POWER)) {
            BlockState state = event.getState();

            if (state.is(BlockTags.create(ResourceLocation.parse("c:ores")))) {
                PowerManager.PlayerProgress progress = pm.getProgress(serverPlayer.getUUID());
                progress.minedOres++;
                if(progress.minedOres>64)
                {
                    pm.assignPower(PowerManager.ROCK_POWER,serverPlayer);
                    serverPlayer.sendSystemMessage(
                            Component.translatable("selectivepowers.messages.offer." + PowerManager.ROCK_POWER, Component.translatable("selectivepowers.name."+PowerManager.ROCK_POWER))
                    );
                }
                pm.setDirty();
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerNotEnlightened(PlayerTickEvent.Post event) {
        if (event.getEntity().level().isClientSide()) return;

        ServerPlayer player = (ServerPlayer) event.getEntity();
        if (player.tickCount % 20 != 0) return;

        if(player.isSpectator()) return;

        PowerManager pm = PowerManager.get((ServerLevel) player.level());

        UUID playerID = pm.getPlayerWithPower(PowerManager.YELLOW_POWER);

        if (playerID == null) {
            if (pm.doesPlayerHaveAnyPower(player.getUUID())) return;
            if (player.getDisplayName().getString().equals(Config.YELLOW_KING.get())) {
                pm.assignPower(PowerManager.YELLOW_POWER, player);
                player.sendSystemMessage(
                        Component.translatable("selectivepowers.messages.offer." + PowerManager.YELLOW_POWER)
                );
                pm.syncToAll((ServerLevel) player.level());
            }
        }else{
            if(playerID.equals(player.getUUID()) && pm.getPowerLevelOfPlayer(playerID)== PowerManager.PowerLevel.BOUND &&  pm.countAtLeastAwokenAbilities()>5){
                pm.upgradePower(PowerManager.YELLOW_POWER);
                player.sendSystemMessage(
                        Component.translatable("selectivepowers.messages.awaking." + PowerManager.YELLOW_POWER)
                );
                pm.syncToAll((ServerLevel) player.level());
            }
        }
        playerID = pm.getPlayerWithPower(PowerManager.MOON_POWER);

        if (playerID == null) {
            if (pm.doesPlayerHaveAnyPower(player.getUUID())) return;
            if (player.getDisplayName().getString().equals(Config.LUNAR_MAIDEN.get())) {
                pm.assignPower(PowerManager.MOON_POWER, player);
                player.sendSystemMessage(
                        Component.translatable("selectivepowers.messages.offer." + PowerManager.MOON_POWER)
                );
                pm.syncToAll((ServerLevel) player.level());
            }
        }else{
            if(playerID.equals(player.getUUID()) && pm.getPowerLevelOfPlayer(player.getUUID())== PowerManager.PowerLevel.BOUND &&  pm.countAtLeastAwokenAbilities()>5){
                pm.upgradePower(PowerManager.MOON_POWER);
                player.sendSystemMessage(
                        Component.translatable("selectivepowers.messages.awaking." + PowerManager.MOON_POWER)
                );
                pm.syncToAll((ServerLevel) player.level());
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerInSubstance(PlayerTickEvent.Post event) {
        if (event.getEntity().level().isClientSide()) return;

        ServerPlayer player = (ServerPlayer) event.getEntity();

        if(player.isCreative() || player.isSpectator()) return;

        PowerManager pm = PowerManager.get((ServerLevel) player.level());

        if (pm.doesPlayerHaveAnyPower(player.getUUID())) return;

        PowerManager.PlayerProgress progress = pm.getProgress(player.getUUID());

        if (pm.isPowerFree(PowerManager.ELEMENTAL_POWER)){
            if(player.isInPowderSnow)
            {
                progress.enteredPowderedSnow = true;
            }
            if(player.isInLava())
            {
                progress.enteredLava = true;
            }
            if(progress.enteredLava && progress.enteredPowderedSnow)
            {
                pm.assignPower(PowerManager.ELEMENTAL_POWER, player);
                player.sendSystemMessage(
                        Component.translatable("selectivepowers.messages.offer."+PowerManager.ELEMENTAL_POWER, Component.translatable("selectivepowers.name."+PowerManager.ELEMENTAL_POWER))
                );
            }
            pm.setDirty();
        }
    }

    @SubscribeEvent
    public static void onPlayerInLight(PlayerTickEvent.Post event) {
        if (event.getEntity().level().isClientSide()) return;

        ServerPlayer player = (ServerPlayer) event.getEntity();

        if(player.isCreative() || player.isSpectator()) return;

        PowerManager pm = PowerManager.get((ServerLevel) player.level());

        if (pm.doesPlayerHaveAnyPower(player.getUUID())) return;

        PowerManager.PlayerProgress progress = pm.getProgress(player.getUUID());

        Vec3 vec = player.getEyePosition();
        BlockPos eyePos = new BlockPos((int)Math.floor(vec.x),(int)Math.floor(vec.y),(int)Math.floor(vec.z));

        if (pm.isPowerFree(PowerManager.LIGHT_POWER)) {
            if(player.level().isDay() && player.level().canSeeSky(player.getOnPos())){
                progress.skylightTimer++;
                if(progress.skylightTimer>=6000)
                {
                    pm.assignPower(PowerManager.LIGHT_POWER, player);
                    player.sendSystemMessage(
                            Component.translatable("selectivepowers.messages.offer."+PowerManager.LIGHT_POWER, Component.translatable("selectivepowers.name."+PowerManager.LIGHT_POWER))
                    );
                }
            }else{
                progress.skylightTimer = 0;
            }
            pm.setDirty();
        }
        if (pm.isPowerFree(PowerManager.DARK_POWER))
        {
            int skyLight = player.level().getBrightness(LightLayer.SKY, eyePos);
            int lightlevel = player.level().getBrightness(LightLayer.BLOCK, eyePos);
            int light = Math.max(skyLight,lightlevel);
            if(light<=2){
                progress.darknessTimer++;
                if(progress.darknessTimer>=1200)
                {
                    pm.assignPower(PowerManager.DARK_POWER, player);
                    player.sendSystemMessage(
                            Component.translatable("selectivepowers.messages.offer."+PowerManager.DARK_POWER, Component.translatable("selectivepowers.name."+PowerManager.DARK_POWER))
                    );
                }
            }else{
                progress.darknessTimer = 0;
            }
            pm.setDirty();
            pm.syncToAll((ServerLevel) player.level());
        }
    }

    @SubscribeEvent
    public static void onPlayerLook(PlayerTickEvent.Post event) {
        if (event.getEntity().level().isClientSide()) return;

        ServerPlayer player = (ServerPlayer) event.getEntity();

        if(player.isCreative() || player.isSpectator()) return;

        if (player.tickCount % 5 != 0) return;

        PowerManager pm = PowerManager.get((ServerLevel) player.level());

        if(pm.doesPlayerHaveAnyPower(player.getUUID())) return;

        if(pm.isPowerFree(PowerManager.ANIMAL_POWER) || pm.isPowerFree(PowerManager.DRAGON_POWER)) {

            Vec3 start = player.getEyePosition();
            Vec3 look = player.getLookAngle();
            double distance = 5.0;

            AABB box = player.getBoundingBox().expandTowards(look.scale(distance)).inflate(1.0);
            List<Entity> entities = player.level().getEntities(player, box, Objects::nonNull);

            for (Entity entity : entities) {
                Vec3 toEntity = entity.getEyePosition().subtract(start).normalize();
                if (look.dot(toEntity) > 0.95) {
                    PowerManager.PlayerProgress progress = pm.getProgress(player.getUUID());
                    if (entity instanceof TamableAnimal && pm.isPowerFree(PowerManager.ANIMAL_POWER)) {
                        progress.lookAtTameableTimer++;
                        if(progress.lookAtTameableTimer >= 1200)
                        {
                            pm.assignPower(PowerManager.ANIMAL_POWER, player);
                            player.sendSystemMessage(
                                    Component.translatable("selectivepowers.messages.offer."+PowerManager.ANIMAL_POWER, Component.translatable("selectivepowers.name."+PowerManager.ANIMAL_POWER))
                            );
                        }
                    }else if (entity.getType().is(ModTags.EntityTypes.DRAGON_LIKE) && pm.isPowerFree(PowerManager.DRAGON_POWER))
                    {
                        pm.assignPower(PowerManager.DRAGON_POWER, player);
                        player.sendSystemMessage(
                                Component.translatable("selectivepowers.messages.offer."+PowerManager.DRAGON_POWER, Component.translatable("selectivepowers.name."+PowerManager.DRAGON_POWER))
                        );
                        pm.syncToAll((ServerLevel) player.level());
                    }

                }
            }
            pm.setDirty();
        }
    }
    @SubscribeEvent
    public static void useBonemeal(BonemealEvent e)
    {
        Player p = e.getPlayer();
        if(p == null)return;
        if(p.level() instanceof ServerLevel sl)
        {
            PowerManager pm = PowerManager.get(sl);
            if(pm.getPowerOfPlayer(p.getUUID()).equals(PowerManager.FORREST_POWER) && pm.getPowerLevelOfPlayer(p.getUUID()).equals(PowerManager.PowerLevel.BOUND))
            {
                PowerManager.PlayerProgress progress = pm.getProgress(p.getUUID());
                progress.ascensionCounter++;
                if(progress.ascensionCounter>=128)
                {
                    pm.upgradePower(PowerManager.FORREST_POWER);
                    p.sendSystemMessage(
                            Component.translatable("selectivepowers.messages.awaking."+PowerManager.FORREST_POWER, Component.translatable("selectivepowers.name."+PowerManager.FORREST_POWER))
                    );
                }
                pm.setDirty();
            }
        }
    }
    @SubscribeEvent
    public static void onItemConsumed(LivingEntityUseItemEvent.Finish event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if(!(player.level() instanceof ServerLevel sl)) return;

        PowerManager pm = PowerManager.get(sl);

        if(pm.getPowerOfPlayer(player.getUUID()).equals(PowerManager.MUSHROOM_POWER) && pm.getPowerLevelOfPlayer(player.getUUID()).equals(PowerManager.PowerLevel.BOUND)) {

            ItemStack stack = event.getItem();

            if (stack.is(ModTags.Items.MUSHROOM_ITEMS)) {
                PowerManager.PlayerProgress progress = pm.getProgress(player.getUUID());
                progress.ascensionCounter++;
                if(progress.ascensionCounter>=32)
                {
                    pm.upgradePower(PowerManager.MUSHROOM_POWER);
                    player.sendSystemMessage(
                            Component.translatable("selectivepowers.messages.awaking."+PowerManager.MUSHROOM_POWER, Component.translatable("selectivepowers.name."+PowerManager.MUSHROOM_POWER))
                    );
                }
                pm.setDirty();
            }
        }
    }

    @SubscribeEvent
    public static void onAnimalTamed(AnimalTameEvent event) {
        Player player = event.getTamer();

        if (player == null) return;
        if(!(player.level() instanceof ServerLevel sl)) return;

        PowerManager pm = PowerManager.get(sl);

        if(pm.getPowerOfPlayer(player.getUUID()).equals(PowerManager.ANIMAL_POWER) && pm.getPowerLevelOfPlayer(player.getUUID()).equals(PowerManager.PowerLevel.BOUND)) {
            PowerManager.PlayerProgress progress = pm.getProgress(player.getUUID());
            progress.ascensionCounter++;
            if(progress.ascensionCounter>=16)
            {
                pm.upgradePower(PowerManager.ANIMAL_POWER);
                player.sendSystemMessage(
                        Component.translatable("selectivepowers.messages.awaking."+PowerManager.ANIMAL_POWER, Component.translatable("selectivepowers.name."+PowerManager.ANIMAL_POWER))
                );
            }
            pm.setDirty();
        }

    }

    @SubscribeEvent
    public static void onReachingBedrock(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if(!(player.level() instanceof ServerLevel sl)) return;

        PowerManager pm = PowerManager.get(sl);

        BlockPos below = player.blockPosition().below();
        BlockState state = sl.getBlockState(below);

        if(pm.getPowerOfPlayer(player.getUUID()).equals(PowerManager.ROCK_POWER) && pm.getPowerLevelOfPlayer(player.getUUID()).equals(PowerManager.PowerLevel.BOUND)) {
            if (state.is(Blocks.BEDROCK)) {
                    pm.upgradePower(PowerManager.ROCK_POWER);
                    player.sendSystemMessage(
                            Component.translatable("selectivepowers.messages.awaking."+PowerManager.ROCK_POWER, Component.translatable("selectivepowers.name."+PowerManager.ROCK_POWER))
                    );
            }
        }
    }
    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        Player player = event.getEntity();
        if(!(player.level() instanceof ServerLevel sl)) return;

        PowerManager pm = PowerManager.get(sl);

        BlockState state = sl.getBlockState(event.getPos());

        if(pm.getPowerOfPlayer(player.getUUID()).equals(PowerManager.ROCK_POWER) && pm.getPowerLevelOfPlayer(player.getUUID()).equals(PowerManager.PowerLevel.BOUND)) {
            if (state.is(Blocks.BEDROCK)) {
                pm.upgradePower(PowerManager.ROCK_POWER);
                player.sendSystemMessage(
                        Component.translatable("selectivepowers.messages.awaking."+PowerManager.ROCK_POWER, Component.translatable("selectivepowers.name."+PowerManager.ROCK_POWER))
                );
            }
        }
    }
    @SubscribeEvent
    public static void onDragonDeath(LivingDeathEvent event) {
        LivingEntity entity = event.getEntity();

        if(!entity.getType().is(ModTags.EntityTypes.DRAGON_LIKE)) return;

        Level level = entity.level();
        if(!(level instanceof ServerLevel sl)) return;

        DamageSource source = event.getSource();
        Entity killer = source.getEntity();

        Player player = null;
        if (killer instanceof Player p) {
            player = p;
        }

        if (player == null) return;

        PowerManager pm = PowerManager.get(sl);
        if(pm.getPowerOfPlayer(player.getUUID()).equals(PowerManager.DRAGON_POWER) && pm.getPowerLevelOfPlayer(player.getUUID()).equals(PowerManager.PowerLevel.BOUND)) {
                pm.upgradePower(PowerManager.DRAGON_POWER);
                player.sendSystemMessage(
                        Component.translatable("selectivepowers.messages.awaking."+PowerManager.DRAGON_POWER, Component.translatable("selectivepowers.name."+PowerManager.DRAGON_POWER))
                );
        }

    }

    @SubscribeEvent
    public static void onReachingAncinetCity(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player.tickCount % 20 != 0) return;
        if(!(player.level() instanceof ServerLevel sl)) return;

        PowerManager pm = PowerManager.get(sl);

        if(pm.getPowerOfPlayer(player.getUUID()).equals(PowerManager.DARK_POWER) && pm.getPowerLevelOfPlayer(player.getUUID()).equals(PowerManager.PowerLevel.BOUND)) {
            List<StructureStart> structureStarts = sl.structureManager().startsForStructure(new ChunkPos(player.blockPosition()), s -> true);
            List<Structure> structures = structureStarts.stream()
                    .filter(ss -> ss.getBoundingBox().isInside(player.blockPosition()))
                    .map(StructureStart::getStructure).toList();
            boolean ancientCity = false;
            for (Structure structure : structures) {
                ResourceLocation key = sl.registryAccess().registryOrThrow(Registries.STRUCTURE).getKey(structure);
                if(key.equals(BuiltinStructures.ANCIENT_CITY.location())){
                    ancientCity=true;
                }

            }
            if(ancientCity)
            {
                pm.upgradePower(PowerManager.DARK_POWER);
                player.sendSystemMessage(
                        Component.translatable("selectivepowers.messages.awaking."+PowerManager.DARK_POWER,Component.translatable("selectivepowers.name."+PowerManager.DARK_POWER))
                );
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerDamaged(LivingDamageEvent.Post event) {
        if (!(event.getEntity() instanceof Player player)) return;

        if(!(player.level() instanceof ServerLevel sl)) return;

        PowerManager pm = PowerManager.get(sl);

        if(pm.getPowerOfPlayer(player.getUUID()).equals(PowerManager.ELEMENTAL_POWER) && pm.getPowerLevelOfPlayer(player.getUUID()).equals(PowerManager.PowerLevel.BOUND)) {

            DamageSource source = event.getSource();
            if(source == null) return;
            Entity sourceEntity = source.getDirectEntity();
            if(sourceEntity == null) return;

            if(sourceEntity.getType().is(ModTags.EntityTypes.ELEMENTAL_PROJECTILES)){
                PowerManager.PlayerProgress progress = pm.getProgress(player.getUUID());
                progress.ascensionCounter++;
                if(progress.ascensionCounter>=64)
                {
                    pm.upgradePower(PowerManager.ELEMENTAL_POWER);
                    player.sendSystemMessage(
                            Component.translatable("selectivepowers.messages.awaking."+PowerManager.ELEMENTAL_POWER, Component.translatable("selectivepowers.name."+PowerManager.ELEMENTAL_POWER))
                    );
                }
                pm.setDirty();
            }
        }
    }

    @SubscribeEvent
    public static void onUndeadKilled(LivingDeathEvent event) {
        if (event.getSource().getEntity() instanceof Player player) {

            if (!(player.level() instanceof ServerLevel sl)) return;

            PowerManager pm = PowerManager.get(sl);

            if (pm.getPowerOfPlayer(player.getUUID()).equals(PowerManager.LIGHT_POWER) && pm.getPowerLevelOfPlayer(player.getUUID()).equals(PowerManager.PowerLevel.BOUND)) {
                LivingEntity entity = event.getEntity();
                if (!(entity.getType().is(EntityTypeTags.SENSITIVE_TO_SMITE))) return;
                PowerManager.PlayerProgress progress = pm.getProgress(player.getUUID());
                progress.ascensionCounter++;
                if(progress.ascensionCounter>=32)
                {
                    pm.upgradePower(PowerManager.LIGHT_POWER);
                    player.sendSystemMessage(
                            Component.translatable("selectivepowers.messages.awaking."+PowerManager.LIGHT_POWER, Component.translatable("selectivepowers.name."+PowerManager.LIGHT_POWER))
                    );
                }
                pm.setDirty();
            }
        }
    }
    @SubscribeEvent
    public static void onVillagerDeath(LivingDeathEvent event) {
        LivingEntity entity = event.getEntity();

        if(!entity.getType().is(ModTags.EntityTypes.VILLAGER_LIKE)) return;

        Level level = entity.level();
        if(!(level instanceof ServerLevel sl)) return;

        DamageSource source = event.getSource();
        Entity killer = source.getEntity();

        Player player = null;
        if (killer instanceof Player p) {
            player = p;
        }

        if(player == null)return;

        PowerManager pm = PowerManager.get(sl);
        if(pm.getPowerOfPlayer(player.getUUID()).equals(PowerManager.RAGE_POWER) && pm.getPowerLevelOfPlayer(player.getUUID()).equals(PowerManager.PowerLevel.BOUND)) {
            PowerManager.PlayerProgress progress = pm.getProgress(player.getUUID());
            progress.ascensionCounter++;
            if(progress.ascensionCounter>=32)
            {
                pm.upgradePower(PowerManager.RAGE_POWER);
                player.sendSystemMessage(
                        Component.translatable("selectivepowers.messages.awaking."+PowerManager.RAGE_POWER, Component.translatable("selectivepowers.name."+PowerManager.RAGE_POWER))
                );
            }
            pm.setDirty();
        }

    }

    @SubscribeEvent
    public static void onLightningRodCraft(PlayerEvent.ItemCraftedEvent event)
    {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        PowerManager pm = PowerManager.get((ServerLevel) player.level());
        PowerManager.PlayerProgress progress = pm.getProgress(player.getUUID());
        if (pm.getPowerOfPlayer(player.getUUID()).equals(PowerManager.STORM_POWER) && pm.getPowerLevelOfPlayer(player.getUUID()).equals(PowerManager.PowerLevel.BOUND)){
            ItemStack result = event.getCrafting();
            if (result.getItem() != Blocks.LIGHTNING_ROD.asItem()) return;
            progress.ascensionCounter += result.getCount();
            if(progress.ascensionCounter>=64) {
                pm.upgradePower(PowerManager.STORM_POWER);
                player.sendSystemMessage(
                        Component.translatable("selectivepowers.messages.awaking."+PowerManager.STORM_POWER,  Component.translatable("selectivepowers.name."+PowerManager.STORM_POWER))
                );
            }
            pm.setDirty();
        }
    }

    @SubscribeEvent
    public static void onReachingLibrary(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player.tickCount % 20 != 0) return;
        if(!(player.level() instanceof ServerLevel sl)) return;

        PowerManager pm = PowerManager.get(sl);

        if(pm.getPowerOfPlayer(player.getUUID()).equals(PowerManager.TRUTH_POWER) && pm.getPowerLevelOfPlayer(player.getUUID()).equals(PowerManager.PowerLevel.BOUND)) {
            List<StructureStart> structureStarts = sl.structureManager().startsForStructure(new ChunkPos(player.blockPosition()), s -> true);
            List<Structure> structures = structureStarts.stream()
                    .filter(ss -> ss.getBoundingBox().isInside(player.blockPosition()))
                    .map(StructureStart::getStructure).toList();
            boolean stronghold = false;
            for (Structure structure : structures) {
                ResourceLocation key = sl.registryAccess().registryOrThrow(Registries.STRUCTURE).getKey(structure);
                if(key.equals(BuiltinStructures.STRONGHOLD.location())){
                    stronghold=true;
                }

            }
            if(stronghold)
            {
                pm.upgradePower(PowerManager.TRUTH_POWER);
                player.sendSystemMessage(
                        Component.translatable("selectivepowers.messages.awaking."+PowerManager.TRUTH_POWER, Component.translatable("selectivepowers.name."+PowerManager.TRUTH_POWER))
                );
            }
        }
    }
    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        LivingEntity entity = event.getEntity();

        ResourceLocation id = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType());

        if(!(entity instanceof Player target)) return;
        if(!(target.level() instanceof ServerLevel sl)) return;
        DamageSource source = event.getSource();
        Entity e = source.getEntity();
        Player killer = null;
        if (e instanceof Player p) {
            killer = p;
        } else if (e instanceof ServerPlayer sp) {
            killer = sp;
        }
        if(killer==null)return;

        PowerManager pm = PowerManager.get(sl);
        if(pm.getPowerLevelOfPlayer(killer.getUUID()).equals(PowerManager.PowerLevel.AWOKEN) && (pm.getPowerLevelOfPlayer(target.getUUID()).equals(PowerManager.PowerLevel.AWOKEN) || pm.getPowerLevelOfPlayer(target.getUUID()).equals(PowerManager.PowerLevel.ASCENDED))) {
            if(!(pm.getPowerOfPlayer(killer.getUUID()).equals(PowerManager.YELLOW_POWER) || pm.getPowerOfPlayer(killer.getUUID()).equals(PowerManager.MOON_POWER))) {
                PowerManager.PlayerProgress progress = pm.getProgress(killer.getUUID());
                pm.upgradePower(pm.getPowerOfPlayer(killer.getUUID()));
                Component message = Component.translatable(
                        "selectivepowers.messages.awoken_after_kill",
                        killer.getDisplayName(),
                        target.getDisplayName()
                );

                for(Player p:sl.getServer().getPlayerList().getPlayers()){
                    p.sendSystemMessage(message);
                }
            }
            if (pm.getPowerLevelOfPlayer(target.getUUID()).equals(PowerManager.PowerLevel.ASCENDED))
            {
                if(pm.getPowerOfPlayer(target.getUUID()).equals(PowerManager.YELLOW_POWER) || pm.getPowerOfPlayer(target.getUUID()).equals(PowerManager.MOON_POWER)) return;
                pm.downgradePower(pm.getPowerOfPlayer(target.getUUID()));
                target.sendSystemMessage(
                        Component.translatable("selectivepowers.messages.lose_ascension")
                );
            }
        }

    }
    @SubscribeEvent
    public static void onPlayerDrownDamaged(LivingDamageEvent.Post event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!(player.level() instanceof ServerLevel sl)) return;

        DamageSource source = event.getSource();
        if (source == null) return;
        if (!source.is(DamageTypes.DROWN)) return;

        PowerManager pm = PowerManager.get(sl);

        if (pm.doesPlayerHaveAnyPower(player.getUUID())) return;
        if (!pm.isPowerFree(PowerManager.WATER_POWER)) return;

        pm.assignPower(PowerManager.WATER_POWER, player);

        player.sendSystemMessage(
                Component.translatable(
                        "selectivepowers.messages.offer." + PowerManager.WATER_POWER,
                        Component.translatable("selectivepowers.name." + PowerManager.WATER_POWER)
                )
        );

        pm.setDirty();
    }

    @SubscribeEvent
    public static void onWaterPowerSwimming(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player.level().isClientSide()) return;
        if (!(player.level() instanceof ServerLevel sl)) return;

        PowerManager pm = PowerManager.get(sl);

        if (!pm.getPowerOfPlayer(player.getUUID()).equals(PowerManager.WATER_POWER)) return;
        if (!pm.getPowerLevelOfPlayer(player.getUUID()).equals(PowerManager.PowerLevel.BOUND)) return;
        if (!player.isSwimming()) return;

        PowerManager.PlayerProgress progress = pm.getProgress(player.getUUID());

        progress.ascensionCounter++;

        if (progress.ascensionCounter >= 10000) {
            pm.upgradePower(PowerManager.WATER_POWER);

            player.sendSystemMessage(
                    Component.translatable(
                            "selectivepowers.messages.awaking." + PowerManager.WATER_POWER,
                            Component.translatable("selectivepowers.name." + PowerManager.WATER_POWER)
                    )
            );
        }
        pm.setDirty();
    }
    @SubscribeEvent
    public static void onRedstonePlaced(BlockEvent.EntityPlaceEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (player.level().isClientSide()) return;
        if (!(player.level() instanceof ServerLevel sl)) return;

        BlockState state = event.getPlacedBlock();
        if (!state.is(Blocks.REDSTONE_WIRE)) return;

        PowerManager pm = PowerManager.get(sl);

        if (pm.doesPlayerHaveAnyPower(player.getUUID())) return;
        if (!pm.isPowerFree(PowerManager.MACHINE_POWER)) return;

        PowerManager.PlayerProgress progress = pm.getProgress(player.getUUID());

        progress.placedRedstone++;

        if (progress.placedRedstone >= 64) {
            pm.assignPower(PowerManager.MACHINE_POWER, player);

            player.sendSystemMessage(
                    Component.translatable(
                            "selectivepowers.messages.offer." + PowerManager.MACHINE_POWER,
                            Component.translatable("selectivepowers.name." + PowerManager.MACHINE_POWER)
                    )
            );

            pm.setDirty();
        }
    }
    @SubscribeEvent
    public static void onRedstoneActivatorUsed(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        if (!(player.level() instanceof ServerLevel sl)) return;

        PowerManager pm = PowerManager.get(sl);

        if (!pm.getPowerOfPlayer(player.getUUID()).equals(PowerManager.MACHINE_POWER)) return;
        if (!pm.getPowerLevelOfPlayer(player.getUUID()).equals(PowerManager.PowerLevel.BOUND)) return;

        BlockState state = player.level().getBlockState(event.getPos());

        if (!state.is(ModTags.Blocks.REDSTONE_ACTIVATOR)) return;

        PowerManager.PlayerProgress progress = pm.getProgress(player.getUUID());

        progress.ascensionCounter++;

        if (progress.ascensionCounter >= 128) {
            pm.upgradePower(PowerManager.MACHINE_POWER);

            player.sendSystemMessage(
                    Component.translatable(
                            "selectivepowers.messages.awaking." + PowerManager.MACHINE_POWER,
                            Component.translatable("selectivepowers.name." + PowerManager.MACHINE_POWER)
                    )
            );
        }
    }
}
