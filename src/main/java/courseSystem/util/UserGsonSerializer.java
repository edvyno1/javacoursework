package courseSystem.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import courseSystem.ds.User;

import java.lang.reflect.Type;

public class UserGsonSerializer implements JsonSerializer<User> {
    @Override
    public JsonElement serialize(User user, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject userJson = new JsonObject();
        userJson.addProperty("id", user.getId());
        userJson.addProperty("login", user.getLogin());
        userJson.addProperty("date created", user.getDateCreated().toString());
        userJson.addProperty("date modified", user.getDateModified().toString());
        return userJson;
    }
}

