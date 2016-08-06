package billund.set;

import billund.item.ItemBrick;
import billund.reference.Colour;
import billund.registry.BillundSubSetRegistry;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.text.translation.I18n;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class BillundSet
{
    private String name, localName;
    private int cost;
    private List<Bricks> bricksList;

    public BillundSet(String name, int cost)
    {
        this.name = name;
        this.localName = null;
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

    public BillundSet setLocalizedName(String name)
    {
        this.localName = name;
        return this;
    }

    public String getLocalizedName()
    {
        return this.localName == null ? I18n.translateToLocal("billund.set." + this.name) : this.localName;
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

    public static BillundSet fromString(String string)
    {
        String[] splitted = string.split(";", 4);
        BillundSet set = new BillundSet(splitted[0], Integer.parseInt(splitted[1]));
        set.setLocalizedName(splitted[2].equals("null") ? null : splitted[2]);
        String[] allBricks = splitted[3].split(";");
        for (String bricks : allBricks)
            set.addBricks(bricks.equals("null") ? null : Bricks.fromString(bricks));
        return set;
    }

    public String asString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append(this.name).append(";");
        builder.append(this.cost).append(";");
        builder.append(this.localName).append(";");
        for (Bricks bricks : this.bricksList)
            builder.append(bricks == null ? "null" : bricks.asString()).append(";");
        return builder.toString();
    }

    @Override
    public String toString()
    {
        return this.name;
    }
}
