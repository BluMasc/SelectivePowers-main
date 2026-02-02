package net.blumasc.selectivepowers.events;

import net.blumasc.selectivepowers.Config;
import net.blumasc.selectivepowers.PowerManager;
import net.blumasc.selectivepowers.SelectivePowers;
import net.blumasc.selectivepowers.block.entity.ProtectionEffigyBlockEntity;
import net.blumasc.selectivepowers.damage.SelectivePowersDamageTypes;
import net.blumasc.selectivepowers.effect.SelectivepowersEffects;
import net.blumasc.selectivepowers.entity.CircleVariant;
import net.blumasc.selectivepowers.entity.SelectivepowersEntities;
import net.blumasc.selectivepowers.entity.custom.YellowKingEntity;
import net.blumasc.selectivepowers.entity.custom.projectile.MagicCircleEntity;
import net.blumasc.selectivepowers.entity.helper.YellowFeverHelper;
import net.blumasc.selectivepowers.item.SelectivepowersItems;
import net.blumasc.selectivepowers.item.custom.FrostShieldItem;
import net.blumasc.selectivepowers.item.custom.ProspectorsShovelItem;
import net.blumasc.selectivepowers.managers.*;
import net.blumasc.selectivepowers.mixin.MobEffectAccessor;
import net.blumasc.selectivepowers.worldgen.ModDimensions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.event.entity.living.*;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import static net.blumasc.selectivepowers.entity.helper.SquidSortHelper.isSorted;

@EventBusSubscriber(modid = SelectivePowers.MODID)
public class PowerUseEvents {
    private static final ResourceLocation NATURAL_ARMOR = ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "natural_armor");

    @SubscribeEvent
    public static void onnaturalArmor(PlayerTickEvent.Pre event) {
        Player player = event.getEntity();
        if (player.level().isClientSide) return;

        AttributeInstance armorAttr = player.getAttribute(Attributes.ARMOR);
        if (armorAttr == null) return;

        armorAttr.removeModifier(NATURAL_ARMOR);
        PowerManager pm = PowerManager.get((ServerLevel) player.level());

        if (pm.getPowerOfPlayer(player.getUUID()).equals(PowerManager.RAGE_POWER)) {

            double currentArmor = armorAttr.getValue();
            double needed = 12 - currentArmor;

            if (needed > 0) {
                AttributeModifier mod = new AttributeModifier(
                        NATURAL_ARMOR,
                        needed,
                        AttributeModifier.Operation.ADD_VALUE
                );
                armorAttr.addTransientModifier(mod);
            }
        }
    }


        @SubscribeEvent
        public static void onAttackEntity(AttackEntityEvent event) {
            if (!(event.getTarget() instanceof LivingEntity target)) return;

            Player attacker = event.getEntity();
            ItemStack held = attacker.getMainHandItem();

            if (held.is(SelectivepowersItems.SUN_SLICER.get())) {

                if (target instanceof Player victim && victim.isCreative()) {
                    event.setCanceled(true);

                    float damage = 5f;
                    victim.hurt(attacker.damageSources().playerAttack(attacker), damage);
                }
            }
        }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if (!(event.getPlayer() instanceof ServerPlayer player)) return;

        ItemStack tool = player.getMainHandItem();
        if (!(tool.getItem() instanceof ProspectorsShovelItem)) return;

        BlockState state = event.getState();
        if (!state.is(BlockTags.MINEABLE_WITH_SHOVEL)) return;

        double chance = Math.min(0.5, 0.05 + player.getLuck() * 0.02);
        if (player.getRandom().nextDouble() > chance) return;

        List<? extends String> configIds = Config.BLOCK_STRINGS_ORE.get();
        if (configIds.isEmpty()) return;

        String targetId = configIds.get(player.getRandom().nextInt(configIds.size()));
        ResourceLocation rl = ResourceLocation.tryParse(targetId);
        if (rl == null) return;

        Block sourceBlock = BuiltInRegistries.BLOCK.getOptional(rl).orElse(null);
        if (sourceBlock == null) return;

        ItemStack extraDrop = getRandomItemDropFromBlock(sourceBlock, player);
        if (extraDrop.isEmpty()) return;

        Block.popResource(player.level(), event.getPos(), extraDrop);
    }


    private static ItemStack getRandomItemDropFromBlock(Block block, ServerPlayer player) {
        ServerLevel level = player.serverLevel();

        LootTable table = level.getServer().reloadableRegistries().getLootTable(block.getLootTable());

        LootParams params = new LootParams.Builder(level)
                .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(player.blockPosition()))
                .withParameter(LootContextParams.TOOL, player.getMainHandItem())
                .withParameter(LootContextParams.THIS_ENTITY, player)
                .withParameter(LootContextParams.BLOCK_STATE, block.defaultBlockState())
                .create(LootContextParamSets.BLOCK);

        List<ItemStack> drops = table.getRandomItems(params);

        List<ItemStack> itemsOnly = drops.stream()
                .filter(stack -> !(stack.getItem() instanceof BlockItem))
                .toList();

        if (itemsOnly.isEmpty()) return ItemStack.EMPTY;

        return itemsOnly
                .get(player.getRandom().nextInt(itemsOnly.size()))
                .copyWithCount(1);
    }





    @SubscribeEvent
    public static void onNoFallDamage(LivingIncomingDamageEvent event) {

        if (event.getEntity().level().dimension() == ModDimensions.LUNAR_DIM_LEVEL){
            event.setCanceled(true);
        }

        if (!event.getSource().is(DamageTypes.FALL)) return;

        if (event.getEntity().hasEffect(SelectivepowersEffects.FALL_IMMUNITY_EFFECT)) {
            event.setCanceled(true);
        }

        if(event.getEntity() instanceof Player p){
            if(p.level() instanceof ServerLevel sl)
            {
                PowerManager pm = PowerManager.get(sl);
                if(pm.getPowerOfPlayer(p.getUUID()).equals(PowerManager.DRAGON_POWER))
                {
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onStillOnMoonEvent(PlayerTickEvent.Post e)
    {
        if (!(e.getEntity() instanceof ServerPlayer player)) return;

        if (player.level().dimension() != ModDimensions.LUNAR_DIM_LEVEL) return;

        if (!player.hasEffect(SelectivepowersEffects.MOON_BOUND_EFFECT)) {
            BlockPos respawnPos = player.getRespawnPosition() != null
                    ? player.getRespawnPosition()
                    : player.level().getSharedSpawnPos();

            ServerLevel respawnLevel = player.getServer().getLevel(
                    player.getRespawnDimension() != null ? player.getRespawnDimension() : Level.OVERWORLD
            );

            if (respawnLevel != null && respawnPos != null) {
                player.teleportTo(respawnLevel,
                        respawnPos.getX() + 0.5,
                        respawnPos.getY() + 0.5,
                        respawnPos.getZ() + 0.5,
                        player.getYRot(),
                        player.getXRot());
            }
        }
    }

    @SubscribeEvent
    public static void onShieldRebuke(LivingShieldBlockEvent event) {
        LivingEntity entity = event.getEntity();

        if (!(entity instanceof Player player)) return;

        if (!player.isBlocking()) return;

        ItemStack stack = player.getUseItem();
        if (!(stack.getItem() instanceof FrostShieldItem shield)) return;

        shield.rebuke(player.level(), player);
    }

    @SubscribeEvent
    public static void onRageDamage(LivingIncomingDamageEvent event) {

        LivingEntity e = event.getEntity();
        if(e.level() instanceof ServerLevel sl) {
            PowerManager pm = PowerManager.get(sl);
            PowerManager.PlayerProgress progress = pm.getProgress(e.getUUID());
            if(pm.getPowerOfPlayer(e.getUUID()).equals(PowerManager.RAGE_POWER) && progress.ultTimer>0)
            {
                MobEffectInstance current = e.getEffect(SelectivepowersEffects.RAGE_EFFECT);
                if (current == null || current.getDuration() != progress.ultTimer) {
                    e.addEffect(new MobEffectInstance(
                                SelectivepowersEffects.RAGE_EFFECT,
                                progress.ultTimer,
                                0,
                                false,
                                false,
                                true
                    ));
                }
            }
            if (e.hasEffect(SelectivepowersEffects.RAGE_EFFECT)) {
                RageManager rm = RageManager.get(sl);
                rm.saveDamage(e.getUUID(), event.getAmount());
                event.setAmount(0.0f);
            }
        }
    }

    @SubscribeEvent
    public static void onTimerTicking(PlayerTickEvent.Pre event) {

        LivingEntity e = event.getEntity();
        if(e.level() instanceof ServerLevel sl) {
            PowerManager pm = PowerManager.get(sl);
            PowerManager.PlayerProgress progress = pm.getProgress(e.getUUID());
            if(progress.ultTimer>0)
            {
                MobEffectInstance current = e.getEffect(SelectivepowersEffects.ULT_TIMER);
                if (current == null || current.getDuration() != progress.ultTimer) {
                    e.addEffect(new MobEffectInstance(
                            SelectivepowersEffects.ULT_TIMER,
                            progress.ultTimer,
                            0,
                            false,
                            false,
                            true
                    ));
                }
            }else
            {
                e.removeEffect(SelectivepowersEffects.ULT_TIMER);
            }
            if(progress.abilityTimer>0)
            {
                    MobEffectInstance current = e.getEffect(SelectivepowersEffects.ABILITY_TIMER);
                    if (current == null || current.getDuration() != progress.abilityTimer) {
                        e.addEffect(new MobEffectInstance(
                                SelectivepowersEffects.ABILITY_TIMER,
                                progress.abilityTimer,
                                0,
                                false,
                                false,
                                true
                        ));
                    }
            }else
            {
                e.removeEffect(SelectivepowersEffects.ABILITY_TIMER);
            }
        }
    }

    @SubscribeEvent
    public static void afterRageDamage(PlayerTickEvent.@NotNull Post e){
        Player p = e.getEntity();
        if(!(p.level() instanceof ServerLevel sl)) return;
        PowerManager pm = PowerManager.get(sl);
        PowerManager.PlayerProgress progress = pm.getProgress(p.getUUID());
        if(pm.getPowerOfPlayer(p.getUUID()).equals(PowerManager.RAGE_POWER) && progress.ultTimer>0)
        {
            MobEffectInstance current = p.getEffect(SelectivepowersEffects.RAGE_EFFECT);
            progress.ultTimer--;
            pm.setDirty();
            if (current == null || current.getDuration() != progress.ultTimer) {
                p.addEffect(new MobEffectInstance(
                        SelectivepowersEffects.RAGE_EFFECT,
                        progress.ultTimer,
                        0,
                        false,
                        false,
                        true
                ));
            }
        }
        if (p.hasEffect(SelectivepowersEffects.RAGE_EFFECT)) return;
        RageManager rm = RageManager.get(sl);
        float damage = rm.getDamage(p.getUUID());
        if(damage >=1.0)
        {
            rm.removeDamage(p.getUUID());
            p.hurt(SelectivePowersDamageTypes.rageDamage(sl),damage);
        }
    }

    @SubscribeEvent
    public static void onYellowFeverAttack(AttackEntityEvent event) {
        if (!(event.getTarget() instanceof LivingEntity target)) return;
        if(!(target instanceof YellowKingEntity || target instanceof Player)) return;
        if(!(target.level() instanceof ServerLevel sl)) return;

        if(target instanceof Player) {
            PowerManager pm = PowerManager.get(sl);
            if(!(pm.getPowerOfPlayer(target.getUUID())==PowerManager.YELLOW_POWER))
            {
                return;
            }
        }
        Player attacker = event.getEntity();

        MobEffectInstance effect = attacker.getEffect(SelectivepowersEffects.YELLOW_FEVER_EFFECT);
        if(effect == null) return;

        int amplifier = effect.getAmplifier();

        float baseDamage = 1.0f;
        float thornsDamage = baseDamage + (0.5f * amplifier);

        attacker.hurt(sl.damageSources().thorns(target), thornsDamage);
    }

    @SubscribeEvent
    public static void onMoonlightGlaiveAttack(LivingIncomingDamageEvent event) {
        if(!(event.getSource().getEntity() instanceof LivingEntity attacker)) return;
        LivingEntity target = event.getEntity();
        if(!(target.level() instanceof ServerLevel sl)) return;
        if(YellowFeverHelper.isYellowFeverImmune(target)) return;


        if(target instanceof Player) {
            PowerManager pm = PowerManager.get(sl);
            if(!(Objects.equals(pm.getPowerOfPlayer(target.getUUID()), PowerManager.YELLOW_POWER)))
            {
                return;
            }
        }
        ItemStack held = attacker.getMainHandItem();

        if (held.is(SelectivepowersItems.MOONLIGHT_GLAIVE.get())) {

            event.setAmount(event.getAmount()*1.2f);
        }
    }

    public static final ResourceLocation POWER_FLIGHT_MODIFIER_RESOURCE = ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "dragon_power_flight_modifier");
    public static final AttributeModifier POWER_FLIGHT_MODIFIER = new AttributeModifier(
            POWER_FLIGHT_MODIFIER_RESOURCE,
            1.0,
            AttributeModifier.Operation.ADD_VALUE
    );

    @SubscribeEvent
    public static void onPlayerFlightTick(PlayerTickEvent.Pre e)
    {
        Player p = e.getEntity();
        if(p.level() instanceof ServerLevel sl)
        {
            PowerManager pm = PowerManager.get(sl);
            if(pm.getPowerOfPlayer(p.getUUID()).equals(PowerManager.DRAGON_POWER)){
                PowerManager.PlayerProgress progress = pm.getProgress(p.getUUID());
                if(progress.abilityTimer>0) {
                    var attribute = p.getAttribute(NeoForgeMod.CREATIVE_FLIGHT);
                    if (attribute != null && attribute.getModifier(POWER_FLIGHT_MODIFIER_RESOURCE) == null) {
                        attribute.addTransientModifier(POWER_FLIGHT_MODIFIER);
                    }
                    progress.abilityTimer--;
                    pm.setDirty();
                    return;
                }
            }

            var attribute = p.getAttribute(NeoForgeMod.CREATIVE_FLIGHT);
            if (attribute != null) {
                attribute.removeModifier(POWER_FLIGHT_MODIFIER_RESOURCE);
            }
            DraconicFlightManager.get(sl).playerTick(p, sl);
        }
    }

    @SubscribeEvent
    public static void onPlayerTickRemovePoisonPotions(PlayerTickEvent.Pre event) {
        Player player = event.getEntity();

        if (player.level().isClientSide()) return;

        if(!(player.level() instanceof ServerLevel sl))return;
        PowerManager pm = PowerManager.get(sl);
        if(!(pm.getPowerOfPlayer(player.getUUID()).equals(PowerManager.MUSHROOM_POWER)))return;

        List<? extends String> effectsToRemove = Config.EFFECT_STRINGS_POISON.get();

        for (Holder<MobEffect> effect : player.getActiveEffectsMap().keySet()) {
            ResourceLocation id = BuiltInRegistries.MOB_EFFECT.getKey(effect.value());
            if (id != null && effectsToRemove.contains(id.toString())) {
                player.removeEffect(effect);
            }
        }
    }

    @SubscribeEvent
    public static void onFireDamaged(LivingIncomingDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        if (player.level().isClientSide()) return;

        if (player.level() instanceof ServerLevel sl) {
            PowerManager pm = PowerManager.get(sl);
            if (!pm.getPowerOfPlayer(player.getUUID()).equals(PowerManager.ELEMENTAL_POWER)) return;
            if (event.getSource().is(DamageTypes.IN_FIRE)
                    || event.getSource().is(DamageTypes.LAVA)
                    || event.getSource().is(DamageTypes.ON_FIRE)
                    || event.getSource().is(DamageTypes.FREEZE)) {

                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onMobSpawn(MobSpawnEvent.PositionCheck event) {
        if(event.getEntity() instanceof Monster) {
            ProtectionEffigyManager pem = ProtectionEffigyManager.get(event.getLevel().getLevel());
            if(pem.isPositionAffected(event.getLevel().getLevel(), event.getEntity().position())) {
                event.setResult(MobSpawnEvent.PositionCheck.Result.FAIL);
            }
        }
    }

    @SubscribeEvent
    public static void onDragonDamaged(LivingIncomingDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        if (player.level().isClientSide()) return;

            DamageSource source = event.getSource();
            if (source.getDirectEntity() instanceof AreaEffectCloud cloud) {
                if(cloud.getParticle() == ParticleTypes.DRAGON_BREATH) {
                    if (isWearingDragonSleevesItem(player)) {

                        event.setCanceled(true);
                        return;
                    }
                }
            }
            if (source.is(DamageTypes.DRAGON_BREATH)) {
                if (isWearingDragonSleevesItem(player)) {

                    event.setCanceled(true);
                }
            }
    }

    public static boolean isWearingDragonSleevesItem(Player player) {
        return CuriosApi.getCuriosInventory(player)
                .flatMap(curiosInventory -> curiosInventory.getStacksHandler("body"))
                .map(handler -> {
                    for (int slot = 0; slot < handler.getSlots(); slot++) {
                        ItemStack stack = handler.getStacks().getStackInSlot(slot);
                        if (!stack.isEmpty() && stack.is(SelectivepowersItems.DRAGON_SLEEVES.get())) {
                            ItemStack particleStack = stack.copy();
                            stack.hurtAndBreak(1, (ServerLevel)player.level(), (ServerPlayer) player, p -> {
                                ServerLevel level = (ServerLevel) player.level();
                                level.playSound(
                                        null,
                                        player.blockPosition(),
                                        SoundEvents.ITEM_BREAK,
                                        SoundSource.PLAYERS,
                                        1.0F,
                                        1.0F
                                );
                                ItemParticleOption particle =
                                        new ItemParticleOption(ParticleTypes.ITEM, particleStack);

                                level.sendParticles(
                                        particle,
                                        player.getX(),
                                        player.getY() + 1.0,
                                        player.getZ(),
                                        20,
                                        0.25, 0.25, 0.25,
                                        0.05
                                );
                            });
                            return true;
                        }
                    }
                    return false;
                })
                .orElse(false);
    }

    @SubscribeEvent
    public static void onPlayerTickgrowPlants(PlayerTickEvent.Post event) {
        Player player = event.getEntity();

        if (player.level().isClientSide()) return;

        if(!(player.level() instanceof ServerLevel sl))return;
        PowerManager pm = PowerManager.get(sl);
        if(!(pm.getPowerOfPlayer(player.getUUID()).equals(PowerManager.FORREST_POWER)))return;

        RandomSource rand = player.getRandom();

        int radius = 5;
        int ticksPerTick = 2;

        for (int i = 0; i < ticksPerTick; i++) {
            int dx = rand.nextInt(radius * 2 + 1) - radius;
            int dy = rand.nextInt(3) - 1;
            int dz = rand.nextInt(radius * 2 + 1) - radius;

            BlockPos pos = player.blockPosition().offset(dx, dy, dz);
            BlockState state = sl.getBlockState(pos);

            if (state.isRandomlyTicking()) {
                state.randomTick(sl, pos, rand);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerYellowArena(PlayerTickEvent.Post event) {
        Player player = event.getEntity();

        if (!(player instanceof ServerPlayer sp)) return;

        if(!(player.level() instanceof ServerLevel sl))return;
        SunBattleManager sbm = SunBattleManager.get(sl);

        if(!(sbm.PlayerInBattle(player)))return;

        ServerLevel targetLevel = sl.getServer().getLevel(ModDimensions.SOLAR_DIM_LEVEL);
        if (targetLevel == null || player.level().dimension() == ModDimensions.SOLAR_DIM_LEVEL) return;
        sbm.endBattle(sl);
    }

    @SubscribeEvent
    public static void onPlayerArenaDeath(LivingDeathEvent event)
    {
        if (!(event.getEntity() instanceof Player player)) return;
        if(!(player.level() instanceof ServerLevel sl))return;
        SunBattleManager sbm = SunBattleManager.get(sl);
        if(!(sbm.PlayerInBattle(player)))return;
        sbm.endBattle(sl);

    }

    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        Player player = event.getEntity();
        if(!(player.level() instanceof ServerLevel sl))return;
        SunBattleManager sbm = SunBattleManager.get(sl);
        if(!(sbm.PlayerInBattle(player)))return;
        sbm.endBattle(sl);
        player.hurt(SelectivePowersDamageTypes.lunarDamage(null), Float.MAX_VALUE);
    }

    @SubscribeEvent
    public static void onMobDeath(LivingDeathEvent event) {
        if(!(event.getEntity() instanceof ServerPlayer target))return;
        if (event.getSource().getEntity() instanceof Player attacker) {
            if(!(target.level() instanceof ServerLevel sl))return;

            PowerManager pm = PowerManager.get(sl);

            if(pm.getPowerLevelOfPlayer(target.getUUID()).equals(PowerManager.PowerLevel.ASCENDED)) {
                ItemStack weapon = attacker.getMainHandItem();
                if (weapon.is(SelectivepowersItems.SUN_SLICER.get())) {
                    Component message = Component.translatable(
                            "selectivepowers.messages.devoured",
                            Component.translatable("selectivepowers.name."+pm.getPowerOfPlayer(target.getUUID())),
                            target.getDisplayName()
                    );
                    for(Player p:sl.getServer().getPlayerList().getPlayers()){
                        p.sendSystemMessage(message);
                        p.sendSystemMessage(Component.translatable("multiplayer.player.left", target.getDisplayName()));
                    }
                    pm.yellowLives++;
                    pm.takePower(pm.getPowerOfPlayer(target.getUUID()));
                    attacker.displayClientMessage(Component.translatable("selectivepowers.messages.yellow_strengthening", pm.yellowLives),false);
                    event.setCanceled(true);
                    dropPlayerInventory(target);
                    target.setGameMode(GameType.SPECTATOR);

                }
            }

            if(pm.getPowerOfPlayer(target.getUUID()).equals(PowerManager.YELLOW_POWER)) {

                if (isWearingCurioNecklaceItem(attacker, SelectivepowersItems.MOON_PENDANT.get())) {
                    pm.yellowLives--;
                    pm.setDirty();
                    target.displayClientMessage(Component.translatable("selectivepowers.messages.yellow_weakening", pm.yellowLives),false);
                    if(pm.yellowLives<=0)
                    {
                        event.setCanceled(true);
                        dropPlayerInventory(target);
                        target.setGameMode(GameType.SPECTATOR);
                        for(Player p:sl.getServer().getPlayerList().getPlayers()){
                            p.sendSystemMessage(Component.translatable("selectivepowers.messages.yellow_killed"));
                        }
                    }
                }
            }
        }
    }

    public static void dropPlayerInventory(Player player) {
        Level level = player.level();

        for (int i = 0; i < player.getInventory().items.size(); i++) {
            ItemStack stack = player.getInventory().items.get(i);
            if (!stack.isEmpty()) {
                spawnItem(level, player, stack);
                player.getInventory().items.set(i, ItemStack.EMPTY);
            }
        }

        for (int i = 0; i < player.getInventory().armor.size(); i++) {
            ItemStack armor = player.getInventory().armor.get(i);
            if (!armor.isEmpty()) {
                spawnItem(level, player, armor);
                player.getInventory().armor.set(i, ItemStack.EMPTY);
            }
        }

        ItemStack offhand = player.getInventory().offhand.get(0);
        if (!offhand.isEmpty()) {
            spawnItem(level, player, offhand);
            player.getInventory().offhand.set(0, ItemStack.EMPTY);
        }
    }

    private static void spawnItem(Level level, Player player, ItemStack stack) {
        if (!level.isClientSide) {
            ItemEntity item = new ItemEntity(
                    level,
                    player.getX(),
                    player.getY() + 0.5,
                    player.getZ(),
                    stack
            );
            item.setPickUpDelay(40);
            level.addFreshEntity(item);
        }
    }


    public static boolean isWearingCurioNecklaceItem(Player player, Item expectedItem) {
        return CuriosApi.getCuriosInventory(player)
                .flatMap(curiosInventory -> curiosInventory.getStacksHandler("necklace"))
                .map(handler -> {
                    for (int slot = 0; slot < handler.getSlots(); slot++) {
                        ItemStack stack = handler.getStacks().getStackInSlot(slot);
                        if (!stack.isEmpty() && stack.is(expectedItem)) {
                            return true;
                        }
                    }
                    return false;
                })
                .orElse(false);
    }



    public static final ResourceLocation POWER_SPEED_MODIFIER_RESOURCE = ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "storm_power_speed_modifier");
    public static final AttributeModifier POWER_SPEED_MODIFIER = new AttributeModifier(
            POWER_SPEED_MODIFIER_RESOURCE,
            0.3,
            AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
    );

    @SubscribeEvent
    public static void onPlayerSpeedTick(PlayerTickEvent.Pre e)
    {
        Player p = e.getEntity();
        if(p.level() instanceof ServerLevel sl)
        {
            PowerManager pm = PowerManager.get(sl);
            if(pm.getPowerOfPlayer(p.getUUID()).equals(PowerManager.STORM_POWER)){

                    var attribute = p.getAttribute(Attributes.MOVEMENT_SPEED);
                    if (attribute != null && attribute.getModifier(POWER_SPEED_MODIFIER_RESOURCE) == null) {
                        attribute.addTransientModifier(POWER_SPEED_MODIFIER);
                    }
                    pm.setDirty();
                    return;
            }
            var attribute = p.getAttribute(Attributes.MOVEMENT_SPEED);
            if (attribute != null) {
                attribute.removeModifier(POWER_SPEED_MODIFIER_RESOURCE);
            }
        }
    }

    public static final ResourceLocation POWER_MINING_MODIFIER_RESOURCE = ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "rock_power_mining_modifier");
    public static final AttributeModifier POWER_MINING_MODIFIER = new AttributeModifier(
            POWER_MINING_MODIFIER_RESOURCE,
            0.5,
            AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
    );

    @SubscribeEvent
    public static void onPlayerMineTick(PlayerTickEvent.Pre e)
    {
        Player p = e.getEntity();
        if(p.level() instanceof ServerLevel sl)
        {
            PowerManager pm = PowerManager.get(sl);
            if(pm.getPowerOfPlayer(p.getUUID()).equals(PowerManager.ROCK_POWER)){

                var attribute = p.getAttribute(Attributes.MINING_EFFICIENCY);
                if (attribute != null && attribute.getModifier(POWER_MINING_MODIFIER_RESOURCE) == null) {
                    attribute.addTransientModifier(POWER_MINING_MODIFIER);
                }
                pm.setDirty();
                return;
            }
            var attribute = p.getAttribute(Attributes.MINING_EFFICIENCY);
            if (attribute != null) {
                attribute.removeModifier(POWER_MINING_MODIFIER_RESOURCE);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerJump(LivingEvent.LivingJumpEvent event) {
        if (event.getEntity() instanceof Player player) {
            if(player.level() instanceof ServerLevel sl) {
                PowerManager pm = PowerManager.get(sl);
                if (pm.getPowerOfPlayer(player.getUUID()).equals(PowerManager.DRAGON_POWER)) {
                    player.setDeltaMovement(player.getDeltaMovement().x, player.getDeltaMovement().y * 1.5, player.getDeltaMovement().z);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTickRemoveRagePotions(PlayerTickEvent.Pre event) {
        Player player = event.getEntity();

        if (player.level().isClientSide()) return;

        if(!(player.level() instanceof ServerLevel sl))return;
        PowerManager pm = PowerManager.get(sl);
        if(!(pm.getPowerOfPlayer(player.getUUID()).equals(PowerManager.RAGE_POWER)))return;

        PowerManager.PlayerProgress progress = pm.getProgress(player.getUUID());
        if(progress.abilityTimer>0) {
            progress.abilityTimer--;
            for (MobEffectInstance effect : player.getActiveEffects()) {
                if (((MobEffectAccessor) effect.getEffect().value()).getCategory().equals(MobEffectCategory.HARMFUL)) {
                    player.removeEffect(effect.getEffect());
                }
            }
            pm.setDirty();
        }
    }

    @SubscribeEvent
    public static void onParalyzedRightClickItem(PlayerInteractEvent.RightClickItem event) {
        if (isParalyzed(event.getEntity())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onParalyzedRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (isParalyzed(event.getEntity())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onParalyzedLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        if (isParalyzed(event.getEntity())) {
            event.setCanceled(true);
        }
    }

    private static boolean isParalyzed(Player player) {
        return player.hasEffect(SelectivepowersEffects.PARALYZE_EFFECT);
    }
    @SubscribeEvent
    public static void onPlayerEffectAdded(MobEffectEvent.Added event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!(player.level() instanceof ServerLevel level)) return;

        if(!(player.level() instanceof ServerLevel sl))return;
        PowerManager pm = PowerManager.get(sl);
        if(!(pm.getPowerOfPlayer(player.getUUID()).equals(PowerManager.ANIMAL_POWER)))return;

        MobEffectInstance effectInstance = event.getEffectInstance();
        if (!((MobEffectAccessor) effectInstance.getEffect().value()).getCategory().equals(MobEffectCategory.BENEFICIAL)) return;

        List<Entity> entities = level.getEntities(player, player.getBoundingBox().inflate(32));

        for (Entity entity : entities) {
            if (!(entity instanceof TamableAnimal tameable)) continue;
            if (!tameable.isOwnedBy(player)) continue;

            MobEffectInstance copy = new MobEffectInstance(
                    effectInstance.getEffect(),
                    effectInstance.getDuration(),
                    effectInstance.getAmplifier(),
                    effectInstance.isAmbient(),
                    effectInstance.isVisible()
            );

            tameable.addEffect(copy);
        }
    }

    @SubscribeEvent
    public static void giveNearbyPlayersDarkness(PlayerTickEvent.Pre event) {
        Player centerPlayer = event.getEntity();

        if(!(centerPlayer.level() instanceof ServerLevel sl))return;
        PowerManager pm = PowerManager.get(sl);
        if(!(pm.getPowerOfPlayer(centerPlayer.getUUID()).equals(PowerManager.DARK_POWER)))return;
        PowerManager.PlayerProgress progress = pm.getProgress(centerPlayer.getUUID());
        if(progress.ultTimer>0) {

            AABB box = centerPlayer.getBoundingBox().inflate(20);

            List<Player> nearbyPlayers = sl.getEntitiesOfClass(Player.class, box, p -> p != centerPlayer);

            for (Player p : nearbyPlayers) {
                p.addEffect(new MobEffectInstance(SelectivepowersEffects.DRAKNESS_EFFECT, 2, 0, true, false));
            }
            progress.ultTimer--;
            pm.setDirty();
        }
    }

    @SubscribeEvent
    public static void downtickElementalWeapons(PlayerTickEvent.Pre event) {
        Player centerPlayer = event.getEntity();

        if (!(centerPlayer.level() instanceof ServerLevel level)) return;

        if(!(centerPlayer.level() instanceof ServerLevel sl))return;
        PowerManager pm = PowerManager.get(sl);
        if(!(pm.getPowerOfPlayer(centerPlayer.getUUID()).equals(PowerManager.ELEMENTAL_POWER)))return;
        PowerManager.PlayerProgress progress = pm.getProgress(centerPlayer.getUUID());
        if(progress.abilityTimer>0) {
            progress.abilityTimer--;
            pm.setDirty();
        }
    }

    @SubscribeEvent
    public static void downtickDarknessWeapons(PlayerTickEvent.Pre event) {
        Player centerPlayer = event.getEntity();

        if (!(centerPlayer.level() instanceof ServerLevel level)) return;

        if(!(centerPlayer.level() instanceof ServerLevel sl))return;
        PowerManager pm = PowerManager.get(sl);
        if(!(pm.getPowerOfPlayer(centerPlayer.getUUID()).equals(PowerManager.DARK_POWER)))return;
        PowerManager.PlayerProgress progress = pm.getProgress(centerPlayer.getUUID());
        if(progress.abilityTimer>0) {
            progress.abilityTimer--;
            pm.setDirty();
        }
    }

    @SubscribeEvent
    public static void onTreeGrowthTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (!(player.level() instanceof ServerLevel level)) return;
        PowerManager pm = PowerManager.get(level);
        if(!(pm.getPowerOfPlayer(player.getUUID()).equals(PowerManager.FORREST_POWER)))return;
        PowerManager.PlayerProgress progress = pm.getProgress(player.getUUID());
        if(progress.ultTimer>0) {

            progress.ultTimer--;
            pm.setDirty();
            if (level.getGameTime() % 10 != 0) return;

            BlockPos center = player.blockPosition();
            RandomSource random = level.random;

            int trees = 1 + random.nextInt(3); // 1–3 trees

            for (int i = 0; i < trees; i++) {
                int dx = random.nextInt(32) - 16;
                int dz = random.nextInt(32) - 16;

                BlockPos pos = findValidTreePos(level, center.offset(dx, 0, dz));
                if (pos == null) continue;
                if (Math.abs(pos.getY() - player.getY()) > 16) continue;

                placeRandomTree(level, pos, random);
            }
        }


    }
    @Nullable
    private static BlockPos findValidTreePos(ServerLevel level, BlockPos start) {
        BlockPos pos = level.getHeightmapPos(
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                start
        );

        Block ground = level.getBlockState(pos.below()).getBlock();
        if (!ground.defaultBlockState().isSolidRender(level, pos.below())) return null;

        if (!level.isEmptyBlock(pos)) return null;

        return pos;
    }

    private static final ResourceKey<ConfiguredFeature<?, ?>>[] RANDOM_TREES = new ResourceKey[] {
            TreeFeatures.OAK,
            TreeFeatures.BIRCH,
            TreeFeatures.SPRUCE,
            TreeFeatures.PINE,
            TreeFeatures.ACACIA,
            TreeFeatures.DARK_OAK,
            TreeFeatures.JUNGLE_TREE,
            TreeFeatures.FANCY_OAK,
            TreeFeatures.AZALEA_TREE,
            TreeFeatures.CHERRY,
            TreeFeatures.MANGROVE,
            TreeFeatures.SWAMP_OAK
    };

    private static void placeRandomTree(ServerLevel level, BlockPos pos, RandomSource random) {
        ResourceKey<ConfiguredFeature<?, ?>> key =
                RANDOM_TREES[random.nextInt(RANDOM_TREES.length)];

        Holder<ConfiguredFeature<?, ?>> feature =
                level.registryAccess()
                        .registryOrThrow(Registries.CONFIGURED_FEATURE)
                        .getHolder(key)
                        .orElse(null);

        if (feature == null) return;

        feature.value().place(
                level,
                level.getChunkSource().getGenerator(),
                random,
                pos
        );
    }
    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (!(player.level() instanceof ServerLevel level)) return;

        PowerManager pm = PowerManager.get(level);
        if(!(pm.getPowerOfPlayer(player.getUUID()).equals(PowerManager.LIGHT_POWER)))return;
        PowerManager.PlayerProgress progress = pm.getProgress(player.getUUID());
        if(progress.ultTimer>0) {

            progress.ultTimer--;
            pm.setDirty();
            Entity follower = getOrSpawnFollower(level, player);
            if (follower == null) return;

            moveFollower(player, follower);
        }
    }

    @Nullable
    private static Entity getOrSpawnFollower(
            ServerLevel level,
            Player player) {
        CelestialBeamManager cbm = CelestialBeamManager.get(level);
        if (cbm.beacon != null) {
            Entity existing = level.getEntity(cbm.beacon);
            if (existing != null && existing.isAlive()) {
                return existing;
            }
            cbm.beacon = null;
        }

        BlockPos eye = PowerActiveUse.getLookTarget(player, 64);
        MagicCircleEntity circle =
                new MagicCircleEntity(SelectivepowersEntities.MAGIC_CIRCLE.get(), level, CircleVariant.CELESTIAL, 20, 290, 4);
        if (circle == null) return null;
        circle.moveTo(eye.getBottomCenter());
        level.addFreshEntity(circle);

        cbm.beacon = circle.getUUID();
        cbm.setDirty();

        return circle;
    }
    private static void moveFollower(Player player, Entity entity) {
        BlockPos target = PowerActiveUse.getLookTarget(player, 64);

        entity.setPos(
                target.getX(),
                target.getY(),
                target.getZ()
        );
    }



}
