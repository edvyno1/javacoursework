package courseSystem.webControllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import courseSystem.ds.User;
import courseSystem.ds.Person;
import courseSystem.hibernateControllers.UserHibernate;
import courseSystem.util.LocalDateSerializer;
import courseSystem.util.UserGsonSerializer;
import courseSystem.util.UserListGsonSerializer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;
import java.util.Properties;

@Controller //localhost:8080/application_context/user
public class UserWebController {

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("CourseSystem");
    UserHibernate userHibController = new UserHibernate(entityManagerFactory);

    @RequestMapping(value = "/user/allUsers", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getAllUsers() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls();
        Type fileList = new TypeToken<List<User>>(){}.getType();
        gsonBuilder.registerTypeAdapter(User.class, new UserGsonSerializer())
                .registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
        List<User> allUsers = userHibController.getAllUsers();
        Gson gson = gsonBuilder.create();
        System.out.println(gson.toJson(allUsers));
        System.out.println(allUsers);
        return gson.toJson(allUsers);
    }

    @RequestMapping(value = "/user/updateUser/{id}", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String updatePerson(@RequestBody String request, @PathVariable(name = "id") int id) {
        Gson gson = new Gson();
        Properties properties = gson.fromJson(request, Properties.class);
        Person updatedPerson = new Person(properties.getProperty("login"), properties.getProperty("psw"), properties.getProperty("name"), properties.getProperty("surname"), properties.getProperty("email"));
        updatedPerson.setId(id);
        userHibController.editUser(updatedPerson);
        return "Success";
    }

    @RequestMapping(value = "/user/addPerson", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String addNewPerson(@RequestBody String request) {
        Gson gson = new Gson();
        Properties properties = gson.fromJson(request, Properties.class);
        Person person = new Person(properties.getProperty("login"), properties.getProperty("psw"), properties.getProperty("name"), properties.getProperty("surname"), properties.getProperty("email"));
        userHibController.createUser(person);
        return "Success";
    }

    @RequestMapping(value = "/user/deleteUser/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String updatePerson(@PathVariable(name = "id") int id) {
        userHibController.deleteUser(id);
        return "Success";
    }

}