package net.blumasc.selectivepowers.item.client.gui;

import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ClientPageOpener {

    public static void open(int page) {
        Minecraft.getInstance().setScreen(new PageScreen(page));
    }
}
