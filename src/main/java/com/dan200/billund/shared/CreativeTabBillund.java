/**
 * This file is part of Billund - http://www.computercraft.info/billund
 * Copyright Daniel Ratcliffe, 2013-2014. See LICENSE for license details.
 */

package dan200.billund.shared;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class CreativeTabBillund extends CreativeTabs
{
    public CreativeTabBillund( int p1, String p2 )
    {
        super( p1, p2 );
    }
    
    @Override
    public ItemStack getIconItemStack()
    {
        return ItemBrick.create( StudColour.Red, 2, 2, 1 ); 
    }
    
    @Override
    public String getTranslatedTabLabel()
    {
    	return getTabLabel();
    }
}
