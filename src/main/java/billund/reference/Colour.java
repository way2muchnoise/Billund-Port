package billund.reference;

public enum Colour
{
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
    WHITE(15, "white"),

    TRANSLUCENT_WALL(-2, "translucentWall"),
    WALL(-1, "wall");


    public final int number;
    public final String name;

    Colour(int number, String name)
    {
        this.number = number;
        this.name = name;
    }

    public static int count()
    {
        return 16;
    }

    public static Colour getValue(int number)
    {
        for (Colour p : values())
        {
            if (p.number == number)
            {
                return p;
            }
        }
        //should never happen
        return null;
    }

    public static Colour getNamed(String name)
    {
        for (Colour p : values())
        {
            if (p.name.toLowerCase().equals(name.toLowerCase()))
            {
                return p;
            }
        }
        //should never happen
        return null;
    }

    @Override
    public String toString()
    {
        return this.name;
    }
}
