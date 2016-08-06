package billund.registry;

import billund.item.ItemBrick;
import billund.item.ItemOrderForm;
import billund.reference.Names;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemRegistry
{
    public static ItemBrick brick;
    public static ItemOrderForm orderForm;

    public static void init()
    {
        brick = new ItemBrick();
        orderForm = new ItemOrderForm();
    }
}
