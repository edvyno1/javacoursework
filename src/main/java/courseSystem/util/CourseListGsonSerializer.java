package courseSystem.util;

import com.google.gson.*;
import courseSystem.ds.Course;

import java.lang.reflect.Type;
import java.util.List;

public class CourseListGsonSerializer implements JsonSerializer<List<Course>> {
    @Override
    public JsonElement serialize(List<Course> courses, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonArray jsonArray = new JsonArray();
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson parser = gsonBuilder.create();

        for (Course f : courses) {
            jsonArray.add((parser.toJson(f)));
        }
        return jsonArray;
    }
}
