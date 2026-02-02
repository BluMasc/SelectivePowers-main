package net.blumasc.selectivepowers.datagen;

import net.blumasc.selectivepowers.entity.SelectivepowersEntities;
import net.blumasc.selectivepowers.item.SelectivepowersItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithEnchantedBonusCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.stream.Stream;

public class ModEntityLoot extends EntityLootSubProvider {
    protected ModEntityLoot(HolderLookup.Provider registries) {
        super(FeatureFlags.DEFAULT_FLAGS, registries);
    }

    @Override
    public void generate() {
        HolderLookup.RegistryLookup<Enchantment> registrylookup =
                this.registries.lookupOrThrow(Registries.ENCHANTMENT);

        add(SelectivepowersEntities.CROW.get(),
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(UniformGenerator.between(1.0F, 3.0F))
                                .add(LootItem.lootTableItem(Items.FEATHER))
                                )
        );
        add(SelectivepowersEntities.QUETZAL.get(),
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(UniformGenerator.between(1.0F, 3.0F))
                                .add(LootItem.lootTableItem(SelectivepowersItems.BURNING_FEATHER.get()))
                        )
        );
        add(SelectivepowersEntities.QUETZAL_YELLOW.get(),
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(UniformGenerator.between(1.0F, 3.0F))
                                .add(LootItem.lootTableItem(SelectivepowersItems.BURNING_FEATHER.get()))
                        )
        );
        add(SelectivepowersEntities.CORRUPTING_MASK.get(),
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                .add(LootItem.lootTableItem(SelectivepowersItems.CORRUPTION_SHARD.get()))
                                .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries,0.5f, 0.1f))
                        )
        );
        add(SelectivepowersEntities.YELLOW_FANATIC.get(),
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                .add(LootItem.lootTableItem(SelectivepowersItems.YELLOW_RAMBLINGS.get()))
                                .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries,0.3f, 0.2f))
                        )
        );
        add(SelectivepowersEntities.YELLOW_KING_BOSS.get(),
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                .add(LootItem.lootTableItem(SelectivepowersItems.BLESSED_IDOL.get()))
                                )
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                .add(LootItem.lootTableItem(SelectivepowersItems.FAKE_CROWN.get()))
                                .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries,0.2f, 0.1f))
                        )
        );
        add(SelectivepowersEntities.LUNAR_MAIDEN.get(),
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                .add(LootItem.lootTableItem(SelectivepowersItems.MOON_PENDANT.get()))
                        )
        );
    }

    @Override
    protected Stream<EntityType<?>> getKnownEntityTypes() {
        return Stream.of(
                SelectivepowersEntities.CROW.get(),
                SelectivepowersEntities.YELLOW_KING_BOSS.get(),
                SelectivepowersEntities.YELLOW_FANATIC.get(),
                SelectivepowersEntities.CORRUPTING_MASK.get(),
                SelectivepowersEntities.QUETZAL.get(),
                SelectivepowersEntities.QUETZAL_YELLOW.get(),
                SelectivepowersEntities.LUNAR_MAIDEN.get()
        );
    }

}
