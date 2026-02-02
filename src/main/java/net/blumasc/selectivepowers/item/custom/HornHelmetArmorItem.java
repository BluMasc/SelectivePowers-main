package net.blumasc.selectivepowers.item.custom;

import net.blumasc.selectivepowers.SelectivePowers;
import net.blumasc.selectivepowers.item.SelectivepowersItems;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

import javax.annotation.Nullable;

public class HornHelmetArmorItem extends  AbstractArmorItem{
    private static final ResourceLocation TEXTURE_LOCATION = ResourceLocation.fromNamespaceAndPath(SelectivePowers.MODID, "textures/entity/armor/horned_helmet.png");
    public HornHelmetArmorItem() {

        super(ArmorMaterials.LEATHER, Type.HELMET, new Properties().rarity(Rarity.RARE));
    }

    @Override
    public @Nullable ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, ArmorMaterial.Layer layer, boolean inner) {
        return TEXTURE_LOCATION;
    }
}
