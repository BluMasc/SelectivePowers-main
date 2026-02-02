package net.blumasc.selectivepowers.component;

import com.mojang.serialization.Codec;
import net.blumasc.selectivepowers.SelectivePowers;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.UnaryOperator;

public class ModDataComponentTypes {
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES =
            DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, SelectivePowers.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> PAGE = register("page",
            builder -> builder.persistent(Codec.INT));

    private static <T>DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String name, UnaryOperator<DataComponentType.Builder<T>> builderOperator){
        return DATA_COMPONENT_TYPES.register(name, ()-> builderOperator.apply(DataComponentType.builder()).build());
    }

    public static void register(IEventBus bus)
    {
        DATA_COMPONENT_TYPES.register(bus);
    }
}
