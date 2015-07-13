package billund.set;

import billund.util.FileHelpler;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import java.io.InputStreamReader;

public class BillundSetLoader
{
    public static void loadSets()
    {
        JsonReader reader = new JsonReader(new InputStreamReader(FileHelpler.getJsonFile(BillundSetLoader.class, "default.json", "default.json", false)));
        JsonParser parser = new JsonParser();

        JsonObject base = parser.parse(reader).getAsJsonObject();
        JsonObject subsets = base.getAsJsonObject("subsets");
        JsonObject sets = base.getAsJsonObject("sets");
    }
}
