package net.blumasc.selectivepowers.entity;

import java.util.Arrays;
import java.util.Comparator;

public enum CircleVariant {
    MOON(0),
    SUN(1),
    CELESTIAL(2);

    private static final CircleVariant[] BY_ID = Arrays.stream(values()).sorted(
            Comparator.comparingInt(CircleVariant::getId)).toArray(CircleVariant[]::new);
    private final int id;

    CircleVariant(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static CircleVariant byId(int id){
        return BY_ID[id % BY_ID.length];
    }
}
