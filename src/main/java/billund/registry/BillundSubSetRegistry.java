package billund.registry;

import billund.set.BillundSubSet;

import java.util.LinkedHashMap;
import java.util.Map;

public class BillundSubSetRegistry
{
    private Map<String, BillundSubSet> subSetMap;
    private static BillundSubSetRegistry instance;

    public static BillundSubSetRegistry instance()
    {
        if (instance == null)
            instance = new BillundSubSetRegistry();
        return instance;
    }

    private BillundSubSetRegistry()
    {
        this.subSetMap = new LinkedHashMap<String, BillundSubSet>();
    }

    public void addBillundSubSet(BillundSubSet subSet)
    {
        this.subSetMap.put(subSet.getName(), subSet);
    }

    public BillundSubSet getBillundSubSet(String name)
    {
        return this.subSetMap.get(name);
    }
}
