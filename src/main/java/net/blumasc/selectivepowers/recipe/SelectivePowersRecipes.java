package net.blumasc.selectivepowers.recipe;

import net.blumasc.selectivepowers.SelectivePowers;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class SelectivePowersRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, SelectivePowers.MODID);
    public static final DeferredRegister<RecipeType<?>> TYPESS =
            DeferredRegister.create(Registries.RECIPE_TYPE, SelectivePowers.MODID);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<AltarRecipe>> ALTAR_SERIALIZER =
            SERIALIZERS.register("altar", AltarRecipe.Serializer::new);

    public static final DeferredHolder<RecipeType<?>, RecipeType<AltarRecipe>> ALTAR_TYPE =
            TYPESS.register("altar", () -> new RecipeType<AltarRecipe>() {
                @Override
                public String toString(){
                    return "altar";
        }
    });

    public static void register(IEventBus bus)
    {
        SERIALIZERS.register(bus);
        TYPESS.register(bus);
    }
}
