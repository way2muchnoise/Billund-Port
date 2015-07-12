/**
 * This file is part of Billund - http://www.computercraft.info/billund
 * Copyright Daniel Ratcliffe, 2013-2014. See LICENSE for license details.
 */

package billund.set;

import billund.reference.Colour;
import billund.registry.BillundSetRegistry;

public class BillundSetData
{
    public static void init()
    {
        // Starter set
        // Basic pieces in 6 colours
        BillundSet starter = new BillundSet("starter", 7);
        starter.addBasicBricks(Colour.RED);
        starter.addBasicBricks(Colour.GREEN);
        starter.addBricks(null);

        starter.addBasicBricks(Colour.BLUE);
        starter.addBasicBricks(Colour.YELLOW);
        starter.addBricks(null);

        starter.addBasicBricks(Colour.WHITE);
        starter.addBasicBricks(Colour.BLACK);
        starter.addBricks(null);

        BillundSetRegistry.instance().addBillundSet(starter);

        // Colour Pack
        // pieces in 3 colours
        BillundSet aPack = new BillundSet("packA", 10);
        aPack.addAllBricks(Colour.RED);
        aPack.addAllBricks(Colour.GREEN);
        aPack.addAllBricks(Colour.BLUE);
        BillundSetRegistry.instance().addBillundSet(aPack);

        // Colour Pack
        // pieces in 3 colours
        BillundSet bPack = new BillundSet("packB", 10);
        bPack.addAllBricks(Colour.ORANGE);
        bPack.addAllBricks(Colour.YELLOW);
        bPack.addAllBricks(Colour.LIME);
        BillundSetRegistry.instance().addBillundSet(bPack);

        // Colour Pack
        // pieces in 3 colours
        BillundSet cPack = new BillundSet("packC", 10);
        cPack.addAllBricks(Colour.PINK);
        cPack.addAllBricks(Colour.PURPLE);
        cPack.addAllBricks(Colour.WHITE);
        BillundSetRegistry.instance().addBillundSet(cPack);

        // Colour Pack
        // pieces in 3 colours
        BillundSet dPack = new BillundSet("packD", 10);
        dPack.addAllBricks(Colour.LIGHT_GREY);
        dPack.addAllBricks(Colour.GREY);
        dPack.addAllBricks(Colour.BLACK);
        BillundSetRegistry.instance().addBillundSet(dPack);
    }
}
