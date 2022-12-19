package courseSystem.webControllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import courseSystem.ds.File;
import courseSystem.ds.Folder;
import courseSystem.hibernateControllers.CourseHibernate;
import courseSystem.hibernateControllers.FolderHibernate;
import courseSystem.util.FileGsonSerializer;
import courseSystem.util.FolderGsonSerializer;
import courseSystem.util.FolderListGsonSerializer;
import courseSystem.util.LocalDateSerializer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.List;
import java.util.Properties;

@Controller
public class FolderWebController {
    public static final String SUCCESS = "Success";
    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("CourseSystem");
    FolderHibernate folderHibernate = new FolderHibernate(entityManagerFactory);
    CourseHibernate courseHibernate = new CourseHibernate(entityManagerFactory);

    @RequestMapping(value = "/folder/allFolders", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getAllFolders() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls();
        Type folderList = new TypeToken<List<Folder>>(){}.getType();
        gsonBuilder.registerTypeAdapter(Folder.class, new FolderGsonSerializer())
                .registerTypeAdapter(LocalDate.class, new LocalDateSerializer())
                .registerTypeAdapter(folderList, new FolderListGsonSerializer())
                .registerTypeAdapter(File.class, new FileGsonSerializer());
        List<Folder> allFolders = folderHibernate.getAllFolders();
        Gson gson = gsonBuilder.create();
        System.out.println(gson.toJson(allFolders));
        System.out.println(allFolders);
        return gson.toJson(allFolders);
    }

    @RequestMapping(value = "/folder/byCourse/{id}", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getCourseById(@PathVariable(name = "id") int id) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls();
        Type folderList = new TypeToken<List<Folder>>(){}.getType();
        gsonBuilder.registerTypeAdapter(Folder.class, new FolderGsonSerializer())
                .registerTypeAdapter(LocalDate.class, new LocalDateSerializer())
                .registerTypeAdapter(folderList, new FolderListGsonSerializer())
                .registerTypeAdapter(File.class, new FileGsonSerializer());
        List<Folder> allFolders = folderHibernate.getFolderByCourseId(id);
        Gson gson = gsonBuilder.create();
        System.out.println(gson.toJson(allFolders));
        System.out.println(allFolders);
        return gson.toJson(allFolders);
    }

    @RequestMapping(value = "/folder/updateFolder/{id}", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String updateFolder(@RequestBody String request, @PathVariable(name = "id") int id) {
        Gson gson = new Gson();
        Properties properties = gson.fromJson(request, Properties.class);
        Folder folder = new Folder(properties.getProperty("title"),
                courseHibernate.getCourseById(Integer.parseInt(properties.getProperty("parentCourse"))),
                folderHibernate.getFolderById(Integer.parseInt(properties.getProperty("parentFolder"))));
        folder.setId(id);
        folderHibernate.updateFolder(folder);
        return SUCCESS;
    }

    @RequestMapping(value = "/folder/addFolder", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String addNewFolder(@RequestBody String request) {
        Gson gson = new Gson();
        Properties properties = gson.fromJson(request, Properties.class);
        Folder folder = new Folder(properties.getProperty("title"),
                courseHibernate.getCourseById(Integer.parseInt(properties.getProperty("parentCourse"))),
                folderHibernate.getFolderById(Integer.parseInt(properties.getProperty("parentFolder"))));
        folderHibernate.createFolder(folder);
        return SUCCESS;
    }

    @RequestMapping(value = "/folder/deleteFolder/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String updateFolder(@PathVariable(name = "id") int id) {
        folderHibernate.deleteFolder(id);
        //Patikrinti ar tikrai istryne
        return SUCCESS;
    }
}

