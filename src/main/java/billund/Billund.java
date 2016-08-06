/**
 * This file is part of Billund - http://www.computercraft.info/billund
 * Copyright Daniel Ratcliffe, 2013-2014. See LICENSE for license details.
 */

package billund;

import billund.command.ReloadCommand;
import billund.handler.ConfigHandler;
import billund.proxy.CommonProxy;
import billund.reference.MetaData;
import billund.reference.Reference;
import billund.registry.BlockRegistry;
import billund.registry.ItemRegistry;
import billund.registry.RecipeRegistry;
import billund.set.BillundSetLoader;
import billund.util.LogHelper;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;

@Mod(modid = Reference.ID, name = Reference.NAME, version = Reference.VERSION_FULL, guiFactory = Reference.GUI_FACTORY_CLASS)
public class Billund
{
    @Mod.Instance(Reference.ID)
    public static Billund instance;

    @SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SEVER_PROXY_CLASS)
    public static CommonProxy proxy;

    @Mod.Metadata(Reference.ID)
    public static ModMetadata metadata;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        // Initialize config
        ConfigHandler.init();
        FMLCommonHandler.instance().bus().register(new ConfigHandler());

        // Init MetaData
        metadata = MetaData.init(metadata);

        // Initialize mod items
        ItemRegistry.init();

        // Initialize mod blocks
        BlockRegistry.init();

        LogHelper.info("preInit Complete!");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        // Initialize mod tile entities
        proxy.registerTileEntities();

        // Initialize mod entities
        proxy.registerEntities();

        // Initialize custom rendering and pre-load textures (Client only)
        proxy.initRenderingAndTextures();

        // Register the Handlers
        proxy.registerHandlers();

        // Register Recipes
        RecipeRegistry.init();

        LogHelper.info("init Complete!");
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        LogHelper.info("postInit Complete!");
    }

    @Mod.EventHandler
    public void onServerStarting(FMLServerStartingEvent event)
    {
        event.registerServerCommand(new ReloadCommand());

        LogHelper.info("serverStarting Complete!");
    }

    @Mod.EventHandler
    public void onServerStarted(FMLServerStartedEvent event)
    {
        // Load Billund Sets
        BillundSetLoader.loadSets();

        LogHelper.info("serverStarted Complete!");
    }
}
