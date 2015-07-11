package billund.registry;

import net.minecraft.entity.player.EntityPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RotationRegistry
{
    private static RotationRegistry instance;
    private Map<UUID, Byte> rotationMap;

    public static RotationRegistry instance()
    {
        if (instance == null)
            instance = new RotationRegistry();
        return instance;
    }

    private RotationRegistry()
    {
        this.rotationMap = new HashMap<UUID, Byte>();
    }

    public void setRotation(EntityPlayer player, byte rotation)
    {
        this.rotationMap.put(player.getUniqueID(), rotation);
    }

    public byte getRotation(EntityPlayer player)
    {
        Byte b = this.rotationMap.get(player.getUniqueID());
        return b == null ? 0 : b;
    }
}
