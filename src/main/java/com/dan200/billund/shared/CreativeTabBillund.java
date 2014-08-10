/**
 * This file is part of Billund - http://www.computercraft.info/billund
 * Copyright Daniel Ratcliffe, 2013-2014. See LICENSE for license details.
 */

package com.dan200.billund.shared;

import com.dan200.billund.Billund;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CreativeTabBillund extends CreativeTabs
{
    public CreativeTabBillund(String p2 )
    {
        super( p2 );
    }
    
    @Override
    public ItemStack getIconItemStack()
    {
        return ItemBrick.create( StudColour.Red, 2, 2, 1 ); 
    }

    @Override
    public Item getTabIconItem() {
        return Billund.ModItems.brick;
    }

    @Override
    public String getTranslatedTabLabel()
    {
    	return getTabLabel();
    }
}
