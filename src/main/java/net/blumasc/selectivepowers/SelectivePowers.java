package net.blumasc.selectivepowers;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.blumasc.selectivepowers.block.SelectivepowersBlocks;
import net.blumasc.selectivepowers.block.entity.SelectivepowersBlockEntities;
import net.blumasc.selectivepowers.component.ModDataComponentTypes;
import net.blumasc.selectivepowers.effect.SelectivepowersEffects;
import net.blumasc.selectivepowers.enchantment.ModEnchantmentEffects;
import net.blumasc.selectivepowers.entity.SelectivepowersEntities;
import net.blumasc.selectivepowers.item.SelectivepowersCreativeModeTabs;
import net.blumasc.selectivepowers.item.SelectivepowersItems;
import net.blumasc.selectivepowers.item.client.gui.ModMenuTypes;
import net.blumasc.selectivepowers.item.dispenserbehaviour.LightningBottleDispenserBehavior;
import net.blumasc.selectivepowers.managers.SunBattleManager;
import net.blumasc.selectivepowers.network.ModNetworking;
import net.blumasc.selectivepowers.potion.SelectivePowerPotions;
import net.blumasc.selectivepowers.recipe.SelectivePowersRecipes;
import net.blumasc.selectivepowers.sound.SelectivepowersSounds;
import net.blumasc.selectivepowers.worldgen.features.ModFeatures;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.ProjectileDispenseBehavior;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(SelectivePowers.MODID)
public class SelectivePowers {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "selectivepowers";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public SelectivePowers(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        SelectivepowersCreativeModeTabs.register(modEventBus);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (ExampleMod) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        SelectivepowersItems.register(modEventBus);
        SelectivepowersBlocks.register(modEventBus);
        SelectivepowersSounds.register(modEventBus);
        SelectivepowersEntities.register(modEventBus);
        SelectivepowersEffects.register(modEventBus);
        SelectivepowersBlockEntities.register(modEventBus);
        SelectivePowersRecipes.register(modEventBus);
        ModFeatures.register(modEventBus);
        ModDataComponentTypes.register(modEventBus);
        SelectivePowerPotions.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        ModEnchantmentEffects.register(modEventBus);
        modEventBus.addListener(ModNetworking::register);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        net.minecraft.world.level.block.DispenserBlock.registerBehavior(
                SelectivepowersItems.CORRUPTING_ARROW.get(),
                new ProjectileDispenseBehavior(SelectivepowersItems.CORRUPTING_ARROW.asItem())
        );

        net.minecraft.world.level.block.DispenserBlock.registerBehavior(
                SelectivepowersItems.BEAM_ARROW.get(),
                new ProjectileDispenseBehavior(SelectivepowersItems.BEAM_ARROW.asItem())
        );

        net.minecraft.world.level.block.DispenserBlock.registerBehavior(
                SelectivepowersItems.FLAMING_EGG.get(),
                new ProjectileDispenseBehavior(SelectivepowersItems.FLAMING_EGG.asItem())
        );

        net.minecraft.world.level.block.DispenserBlock.registerBehavior(
                SelectivepowersItems.LIGHTNING_IN_A_BOTTLE.get(),
                new LightningBottleDispenserBehavior()
        );

        net.minecraft.world.level.block.DispenserBlock.registerBehavior(
                SelectivepowersItems.BURNING_FEATHER.get(),
                new ProjectileDispenseBehavior(SelectivepowersItems.BURNING_FEATHER.asItem())
        );
    }



    @SubscribeEvent
    public void registerCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(
                Commands.literal("selectivepowers")
                        .requires(source -> source.hasPermission(2))
                        .then(Commands.literal("give")
                                .then(Commands.argument("player", EntityArgument.player())
                                        .then(Commands.argument("power", StringArgumentType.word())
                                                .suggests((context, builder) -> {
                                                    return SharedSuggestionProvider.suggest(PowerManager.getPowerNames(), builder);
                                                })
                                                .executes(context -> {
                                                    ServerPlayer target = EntityArgument.getPlayer(context, "player");
                                                    String power = StringArgumentType.getString(context, "power");
                                                    return givePower(context.getSource(), target, power);
                                                })
                                        )
                                )
                        )
                        .then(Commands.literal("remove")
                                .then(Commands.argument("player", EntityArgument.player())
                                        .executes(context -> {
                                            ServerPlayer target = EntityArgument.getPlayer(context, "player");
                                            return removePower(context.getSource(), target);
                                        })
                                )
                        )
                        .then(Commands.literal("increase")
                                .then(Commands.argument("player", EntityArgument.player())
                                        .executes(context -> {
                                            ServerPlayer target = EntityArgument.getPlayer(context, "player");
                                            return increasePower(context.getSource(), target);
                                        })
                                )
                        )
                        .then(Commands.literal("list")
                                .executes(context -> listPowers(context.getSource()))
                        )
        );
        event.getDispatcher().register(
                Commands.literal("spawn_solar_arena")
                        .executes(ctx -> {
                            ServerPlayer player = ctx.getSource().getPlayerOrException();
                            spawnAtPlayer(player);
                            return 1;
                        })
        );
    }

    private static int givePower(CommandSourceStack source, ServerPlayer player, String power) {
        ServerLevel level = player.level().getServer().overworld();
        PowerManager manager = PowerManager.get(level);

        // Check if the power is already taken
        if (!manager.isPowerFree(power)) {
            source.sendFailure(Component.literal("Power already taken!"));
            return 0;
        }

        // Check if the player already has any power
        if (manager.doesPlayerHaveAnyPower(player.getUUID())) {
            source.sendFailure(Component.literal("Player already has a power!"));
            return 0;
        }

        manager.assignPower(power, player);
        manager.syncToAll((ServerLevel) player.level());
        source.sendSuccess(() -> Component.literal(player.getName().getString() + " now has the power: " + power),false);
        return 1;
    }

    private static int removePower(CommandSourceStack source, ServerPlayer player) {
        ServerLevel level = player.level().getServer().overworld();
        PowerManager manager = PowerManager.get(level);

        String power = manager.getPowerOfPlayer(player.getUUID());
        if (power == null) {
            source.sendFailure(Component.literal("Player has no power!"));
            return 0;
        }

        manager.powerAssignments.remove(power);
        manager.removeProgress(player.getUUID());
        manager.setDirty();
        manager.syncToAll((ServerLevel) player.level());
        source.sendSuccess(() -> Component.literal("Removed power " + power + " from " + player.getName().getString()), true);
        return 1;
    }

    private static int increasePower(CommandSourceStack source, ServerPlayer player) {
        ServerLevel level = player.level().getServer().overworld();
        PowerManager manager = PowerManager.get(level);

        String power = manager.getPowerOfPlayer(player.getUUID());
        if (power == null) {
            source.sendFailure(Component.literal("Player has no power!"));
            return 0;
        }

        manager.upgradePower(power);
        manager.setDirty();
        manager.syncToAll((ServerLevel) player.level());
        source.sendSuccess(() -> Component.literal("Increased power " + power + " from " + player.getName().getString()+ " to "+manager.getPowerLevelOfPlayer(player.getUUID()).toString()), true);
        return 1;
    }

    private static int listPowers(CommandSourceStack source) {
        ServerLevel level = source.getLevel().getServer().overworld();
        PowerManager manager = PowerManager.get(level);

        if (manager.powerAssignments.isEmpty()) {
            source.sendSuccess(() -> Component.literal("No powers assigned."), false);
            return 1;
        }

        source.sendSuccess(() -> Component.literal("Current powers:"), false);
        manager.powerAssignments.forEach((power, pa) -> {
            if (pa.owner == null) {
                String status = pa.devoured ? "devoured" : "free";
                source.sendSuccess(
                        () -> Component.literal(power + " -> (" + status + ")"),
                        false
                );
                return ;
            }

            GameProfile profile = source.getServer()
                    .getProfileCache()
                    .get(pa.owner)
                    .orElse(null);

            String name = profile != null ? profile.getName() : "(unknown)";

            source.sendSuccess(
                    () -> Component.literal(power + " -> " + name + " (" + pa.level + ")"),
                    false
            );
        });
        return 1;
    }

    private static void spawnAtPlayer(ServerPlayer player) {

        ServerLevel sl = (ServerLevel) player.level();
        SunBattleManager sbm = SunBattleManager.get(sl);
        sbm.spawnAndTeleport(sl, null, player);
    }

}
