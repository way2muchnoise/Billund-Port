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
}
