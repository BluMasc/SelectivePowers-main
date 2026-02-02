package net.blumasc.selectivepowers.item.custom;

import com.mojang.blaze3d.vertex.PoseStack;
import net.blumasc.selectivepowers.SelectivePowers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

public class MoonGlaiveItem extends SwordItem {
    public MoonGlaiveItem(Properties properties) {
        super(Tiers.NETHERITE, properties);
    }


}
