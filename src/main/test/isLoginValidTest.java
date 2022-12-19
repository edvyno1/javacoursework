import courseSystem.ds.Person;
import courseSystem.fxControllers.LoginController;
import courseSystem.hibernateControllers.UserHibernate;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;


public class isLoginValidTest extends LoginController {

    static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("CourseSystem");
    static UserHibernate userHibernate = new UserHibernate(entityManagerFactory);

    @Test
    public void testIsLoginValidWithWrongCredentials() throws IOException {
        String username = "test";
        String password = "test";

        Boolean result = isLoginValid(username, password);

        Assertions.assertFalse(result);
    }

    @Test
    public void testIsLoginValidWithEmptyCredentials() throws IOException {
        String username = "";
        String password = "";

        Boolean result = isLoginValid(username, password);

        Assertions.assertFalse(result);
    }

    @Test
    public void testIsLoginValidWithCorrectCredentials() throws IOException {
        String hashedPass = BCrypt.hashpw("test", BCrypt.gensalt());
        Person person = new Person("test", hashedPass, "test", "test", "test");
        userHibernate.createUser(person);
        String username = "test";
        String password = "test";

        Boolean result = isLoginValid(username, password);

        Assertions.assertTrue(result);
    }

    @Test
    public void testIsLoginValidWithIncorrectPassword() throws IOException {
        String hashedPass = BCrypt.hashpw("test2", BCrypt.gensalt());
        Person person = new Person("test2", hashedPass, "test2", "test2", "test2");
        userHibernate.createUser(person);
        String username = "test2";
        String password = "incorrect";

        Boolean result = isLoginValid(username, password);

        Assertions.assertFalse(result);
    }

    @AfterAll
    static void clearData() {
        userHibernate.deleteUser(userHibernate.getUserByLogin("test").getId());
        userHibernate.deleteUser(userHibernate.getUserByLogin("test2").getId());
    }

}
