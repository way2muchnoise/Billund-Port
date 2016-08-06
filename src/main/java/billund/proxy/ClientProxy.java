/**
 * This file is part of Billund - http://www.computercraft.info/billund
 * Copyright Daniel Ratcliffe, 2013-2014. See LICENSE for license details.
 */

package billund.proxy;

import billund.gui.GuiOrderForm;
import billund.handler.ForgeClientEventHandler;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy
{
    public static byte rotation = 0;

    @Override
    public boolean isClient()
    {
        return true;
    }

    public void initRenderingAndTextures()
    {
        //BlockRegistry.billund.blockRenderID = RenderingRegistry.getNextAvailableRenderId();
        //RenderingRegistry.registerBlockHandler(new BillundBlockRenderingHandler());
        //MinecraftForgeClient.registerItemRenderer(ItemRegistry.brick, new BrickRenderer());
        //RenderingRegistry.registerEntityRenderingHandler(TileEntityAirDrop.class, new TileEntityRendererAirDrop());
    }

    @Override
    public void registerHandlers()
    {
        super.registerHandlers();
        MinecraftForge.EVENT_BUS.register(new ForgeClientEventHandler());
        MinecraftForge.EVENT_BUS.register(new ForgeClientEventHandler());
        ClientRegistry.registerKeyBinding(ForgeClientEventHandler.KEY_ROTATE);
    }

    @Override
    public void openOrderFormGUI(EntityPlayer player)
    {
        GuiScreen gui = new GuiOrderForm(player);
        FMLClientHandler.instance().displayGuiScreen(player, gui);
    }
}
