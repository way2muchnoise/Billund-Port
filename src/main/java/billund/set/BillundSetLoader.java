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

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class BillundSetLoader
{
    public static void loadSets()
    {
        JsonReader reader = new JsonReader(new InputStreamReader(FileHelpler.getJsonFile(BillundSetLoader.class, "default.json", ConfigHandler.configDir + "sets/default.json", ConfigHandler.reloadDefaultPacks)));
        JsonParser parser = new JsonParser();

        List<JsonObject> subSetsList = new LinkedList<JsonObject>();
        List<JsonObject> setsList = new LinkedList<JsonObject>();

        JsonObject base = parser.parse(reader).getAsJsonObject();
        if (base.has("subsets"))
            subSetsList.add(base.getAsJsonObject("subsets"));
        if (base.has("sets"))
            setsList.add(base.getAsJsonObject("sets"));

        InputStream[] streams =  FileHelpler.getJsonFiles(ConfigHandler.configDir + "sets", "default.json");
        if (streams != null)
        {
            for (InputStream stream : streams)
            {
                reader = new JsonReader(new InputStreamReader(stream));
                base = parser.parse(reader).getAsJsonObject();
                if (base.has("subsets"))
                    subSetsList.add(base.getAsJsonObject("subsets"));
                if (base.has("sets"))
                    setsList.add(base.getAsJsonObject("sets"));
            }
        }

        for (JsonObject subsets : subSetsList)
        {
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
        }

        for (JsonObject sets : setsList)
        {
            for (Map.Entry<String, JsonElement> entry : sets.entrySet())
            {
                JsonObject jsonSet = entry.getValue().getAsJsonObject();
                BillundSet set = new BillundSet(entry.getKey(), jsonSet.get("cost").getAsInt());
                if (jsonSet.has("name"))
                    set.setLocalizedName(jsonSet.get("name").getAsString());
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
}
