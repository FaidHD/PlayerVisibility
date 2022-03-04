package de.faidhd.playervisibility.objects;

public enum VisibilityType {

    ALL,
    VIP,
    NONE;

    public static VisibilityType getVisibilityTypeByName(String name) {
        for (VisibilityType visibilityType : values())
            if (visibilityType.name().equalsIgnoreCase(name))
                return visibilityType;
        return null;
    }

}
