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
        starter.addBricks("basic", Colour.RED);
        starter.addBricks("basic", Colour.GREEN);
        starter.addBricks((Bricks)null);

        starter.addBricks("basic", Colour.BLUE);
        starter.addBricks("basic", Colour.YELLOW);
        starter.addBricks((Bricks)null);

        starter.addBricks("basic", Colour.WHITE);
        starter.addBricks("basic", Colour.BLACK);
        starter.addBricks((Bricks)null);

        BillundSetRegistry.instance().addBillundSet(starter);

        // Colour Pack
        // pieces in 3 colours
        BillundSet aPack = new BillundSet("packA", 10);
        aPack.addBricks("all", Colour.RED);
        aPack.addBricks("all", Colour.GREEN);
        aPack.addBricks("all", Colour.BLUE);
        BillundSetRegistry.instance().addBillundSet(aPack);

        // Colour Pack
        // pieces in 3 colours
        BillundSet bPack = new BillundSet("packB", 10);
        bPack.addBricks("all", Colour.ORANGE);
        bPack.addBricks("all", Colour.YELLOW);
        bPack.addBricks("all", Colour.LIME);
        BillundSetRegistry.instance().addBillundSet(bPack);

        // Colour Pack
        // pieces in 3 colours
        BillundSet cPack = new BillundSet("packC", 10);
        cPack.addBricks("all", Colour.PINK);
        cPack.addBricks("all", Colour.PURPLE);
        cPack.addBricks("all", Colour.WHITE);
        BillundSetRegistry.instance().addBillundSet(cPack);

        // Colour Pack
        // pieces in 3 colours
        BillundSet dPack = new BillundSet("packD", 10);
        dPack.addBricks("all", Colour.LIGHT_GREY);
        dPack.addBricks("all", Colour.GREY);
        dPack.addBricks("all", Colour.BLACK);
        BillundSetRegistry.instance().addBillundSet(dPack);
    }
}
