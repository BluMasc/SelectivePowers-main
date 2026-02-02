package net.blumasc.selectivepowers.events;

import it.unimi.dsi.fastutil.floats.Float2FloatFunction;
import it.unimi.dsi.fastutil.floats.Float2FloatFunctions;
import net.blumasc.selectivepowers.Config;
import net.blumasc.selectivepowers.PowerManager;
import net.blumasc.selectivepowers.SelectivePowers;
import net.blumasc.selectivepowers.block.SelectivepowersBlocks;
import net.blumasc.selectivepowers.block.entity.TrapBlockEntity;
import net.blumasc.selectivepowers.damage.SelectivePowersDamageTypes;
import net.blumasc.selectivepowers.effect.SelectivepowersEffects;
import net.blumasc.selectivepowers.enchantment.ModEnchantments;
import net.blumasc.selectivepowers.entity.CircleVariant;
import net.blumasc.selectivepowers.entity.SelectivepowersEntities;
import net.blumasc.selectivepowers.entity.custom.projectile.MagicCircleEntity;
import net.blumasc.selectivepowers.entity.custom.projectile.PickaxeBoomerangEntity;
import net.blumasc.selectivepowers.entity.custom.projectile.ShardProjectileEntity;
import net.blumasc.selectivepowers.entity.custom.projectile.SpikeEntity;
import net.blumasc.selectivepowers.item.custom.FrostShieldItem;
import net.blumasc.selectivepowers.network.DiviningDataSyncPacket;
import net.blumasc.selectivepowers.network.ModNetworking;
import net.blumasc.selectivepowers.network.PowerInfoSyncPacket;
import net.blumasc.selectivepowers.sound.SelectivepowersSounds;
import net.blumasc.selectivepowers.state.PlayerEnchantmentState;
import net.blumasc.selectivepowers.state.PlayerEnchantmentStateHandler;
import net.blumasc.selectivepowers.util.ModTags;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.entity.projectile.windcharge.AbstractWindCharge;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import net.minecraft.world.phys.*;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.event.entity.EntityTeleportEvent;
import net.neoforged.neoforge.event.entity.ProjectileImpactEvent;
import net.neoforged.neoforge.event.entity.living.*;
import net.neoforged.neoforge.event.entity.player.PlayerContainerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerDestroyItemEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockDropsEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleType;
import virtuoel.pehkui.api.ScaleTypes;

import java.awt.event.ItemEvent;
import java.util.*;

@EventBusSubscriber(modid = SelectivePowers.MODID)
public class EnchantmentEventHandlers {

    @SubscribeEvent
    public static void onShrinkPlayerTick(PlayerTickEvent.Pre event) {

        Player player = event.getEntity();

        if (player.level().isClientSide) return;

        Holder<Enchantment> shrinkHolder = player.level().registryAccess()
                .registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(ModEnchantments.SHRINK);

        ItemStack boots = player.getItemBySlot(EquipmentSlot.FEET);
        int level = boots.getEnchantmentLevel(shrinkHolder);
        ScaleData scaleData = ScaleTypes.BASE.getScaleData(player);

        if (level > 0 && player.isCrouching()) {
            float target = (float) Math.pow(0.5, level);
            scaleData.setScale(target);
        } else {
            scaleData.setScale(1.0F);
        }
    }

    @SubscribeEvent
    public static void onDivinerPlayerTick(PlayerTickEvent.Pre event) {

        Player player = event.getEntity();
        if (player.tickCount % 10 != 0) return;

        if (player.level().isClientSide) return;

        Holder<Enchantment> shrinkHolder = player.level().registryAccess()
                .registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(ModEnchantments.DIVINER);

        ItemStack boots = player.getItemBySlot(EquipmentSlot.HEAD);
        int level = boots.getEnchantmentLevel(shrinkHolder);

        if (level <= 0) return;
        double radius = 10 + (5 * level);
        AABB area = new AABB(
                player.getX() - radius, player.getY() - radius, player.getZ() - radius,
                player.getX() + radius, player.getY() + radius, player.getZ() + radius
        );

        List<Vec3> mobPositions = new ArrayList<>();
        List<Vec3> playerPositions = new ArrayList<>();

        for (LivingEntity entity : player.level().getEntitiesOfClass(LivingEntity.class, area)) {
            if (entity == player) continue; // skip self

            Vec3 pos = entity.position();
            if (entity instanceof Player && level >= 3) {
                playerPositions.add(pos);
            } else if (entity instanceof Mob mob && mob.getTarget() == player) {
                mobPositions.add(pos);
            }
        }

        if (player instanceof ServerPlayer serverPlayer) {
            PacketDistributor.sendToPlayer(
                    serverPlayer,
                    new DiviningDataSyncPacket(mobPositions, playerPositions)
            );
        }
    }

    @SubscribeEvent
    public static void onBulletTimeTick(PlayerTickEvent.Pre event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (player.level().isClientSide()) return;
        if(player.onGround())return;

        ItemStack item = player.getMainHandItem();

        Holder<Enchantment> bulletTimeHolder = player.level().registryAccess()
                .registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(ModEnchantments.BULLET_TIME);

        int level = item.getEnchantmentLevel(bulletTimeHolder);
        if (level <= 0) return;

        player.addEffect(new MobEffectInstance(
                MobEffects.SLOW_FALLING,
                2,
                level - 1,
                true,
                false
        ));
    }

    @SubscribeEvent
    public static void onHungryPlayerTick(PlayerTickEvent.Pre event) {

        Player player = event.getEntity();

        //if (player.level().isClientSide) return;
        if (player.tickCount % 20 != 0) return;

        Holder<Enchantment> hungryHolder = player.level().registryAccess()
                .registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(ModEnchantments.HUNGRY);

        ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
        int level = helmet.getEnchantmentLevel(hungryHolder);
        if (level > 0 && player.getFoodData().needsFood()) {
            PlayerEnchantmentState pes = PlayerEnchantmentStateHandler.loadState(player);
            if (!pes.isOnCooldown(ModEnchantments.HUNGRY.location(), player.level().getGameTime())) {
                ItemStack food = selectFood(player);
                if (food.isEmpty()) return;

                fakeEat(player, food);
                pes.setCooldown(ModEnchantments.HUNGRY.location(), player.level().getGameTime() + 5 * 20);
                PlayerEnchantmentStateHandler.saveState(player, pes);
            }
        }
    }

    private static void fakeEat(Player player, ItemStack foodStack) {
        FoodProperties food = foodStack.getFoodProperties(player);
        if (food == null) return;

        player.getFoodData().eat(food.nutrition(), food.saturation());

        player.level().playSound(
                null,
                player.getX(),
                player.getEyeY(),
                player.getZ(),
                SoundEvents.GENERIC_EAT,
                SoundSource.PLAYERS,
                0.8F,
                0.9F + player.level().random.nextFloat() * 0.2F
        );
        ItemParticleOption particle =
                new ItemParticleOption(ParticleTypes.ITEM, foodStack);

        if (player.level() instanceof ServerLevel sl) {

            sl.sendParticles(
                    particle,
                    player.getX(),
                    player.getEyeY() ,
                    player.getZ(),
                    20,
                    0.25, 0.25, 0.25,
                    0.05
            );
        }
        ItemStack result = foodStack.finishUsingItem(player.level(), player);
        foodStack.shrink(1);
        if (!result.isEmpty() && result != foodStack) {
            if (!player.getInventory().add(result)) {
                player.drop(result, false);
            }
        }
    }

    private static ItemStack selectFood(Player player) {
        if (isValidFood(player.getMainHandItem(), player))
            return player.getMainHandItem();

        if (isValidFood(player.getOffhandItem(), player))
            return player.getOffhandItem();

        return findBestFood(player);
    }

    private static ItemStack findBestFood(Player player) {
        int missing = 20 - player.getFoodData().getFoodLevel();

        ItemStack best = ItemStack.EMPTY;
        int bestDiff = Integer.MAX_VALUE;

        for (ItemStack stack : player.getInventory().items) {
            if (!isValidFood(stack, player)) continue;

            int foodValue = stack.getFoodProperties(player).nutrition();
            int diff = Math.abs(foodValue - missing);

            if (diff < bestDiff) {
                bestDiff = diff;
                best = stack;
            }
        }

        return best;
    }

    private static boolean isValidFood(ItemStack stack, Player player) {
        if (stack.isEmpty()) return false;
        FoodProperties food = stack.getFoodProperties(player);
        if (food == null) return false;
        return food.effects().isEmpty();
    }

    @SubscribeEvent
    public static void onSquidHit(LivingDamageEvent.Post event) {
        LivingEntity entity = event.getEntity();
        if (entity.level().isClientSide()) return;
        if (entity.getLastDamageSource() == null) return;
        Entity causer = event.getEntity().getLastDamageSource().getEntity();
        if (causer instanceof LivingEntity livingcauser) {
            if (entity.distanceToSqr(livingcauser) > 5 * 5) return;
            Holder<Enchantment> squidHolder = entity.level().registryAccess()
                    .registryOrThrow(Registries.ENCHANTMENT)
                    .getHolderOrThrow(ModEnchantments.SQUID_ENCOUNTER);

            ItemStack chest = entity.getItemBySlot(EquipmentSlot.CHEST);
            int level = chest.getEnchantmentLevel(squidHolder);
            if (level > 0) {
                PlayerEnchantmentState pes = PlayerEnchantmentStateHandler.loadState(entity);
                if (!pes.isOnCooldown(ModEnchantments.SQUID_ENCOUNTER.location(), entity.level().getGameTime())) {
                    livingcauser.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 15 * 20));
                    entity.level().playSound(
                            null,
                            entity.getX(), entity.getY(), entity.getZ(),
                            SoundEvents.SQUID_HURT,
                            SoundSource.PLAYERS,
                            1.0f,
                            1.0f
                    );
                    if (livingcauser.level() instanceof ServerLevel world) {
                        double x = livingcauser.getX();
                        double y = livingcauser.getY() + livingcauser.getBbHeight() / 2.0;
                        double z = livingcauser.getZ();
                        RandomSource random = world.getRandom();

                        for (int i = 0; i < 20; i++) {
                            world.sendParticles(
                                    ParticleTypes.SQUID_INK,
                                    x + (random.nextDouble() - 0.5),
                                    y + (random.nextDouble() - 0.5),
                                    z + (random.nextDouble() - 0.5),
                                    1,
                                    0.0, 0.0, 0.0,
                                    0.05
                            );
                        }
                    }
                    pes.setCooldown(ModEnchantments.SQUID_ENCOUNTER.location(), entity.level().getGameTime() + (20 * 60 * (6 - level)));
                    PlayerEnchantmentStateHandler.saveState(entity, pes);
                }
            }
        }

    }

    @SubscribeEvent
    public static void onSneakAttack(LivingIncomingDamageEvent event) {
        LivingEntity target = event.getEntity();
        if (target.level().isClientSide()) return;

        Entity causer = event.getSource().getEntity();
        if (!(causer instanceof LivingEntity attacker)) return;

        Holder<Enchantment> sneakAttackHolder = target.level().registryAccess()
                .registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(ModEnchantments.SNEAK_ATTACK);

        ItemStack weapon = attacker.getMainHandItem();
        int level = weapon.getEnchantmentLevel(sneakAttackHolder);
        if (level <= 0) return;

        if (attacker.getHealth() < attacker.getMaxHealth()) return;
        if (target.getHealth() < target.getMaxHealth()) return;

        float extraDamage = 2.0f * level;
        event.setAmount(event.getAmount() + extraDamage);

        if (target.level() instanceof ServerLevel world) {
            double x = target.getX();
            double y = target.getY();
            double z = target.getZ();
            RandomSource random = world.getRandom();

            for (int i = 0; i < 10; i++) {
                world.sendParticles(
                        ParticleTypes.ASH,
                        x + (random.nextDouble() - 0.5),
                        y + (random.nextDouble()*target.getBbHeight()),
                        z + (random.nextDouble() - 0.5),
                        1,
                        0, 0, 0,
                        0.05
                );
            }
        }
    }

    @SubscribeEvent
    public static void onCleaveHit(LivingShieldBlockEvent event) {
        LivingEntity target = event.getEntity();
        if (target.level().isClientSide()) return;

        Entity causer = event.getDamageSource().getEntity();
        if (!(causer instanceof LivingEntity attacker)) return;

        Holder<Enchantment> cleaveHolder = target.level().registryAccess()
                .registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(ModEnchantments.BREACHING);

        ItemStack weapon = attacker.getMainHandItem();
        int level = weapon.getEnchantmentLevel(cleaveHolder);
        if (level <= 0) return;

        ItemStack shield = target.getUseItem();
        if (!target.isBlocking()) return;

        int disableTicks = 20 * 20 * level;
        if (target instanceof Player player) {
            player.getCooldowns().addCooldown(shield.getItem(), disableTicks);
        }
    }
    @SubscribeEvent
    public static void onSoulRetrieverKill(LivingDeathEvent event) {
        LivingEntity deadEntity = event.getEntity();
        if (deadEntity.level().isClientSide()) return;

        DamageSource source = event.getSource();

        Entity killerEntity = source.getEntity();
        if (!(killerEntity instanceof LivingEntity attacker)) return;

        if (!(attacker instanceof Player player)) return;
        Holder<Enchantment> soulRetrieverHolder = deadEntity.level().registryAccess()
                .registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(ModEnchantments.SOUL_RETRIEVER);

        ItemStack weapon = attacker.getMainHandItem();
        int level = weapon.getEnchantmentLevel(soulRetrieverHolder);
        if (level <= 0) return;

        float lastDamage = deadEntity.getMaxHealth()/10;
        int amplifier = (int) lastDamage - 1;
        if (amplifier < 0) amplifier = 0;
        if(amplifier>10) amplifier = 10;

        attacker.addEffect(new MobEffectInstance(
                MobEffects.ABSORPTION,
                200,
                amplifier,
                false,
                true
        ));
    }

    @SubscribeEvent
    public static void onReapingHit(LivingDamageEvent.Post event) {
        LivingEntity target = event.getEntity();
        if (target.level().isClientSide()) return;

        Entity causer = event.getSource().getEntity();
        if (!(causer instanceof LivingEntity attacker)) return;

        Holder<Enchantment> reapingHolder = target.level().registryAccess()
                .registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(ModEnchantments.REAPING);

        ItemStack weapon = attacker.getMainHandItem();
        int level = weapon.getEnchantmentLevel(reapingHolder);
        if (level <= 0) return;

        List<MobEffectInstance> eligibleEffects = target.getActiveEffects().stream()
                .filter(MobEffectInstance::showIcon)
                .toList();

        if (eligibleEffects.isEmpty()) return;

        RandomSource random = target.level().getRandom();
        MobEffectInstance effect = eligibleEffects.get(random.nextInt(eligibleEffects.size()));

        int stolenDuration = (int) (effect.getDuration() * 0.1f * level);
        if (stolenDuration <= 0) return;

        attacker.addEffect(new MobEffectInstance(
                effect.getEffect(),
                stolenDuration,
                1,
                false,
                true
        ));
    }



    @SubscribeEvent
    public static void onFrenzyHit(LivingIncomingDamageEvent event) {
        LivingEntity target = event.getEntity();
        if (target == null) return;
        if (target.level().isClientSide()) return;
        if (event.getSource() == null) return;

        Entity causer = event.getSource().getEntity();
        if (!(causer instanceof LivingEntity attacker)) return;

        Holder<Enchantment> frenzyHolder = target.level().registryAccess()
                .registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(ModEnchantments.FRENZY);

        ItemStack weapon = attacker.getMainHandItem();
        int level = weapon.getEnchantmentLevel(frenzyHolder);
        if (level <= 0) return;

        PlayerEnchantmentState pes = PlayerEnchantmentStateHandler.loadState(attacker);
        if (pes.getFrenzyLastAttackedEntity() != null  && pes.getFrenzyLastAttackedEntity().equals(target.getUUID())) {
            float multiplier = 1.0f + 0.15f * level;
            event.setAmount(event.getAmount() * multiplier);
        }

        pes.setFrenzyLastAttackedEntity(target.getUUID());
        PlayerEnchantmentStateHandler.saveState(attacker, pes);
    }

    @SubscribeEvent
    public static void onLifeLeachPost(LivingDamageEvent.Post event) {
        LivingEntity target = event.getEntity();
        if (target.level().isClientSide()) return;

        Entity causer = event.getSource().getEntity();
        if (!(causer instanceof LivingEntity attacker)) return;

        Holder<Enchantment> lifeLeachHolder = target.level().registryAccess()
                .registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(ModEnchantments.LIFE_LEACH);

        ItemStack weapon = attacker.getMainHandItem();
        int level = weapon.getEnchantmentLevel(lifeLeachHolder);
        if (level <= 0) return;

        float healPercent = 0.2f + 0.1f * level;
        float healAmount = event.getNewDamage() * healPercent;

        attacker.heal(healAmount);
    }

    @SubscribeEvent
    public static void onDequipingHit(LivingIncomingDamageEvent event) {
        LivingEntity target = event.getEntity();
        if (target.level().isClientSide()) return;

        Entity causer = event.getSource().getEntity();
        if (!(causer instanceof LivingEntity attacker)) return;

        Holder<Enchantment> dequipingHolder = target.level().registryAccess()
                .registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(ModEnchantments.DEQUIPING);

        ItemStack weapon = attacker.getMainHandItem();
        int level = weapon.getEnchantmentLevel(dequipingHolder);
        if (level <= 0) return;

        event.setAmount(1.0f);

        float chance = 0.10f + 0.05f * level;
        RandomSource random = target.level().getRandom();
        if (random.nextFloat() > chance) return;

        List<EquipmentSlot> armorSlots = List.of(
                EquipmentSlot.HEAD,
                EquipmentSlot.CHEST,
                EquipmentSlot.LEGS,
                EquipmentSlot.FEET
        );

        EquipmentSlot slot = armorSlots.get(random.nextInt(armorSlots.size()));
        ItemStack armor = target.getItemBySlot(slot);
        if (armor.isEmpty()) return;

        Holder<Enchantment> bindingHolder = target.level().registryAccess()
                .registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(Enchantments.BINDING_CURSE);
        if (armor.getEnchantmentLevel(bindingHolder)>0) return;
        target.setItemSlot(slot, ItemStack.EMPTY);
        if (!target.level().isClientSide()) {
            ItemEntity dropped = new ItemEntity(
                    target.level(),
                    target.getX(), target.getY(), target.getZ(),
                    armor
            );
            target.level().addFreshEntity(dropped);
        }
    }

    @SubscribeEvent
    public static void onForkPost(LivingDamageEvent.Post event) {
        LivingEntity target = event.getEntity();
        if (target.level().isClientSide()) return;

        Entity causer = event.getSource().getEntity();
        if (!(causer instanceof Player player)) return;

        ItemStack weapon = player.getMainHandItem();

        Holder<Enchantment> forkHolder = player.level().registryAccess()
                .registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(ModEnchantments.FORK);

        int level = weapon.getEnchantmentLevel(forkHolder);
        if (level <= 0) return;

        float damage = event.getNewDamage();
        int hungerGain = Math.max(1, (int) (damage * 0.5f));
        float saturationGain = hungerGain * 0.3f;

        player.getFoodData().eat(hungerGain, saturationGain);
    }



    @SubscribeEvent
    public static void onGravityImplosion(LivingDamageEvent.Post event) {
        LivingEntity target = event.getEntity();
        if (target.level().isClientSide()) return;

        Entity causer = event.getSource().getEntity();
        if (!(causer instanceof LivingEntity attacker)) return;

        if (!(attacker instanceof Player player)) return;
        ItemStack weapon = player.getMainHandItem();

        if (player.fallDistance < 1.5f) return;
        if (player.onGround()) return;
        if (player.isFallFlying()) return;

        Holder<Enchantment> gravityHolder = attacker.level().registryAccess()
                .registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(ModEnchantments.GRAVITY_IMPLOSION);

        int level = weapon.getEnchantmentLevel(gravityHolder);
        if (level <= 0) return;

        double radius = 3.0 + level;

        List<LivingEntity> entities = attacker.level().getEntitiesOfClass(
                LivingEntity.class,
                new AABB(
                        attacker.getX() - radius, attacker.getY() - radius, attacker.getZ() - radius,
                        attacker.getX() + radius, attacker.getY() + radius, attacker.getZ() + radius
                ),
                e -> e != attacker && e != target
        );

        for (LivingEntity mob : entities) {
            if (mob instanceof Player p && p.isShiftKeyDown()) continue;

            double dx = attacker.getX() - mob.getX();
            double dy = attacker.getY() - mob.getY();
            double dz = attacker.getZ() - mob.getZ();

            double distance = Math.sqrt(dx*dx + dy*dy + dz*dz);
            if (distance < 0.001) continue;

            double pullStrength = 1.3;
            mob.setDeltaMovement(
                    mob.getDeltaMovement().add(
                            (dx / distance) * pullStrength,
                            (dy / distance) * pullStrength,
                            (dz / distance) * pullStrength
                    )
            );

            mob.hasImpulse = true;
        }
    }







    @SubscribeEvent
    public static void projectilePocketHit(ProjectileImpactEvent event) {
        HitResult result = event.getRayTraceResult();
        if (result.getType() == HitResult.Type.ENTITY) {
            Entity hitEntity = ((EntityHitResult) result).getEntity();
            if (hitEntity instanceof Player p) {
                Holder<Enchantment> pocketsHolder = p.level().registryAccess()
                        .registryOrThrow(Registries.ENCHANTMENT)
                        .getHolderOrThrow(ModEnchantments.COLLECTING_POCKETS);

                ItemStack legs = p.getItemBySlot(EquipmentSlot.LEGS);
                int level = legs.getEnchantmentLevel(pocketsHolder);
                if (p.getRandom().nextFloat() < (0.1 + (0.1 * level))) {
                    ItemStack out = null;
                    Projectile pr = event.getProjectile();
                    if (pr instanceof ThrowableItemProjectile tip) {
                        out = tip.getItem();
                    }
                    if (pr instanceof AbstractArrow aa) {
                        out = aa.getPickupItemStackOrigin();
                    }
                    if (pr instanceof Fireball) {
                        out = Items.FIRE_CHARGE.getDefaultInstance();
                    }
                    if (pr instanceof AbstractWindCharge) {
                        out = Items.WIND_CHARGE.getDefaultInstance();
                    }
                    if (out != null) {
                        p.addItem(out);
                    }

                }
            }
        }
    }

    @SubscribeEvent
    public static void arrowInfestedHit(ProjectileImpactEvent event) {
        Projectile pr = event.getProjectile();
        if (pr instanceof AbstractArrow aa) {
            ItemStack source = aa.getWeaponItem();
            if (source == null) return;
            Holder<Enchantment> infestedHolder = aa.level().registryAccess()
                    .registryOrThrow(Registries.ENCHANTMENT)
                    .getHolderOrThrow(ModEnchantments.INFESTED);
            int level = source.getEnchantmentLevel(infestedHolder);
            if (level > 0) {
                Vec3 spawnPos = null;
                LivingEntity target = null;

                if (event.getRayTraceResult().getType() == HitResult.Type.ENTITY) {
                    Entity hit = ((EntityHitResult) event.getRayTraceResult()).getEntity();
                    spawnPos = hit.position();
                    Entity hitEntity = ((EntityHitResult) event.getRayTraceResult()).getEntity();
                    if (hitEntity instanceof LivingEntity living) {
                        target = living;
                    }
                } else if (event.getRayTraceResult().getType() == HitResult.Type.BLOCK) {
                    BlockHitResult bhr = (BlockHitResult) event.getRayTraceResult();
                    spawnPos = Vec3.atCenterOf(bhr.getBlockPos().relative(bhr.getDirection()));
                }

                if (spawnPos == null) return;
                RandomSource random = aa.level().getRandom();
                String id = Config.MOB_STRINGS_LITTLE_BUGS.get().get(random.nextInt(Config.MOB_STRINGS_LITTLE_BUGS.get().size()));
                ResourceLocation rl = ResourceLocation.parse(id);
                Optional<EntityType<?>> typeOpt =
                        BuiltInRegistries.ENTITY_TYPE.getOptional(rl);

                if (typeOpt.isEmpty()) return;

                EntityType<?> type = typeOpt.get();
                Entity entity = type.create(aa.level());
                if (target != null && entity instanceof Mob mob) {
                    mob.setTarget(target);
                    mob.setLastHurtByMob(target);
                }
                if (entity != null) {
                    entity.moveTo(
                            spawnPos.x,
                            spawnPos.y,
                            spawnPos.z,
                            random.nextFloat() * 360F,
                            0
                    );
                    aa.level().addFreshEntity(entity);
                }
            }
        }
    }

    @SubscribeEvent
    public static void arrowSunrayHit(ProjectileImpactEvent event) {
        Projectile pr = event.getProjectile();
        if (pr instanceof AbstractArrow aa) {
            ItemStack source = aa.getWeaponItem();
            Holder<Enchantment> sunbeamHolder = aa.level().registryAccess()
                    .registryOrThrow(Registries.ENCHANTMENT)
                    .getHolderOrThrow(ModEnchantments.SUNRAY);
            if (source == null) return;
            int level = source.getEnchantmentLevel(sunbeamHolder);
            if (level > 0) {
                if (event.getRayTraceResult().getType() == HitResult.Type.BLOCK) {
                    BlockHitResult bhr = (BlockHitResult) event.getRayTraceResult();
                    Vec3 spawnPos = bhr.getBlockPos().above().getBottomCenter();
                    MagicCircleEntity circle =
                            new MagicCircleEntity(SelectivepowersEntities.MAGIC_CIRCLE.get(), aa.level(), CircleVariant.SUN, 40, 20, level);
                    circle.moveTo(spawnPos);
                    aa.level().addFreshEntity(circle);
                    aa.discard();
                }
            }
        }
    }

    @SubscribeEvent
    public static void arrowPinningHit(ProjectileImpactEvent event) {
        Projectile pr = event.getProjectile();
        if (pr instanceof AbstractArrow aa) {
            ItemStack source = aa.getWeaponItem();
            Holder<Enchantment> pinningHolder = aa.level().registryAccess()
                    .registryOrThrow(Registries.ENCHANTMENT)
                    .getHolderOrThrow(ModEnchantments.PINNING);
            if (source == null) return;
            int level = source.getEnchantmentLevel(pinningHolder);
            if (level > 0) {
                if (aa.getDeltaMovement().length() >= 2.0) {
                    if (event.getRayTraceResult().getType() == HitResult.Type.ENTITY) {
                        Entity hit = ((EntityHitResult) event.getRayTraceResult()).getEntity();
                        if (hit instanceof LivingEntity le) {
                            le.addEffect(new MobEffectInstance(SelectivepowersEffects.PINNED, 15 * level * 20));
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void arrowRopedHit(ProjectileImpactEvent event) {
        Projectile pr = event.getProjectile();
        if (pr instanceof AbstractArrow aa) {
            ItemStack source = aa.getWeaponItem();
            Holder<Enchantment> ropedHolder = aa.level().registryAccess()
                    .registryOrThrow(Registries.ENCHANTMENT)
                    .getHolderOrThrow(ModEnchantments.ROPED);
            if (source == null) return;
            int level = source.getEnchantmentLevel(ropedHolder);
            if (level > 0) {
                if (event.getRayTraceResult().getType() == HitResult.Type.ENTITY) {
                    Entity hit = ((EntityHitResult) event.getRayTraceResult()).getEntity();
                    if (aa.getOwner() != null) {
                        if (aa.getOwner().level() == hit.level()) {
                            Vec3 shooterPos = aa.getOwner().position();

                            Vec3 dir = shooterPos.subtract(hit.position()).normalize();
                            dir = new Vec3(dir.x, Mth.clamp(dir.y, -0.1, 0.1), dir.z);
                            Vec3 velocity = dir.scale(0.8 * level);

                            hit.setDeltaMovement(velocity);
                            hit.hasImpulse = true;
                            hit.level().playSound(null, hit.getX(), hit.getY(), hit.getZ(), SelectivepowersSounds.ROPE.get(), aa.getSoundSource());
                            aa.getOwner().level().playSound(null, aa.getOwner().getX(), aa.getOwner().getY(), aa.getOwner().getZ(), SelectivepowersSounds.ROPE.get(), aa.getOwner().getSoundSource());
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void arrowSharingHit(ProjectileImpactEvent event) {
        Projectile pr = event.getProjectile();
        if (pr instanceof AbstractArrow aa) {
            ItemStack source = aa.getWeaponItem();
            Holder<Enchantment> sharingHolder = aa.level().registryAccess()
                    .registryOrThrow(Registries.ENCHANTMENT)
                    .getHolderOrThrow(ModEnchantments.SHARING);
            if (source == null) return;
            int level = source.getEnchantmentLevel(sharingHolder);
            if (level > 0) {
                if (event.getRayTraceResult().getType() == HitResult.Type.ENTITY) {
                    Entity hit = ((EntityHitResult) event.getRayTraceResult()).getEntity();
                    if (hit instanceof LivingEntity hitLiving) {
                        if (aa.getOwner() != null) {
                            if (aa.getOwner() instanceof LivingEntity shooterLiving) {
                                Collection<MobEffectInstance> effects = shooterLiving.getActiveEffects();
                                if (!effects.isEmpty()) {
                                    MobEffectInstance chosen = effects.stream()
                                            .filter(MobEffectInstance::showIcon)
                                            .skip(shooterLiving.level().getRandom().nextInt(effects.size()))
                                            .findFirst()
                                            .orElse(null);

                                    if (chosen != null) {
                                        int duration = Math.min(20 * 5 * level, chosen.getDuration());
                                        MobEffectInstance newEffect = new MobEffectInstance(
                                                chosen.getEffect(),
                                                duration,
                                                0
                                        );

                                        hitLiving.addEffect(newEffect);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onReverberating(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        Level world = (Level) event.getLevel();
        BlockPos origin = event.getPos();
        ItemStack stack = player.getMainHandItem();

        Holder<Enchantment> reverberatingHolder = player.level().registryAccess()
                .registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(ModEnchantments.REVERBERATING);

        int enchantLevel = stack.getEnchantmentLevel(reverberatingHolder);
        if (enchantLevel <= 0) return;

        BlockState originalState = world.getBlockState(origin);

        if (!stack.isCorrectToolForDrops(originalState)) return;

        Queue<BlockPos> queue = new ArrayDeque<>();
        Set<BlockPos> visited = new HashSet<>();

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                for (int dz = -1; dz <= 1; dz++) {
                    if (dx == 0 && dy == 0 && dz == 0) continue;
                    BlockPos neighbor = origin.offset(dx, dy, dz);
                    queue.add(neighbor);
                    visited.add(neighbor);
                }
            }
        }

        int maxExtraBlocks = enchantLevel * 5;
        int brokenCount = 0;

        while (!queue.isEmpty() && brokenCount < maxExtraBlocks) {
            BlockPos pos = queue.poll();
            BlockState state = world.getBlockState(pos);

            if (state.getBlock() != originalState.getBlock()) continue;

            state.getBlock().playerDestroy(world, player, pos, state, world.getBlockEntity(pos), player.getMainHandItem());
            world.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            brokenCount++;


            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    for (int dz = -1; dz <= 1; dz++) {
                        BlockPos neighbor = pos.offset(dx, dy, dz);
                        if (!visited.contains(neighbor)) {
                            queue.add(neighbor);
                            visited.add(neighbor);
                        }
                    }
                }
            }
        }

    }

    @SubscribeEvent
    public static void onScorchBreak(BlockDropsEvent event) {
        Entity e = event.getBreaker();
        if (e == null) return;
        ItemStack stack = e.getWeaponItem();
        if (stack == null) return;
        if (e.level().isClientSide) return;

        Holder<Enchantment> scorchHolder = e.level().registryAccess()
                .registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(ModEnchantments.SCORCH);
        int enchantLevel = stack.getEnchantmentLevel(scorchHolder);
        if (enchantLevel <= 0) return;

        List<ItemEntity> newDrops = new ArrayList<>();
        for (ItemEntity dropEntity : event.getDrops()) {
            ItemStack drop = dropEntity.getItem();
            ItemStack smelted = getSmeltingResult(e.level(), drop);

            if (smelted != null && !smelted.isEmpty()) {
                ItemEntity newEntity = new ItemEntity(
                        e.level(),
                        dropEntity.getX(), dropEntity.getY(), dropEntity.getZ(),
                        smelted.copy() // make sure it’s a fresh copy
                );
                newDrops.add(newEntity);

                if (!e.level().isClientSide) {
                    ((ServerLevel) e.level()).sendParticles(ParticleTypes.FLAME,
                            dropEntity.getX() + 0.5, dropEntity.getY() + 0.5, dropEntity.getZ() + 0.5,
                            2, 0.25, 0.25, 0.25, 0.02);
                }
            } else {
                newDrops.add(dropEntity);
            }
        }

        event.getDrops().clear();
        event.getDrops().addAll(newDrops);
    }

    @SubscribeEvent
    public static void onScorchKill(LivingDropsEvent event) {
        DamageSource d = event.getSource();
        if (d == null) return;
        Entity e = d.getEntity();
        if (e == null) return;
        ItemStack stack = e.getWeaponItem();
        if (stack == null) return;
        if (e.level().isClientSide) return;

        Holder<Enchantment> scorchHolder = e.level().registryAccess()
                .registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(ModEnchantments.SCORCH);
        int enchantLevel = stack.getEnchantmentLevel(scorchHolder);
        if (enchantLevel <= 0) return;

        List<ItemEntity> newDrops = new ArrayList<>();
        for (ItemEntity dropEntity : event.getDrops()) {
            ItemStack drop = dropEntity.getItem();
            ItemStack smelted = getSmeltingResult(e.level(), drop);

            if (smelted != null && !smelted.isEmpty()) {
                ItemEntity newEntity = new ItemEntity(
                        e.level(),
                        dropEntity.getX(), dropEntity.getY(), dropEntity.getZ(),
                        smelted.copy() // fresh copy
                );
                newDrops.add(newEntity);

                if (!e.level().isClientSide) {
                    ((ServerLevel) e.level()).sendParticles(ParticleTypes.FLAME,
                            dropEntity.getX(), dropEntity.getY(), dropEntity.getZ(),
                            2, 0.25, 0.25, 0.25, 0.02);
                }
            } else {
                newDrops.add(dropEntity);
            }
        }

        event.getDrops().clear();
        event.getDrops().addAll(newDrops);
    }

    private static @Nullable ItemStack getSmeltingResult(Level l, ItemStack input) {
        RecipeManager rm = l.getRecipeManager();
        Optional<RecipeHolder<SmeltingRecipe>> recipe =
                rm.getRecipeFor(RecipeType.SMELTING, new SingleRecipeInput(input), l);
        if (recipe.isEmpty()) return null;

        return recipe.get().value().getResultItem(l.registryAccess());
    }

    @SubscribeEvent
    public static void onMagneticBreak(BlockDropsEvent event) {
        Entity e = event.getBreaker();
        if (e == null) return;
        if (!(e instanceof Player p)) return;
        ItemStack stack = e.getWeaponItem();
        if (stack == null) return;
        if (e.level().isClientSide) return;

        Holder<Enchantment> magneticHolder = e.level().registryAccess()
                .registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(ModEnchantments.MAGNETIC);
        int enchantLevel = stack.getEnchantmentLevel(magneticHolder);
        if (enchantLevel <= 0) return;

        List<ItemEntity> newDrops = new ArrayList<>();
        for (ItemEntity dropEntity : event.getDrops()) {
            ItemStack stackCopy = dropEntity.getItem().copy();
            if (!p.addItem(stackCopy)) {
                newDrops.add(dropEntity);
            } else {
                p.level().playSound((Player) null, p.getX(), p.getY(), p.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.7f, 1.0f);
            }
        }

        event.getDrops().clear();
        event.getDrops().addAll(newDrops);
    }

    @SubscribeEvent
    public static void onMagneticKill(LivingDropsEvent event) {
        DamageSource d = event.getSource();
        if (d == null) return;
        Entity e = d.getEntity();
        if (e == null) return;
        if (!(e instanceof Player p)) return;
        ItemStack stack = e.getWeaponItem();
        if (stack == null) return;
        if (e.level().isClientSide) return;

        Holder<Enchantment> magneticHolder = e.level().registryAccess()
                .registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(ModEnchantments.MAGNETIC);
        int enchantLevel = stack.getEnchantmentLevel(magneticHolder);
        if (enchantLevel <= 0) return;

        List<ItemEntity> newDrops = new ArrayList<>();
        for (ItemEntity dropEntity : event.getDrops()) {
            ItemStack stackCopy = dropEntity.getItem().copy();
            if (!p.addItem(stackCopy)) {
                newDrops.add(dropEntity);
            } else {
                p.level().playSound((Player) null, p.getX(), p.getY(), p.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.7f, 1.0f);
            }
        }

        event.getDrops().clear();
        event.getDrops().addAll(newDrops);
    }

    public static final ResourceLocation RUN_AWAY_MODIFIER_RESOURCE =
            ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "run_away_modifier");

    @SubscribeEvent
    public static void onPlayerRunAwayTick(PlayerTickEvent.Pre e) {
        Player p = e.getEntity();

        Holder<Enchantment> shrinkHolder = p.level().registryAccess()
                .registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(ModEnchantments.RUN_AWAY);

        ItemStack chest = p.getItemBySlot(EquipmentSlot.CHEST);
        int level = chest.getEnchantmentLevel(shrinkHolder);

        var attribute = p.getAttribute(Attributes.MOVEMENT_SPEED);
        if (attribute == null) return;

        if (attribute.getModifier(RUN_AWAY_MODIFIER_RESOURCE) != null) {
            attribute.removeModifier(RUN_AWAY_MODIFIER_RESOURCE);
        }

        if (level > 0) {
            double missingHealth = p.getMaxHealth() - p.getHealth();
            int scale = (int) (missingHealth / 6.0);

            double bonus = (0.05 + 0.05 * level) * scale;

            attribute.addTransientModifier(new AttributeModifier(
                    RUN_AWAY_MODIFIER_RESOURCE,
                    bonus,
                    AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
            ));
        }
    }

    public static final ResourceLocation LAST_STAND_MODIFIER_RESOURCE =
            ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "last_stand_modifier");

    @SubscribeEvent
    public static void onPlayerLastStandTick(PlayerTickEvent.Pre e) {
        Player p = e.getEntity();

        Holder<Enchantment> shrinkHolder = p.level().registryAccess()
                .registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(ModEnchantments.LAST_STAND);

        ItemStack chest = p.getItemBySlot(EquipmentSlot.CHEST);
        int level = chest.getEnchantmentLevel(shrinkHolder);

        var attribute = p.getAttribute(Attributes.MOVEMENT_SPEED);
        if (attribute == null) return;

        if (attribute.getModifier(LAST_STAND_MODIFIER_RESOURCE) != null) {
            attribute.removeModifier(LAST_STAND_MODIFIER_RESOURCE);
        }

        if (level > 0) {
            double missingHealth = p.getMaxHealth() - p.getHealth();
            int scale = (int) (missingHealth / 6.0);

            double bonus = scale;

            attribute.addTransientModifier(new AttributeModifier(
                    LAST_STAND_MODIFIER_RESOURCE,
                    bonus,
                    AttributeModifier.Operation.ADD_VALUE
            ));
        }
    }

    @SubscribeEvent
    public static void onEvokerMawHit(LivingDamageEvent.Post event) {
        LivingEntity entity = event.getEntity();
        if (entity == null) return;
        if (entity.level().isClientSide()) return;
        if (entity.getLastDamageSource() == null) return;

        Entity causer = entity.getLastDamageSource().getEntity();
        if (!(causer instanceof LivingEntity livingCauser)) return;

        Holder<Enchantment> mawHolder = entity.level().registryAccess()
                .registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(ModEnchantments.EVOKING);

        ItemStack chest = entity.getItemBySlot(EquipmentSlot.LEGS);
        int level = chest.getEnchantmentLevel(mawHolder);
        if (level <= 0) return;

        PlayerEnchantmentState pes = PlayerEnchantmentStateHandler.loadState(entity);
        if (pes.isOnCooldown(ModEnchantments.EVOKING.location(), entity.level().getGameTime())) return;

        pes.setCooldown(ModEnchantments.EVOKING.location(),
                entity.level().getGameTime() + (20 * 60));
        PlayerEnchantmentStateHandler.saveState(entity, pes);

        if (entity.level() instanceof ServerLevel world) {
            double centerX = entity.getX();
            double centerY = entity.getY();
            double centerZ = entity.getZ();

            int maxCircles = Math.min(level, 3);

            double dx = livingCauser.getX() - centerX;
            double dz = livingCauser.getZ() - centerZ;
            double attackerAngle = Math.atan2(dz, dx);

            for (int circle = 0; circle < maxCircles; circle++) {
                int mouthsPerCircle = 4 + (2 * circle);
                double radius = 2 + circle * 1.5;

                for (int i = 0; i < mouthsPerCircle; i++) {
                    float angle = (float) (attackerAngle + (2 * Math.PI / mouthsPerCircle) * i);

                    double x = centerX + radius * Math.cos(angle);
                    double z = centerZ + radius * Math.sin(angle);
                    double y = centerY;

                    EvokerFangs maw = new EvokerFangs(world, x, y, z, angle, 0, entity);
                    world.addFreshEntity(maw);
                }
            }
            world.playSound(
                    null,
                    centerX, centerY, centerZ,
                    SoundEvents.EVOKER_FANGS_ATTACK,
                    SoundSource.PLAYERS,
                    1.0f,
                    1.0f
            );
        }
    }

    @SubscribeEvent
    public static void onAirDash(PlayerTickEvent.Post event) {
        Player player = event.getEntity();

        Holder<Enchantment> dashHolder = player.level().registryAccess()
                .registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(ModEnchantments.DASH);

        ItemStack boots = player.getItemBySlot(EquipmentSlot.LEGS);
        int level = boots.getEnchantmentLevel(dashHolder);
        if (level <= 0) return;

        PlayerEnchantmentState pes = PlayerEnchantmentStateHandler.loadState(player);

        if (player.onGround()) {
            pes.setDashUsed(false);
            PlayerEnchantmentStateHandler.saveState(player, pes);
            return;
        }

        if (!player.isCrouching()) return;
        if (pes.isDashUsed()) return;

        Vec3 look = player.getLookAngle();
        Vec3 dash = new Vec3(look.x, 0, look.z).normalize().scale(level / 2.5d);
        player.setDeltaMovement(dash);
        player.hasImpulse = true;

        if (player.level() instanceof ServerLevel world) {
            world.playSound(
                    null,
                    player.getX(), player.getY(), player.getZ(),
                    SoundEvents.PLAYER_ATTACK_SWEEP,
                    SoundSource.PLAYERS,
                    1.0f,
                    1.0f
            );
            Vec3 back = player.getLookAngle().normalize().scale(-0.4);

            world.sendParticles(
                    ParticleTypes.CAMPFIRE_COSY_SMOKE,
                    player.getX() + back.x,
                    player.getY() + 0.1,
                    player.getZ() + back.z,
                    3,
                    0.2, 0.05, 0.2,
                    0.02
            );
        }
        pes.setDashUsed(true);
        PlayerEnchantmentStateHandler.saveState(player, pes);
    }

    @SubscribeEvent
    public static void onChargeAttack(LivingIncomingDamageEvent event) {
        LivingEntity target = event.getEntity();
        if (target.level().isClientSide()) return;

        if (event.getSource().getEntity() instanceof Player player) {

            Holder<Enchantment> chargeHolder = player.level().registryAccess()
                    .registryOrThrow(Registries.ENCHANTMENT)
                    .getHolderOrThrow(ModEnchantments.CHARGE);

            ItemStack boots = player.getItemBySlot(EquipmentSlot.LEGS);
            int level = boots.getEnchantmentLevel(chargeHolder);
            if (level <= 0) return;

            if (!player.isSprinting()) return;
            event.setAmount(event.getAmount() + level);
        }
    }

    @SubscribeEvent
    public static void onLiquidWalk(PlayerTickEvent.Pre event) {
        Player player = event.getEntity();

        if (player.isCrouching()) return;

        Holder<Enchantment> liquidHolder = player.level().registryAccess()
                .registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(ModEnchantments.BUOYANT);

        ItemStack boots = player.getItemBySlot(EquipmentSlot.FEET);
        int level = boots.getEnchantmentLevel(liquidHolder);
        if (level <= 0) return;

        Level levelWorld = player.level();

        BlockPos below = BlockPos.containing(
                player.getX(),
                player.getY() - 0.1,
                player.getZ()
        );

        BlockState stateBelow = levelWorld.getBlockState(below);

        if (!stateBelow.getFluidState().isEmpty()) {

            Vec3 motion = player.getDeltaMovement();
            if (motion.y < 0) {
                player.setDeltaMovement(motion.x, 0.0, motion.z);
            }

            double surfaceY = below.getY() + 1.0;
            player.setPos(player.getX(), surfaceY, player.getZ());

            player.setOnGround(true);

            player.fallDistance = 0.0F;
        }
    }

    @SubscribeEvent
    public static void onMidAirJump(PlayerTickEvent.Pre event) {
        Player player = event.getEntity();

        PlayerEnchantmentState pes = PlayerEnchantmentStateHandler.loadState(player);
        if (!Minecraft.getInstance().options.keyJump.isDown()) {
            pes.setJustJumped(false);
            PlayerEnchantmentStateHandler.saveState(player, pes);
            return;
        }
        if (player.onGround()) {
            pes.resetCloudStepJumps();
            PlayerEnchantmentStateHandler.saveState(player, pes);
            return;
        }

        Holder<Enchantment> jumpHolder = player.level().registryAccess()
                .registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(ModEnchantments.CLOUD_STEP);

        ItemStack boots = player.getItemBySlot(EquipmentSlot.FEET);
        int level = boots.getEnchantmentLevel(jumpHolder);
        if (level <= 0) return;

        int jumpsUsed = pes.getCloudStepJumpsUsed();
        if (jumpsUsed >= level + 1) return;
        if (!pes.hasJustJumped()) {

            Vec3 motion = player.getDeltaMovement();
            player.setDeltaMovement(motion.x, 0.42, motion.z);

            pes.incrementCloudStepJumps();

            player.level().playSound(
                    null,
                    player.getX(), player.getY(), player.getZ(),
                    SoundEvents.BREEZE_SHOOT,
                    SoundSource.PLAYERS,
                    1.0f,
                    1.0f
            );
        }
        pes.setJustJumped(true);
        PlayerEnchantmentStateHandler.saveState(player, pes);
    }

    @SubscribeEvent
    public static void onVoidDamaged(LivingIncomingDamageEvent event) {
        DamageSource s = event.getSource();
        Entity causer = s.getEntity();
        if (s.is(DamageTypeTags.BYPASSES_ARMOR)) return;
        if (causer instanceof LivingEntity player) {
            Holder<Enchantment> apHolder = player.level().registryAccess()
                    .registryOrThrow(Registries.ENCHANTMENT)
                    .getHolderOrThrow(ModEnchantments.SOUL_REND);

            ItemStack weapon = player.getWeaponItem();
            int level = weapon.getEnchantmentLevel(apHolder);
            if (level > 0) {
                float damage = Math.min(level, event.getAmount());
                event.getEntity().hurt(SelectivePowersDamageTypes.soulDamage(event.getEntity().level(), causer), damage);
                event.setCanceled(true);
            }
        }
    }


    @SubscribeEvent
    public static void onReverseShardHit(LivingDamageEvent.Post event) {
        if (event.getEntity().level().isClientSide()) return;

        if (event.getEntity().getLastDamageSource() == null) return;

        if (!(event.getEntity().getLastDamageSource().getEntity() instanceof Player attacker)) return;

        Holder<Enchantment> shardHolder = attacker.level().registryAccess()
                .registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(ModEnchantments.SPLATTER);

        ItemStack weapon = attacker.getMainHandItem();
        int level = weapon.getEnchantmentLevel(shardHolder);
        if (level <= 0) return;

        LivingEntity target = event.getEntity();

        RandomSource random = target.level().getRandom();
        Vec3 reverseDir = attacker.getLookAngle().normalize();

        for (int i = 0; i < level; i++) {
            float yawOffset = (random.nextFloat() - 0.5f) * 30f;
            float pitchOffset = (random.nextFloat() - 0.5f) * 20f;

            Vec3 dir = rotateYaw(reverseDir, yawOffset);
            dir = rotatePitch(dir, pitchOffset);

            double spawnX = target.getX();
            double spawnY = target.getEyeY() - 0.2;
            double spawnZ = target.getZ();

            ShardProjectileEntity projectile = new ShardProjectileEntity(
                    target,
                    target.level(),
                    SelectivePowersDamageTypes.spikeDamage(target.level()).typeHolder(),
                    0xff0000
            );

            projectile.setPos(spawnX, spawnY, spawnZ);
            projectile.shoot(dir.x, dir.y, dir.z, 1.0F, 0.2F);
            target.level().addFreshEntity(projectile);
        }
    }

    private static Vec3 rotateYaw(Vec3 vec, float degrees) {
        double radians = Math.toRadians(degrees);
        double cos = Math.cos(radians);
        double sin = Math.sin(radians);

        double x = vec.x * cos - vec.z * sin;
        double z = vec.x * sin + vec.z * cos;

        return new Vec3(x, vec.y, z).normalize();
    }

    private static Vec3 rotatePitch(Vec3 vec, float degrees) {
        double rad = Math.toRadians(degrees);
        double cos = Math.cos(rad);
        double sin = Math.sin(rad);
        return new Vec3(
                vec.x,
                vec.y * cos - vec.z * sin,
                vec.y * sin + vec.z * cos
        );
    }

    @SubscribeEvent
    public static void onPickerang(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity();
        Level world = player.level();
        ItemStack stack = event.getItemStack();
        if (world.isClientSide()) return;

        Holder<Enchantment> pickerangHolder = player.level().registryAccess()
                .registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(ModEnchantments.PICKERANG);

        int enchantLevel = stack.getEnchantmentLevel(pickerangHolder);
        if (enchantLevel <= 0) return;
        PickaxeBoomerangEntity boomerang = new PickaxeBoomerangEntity(world, player, stack.copy(), 10 + (5 * enchantLevel));
        boomerang.setPos(player.getX(), player.getY() + player.getEyeHeight(), player.getZ());
        world.addFreshEntity(boomerang);
        stack.shrink(1);
        ServerLevel level = (ServerLevel) world;
        level.playSound(
                null,
                player.blockPosition(),
                SelectivepowersSounds.PICKERANG.get(),
                SoundSource.PLAYERS,
                1.0F,
                player.getRandom().nextFloat() + 0.5f
        );
        event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onShovelPlaceSand(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        Level world = player.level();
        ItemStack stack = player.getItemInHand(event.getHand());
        if (world.isClientSide) return;
        Holder<Enchantment> sandHolder = world.registryAccess()
                .registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(ModEnchantments.POCKET_SAND);
        int level = stack.getEnchantmentLevel(sandHolder);
        if (level <= 0) return;
        int damage = 30 - (5 * level);
        if (stack.getMaxDamage() - stack.getDamageValue() < damage) return;
        BlockPos placePos = event.getPos().relative(event.getFace());
        if (!world.getBlockState(placePos).isAir()) return;
        world.setBlock(placePos, Blocks.SAND.defaultBlockState(), 3);
        ItemStack particleStack = stack.copy();
        stack.hurtAndBreak(damage, (ServerLevel) world, player, (p) -> {
            ServerLevel l = (ServerLevel) world;
            l.playSound(
                    null,
                    player.getOnPos(),
                    SoundEvents.ITEM_BREAK,
                    SoundSource.PLAYERS,
                    1.0F,
                    1.0F
            );
            ItemParticleOption particle =
                    new ItemParticleOption(ParticleTypes.ITEM, particleStack);

            l.sendParticles(
                    particle,
                    player.getX(),
                    player.getY() + 1.0,
                    player.getZ(),
                    20,
                    0.25, 0.25, 0.25,
                    0.05
            );
        });
        world.playSound(null, placePos, SoundEvents.SAND_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.SUCCESS);
    }

    @SubscribeEvent
    public static void onShovelPlaceTrap(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        Level world = player.level();
        ItemStack stack = player.getItemInHand(event.getHand());
        if (world.isClientSide) return;
        Holder<Enchantment> sandHolder = world.registryAccess()
                .registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(ModEnchantments.TRAP_DIGGER);
        int level = stack.getEnchantmentLevel(sandHolder);
        if (level <= 0) return;
        if (stack.getMaxDamage() - stack.getDamageValue() < 20) return;
        if (event.getFace() != Direction.UP) return;
        BlockPos placePos = event.getPos().relative(event.getFace());
        if (!world.getBlockState(placePos).isAir()) return;
        if (!world.getBlockState(event.getPos()).is(BlockTags.MINEABLE_WITH_SHOVEL)) return;
        if(!(world.getBlockState(event.getPos()).isSolidRender(world, event.getPos())))return;
        world.setBlock(placePos, SelectivepowersBlocks.PITFALL_TRAP.get().defaultBlockState(), 3);
        if (world.getBlockEntity(placePos) instanceof TrapBlockEntity be) {
            be.setTimer(600 - (100 * level));
        }
        ItemStack particleStack = stack.copy();
        stack.hurtAndBreak(20, (ServerLevel) world, player, (p) -> {
            ServerLevel l = (ServerLevel) world;
            l.playSound(
                    null,
                    player.getOnPos(),
                    SoundEvents.ITEM_BREAK,
                    SoundSource.PLAYERS,
                    1.0F,
                    1.0F
            );
            ItemParticleOption particle =
                    new ItemParticleOption(ParticleTypes.ITEM, particleStack);

            l.sendParticles(
                    particle,
                    player.getX(),
                    player.getY() + 1.0,
                    player.getZ(),
                    20,
                    0.25, 0.25, 0.25,
                    0.05
            );
        });
        world.playSound(null, placePos, SoundEvents.ROOTED_DIRT_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.SUCCESS);
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        Level world = player.level();
        ItemStack stack = player.getItemInHand(event.getHand());
        if (world.isClientSide) return;
        if (stack.getMaxDamage() - stack.getDamageValue() < 20) return;

        Holder<Enchantment> pickerangHolder = player.level().registryAccess()
                .registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(ModEnchantments.EARTHEN_SPIKE);

        int enchantLevel = stack.getEnchantmentLevel(pickerangHolder);
        if (enchantLevel <= 0) return;

        BlockPos pos = event.getPos();
        if (!(world.getBlockState(pos).is(BlockTags.MINEABLE_WITH_PICKAXE))) return;
        if(!(world.getBlockState(pos).isSolidRender(world, pos)))return;

        SpikeEntity spike = new SpikeEntity(world, pos, enchantLevel, 200 * enchantLevel, player);
        world.addFreshEntity(spike);
        ItemStack particleStack = stack.copy();
        stack.hurtAndBreak(20, (ServerLevel) world, player, (p) -> {
            ServerLevel l = (ServerLevel) world;
            l.playSound(
                    null,
                    player.getOnPos(),
                    SoundEvents.ITEM_BREAK,
                    SoundSource.PLAYERS,
                    1.0F,
                    1.0F
            );
            ItemParticleOption particle =
                    new ItemParticleOption(ParticleTypes.ITEM, particleStack);

            l.sendParticles(
                    particle,
                    player.getX(),
                    player.getY() + 1.0,
                    player.getZ(),
                    20,
                    0.25, 0.25, 0.25,
                    0.05
            );
        });
        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.SUCCESS);
    }

    @SubscribeEvent
    public static void crobbowLoading(LivingGetProjectileEvent e) {
        LivingEntity player = e.getEntity();
        ItemStack s = e.getProjectileWeaponItemStack();
        Holder<Enchantment> echoingLevel = player.level().registryAccess()
                .registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(ModEnchantments.ECHOING);

        int enchantLevel = s.getEnchantmentLevel(echoingLevel);
        if (enchantLevel > 0) {
            if (player instanceof Player p && !p.hasInfiniteMaterials()) {
                ItemStack echoShardStack = ItemStack.EMPTY;

                for (ItemStack stack : p.getInventory().items) {
                    if (stack.is(Items.ECHO_SHARD)) {
                        echoShardStack = stack;
                        break;
                    }
                }
                e.setProjectileItemStack(echoShardStack);
            } else {
                e.setProjectileItemStack(Items.ECHO_SHARD.getDefaultInstance().copy());
            }
        }
        Holder<Enchantment> galvanizingHolder = player.level().registryAccess()
                .registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(ModEnchantments.GALVANIZING);

        enchantLevel = s.getEnchantmentLevel(galvanizingHolder);
        if (enchantLevel > 0) {
            if (player instanceof Player p && !p.hasInfiniteMaterials()) {
                ItemStack lightningRodStack = ItemStack.EMPTY;

                for (ItemStack stack : p.getInventory().items) {
                    if (stack.is(Items.LIGHTNING_ROD)) {
                        lightningRodStack = stack;
                        break;
                    }
                }
                e.setProjectileItemStack(lightningRodStack);
            } else {
                e.setProjectileItemStack(Items.LIGHTNING_ROD.getDefaultInstance().copy());
            }
        }
        Holder<Enchantment> grapeShotHolder = player.level().registryAccess()
                .registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(ModEnchantments.GRAPE_SHOT);

        enchantLevel = s.getEnchantmentLevel(grapeShotHolder);
        if (enchantLevel > 0) {
            if (player instanceof Player p && !p.hasInfiniteMaterials()) {
                ItemStack lightningRodStack = ItemStack.EMPTY;

                for (ItemStack stack : p.getInventory().items) {
                    if (stack.is(ItemTags.STONE_TOOL_MATERIALS)) {
                        lightningRodStack = stack;
                        break;
                    }
                }
                e.setProjectileItemStack(lightningRodStack);
            } else {
                e.setProjectileItemStack(Items.COBBLESTONE.getDefaultInstance().copy());
            }
        }
        if(e.getProjectileItemStack().isEmpty()) {
            Holder<Enchantment> livingwoodHolder = player.level().registryAccess()
                    .registryOrThrow(Registries.ENCHANTMENT)
                    .getHolderOrThrow(ModEnchantments.LIVING_WOOD);

            enchantLevel = s.getEnchantmentLevel(livingwoodHolder);
            if (enchantLevel > 0) {
                e.setProjectileItemStack(Items.STICK.getDefaultInstance().copy());
            }
        }
    }
    @SubscribeEvent
    public static void onShieldBouncy(LivingShieldBlockEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        ItemStack shield = player.getUseItem();;
        if (shield == null) return;

        Holder<Enchantment> bouncyHolder = player.level().registryAccess()
                .registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(ModEnchantments.BOUNCY);

        if (shield.getEnchantmentLevel(bouncyHolder) <= 0) return;

        Entity attacker = event.getDamageSource().getEntity();

        if (attacker == null) return;

        double dx = player.getX() - attacker.getX();
        double dz = player.getZ() - attacker.getZ();

        double magnitude = Math.sqrt(dx * dx + dz * dz);
        if (magnitude < 0.001) return;

        dx /= magnitude;
        dz /= magnitude;

        float strength = 0.4F;

        player.knockback(strength, dx, dz);
    }

    @SubscribeEvent
    public static void onShieldDevouring(LivingShieldBlockEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        ItemStack shield = player.getUseItem();;
        if (shield == null) return;

        Holder<Enchantment> bouncyHolder = player.level().registryAccess()
                .registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(ModEnchantments.DEVOURING);

        int enchantLevel = shield.getEnchantmentLevel(bouncyHolder);
        if (enchantLevel <= 0) return;

        Entity attacker = event.getDamageSource().getEntity();

        if (attacker == null) return;
        if (!(attacker instanceof Player attPlayer)) return;

        attPlayer.causeFoodExhaustion((float) (event.getBlockedDamage()*(0.1*enchantLevel)));

        if(attPlayer.level() instanceof ServerLevel world) {
            world.playSound(
                    null,
                    player.getOnPos(),
                    SoundEvents.GENERIC_EAT,
                    SoundSource.PLAYERS,
                    1.0F,
                    attPlayer.getRandom().nextFloat()+0.5f
            );
        }
    }
    @SubscribeEvent
    public static void onShieldContingencyBreak(PlayerDestroyItemEvent event){
        ItemStack brokenItem = event.getOriginal();
        Player entityLiving = event.getEntity();

        Holder<Enchantment> contingencyHolder = entityLiving.level().registryAccess()
                .registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(ModEnchantments.CONTINGENCY);

        int enchLevel = brokenItem.getEnchantmentLevel(contingencyHolder);
        if (enchLevel <= 0) return;

        Level level = entityLiving.level();

        if (!level.isClientSide) {
            for(int i = 0; i < 16; ++i) {
                double d0 = entityLiving.getX() + (entityLiving.getRandom().nextDouble() - (double)0.5F) * (double)16.0F;
                double d1 = Mth.clamp(entityLiving.getY() + (double)(entityLiving.getRandom().nextInt(16) - 8), (double)level.getMinBuildHeight(), (double)(level.getMinBuildHeight() + ((ServerLevel)level).getLogicalHeight() - 1));
                double d2 = entityLiving.getZ() + (entityLiving.getRandom().nextDouble() - (double)0.5F) * (double)16.0F;
                if (entityLiving.isPassenger()) {
                    entityLiving.stopRiding();
                }

                Vec3 vec3 = entityLiving.position();
                EntityTeleportEvent.ChorusFruit chorusEvent = EventHooks.onChorusFruitTeleport(entityLiving, d0, d1, d2);
                if (entityLiving.randomTeleport(chorusEvent.getTargetX(), chorusEvent.getTargetY(), chorusEvent.getTargetZ(), true)) {
                    level.gameEvent(GameEvent.TELEPORT, vec3, GameEvent.Context.of(entityLiving));
                    SoundSource soundsource;
                    SoundEvent soundevent;
                    soundevent = SoundEvents.CHORUS_FRUIT_TELEPORT;
                    soundsource = SoundSource.PLAYERS;

                    level.playSound((Player)null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), soundevent, soundsource);
                    entityLiving.resetFallDistance();
                    break;
                }
            }
        }
    }
    @SubscribeEvent
    public static void onShieldContingencyDisable(LivingShieldBlockEvent event) {
        LivingEntity entityLiving = event.getEntity();
        if (!(entityLiving instanceof Player player)) return;
        if (player.level().isClientSide()) return;

        ItemStack shield = entityLiving.getUseItem();

        Holder<Enchantment> contingencyHolder = player.level().registryAccess()
                .registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(ModEnchantments.CONTINGENCY);

        int contongencyLevel = shield.getEnchantmentLevel(contingencyHolder);
        if (contongencyLevel <= 0) return;

        Entity sourceEntity = event.getDamageContainer().getSource().getEntity();
        if (!(sourceEntity instanceof LivingEntity attacker)) return;

        ItemStack weapon = attacker.getMainHandItem();
        if (!(weapon.getItem() instanceof AxeItem)) return;

        Level level = entityLiving.level();

        if (!level.isClientSide) {
            for(int i = 0; i < 16; ++i) {
                double d0 = entityLiving.getX() + (entityLiving.getRandom().nextDouble() - (double)0.5F) * (double)16.0F;
                double d1 = Mth.clamp(entityLiving.getY() + (double)(entityLiving.getRandom().nextInt(16) - 8), (double)level.getMinBuildHeight(), (double)(level.getMinBuildHeight() + ((ServerLevel)level).getLogicalHeight() - 1));
                double d2 = entityLiving.getZ() + (entityLiving.getRandom().nextDouble() - (double)0.5F) * (double)16.0F;
                if (entityLiving.isPassenger()) {
                    entityLiving.stopRiding();
                }

                Vec3 vec3 = entityLiving.position();
                EntityTeleportEvent.ChorusFruit chorusEvent = EventHooks.onChorusFruitTeleport(entityLiving, d0, d1, d2);
                if (entityLiving.randomTeleport(chorusEvent.getTargetX(), chorusEvent.getTargetY(), chorusEvent.getTargetZ(), true)) {
                    level.gameEvent(GameEvent.TELEPORT, vec3, GameEvent.Context.of(entityLiving));
                    SoundSource soundsource;
                    SoundEvent soundevent;
                    soundevent = SoundEvents.CHORUS_FRUIT_TELEPORT;
                    soundsource = SoundSource.PLAYERS;

                    level.playSound((Player)null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), soundevent, soundsource);
                    entityLiving.resetFallDistance();
                    break;
                }
            }
        }
    }

    @SubscribeEvent
    public static void onShieldImmortality(LivingDeathEvent event) {
        LivingEntity entity = event.getEntity();
        if (!(entity instanceof Player player)) return;

        ItemStack shield = player.getItemBySlot(EquipmentSlot.OFFHAND);
        if (shield.isEmpty() || shield.getItem() != Items.SHIELD){
            shield = player.getItemBySlot(EquipmentSlot.MAINHAND);
        }
        if (shield.isEmpty() || shield.getItem() != Items.SHIELD)return;

        Holder<Enchantment> immortalityHolder = player.level().registryAccess()
                .registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(ModEnchantments.IMMORTALITY);

        int level = shield.getEnchantmentLevel(immortalityHolder);
        if (level <= 0) return;

        event.setCanceled(true);

        shield.shrink(1);

        player.setHealth(player.getMaxHealth() / 2f);

        player.removeAllEffects();

        player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 900, 1));
        player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 1));
        player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 800, 0));

        player.level().levelEvent(1038, player.blockPosition(), 0);

        player.level().playSound(
                null,
                player.getX(), player.getY(), player.getZ(),
                SoundEvents.TOTEM_USE,
                SoundSource.PLAYERS,
                1.0f,
                1.0f
        );

        if (player.level() instanceof ServerLevel server) {
            server.sendParticles(
                    ParticleTypes.TOTEM_OF_UNDYING,
                    player.getX(), player.getY() + 1.0, player.getZ(),
                    30, 0.5, 0.5, 0.5, 0.0
            );
        }
    }
    @SubscribeEvent
    public static void onPheromoneStingerHit(LivingDamageEvent.Post event) {
        LivingEntity target = event.getEntity();
        if (target.level().isClientSide()) return;

        Entity causer = event.getSource().getEntity();
        if (!(causer instanceof LivingEntity attacker)) return;

        ItemStack weapon = attacker.getMainHandItem();
        Holder<Enchantment> stingerHolder = attacker.level().registryAccess()
                .registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(ModEnchantments.PHEROMONE_STINGER);

        int level = weapon.getEnchantmentLevel(stingerHolder);
        if (level <= 0) return;

        int duration = 20 * 5 * level;
        target.addEffect(new MobEffectInstance(
                SelectivepowersEffects.PHEROMONES,
                duration,
                0,
                false,
                true
        ));
    }
    @SubscribeEvent
    public static void onDevilsToolNetherImpaling(LivingIncomingDamageEvent event) {
        LivingEntity target = event.getEntity();
        if (target.level().isClientSide()) return;

        DamageSource source = event.getSource();
        Entity attackerEntity = source.getEntity();
        ItemStack weapon = null;
        if ((attackerEntity instanceof LivingEntity attacker)) {
            weapon = attacker.getWeaponItem();
        }
        if(attackerEntity instanceof ThrownTrident trident){
            weapon = trident.getWeaponItem();
        }
        if(weapon == null) return;
        if (weapon.isEmpty()) return;

        Holder<Enchantment> devilHolder = target.level().registryAccess()
                .registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(ModEnchantments.DEVILS_TOOL);

        int level = weapon.getEnchantmentLevel(devilHolder);
        if (level <= 0) return;
        Holder<Enchantment> impalingHolder = target.level().registryAccess()
                .registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(Enchantments.IMPALING);
        level = weapon.getEnchantmentLevel(impalingHolder);
        if(level<=0) return;

        if(target.getType().is(EntityTypeTags.SENSITIVE_TO_IMPALING))
        {
            event.setAmount(event.getAmount()-2.5f*level);
        }

        if(target.getType().is(ModTags.EntityTypes.SENSITIVE_TO_DEVILS_IMPALING))
        {
            event.setAmount(event.getAmount()+2.5f*level);
        }
    }
    @SubscribeEvent
    public static void onPoseidonTridentImpact(ProjectileImpactEvent event) {
        if (!(event.getProjectile() instanceof ThrownTrident trident)) return;
        if (trident.level().isClientSide()) return;
        if (!(event.getRayTraceResult() instanceof BlockHitResult hit)) return;

        ItemStack stack = trident.getWeaponItem();
        if (stack == null || stack.isEmpty()) return;

        Level level = trident.level();

        Holder<Enchantment> poseidonHolder = level.registryAccess()
                .registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(ModEnchantments.POSEIDON);

        if (stack.getEnchantmentLevel(poseidonHolder) <= 0) return;

        Holder<Enchantment> devilHolder = level.registryAccess()
                .registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(ModEnchantments.DEVILS_TOOL);

        boolean hasDevilsTool = stack.getEnchantmentLevel(devilHolder) > 0;

        BlockPos placePos = hit.getBlockPos().relative(hit.getDirection());

        if (!level.getBlockState(placePos).canBeReplaced()) return;
        if (!level.getFluidState(placePos).isEmpty()) return;

        if (hasDevilsTool) {
            BlockState fireState = BaseFireBlock.getState(level, placePos);
            if (fireState != null) {
                level.setBlock(placePos, fireState, Block.UPDATE_ALL);
            }
        } else {
            level.setBlock(placePos, Blocks.WATER.defaultBlockState(), Block.UPDATE_ALL);
        }
    }

}
