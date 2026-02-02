package net.blumasc.selectivepowers.entity;

import java.util.Arrays;
import java.util.Comparator;

public enum PackwingVariant {
    CYAN(0),
    PINK(1),
    LIME(2),
    RED(3),
    BLUE(4);

    private static final PackwingVariant[] BY_ID = Arrays.stream(values()).sorted(
            Comparator.comparingInt(PackwingVariant::getId)).toArray(PackwingVariant[]::new);
    private final int id;

    PackwingVariant(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static PackwingVariant byId(int id){
        return BY_ID[id % BY_ID.length];
    }
}
