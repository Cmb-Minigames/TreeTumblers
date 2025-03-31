package xyz.devcmb.treeTumblers.interfaces;

public enum UIPriority {
    HIGHEST(5),
    HIGH(4),
    MEDIUM(3),
    LOW(2),
    LOWEST(1);

    public final int value;
    UIPriority(int value) {
        this.value = value;
    }
}
