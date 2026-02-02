package net.blumasc.selectivepowers.item.client.gui;

import net.blumasc.selectivepowers.SelectivePowers;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModMenus {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, SelectivePowers.MODID);

    public static final Supplier<MenuType<LoreScrollMenu>> LORE_SCROLL_MENU =
            MENUS.register("lore_scroll_menu",
                    () -> IMenuTypeExtension.create(LoreScrollMenu::new));
}
