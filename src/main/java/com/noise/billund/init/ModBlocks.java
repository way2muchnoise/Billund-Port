package com.noise.billund.init;

import com.noise.billund.block.BlockBillund;
import com.noise.billund.reference.Names;
import com.noise.billund.reference.Reference;
import cpw.mods.fml.common.registry.GameRegistry;

@GameRegistry.ObjectHolder(Reference.MOD_ID)
public class ModBlocks {

    public static BlockBillund billund = new BlockBillund();

    public static void init() {
        GameRegistry.registerBlock(billund, Names.Blocks.BILLUND);
    }
}