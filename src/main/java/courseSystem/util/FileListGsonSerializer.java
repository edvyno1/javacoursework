package courseSystem.util;

import com.google.gson.*;
import courseSystem.ds.File;

import java.lang.reflect.Type;
import java.util.List;

public class FileListGsonSerializer implements JsonSerializer<List<File>> {
    @Override
    public JsonElement serialize(List<File> files, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonArray jsonArray = new JsonArray();
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson parser = gsonBuilder.create();

        for (File f : files) {
            jsonArray.add((parser.toJson(f)));
        }
        return jsonArray;
    }
}
