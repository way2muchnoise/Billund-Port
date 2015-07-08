package billund.init;

import billund.block.BlockBillund;
import billund.reference.Names;
import billund.reference.Reference;
import cpw.mods.fml.common.registry.GameRegistry;

@GameRegistry.ObjectHolder(Reference.ID)
public class ModBlocks
{

    public static BlockBillund billund = new BlockBillund();

    public static void init()
    {
        GameRegistry.registerBlock(billund, Names.Blocks.BILLUND);
    }
}
