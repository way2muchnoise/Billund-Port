/**
 * This file is part of Billund - http://www.computercraft.info/billund
 * Copyright Daniel Ratcliffe, 2013-2014. See LICENSE for license details.
 */

package com.dan200.billund.shared;

import net.minecraft.entity.player.EntityPlayer;

public interface IBillundProxy
{
	public boolean isClient();
	public void preLoad();
	public void load();

	public void openOrderFormGUI( EntityPlayer player );
}
