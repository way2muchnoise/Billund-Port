package billund.registry;

import billund.block.BlockBillund;
import billund.reference.Names;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockRegistry
{
    public static BlockBillund billund;

    public static void init()
    {
        billund = new BlockBillund();
    }
}
