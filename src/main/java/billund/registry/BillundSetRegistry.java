package billund.registry;

import billund.set.BillundSet;
import billund.set.BillundSetLoader;

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

    public String asString()
    {
        StringBuilder builder = new StringBuilder();
        for (BillundSet set : this.billundSets.values())
            builder.append(set.asString()).append("\n");
        return builder.toString();
    }

    public void serverSync(String string)
    {
        String[] splitted = string.split("\n");
        this.billundSets.clear();
        for (String set : splitted)
            addBillundSet(BillundSet.fromString(set));
    }

    public void reload()
    {
        this.billundSets.clear();
        BillundSetLoader.loadSets();
    }
}
