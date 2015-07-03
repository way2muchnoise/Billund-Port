package billund.handler;

import billund.reference.Reference;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class ConfigHandler {

    public static Configuration config;
    public static boolean redye = true;
    public static int speedMultiplier = 1;

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

        redye = config.getBoolean("redye", Configuration.CATEGORY_GENERAL, true, "You can redye Billunds when set to true");
        speedMultiplier = config.getInt("speedMultiplier", Configuration.CATEGORY_GENERAL, 1, 1, 50, "Speed multipier for airdrops");

        if (config.hasChanged()) {
            config.save();
        }

    }
}
