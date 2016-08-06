package billund.handler;

import billund.reference.Reference;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class ConfigHandler
{
    public static Configuration config;
    public static final String configDir = "config/billund/";
    public static boolean redye = true;
    public static boolean reloadDefaultPacks = false;
    public static int speedMultiplier = 1;

    public static void init()
    {
        if (config == null)
        {
            config = new Configuration(new File(configDir + "billund.cfg"));
            loadConfig();
        }
    }

    @SubscribeEvent
    public void onConfigChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event)
    {
        if (event.getModID().equalsIgnoreCase(Reference.ID))
            loadConfig();
    }

    private static void loadConfig()
    {
        reloadDefaultPacks = config.getBoolean("defaultPacks", Configuration.CATEGORY_GENERAL, false, "Reload the default billund sets");
        redye = config.getBoolean("redye", Configuration.CATEGORY_GENERAL, true, "You can redye Billunds when set to true");
        speedMultiplier = config.getInt("speedMultiplier", Configuration.CATEGORY_GENERAL, 1, 1, 50, "Speed multipier for airdrops");

        if (config.hasChanged())
            config.save();
    }
}
