package courseSystem.util;

import com.google.gson.*;

import com.google.gson.reflect.TypeToken;
import courseSystem.ds.File;
import courseSystem.ds.Folder;

import java.lang.reflect.Type;
import java.util.List;

public class FolderGsonSerializer implements JsonSerializer<Folder> {
    @Override
    public JsonElement serialize(Folder folder, Type type, JsonSerializationContext jsonSerializationContext) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson parser = gsonBuilder.create();

        JsonObject folderJson = new JsonObject();
        folderJson.addProperty("id", folder.getId());
        folderJson.addProperty("title", folder.getTitle());
        if(folder.getParentFolder() == null){
            folderJson.addProperty("parentFolder", (Number) null);
        }
        else folderJson.addProperty("parentFolder", folder.getParentFolder().getId());

        JsonArray subFolders = new JsonArray();
        for (Folder f : folder.getSubFolders()) {
            subFolders.add(parser.toJson(f.getId()));
        }
        folderJson.add("subFolders", subFolders);

        JsonArray folderFiles = new JsonArray();
        for (File f : folder.getFolderFiles()) {
            folderFiles.add((parser.toJson(f.getId())));
        }
        folderJson.add("folderFiles", folderFiles);
        if(folder.getParentCourse() == null){
            folderJson.addProperty("parentCourse", (Number) null);
        }
        else folderJson.addProperty("parentCourse", folder.getParentCourse().getId());
        return folderJson;
    }
}