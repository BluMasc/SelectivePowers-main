package net.blumasc.selectivepowers.datagen;

import net.blumasc.selectivepowers.SelectivePowers;
import net.blumasc.selectivepowers.damage.SelectivePowersDamageTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.tags.DamageTypeTags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModDamageTagProvider extends DamageTypeTagsProvider {
    public ModDamageTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, SelectivePowers.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(DamageTypeTags.NO_KNOCKBACK)
                .add(SelectivePowersDamageTypes.RAGE_DAMAGE)
                .add(SelectivePowersDamageTypes.LUNAR_DAMAGE)
                .add(SelectivePowersDamageTypes.SOLAR_DAMAGE)
                .add(SelectivePowersDamageTypes.SPIKE_DAMAGE);
        tag(DamageTypeTags.BYPASSES_COOLDOWN)
                .add(SelectivePowersDamageTypes.RAGE_DAMAGE);
        tag(DamageTypeTags.BYPASSES_INVULNERABILITY)
                .add(SelectivePowersDamageTypes.RAGE_DAMAGE);
        tag(DamageTypeTags.BYPASSES_SHIELD)
                .add(SelectivePowersDamageTypes.RAGE_DAMAGE)
                .add(SelectivePowersDamageTypes.SPIKE_DAMAGE)
                .add(SelectivePowersDamageTypes.SOUL_DAMAGE);
        tag(DamageTypeTags.BYPASSES_ARMOR)
                .add(SelectivePowersDamageTypes.SOUL_DAMAGE);
    }
}
