package billund.registry;

import billund.item.ItemBrick;
import billund.item.ItemOrderForm;
import billund.reference.Names;
import cpw.mods.fml.common.registry.GameRegistry;

public class ItemRegistry
{
    public static ItemBrick brick;
    public static ItemOrderForm orderForm;

    public static void init()
    {
        brick = new ItemBrick();
        GameRegistry.registerItem(brick, Names.Items.BRICK);
        orderForm = new ItemOrderForm();
        GameRegistry.registerItem(orderForm, Names.Items.ORDER_FORM);
    }
}
