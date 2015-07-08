/**
 * This file is part of Billund - http://www.computercraft.info/billund
 * Copyright Daniel Ratcliffe, 2013-2014. See LICENSE for license details.
 */

package billund.creativetab;

import billund.item.ItemBrick;
import billund.reference.Colour;
import billund.reference.Reference;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CreativeTab
{
    public static final CreativeTabs BILLUND_TAB = new CreativeTabs(Reference.MOD_ID.toLowerCase())
    {
        @Override
        public ItemStack getIconItemStack()
        {
            return ItemBrick.create(Colour.RED, 2, 2, 1);
        }

        @Override
        public Item getTabIconItem()
        {
            return null;
        }
    };
}
