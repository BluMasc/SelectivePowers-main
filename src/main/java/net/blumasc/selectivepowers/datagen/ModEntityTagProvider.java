package net.blumasc.selectivepowers.datagen;

import net.blumasc.selectivepowers.SelectivePowers;
import net.blumasc.selectivepowers.entity.SelectivepowersEntities;
import net.blumasc.selectivepowers.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.internal.NeoForgeEntityTypeTagsProvider;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModEntityTagProvider extends EntityTypeTagsProvider {
    public ModEntityTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, SelectivePowers.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(EntityTypeTags.ARROWS)
                .add(SelectivepowersEntities.CORRUPTING_ARROW.get())
                .add(SelectivepowersEntities.LIGHT_BEAM_ARROW.get());
        tag(Tags.EntityTypes.BOSSES)
                .add(SelectivepowersEntities.YELLOW_KING_BOSS.get());
        tag(Tags.EntityTypes.CAPTURING_NOT_SUPPORTED)
                .add(SelectivepowersEntities.YELLOW_KING_BOSS.get())
                .add(SelectivepowersEntities.YELLOW_KING.get())
                .add(SelectivepowersEntities.LUNAR_MAIDEN.get());
        tag(ModTags.EntityTypes.DRAGON_LIKE)
                .add(EntityType.ENDER_DRAGON);
        tag(ModTags.EntityTypes.VILLAGER_LIKE)
                .add(EntityType.VILLAGER)
                .add(EntityType.VINDICATOR)
                .add(EntityType.PIGLIN_BRUTE)
                .add(EntityType.PIGLIN)
                .add(EntityType.WITCH)
                .add(EntityType.PILLAGER)
                .add(EntityType.ILLUSIONER)
                .add(EntityType.EVOKER)
                .add(EntityType.WANDERING_TRADER);
        tag(ModTags.EntityTypes.ELEMENTAL_PROJECTILES)
                .add(EntityType.FIREBALL)
                .add(EntityType.SMALL_FIREBALL)
                .add(EntityType.SNOWBALL)
                .add(EntityType.WIND_CHARGE)
                .add(EntityType.BREEZE_WIND_CHARGE);
    }
}
