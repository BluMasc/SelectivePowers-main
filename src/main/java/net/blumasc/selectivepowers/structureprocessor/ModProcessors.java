package net.blumasc.selectivepowers.structureprocessor;

import net.blumasc.selectivepowers.SelectivePowers;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModProcessors {
    public static final DeferredRegister<StructureProcessorType<?>> PROCESSOR_TYPES =
            DeferredRegister.create(Registries.STRUCTURE_PROCESSOR, SelectivePowers.MODID);

    public static final DeferredHolder<StructureProcessorType<?>, StructureProcessorType<StructureCleanupProcessor>> CLEANUP_PROCESSOR =
            PROCESSOR_TYPES.register("cleanup_processor",
                    () -> () -> StructureCleanupProcessor.CODEC);
}
