package courseSystem.webControllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import courseSystem.ds.File;
import courseSystem.hibernateControllers.FileHibernate;
import courseSystem.hibernateControllers.FolderHibernate;
import courseSystem.util.FileListGsonSerializer;
import courseSystem.util.LocalDateSerializer;
import courseSystem.util.FileGsonSerializer;
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
public class FileWebController {
    public static final String SUCCESS = "Success";
    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("CourseSystem");
    FileHibernate fileHibernate = new FileHibernate(entityManagerFactory);
    FolderHibernate folderHibernate = new FolderHibernate(entityManagerFactory);

    @RequestMapping(value = "/file/allFiles", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getAllFiles() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls();
        Type fileList = new TypeToken<List<File>>(){}.getType();
        gsonBuilder.registerTypeAdapter(File.class, new FileGsonSerializer())
                .registerTypeAdapter(LocalDate.class, new LocalDateSerializer())
        .registerTypeAdapter(fileList, new FileListGsonSerializer());
        List<File> allFiles = fileHibernate.getAllFiles();
        Gson gson = gsonBuilder.create();
        System.out.println(gson.toJson(allFiles));
        System.out.println(allFiles);
        return gson.toJson(allFiles);
    }
    @RequestMapping(value = "/file/updateFile/{id}", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String updateFolder(@RequestBody String request, @PathVariable(name = "id") int id) {
        Gson gson = new Gson();
        Properties properties = gson.fromJson(request, Properties.class);
        File file = new File(properties.getProperty("name"),
                folderHibernate.getFolderById(Integer.parseInt(properties.getProperty("parentFolder"))));
        file.setId(id);
        fileHibernate.updateFile(file);
        return SUCCESS;
    }

    @RequestMapping(value = "/file/addFile", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String addNewFile(@RequestBody String request) {
        Gson gson = new Gson();
        Properties properties = gson.fromJson(request, Properties.class);
        File file = new File(properties.getProperty("name"),
                folderHibernate.getFolderById(Integer.parseInt(properties.getProperty("parentFolder"))));
        fileHibernate.createFile(file);
        return SUCCESS;
    }

    @RequestMapping(value = "/file/deleteFile/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String updateFolder(@PathVariable(name = "id") int id) {
        fileHibernate.deleteFile(id);
        //Patikrinti ar tikrai istryne
        return SUCCESS;
    }
}
