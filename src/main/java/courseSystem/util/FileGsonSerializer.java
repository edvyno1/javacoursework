package courseSystem.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import courseSystem.ds.File;

import java.lang.reflect.Type;

public class FileGsonSerializer implements JsonSerializer<File> {
    @Override
    public JsonElement serialize(File file, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject fileJson = new JsonObject();
        fileJson.addProperty("id", file.getId());
        fileJson.addProperty("name", file.getName());
        //fileJson.addProperty("location", file.getLocation());
        fileJson.addProperty("file", file.getFolder().getId());
        return fileJson;
    }
}
