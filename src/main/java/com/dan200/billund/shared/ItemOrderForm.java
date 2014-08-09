/**
 * This file is part of Billund - http://www.computercraft.info/billund
 * Copyright Daniel Ratcliffe, 2013-2014. See LICENSE for license details.
 */

package com.dan200.billund.shared;

import com.dan200.billund.Billund;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.List;

public class ItemOrderForm extends Item
{
	private static IIcon s_icon;
	
	public ItemOrderForm()
    {
        super();
        setMaxStackSize( 1 );
		setHasSubtypes( true );
		setUnlocalizedName( "billform" );
		setCreativeTab( Billund.getCreativeTab() );
    }

	public static ItemStack create( int colour, int width, int depth, int quantity )
	{
		int damage = ((width - 1) & 0x1) + (((depth - 1) & 0x7) << 1) + ((colour & 0xf) << 4);
		return new ItemStack( Billund.ModItems.brick, quantity, damage );
	}

    @Override
    public void getSubItems( Item item, CreativeTabs tabs, List list )
    {
		list.add( new ItemStack( Billund.ModItems.orderForm, 1, 0 ) );
    }
    
	@Override
    public ItemStack onItemRightClick( ItemStack stack, World world, EntityPlayer player )
    {
    	if( Billund.isClient() && world.isRemote )
    	{
    		Billund.openOrderFormGUI( player );
    	}
		return stack;
	}

	@Override
	public void registerIcons( IIconRegister iconRegister )
	{
		s_icon = iconRegister.registerIcon( "billund:orderform" );
	}

    @Override
    public IIcon getIconFromDamage( int damage )
    {
    	return s_icon;
    }
}
