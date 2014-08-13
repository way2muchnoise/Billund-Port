/**
 * This file is part of Billund - http://www.computercraft.info/billund
 * Copyright Daniel Ratcliffe, 2013-2014. See LICENSE for license details.
 */

package com.noise.billund;

import com.noise.billund.handler.ConfigHandler;
import com.noise.billund.init.ModBlocks;
import com.noise.billund.init.ModItems;
import com.noise.billund.init.Recipes;
import com.noise.billund.proxy.IProxy;
import com.noise.billund.reference.Reference;
import com.noise.billund.util.LogHelper;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.MOD_VERSION, guiFactory = Reference.GUI_FACTORY_CLASS)
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
