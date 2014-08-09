/**
 * This file is part of Billund - http://www.computercraft.info/billund
 * Copyright Daniel Ratcliffe, 2013-2014. See LICENSE for license details.
 */
 
package com.dan200.billund;

import com.dan200.billund.shared.*;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;

import java.util.Random;

///////////////
// UNIVERSAL //
///////////////

@Mod( modid = "Billund", name = BuildInfo.ModName, version = BuildInfo.Version )
public class Billund
{
	// Configuration options
	// None
	
	// Blocks and Items
	public static class ModBlocks
	{
		public static BlockBillund billund = new BlockBillund();
	}
	
	public static class ModItems
	{
		public static ItemBrick brick = new ItemBrick();
		public static ItemOrderForm orderForm = new ItemOrderForm();
	}
	
	// Other stuff	
	public static CreativeTabs creativeTab;
	
	public static CreativeTabs getCreativeTab()
	{
		return creativeTab;
	}
	
	// Implementation
	@Mod.Instance( value = "Billund" )
	public static Billund instance;
	
	@SidedProxy( clientSide = "com.dan200.billund.client.BillundProxyClient", serverSide = "com.dan200.billund.server.BillundProxyServer" )
	public static IBillundProxy proxy;
	
	public Billund()
	{
	}
	
	@Mod.EventHandler
	public void preInit( FMLPreInitializationEvent event )
	{
		// Load config
		Configuration config = new Configuration( event.getSuggestedConfigurationFile() );
		config.load();
		
		// Setup general
		PacketHandler.init();
		
		// Save config
		config.save();
		
		proxy.preLoad();
	}
	
	@Mod.EventHandler
	public void init( FMLInitializationEvent event )
	{	
		proxy.load();
	}
	
	public static boolean isClient()
	{
		return proxy.isClient();
	}
		
	public static boolean isServer()
	{
		return !proxy.isClient();
	}
	
	public static String getVersion()
	{
		return BuildInfo.Version;
	}
	
	private static boolean removeEmeralds( EntityPlayer player, int cost )
	{
		// Find enough emeralds
		int emeralds = 0;
		for( int i=0; i<player.inventory.getSizeInventory(); ++i )
		{
			ItemStack stack = player.inventory.getStackInSlot( i );
            if( stack != null && stack.getItem() == Items.emerald )
			{
				emeralds += stack.stackSize;
				if( emeralds >= cost )
				{
					break;
				}
			}
		}
		
		if( emeralds >= cost )
		{
			// Then expend them
			emeralds = cost;
			for( int i=0; i<player.inventory.getSizeInventory(); ++i )
			{
				ItemStack stack = player.inventory.getStackInSlot( i );
				if( stack != null && stack.getItem() == Items.emerald )
				{
					if( stack.stackSize <= emeralds )
					{
						player.inventory.setInventorySlotContents( i, null );
						emeralds -= stack.stackSize;
					}
					else
					{
						stack.stackSize -= emeralds;
						emeralds = 0;
					}
					if( emeralds == 0 )
					{
						break;
					}
				}
			}
			player.inventory.markDirty();
			return true;
		}
		return false;
	}
	
	public static void handlePacket( BillundPacket packet, EntityPlayer player )
	{
		switch( packet.packetType )
		{
			case BillundPacket.OrderSet:
			{
				World world = player.worldObj;
				int set = packet.dataInt[0];
				int cost = BillundSet.get( set ).getCost();
				if( removeEmeralds( player, cost ) )
				{
					Random r = new Random();
					world.spawnEntityInWorld( new EntityAirDrop(
						world,
						Math.floor( player.posX - 8 + r.nextInt(16) ) + 0.5f,
						Math.min( world.getHeight(), 255 ) - r.nextInt(32) - 0.5f,
						Math.floor( player.posZ - 8 + r.nextInt(16) ) + 0.5f,
						set
					) );
				}
				break;
			}
		}
	}
	
	public static void openOrderFormGUI( EntityPlayer player )
	{
		proxy.openOrderFormGUI( player );
    }
}
