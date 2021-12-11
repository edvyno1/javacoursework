package courseSystem.util;

import com.google.gson.*;
import courseSystem.ds.Course;
import courseSystem.ds.Folder;

import java.lang.reflect.Type;

public class CourseGsonSerializer implements JsonSerializer<Course> {
    @Override
    public JsonElement serialize(Course course, Type type, JsonSerializationContext jsonSerializationContext) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson parser = gsonBuilder.create();
        JsonObject courseJson = new JsonObject();
        courseJson.addProperty("id", course.getId());
        courseJson.addProperty("title", course.getTitle());
        courseJson.addProperty("description", course.getDescription());
        courseJson.addProperty("startDate", course.getStartDate().toString());
        courseJson.addProperty("endDate", course.getEndDate().toString());
        courseJson.addProperty("courseFolders", course.getCourseFolders().toString());
        JsonArray courseFolders = new JsonArray();
        for (Folder f : course.getCourseFolders()) {
            courseFolders.add((parser.toJson(f.getId())));
        }
        courseJson.add("courseFolders",courseFolders);
        return courseJson;
    }
}
