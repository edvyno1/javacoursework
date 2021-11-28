package courseSystem.webControllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import courseSystem.ds.Course;
import courseSystem.hibernateControllers.CourseHibernate;
import courseSystem.util.CourseGsonSerializer;
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
public class CourseWebController {
    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("CourseSystem");
    CourseHibernate courseHibernate = new CourseHibernate(entityManagerFactory);

    @RequestMapping(value = "/course/allCourses", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getAllCourses() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls();
        Type fileList = new TypeToken<List<Course>>(){}.getType();
        gsonBuilder.registerTypeAdapter(Course.class, new CourseGsonSerializer())
                .registerTypeAdapter(LocalDate.class, new LocalDateSerializer())
        /*.registerTypeAdapter(fileList, new CourseListGsonSerializer())*/;
        List<Course> allCourses = courseHibernate.getAllCourses(true,0,0);
        Gson gson = gsonBuilder.create();
        System.out.println(gson.toJson(allCourses));
        System.out.println(allCourses);
        return gson.toJson(allCourses);
    }

    @RequestMapping(value = "/course/updateCourse/{id}", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String updateCourse(@RequestBody String request, @PathVariable(name = "id") int id) {
        Gson gson = new Gson();
        Properties properties = gson.fromJson(request, Properties.class);
        Course course = new Course(properties.getProperty("title"), properties.getProperty("description"), LocalDate.parse(properties.getProperty("startDate")),  LocalDate.parse(properties.getProperty("endDate")));
        course.setId(id);
        courseHibernate.editCourse(course);
        return "Success";
    }

    @RequestMapping(value = "/course/addCourse", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String addNewCourse(@RequestBody String request) {
        Gson gson = new Gson();
        Properties properties = gson.fromJson(request, Properties.class);
        Course course = new Course(properties.getProperty("title"), properties.getProperty("description"), LocalDate.parse(properties.getProperty("startDate")),  LocalDate.parse(properties.getProperty("endDate")));
        courseHibernate.createCourse(course);
        return "Success";
    }

    @RequestMapping(value = "/course/deleteCourse/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String deleteCourse(@PathVariable(name = "id") int id) {
        courseHibernate.deleteCourse(id);
        //Patikrinti ar tikrai istryne
        return "Success";
    }
}
