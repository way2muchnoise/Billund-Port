/**
 * This file is part of Billund - http://www.computercraft.info/billund
 * Copyright Daniel Ratcliffe, 2013-2014. See LICENSE for license details.
 */

package com.noise.billund.creativetab;

import com.noise.billund.item.ItemBrick;
import com.noise.billund.reference.Reference;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CreativeTabBillund
{
    public static final CreativeTabs BILLUND_TAB = new CreativeTabs(Reference.MOD_ID.toLowerCase()) {
        @Override
        public ItemStack getIconItemStack() {
            return ItemBrick.create(0, 2, 2, 1);
        }

        @Override
        public Item getTabIconItem() {
            return null;
        }
    };
}
