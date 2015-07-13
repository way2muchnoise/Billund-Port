package billund.set;

import billund.registry.BillundSubSetRegistry;

public class BillundSubSetData
{
    public static void init()
    {
        BillundSubSet basic = new BillundSubSet("basic");
        basic.addBricks(1, 2, 24);
        basic.addBricks(1, 4, 24);
        basic.addBricks(2, 2, 24);
        basic.addBricks(2, 4, 24);
        BillundSubSetRegistry.instance().addBillundSubSet(basic);

        BillundSubSet all = new BillundSubSet("all");
        all.addBricks(1, 1, 24);
        all.addBricks(1, 2, 24);
        all.addBricks(1, 3, 24);
        all.addBricks(1, 4, 24);
        all.addBricks(1, 6, 24);
        all.addBricks(2, 1, 24);
        all.addBricks(2, 2, 24);
        all.addBricks(2, 3, 24);
        all.addBricks(2, 4, 24);
        all.addBricks(2, 6, 24);
        BillundSubSetRegistry.instance().addBillundSubSet(all);
    }
}
