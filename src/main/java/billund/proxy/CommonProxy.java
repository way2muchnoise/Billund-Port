/**
 * This file is part of Billund - http://www.computercraft.info/billund
 * Copyright Daniel Ratcliffe, 2013-2014. See LICENSE for license details.
 */

package billund.proxy;

import billund.Billund;
import billund.handler.ForgeServerEventHandler;
import billund.network.MessageHandler;
import billund.tileentity.TileEntityAirDrop;
import billund.tileentity.TileEntityBillund;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy
{
    public void openOrderFormGUI(EntityPlayer player)
    {
        // NOOP
    }

    public void initRenderingAndTextures()
    {
        // NOOP
    }

    public boolean isClient()
    {
        return false;
    }

    public void registerEntities()
    {
        // airdrop entity
        EntityRegistry.registerModEntity(TileEntityAirDrop.class, "airDrop", 1, Billund.instance, 80, 3, true);
    }

    public void registerTileEntities()
    {
        // Tile Entities
        GameRegistry.registerTileEntity(TileEntityBillund.class, "billund");
    }

    public void registerHandlers()
    {
        MinecraftForge.EVENT_BUS.register(new ForgeServerEventHandler());
        MessageHandler.init();
    }
}
