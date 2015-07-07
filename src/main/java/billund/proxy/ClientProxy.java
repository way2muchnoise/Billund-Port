/**
 * This file is part of Billund - http://www.computercraft.info/billund
 * Copyright Daniel Ratcliffe, 2013-2014. See LICENSE for license details.
 */

package billund.proxy;

import billund.client.gui.GuiOrderForm;
import billund.client.render.BillundBlockRenderingHandler;
import billund.client.render.BrickRenderer;
import billund.client.render.tileentity.TileEntityRendererAirDrop;
import billund.handler.ForgeEventHandler;
import billund.init.ModBlocks;
import billund.init.ModItems;
import billund.tileentity.TileEntityAirDrop;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy
{

    @Override
    public boolean isClient()
    {
        return true;
    }

    public void initRenderingAndTextures()
    {
        ModBlocks.billund.blockRenderID = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new BillundBlockRenderingHandler());
        MinecraftForgeClient.registerItemRenderer(ModItems.brick, new BrickRenderer());
        RenderingRegistry.registerEntityRenderingHandler(TileEntityAirDrop.class, new TileEntityRendererAirDrop());
    }

    @Override
    public void registerHandlers()
    {
        super.registerHandlers();
        MinecraftForge.EVENT_BUS.register(new ForgeEventHandler());
        FMLCommonHandler.instance().bus().register(new ForgeEventHandler());
    }

    @Override
    public void openOrderFormGUI(EntityPlayer player)
    {
        GuiScreen gui = new GuiOrderForm(player);
        FMLClientHandler.instance().displayGuiScreen(player, gui);
    }
}
