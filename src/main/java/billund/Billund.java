/**
 * This file is part of Billund - http://www.computercraft.info/billund
 * Copyright Daniel Ratcliffe, 2013-2014. See LICENSE for license details.
 */

package billund;

import billund.handler.ConfigHandler;
import billund.init.ModBlocks;
import billund.init.ModItems;
import billund.init.Recipes;
import billund.proxy.IProxy;
import billund.reference.Reference;
import billund.util.LogHelper;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION_FULL, guiFactory = Reference.GUI_FACTORY_CLASS)
public class Billund {
    @Mod.Instance(value = Reference.MOD_ID)
    public static Billund instance;

    @SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SEVER_PROXY_CLASS)
    public static IProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        //Initialize config
        ConfigHandler.init(event.getSuggestedConfigurationFile());
        FMLCommonHandler.instance().bus().register(new ConfigHandler());

        // Initialize mod items
        ModItems.init();

        // Initialize mod blocks
        ModBlocks.init();

        LogHelper.info("preInit Complete!");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        // Initialize mod tile entities
        proxy.registerTileEntities();

        // Initialize mod entities
        proxy.registerEntities();

        // Initialize custom rendering and pre-load textures (Client only)
        proxy.initRenderingAndTextures();

        // Register the Handlers
        proxy.registerHandlers();

        Recipes.init();

        LogHelper.info("init Complete!");
    }
}
