package billund.set;

import billund.handler.ConfigHandler;
import billund.reference.Colour;
import billund.registry.BillundSetRegistry;
import billund.registry.BillundSubSetRegistry;
import billund.util.FileHelpler;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import java.io.InputStreamReader;
import java.util.Map;

public class BillundSetLoader
{
    public static void loadSets()
    {
        JsonReader reader = new JsonReader(new InputStreamReader(FileHelpler.getJsonFile(BillundSetLoader.class, "default.json", "billundSets\\default.json", ConfigHandler.reloadDefaultPacks)));
        JsonParser parser = new JsonParser();
        JsonObject base = parser.parse(reader).getAsJsonObject();

        JsonObject subsets = base.getAsJsonObject("subsets");
        for (Map.Entry<String, JsonElement> entry : subsets.entrySet())
        {
            BillundSubSet subset = new BillundSubSet(entry.getKey());
            for (JsonElement element : entry.getValue().getAsJsonArray())
            {
                JsonArray array = element.getAsJsonArray();
                subset.addBricks(array.get(0).getAsInt(), array.get(1).getAsInt(), array.get(2).getAsInt());
            }
            BillundSubSetRegistry.instance().addBillundSubSet(subset);
        }

        JsonObject sets = base.getAsJsonObject("sets");
        for(Map.Entry<String, JsonElement> entry : sets.entrySet())
        {
            JsonObject jsonSet = entry.getValue().getAsJsonObject();
            BillundSet set = new BillundSet(entry.getKey(), jsonSet.get("cost").getAsInt());
            for (JsonElement element : jsonSet.get("bricks").getAsJsonArray())
            {
                JsonObject object = element.getAsJsonObject();
                if (object.has("empty"))
                    for (int i = object.get("empty").getAsInt(); i > 0; i--)
                        set.addBricks((Bricks) null);
                if (object.has("bricks"))
                {
                    for (JsonElement brick : object.getAsJsonArray("bricks"))
                    {
                        JsonArray array = brick.getAsJsonArray();
                        set.addBricks(new Bricks(Colour.getNamed(object.get("colour").getAsString()), array.get(0).getAsInt(), array.get(1).getAsInt(), array.get(2).getAsInt()));
                    }
                }
                if (object.has("subset"))
                    set.addBricks(object.get("subset").getAsString(), Colour.getNamed(object.get("colour").getAsString()));
            }
            BillundSetRegistry.instance().addBillundSet(set);
        }
    }
}
