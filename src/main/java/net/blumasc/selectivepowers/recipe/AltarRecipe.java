package net.blumasc.selectivepowers.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.blumasc.selectivepowers.block.custom.AltarBlock;
import net.blumasc.selectivepowers.block.entity.AltarBlockEntity;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public record AltarRecipe (Ingredient inputItem, boolean night, ItemStack output) implements Recipe<AltarRecipeInput> {
    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.create();
        list.add(inputItem);
        list.add(sacrificeItem());
        list.add(sacrificeItem());
        list.add(sacrificeItem());
        list.add(sacrificeItem());
        return list;
    }

    public Ingredient sacrificeItem(){
        if(night) return Ingredient.of(AltarBlockEntity.moonCatalyst);
        return Ingredient.of(AltarBlockEntity.sunCatalyst);
    }

    @Override
    public boolean matches(AltarRecipeInput altarRecipeInput, Level level) {
        if (level.isClientSide())
    {
        return false;
    }
        boolean ret = inputItem.test(altarRecipeInput.getItem(0));
        for (int i =1;i<5;i++)
        {
            ret = ret && sacrificeItem().test(altarRecipeInput.getItem(i));
        }
        return ret;

    }

    @Override
    public ItemStack assemble(AltarRecipeInput altarRecipeInput, HolderLookup.Provider provider) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return output;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SelectivePowersRecipes.ALTAR_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return SelectivePowersRecipes.ALTAR_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<AltarRecipe>
    {
        public static final MapCodec<AltarRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(AltarRecipe::inputItem),
                Codec.BOOL.fieldOf("night").forGetter(AltarRecipe::night),
                ItemStack.CODEC.fieldOf("result").forGetter(AltarRecipe::output)
        ).apply(inst, AltarRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, AltarRecipe> STREAM_CODEC =
                StreamCodec.composite(
                        Ingredient.CONTENTS_STREAM_CODEC, AltarRecipe::inputItem,
                        StreamCodec.of(
                                (buf, night) -> buf.writeBoolean(night), // writer
                                buf -> buf.readBoolean()                 // reader
                        ), AltarRecipe::night,
                        ItemStack.STREAM_CODEC, AltarRecipe::output,
                        AltarRecipe::new
                );

        @Override
        public MapCodec<AltarRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, AltarRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
