package billund.set;

import billund.item.ItemBrick;
import billund.reference.Colour;
import billund.registry.BillundSubSetRegistry;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.StatCollector;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class BillundSet
{
    private String name;
    private int cost;
    private List<Bricks> bricksList;

    public BillundSet(String name, int cost)
    {
        this.name = name;
        this.cost = cost;
        this.bricksList = new LinkedList<Bricks>();
    }

    public int getCost()
    {
        return this.cost;
    }

    public String getName()
    {
        return this.name;
    }

    public String getLocalizedName()
    {
        return StatCollector.translateToLocal("billund.set." + this.name);
    }

    public void populateChest(IInventory inv)
    {
        int slot = 0;
        for (Bricks brick : this.bricksList)
        {
            if (slot > inv.getSizeInventory()) break;
            inv.setInventorySlotContents(slot++, brick == null ? null : ItemBrick.create(brick));
        }
    }

    public void addBricks(Colour colour, int width, int depth, int quantity)
    {
        this.bricksList.add(new Bricks(colour, width, depth, quantity));
    }

    public void addBricks(Bricks bricks)
    {
        this.bricksList.add(bricks);
    }

    public void addBricks(Collection<Bricks> bricks)
    {
        this.bricksList.addAll(bricks);
    }

    public void addBricks(String subset, Colour colour)
    {
        BillundSubSet subSet = BillundSubSetRegistry.instance().getBillundSubSet(subset);
        if (subSet != null)
            this.bricksList.addAll(subSet.getBricks(colour));
    }

    @Override
    public String toString()
    {
        return this.name;
    }
}
