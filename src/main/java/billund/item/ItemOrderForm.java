/**
 * This file is part of Billund - http://www.computercraft.info/billund
 * Copyright Daniel Ratcliffe, 2013-2014. See LICENSE for license details.
 */

package billund.item;

import billund.Billund;
import billund.reference.Names;
import billund.registry.ItemRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import java.util.List;

public class ItemOrderForm extends ItemBillund
{

    public ItemOrderForm()
    {
        super();
        setMaxStackSize(1);
        setHasSubtypes(true);
        setUnlocalizedName(Names.Items.ORDER_FORM);
    }

    public static ItemStack create(int colour, int width, int depth, int quantity)
    {
        int damage = ((width - 1) & 0x1) + (((depth - 1) & 0x7) << 1) + ((colour & 0xf) << 4);
        return new ItemStack(ItemRegistry.brick, quantity, damage);
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tabs, List list)
    {
        list.add(new ItemStack(ItemRegistry.orderForm, 1, 0));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand)
    {
        if (Billund.proxy.isClient() && world.isRemote)
            Billund.proxy.openOrderFormGUI(player);
        return ActionResult.newResult(EnumActionResult.PASS, stack);
    }
}
