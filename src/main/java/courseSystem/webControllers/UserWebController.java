package courseSystem.webControllers;

import com.google.gson.Gson;
import courseSystem.ds.Person;
import courseSystem.hibernateControllers.UserHibernate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.Connection;
import java.util.Properties;

@Controller //localhost:8080/application_context/user
public class UserWebController {

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("CourseSystem");
    UserHibernate userHibController = new UserHibernate(entityManagerFactory);

    @RequestMapping(value = "/user/allUsers", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getAllUsers() {
        Gson gson = new Gson();
        return gson.toJson(userHibController.getAllUsers().toString());
    }

    @RequestMapping(value = "/user/updatePerson/{id}", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String updatePerson(@RequestBody String request, @PathVariable(name = "id") int id) {
        Gson gson = new Gson();
        Properties properties = gson.fromJson(request, Properties.class);

        Person person = (Person) userHibController.getUserById(id);

        person.setName(properties.getProperty("name"));
        person.setLogin(properties.getProperty("login"));
        //pabaigsim

        //Person person = new Person(properties.getProperty("login"), properties.getProperty("psw"), properties.getProperty("name"), properties.getProperty("surname"), properties.getProperty("email"));
        userHibController.editUser(person);
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
        //Patikrinti ar tikrai istryne
        return "Success";
    }

}