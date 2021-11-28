package courseSystem.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import courseSystem.ds.Course;

import java.lang.reflect.Type;

public class CourseGsonSerializer implements JsonSerializer<Course> {
    @Override
    public JsonElement serialize(Course file, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject fileJson = new JsonObject();
        fileJson.addProperty("id", file.getId());
        fileJson.addProperty("name", file.getTitle());
        fileJson.addProperty("file", file.getDescription());
        fileJson.addProperty("start date", file.getStartDate().toString());
        fileJson.addProperty("end date", file.getEndDate().toString());
        fileJson.addProperty("course folders", file.getCourseFolders().toString());
        return fileJson;
    }
}
