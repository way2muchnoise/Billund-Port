package com.noise.billund.init;

import com.noise.billund.item.ItemBrick;
import com.noise.billund.item.ItemOrderForm;
import com.noise.billund.reference.Names;
import cpw.mods.fml.common.registry.GameRegistry;

public class ModItems {

    public static ItemBrick brick = new ItemBrick();
    public static ItemOrderForm orderForm = new ItemOrderForm();

    public static void init() {
        GameRegistry.registerItem(brick, Names.Items.BRICK);
        GameRegistry.registerItem(orderForm, Names.Items.ORDER_FORM);
    }
}
