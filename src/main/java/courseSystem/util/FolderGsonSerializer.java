package courseSystem.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import courseSystem.ds.Course;
import courseSystem.ds.Folder;

import java.lang.reflect.Type;

public class FolderGsonSerializer implements JsonSerializer<Folder> {
    @Override
    public JsonElement serialize(Folder folder, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject folderJson = new JsonObject();
        folderJson.addProperty("id", folder.getId());
        folderJson.addProperty("title", folder.getTitle());
        //folderJson.addProperty("parent folder", folder.getParentFolder().toString());
        folderJson.addProperty("sub folders", folder.getSubFolders().toString());
        folderJson.addProperty("parent course", folder.getParentCourse().toString());
        folderJson.addProperty("folder files", folder.getFolderFiles().toString());
        return folderJson;
    }
}
