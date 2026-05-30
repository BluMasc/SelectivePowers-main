package net.blumasc.selectivepowers.components;

import com.mojang.serialization.Codec;
import net.blumasc.selectivepowers.SelectivePowers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModDataComponents {

    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS =
            DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, SelectivePowers.MODID);

    public static final Supplier<DataComponentType<BlockPos>> BOUND_LEVER =
            DATA_COMPONENTS.register("bound_lever", () ->
                    DataComponentType.<BlockPos>builder()
                            .persistent(BlockPos.CODEC)
                            .build()
            );

    public static void register(IEventBus e){
        DATA_COMPONENTS.register(e);
    }
}