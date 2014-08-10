/**
 * This file is part of Billund - http://www.computercraft.info/billund
 * Copyright Daniel Ratcliffe, 2013-2014. See LICENSE for license details.
 */

package com.noise.billund.proxy;

import net.minecraft.entity.player.EntityPlayer;

public interface IProxy
{
    public abstract void registerTileEntities();
    public abstract void registerHandlers();
    public abstract void initRenderingAndTextures();
    public abstract void registerEntities();
	public abstract void openOrderFormGUI( EntityPlayer player );
    public abstract boolean isClient();
}