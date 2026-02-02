package net.blumasc.selectivepowers.entity;

import java.util.Arrays;
import java.util.Comparator;

public enum CrowVariant {
    GRAY(0),
    WHITE(1),
    BLUE(2);

    private static final CrowVariant[] BY_ID = Arrays.stream(values()).sorted(
            Comparator.comparingInt(CrowVariant::getId)).toArray(CrowVariant[]::new);
    private final int id;

    CrowVariant(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static CrowVariant byId(int id){
        return BY_ID[id % BY_ID.length];
    }
}
