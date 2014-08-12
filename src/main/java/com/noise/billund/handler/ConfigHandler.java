package com.noise.billund.handler;

import com.noise.billund.reference.Reference;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class ConfigHandler {

    public static Configuration config;

    public static void init(File configFile) {

        if (config == null) {
            config = new Configuration(configFile);
            loadConfig();
        }
    }

    @SubscribeEvent
    public void onConfigChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event) {

        if (event.modID.equalsIgnoreCase(Reference.MOD_ID)) {
            loadConfig();
        }

    }

    private static void loadConfig() {

        if (config.hasChanged()) {
            config.save();
        }

    }
}
