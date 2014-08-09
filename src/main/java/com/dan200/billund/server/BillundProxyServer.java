/**
 * This file is part of Billund - http://www.computercraft.info/billund
 * Copyright Daniel Ratcliffe, 2013-2014. See LICENSE for license details.
 */

package com.dan200.billund.server;

import com.dan200.billund.shared.BillundProxyCommon;
import net.minecraft.entity.player.EntityPlayer;

public class BillundProxyServer extends BillundProxyCommon
{
	public BillundProxyServer()
	{
	}
	
	// IBillundProxy implementation
	
	@Override
	public void load()
	{
		super.load();
		registerForgeHandlers();
	}
	
	@Override
	public boolean isClient()
	{
		return false;
	}

	@Override
	public void openOrderFormGUI( EntityPlayer player )
	{
	}
	
	// private stuff
	
	private void registerForgeHandlers()
	{
	}
}
