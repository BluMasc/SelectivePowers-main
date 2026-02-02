package net.blumasc.selectivepowers.block.entity;

import net.blumasc.selectivepowers.SelectivePowers;
import net.blumasc.selectivepowers.block.SelectivepowersBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class SelectivepowersBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, SelectivePowers.MODID);

    public static final Supplier<BlockEntityType<AltarBlockEntity>> ALTAR_BE=
            BLOCK_ENTITIES.register("altar_be", () -> BlockEntityType.Builder.of(
                    AltarBlockEntity::new, SelectivepowersBlocks.ALTAR.get()
            ).build(null));

    public static final Supplier<BlockEntityType<NestBlockEntity>> NEST_BE=
            BLOCK_ENTITIES.register("nest_be", () -> BlockEntityType.Builder.of(
                    NestBlockEntity::new, SelectivepowersBlocks.CROWS_NEST.get()
            ).build(null));

    public static final Supplier<BlockEntityType<VoidBlockEntity>> VOID_BLOCK_BE=
            BLOCK_ENTITIES.register("void_block_be", () -> BlockEntityType.Builder.of(
                    VoidBlockEntity::new, SelectivepowersBlocks.VOID_BLOCK.get()
            ).build(null));

    public static final Supplier<BlockEntityType<SporeMushroomEntity>> SPORE_MUSHROOM_BE=
            BLOCK_ENTITIES.register("spore_mushroom_be", () -> BlockEntityType.Builder.of(
                    SporeMushroomEntity::new, SelectivepowersBlocks.SPORE_MUSHROOM_BLOCK.get()
            ).build(null));

    public static final Supplier<BlockEntityType<ProtectionEffigyBlockEntity>> PROTECTION_EFFIGY_BE=
            BLOCK_ENTITIES.register("protection_effigy_be", () -> BlockEntityType.Builder.of(
                    ProtectionEffigyBlockEntity::new, SelectivepowersBlocks.PROTECTION_EFFIGY_BLOCK.get()
            ).build(null));

    public static final Supplier<BlockEntityType<TesslaCoilBlockEntity>> TESSLA_COIL_BE=
            BLOCK_ENTITIES.register("tessla_coil_be", () -> BlockEntityType.Builder.of(
                    TesslaCoilBlockEntity::new, SelectivepowersBlocks.TESSLA_COIL.get()
            ).build(null));

    public static final Supplier<BlockEntityType<SacrificeAltarBlockEntity>> SAC_ALTAR_BE=
            BLOCK_ENTITIES.register("sac_altar_be", () -> BlockEntityType.Builder.of(
                    SacrificeAltarBlockEntity::new, SelectivepowersBlocks.SACRIFICIAL_ALTAR_BLOCK.get()
            ).build(null));

    public static final Supplier<BlockEntityType<TrapBlockEntity>> PITFALL_TRAP_BE=
            BLOCK_ENTITIES.register("pitfall_trap_be", () -> BlockEntityType.Builder.of(
                    TrapBlockEntity::new, SelectivepowersBlocks.PITFALL_TRAP.get()
            ).build(null));



    public static void register(IEventBus bus){
        BLOCK_ENTITIES.register(bus);
    }
}
