package billund.handler;

import billund.network.MessageHandler;
import billund.network.message.MessageRotation;
import billund.proxy.ClientProxy;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;

public class ClientEventHandler
{
    public static KeyBinding KEY_ROTATE = new KeyBinding("billund.rotate", Keyboard.KEY_R, "keys.general");

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event)
    {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        if (player == null) return;

        if (KEY_ROTATE.isPressed())
        {
            ClientProxy.rotation = (byte) ((ClientProxy.rotation + 1) % 4);
            MessageHandler.INSTANCE.sendToServer(new MessageRotation(ClientProxy.rotation));
        }
    }
}
