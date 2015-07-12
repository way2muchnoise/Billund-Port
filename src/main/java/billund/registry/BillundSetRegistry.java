package billund.registry;

import billund.set.BillundSet;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class BillundSetRegistry
{
    private Map<String, BillundSet> billundSets;
    private static BillundSetRegistry instance;

    public static BillundSetRegistry instance()
    {
        if (instance == null)
            instance = new BillundSetRegistry();
        return instance;
    }

    private BillundSetRegistry()
    {
        this.billundSets = new LinkedHashMap<String, BillundSet>();
    }

    public void addBillundSet(BillundSet set)
    {
        this.billundSets.put(set.getName(), set);
    }

    public BillundSet getBillundSet(String name)
    {
        return this.billundSets.get(name);
    }

    public List<BillundSet> getAll()
    {
        return new LinkedList<BillundSet>(this.billundSets.values());
    }
}
