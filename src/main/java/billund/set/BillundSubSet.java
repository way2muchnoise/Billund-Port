package billund.set;

import billund.reference.Colour;

import java.util.LinkedList;
import java.util.List;

public class BillundSubSet
{
    private String name;
    private List<Bricks> bricks;

    public BillundSubSet(String name)
    {
        this.name = name;
        this.bricks = new LinkedList<Bricks>();
    }

    public void addBricks(int width, int depth, int quantity)
    {
        this.bricks.add(new Bricks(null, width, depth, quantity));
    }

    public String getName()
    {
        return this.name;
    }

    public List<Bricks> getBricks(Colour colour)
    {
        List<Bricks> coloured = new LinkedList<Bricks>();
        for (Bricks brick : this.bricks)
            coloured.add(new Bricks(brick).setColour(colour));
        return coloured;
    }

    @Override
    public String toString()
    {
        return this.name;
    }
}
