package courseSystem.util;

import com.google.gson.*;
import courseSystem.ds.User;

import java.lang.reflect.Type;
import java.util.List;

public class UserListGsonSerializer implements JsonSerializer<List<User>> {
    @Override
    public JsonElement serialize(List<User> files, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonArray jsonArray = new JsonArray();
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson parser = gsonBuilder.create();

        for (User f : files) {
            jsonArray.add((parser.toJson(f)));
        }
        return jsonArray;
    }
}