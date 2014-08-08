/**
 * This file is part of Billund - http://www.computercraft.info/billund
 * Copyright Daniel Ratcliffe, 2013-2014. See LICENSE for license details.
 */

package dan200.billund.server;

import net.minecraft.entity.player.EntityPlayer;

import dan200.billund.shared.BillundProxyCommon;

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
