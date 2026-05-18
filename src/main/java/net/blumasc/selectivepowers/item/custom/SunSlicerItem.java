package net.blumasc.selectivepowers.item.custom;

import net.blumasc.selectivepowers.damage.SelectivePowersDamageTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;

public class SunSlicerItem extends SwordItem {
    public SunSlicerItem(Properties properties) {
        super(Tiers.NETHERITE, properties);
    }
}
