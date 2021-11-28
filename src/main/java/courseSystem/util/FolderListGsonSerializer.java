package courseSystem.util;

import com.google.gson.*;
import courseSystem.ds.Folder;

import java.lang.reflect.Type;
import java.util.List;

public class FolderListGsonSerializer implements JsonSerializer<List<Folder>> {
    @Override
    public JsonElement serialize(List<Folder> folders, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonArray jsonArray = new JsonArray();
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson parser = gsonBuilder.create();

        for (Folder f : folders) {
            jsonArray.add((parser.toJson(f)));
        }
        return jsonArray;
    }
}