/**
 * This file is part of Billund - http://www.computercraft.info/billund
 * Copyright Daniel Ratcliffe, 2013-2014. See LICENSE for license details.
 */

package com.dan200.billund.shared;

import com.dan200.billund.Billund;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

import java.util.Random;

public abstract class BillundProxyCommon implements IBillundProxy
{
	public BillundProxyCommon()
	{
	}
	
	// IBillundProxy implementation

	@Override
	public void preLoad()
	{
		registerItems();
	}
	
	@Override		
	public void load()
	{
		System.out.println( "Loading Billund v"+BuildInfo.Version );
		registerEntities();
		registerTileEntities();
		registerForgeHandlers();
	}
	
	@Override
	public abstract boolean isClient();

	@Override
	public abstract void openOrderFormGUI( EntityPlayer player );
		
	private void registerItems()
	{		
		// Register our own creative tab
		Billund.creativeTab = new CreativeTabBillund( CreativeTabs.getNextID(), "Billund" );

        // Setup blocks
        GameRegistry.registerBlock(Billund.ModBlocks.billund, "billund");

        // Setup items
        GameRegistry.registerItem(Billund.ModItems.brick, "brick");
        GameRegistry.registerItem(Billund.ModItems.orderForm, "orderForm");
	}
	
	private void registerEntities()
	{
		// airdrop entity
		EntityRegistry.registerModEntity( EntityAirDrop.class, "airDrop", 1, Billund.instance, 80, 3, true );
	}
		
	private void registerTileEntities()
	{
		// Tile Entities
		GameRegistry.registerTileEntity( TileEntityBillund.class, "billund" );
	}
		
	private void registerForgeHandlers()
	{
		ForgeHandlers handlers = new ForgeHandlers();
		MinecraftForge.EVENT_BUS.register( handlers );
	}
			
	public class ForgeHandlers
	{
		private Random r = new Random();
		
		private ForgeHandlers()
		{
		}

		// Forge event responses 
		
		@Mod.EventHandler
		public void onEntityLivingDeath( LivingDeathEvent event )
		{
			if( event.entity.worldObj.isRemote )
			{
				return;
			}
			
			if( event.entity instanceof EntityZombie )
			{
				EntityLivingBase living = (EntityLivingBase)event.entity;
				if( (living.isChild() && r.nextInt(20) == 0) ||
				    (!living.isChild() && r.nextInt(100) == 0) )
				{
					event.entity.entityDropItem( new ItemStack( Billund.ModItems.orderForm, 1 ), 0.0f );
				}
			}
		}
	}
}
