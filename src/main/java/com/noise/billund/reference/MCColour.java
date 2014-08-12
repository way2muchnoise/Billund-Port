package com.noise.billund.reference;

public enum MCColour {
    BLACK(0, "black"),
    RED(1, "red"),
    GREEN(2, "green"),
    BROWN(3, "brown"),
    BLUE(4, "blue"),
    PURPLE(5, "purple"),
    CYAN(6, "cyan"),
    LIGHT_GREY(7, "lightGrey"),
    GREY(8, "grey"),
    PINK(9, "pink"),
    LIME(10, "lime"),
    YELLOW(11, "yellow"),
    LIGHT_BLUE(12, "lightBlue"),
    MAGENTA(13, "magenta"),
    ORANGE(14, "orange"),
    WHITE(15, "white");

    public final int number;
    public final String name;
    MCColour(int number, String name) {
        this.number = number;
        this.name = name;
    }
}
