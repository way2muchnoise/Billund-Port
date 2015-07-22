package billund.set;

import billund.reference.Colour;

public class Bricks
{
    public Colour colour;
    public int width;
    public int depth;
    public int quantity;

    public Bricks(Colour colour, int width, int depth, int quantity)
    {
        this.colour = colour;
        this.width = width;
        this.depth = depth;
        this.quantity = quantity;
    }

    public Bricks(Bricks brick)
    {
        this.colour = brick.colour;
        this.width = brick.width;
        this.depth = brick.depth;
        this.quantity = brick.quantity;
    }

    public Bricks setColour(Colour colour)
    {
        this.colour = colour;
        return this;
    }

    public static Bricks fromString(String string)
    {
        String[] splitted = string.split(",");
        int[] ints = new int[splitted.length-1];
        for (int i = 1; i < splitted.length; i++)
            ints[i-1] = Integer.parseInt(splitted[i]);
        return new Bricks(Colour.getNamed(splitted[0]), ints[0], ints[1], ints[2]);
    }

    public String asString()
    {
        return (this.colour != null ? this.colour.name : "null") + "," + this.width + "," + this.depth + "," + this.quantity;
    }

    @Override
    public String toString()
    {
        return (this.colour != null ? this.colour.name : "null") + ": " + this.width + "x" + this.depth + " " + this.quantity;
    }
}
