package net.blumasc.selectivepowers.entity.helper;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SquidSortHelper {

    private static final String[] MOON_WORDS = {"moon", "lunar", "cheese", "night", "blue"};
    private static final String[] SUN_WORDS = {"sun", "solar", "ray", "day", "yellow","gold","crown"};

    public static final Comparator<ItemStack> squidSortComparator = (a, b) -> {
        if (a.isEmpty() && b.isEmpty()) return 0;
        if (a.isEmpty()) return 1;
        if (b.isEmpty()) return -1;

        int categoryA = getCategory(a);
        int categoryB = getCategory(b);
        if (categoryA != categoryB) return Integer.compare(categoryA, categoryB);

        char secondA = secondLetter(a);
        char secondB = secondLetter(b);
        int cmp = Character.compare(secondB, secondA);
        if (cmp != 0) return cmp;

        cmp = Integer.compare(a.getCount(), b.getCount());
        if (cmp != 0) return cmp;

        cmp = Integer.compare(thirdLetterOrder(a), thirdLetterOrder(b));
        if (cmp != 0) return cmp;

        return getItemName(a).compareTo(getItemName(b));
    };

    private static int getCategory(ItemStack stack) {
        if (hasMoonWord(stack)) return 0;
        if (stack.has(DataComponents.FOOD)) return 1;
        if (!hasSunWord(stack)) return 2;
        return 3;
    }

    private static String getItemName(ItemStack stack) {
        Item item = stack.getItem();
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(item);
        if (id == null) return item.toString();
        String fullId = id.toString();
        int colon = fullId.indexOf(':');
        return colon >= 0 ? fullId.substring(colon + 1) : fullId;
    }

    private static char secondLetter(ItemStack stack) {
        String name = getItemName(stack);
        return name.length() >= 2 ? name.charAt(1) : '\0';
    }

    private static int thirdLetterOrder(ItemStack stack) {
        String name = getItemName(stack);
        if (name.length() < 3) return -1;
        char c = name.charAt(2);
        if (c >= 'f' && c <= 'z') return c - 'f';
        if (c >= 'a' && c <= 'e') return 21 + (c - 'a');
        return 26;
    }

    private static boolean hasMoonWord(ItemStack stack) {
        String name = getItemName(stack).toLowerCase();
        for (String word : MOON_WORDS) if (name.contains(word)) return true;
        return false;
    }

    private static boolean hasSunWord(ItemStack stack) {
        String name = getItemName(stack).toLowerCase();
        for (String word : SUN_WORDS) if (name.contains(word)) return true;
        return false;
    }

    public static boolean isSorted(Container container) {
        List<ItemStack> items = IntStream.range(0, container.getContainerSize())
                .mapToObj(container::getItem)
                .toList();

        List<ItemStack> sorted = items.stream()
                .sorted(squidSortComparator)
                .toList();

        if (items.size() != sorted.size()) return false;

        for (int i = 0; i < items.size(); i++) {
            if (!ItemStack.matches(items.get(i), sorted.get(i))) return false;
        }

        return true;
    }

    public static void sortContainer(Container container) {
        List<ItemStack> items = IntStream.range(0, container.getContainerSize())
                .mapToObj(container::getItem)
                .filter(stack -> !stack.isEmpty()).sorted(squidSortComparator).toList();

        for (int i = 0; i < container.getContainerSize(); i++) {
            container.setItem(i, ItemStack.EMPTY);
        }

        for (int i = 0; i < items.size(); i++) {
            container.setItem(i, items.get(i));
        }
    }

    public static boolean insertionSortStep(Container container, int step) {
        int size = container.getContainerSize();
        if (step >= size) return true;

        List<ItemStack> items = IntStream.range(0, step + 1)
                .mapToObj(container::getItem).sorted(squidSortComparator).toList();

        for (int i = 0; i < items.size(); i++) {
            container.setItem(i, items.get(i));
        }
        return step + 1 >= size;
    }
}

