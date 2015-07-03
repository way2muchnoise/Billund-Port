package billund.init;

import billund.item.ItemBrick;
import billund.item.ItemOrderForm;
import billund.reference.Names;
import cpw.mods.fml.common.registry.GameRegistry;

public class ModItems {

    public static ItemBrick brick = new ItemBrick();
    public static ItemOrderForm orderForm = new ItemOrderForm();

    public static void init() {
        GameRegistry.registerItem(brick, Names.Items.BRICK);
        GameRegistry.registerItem(orderForm, Names.Items.ORDER_FORM);
    }
}
