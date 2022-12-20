import courseSystem.ds.Company;
import courseSystem.ds.Person;
import courseSystem.ds.User;
import courseSystem.fxControllers.SignupController;
import courseSystem.hibernateControllers.UserHibernate;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class createUserTest extends SignupController {

    static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("CourseSystem");
    static UserHibernate userHibernate = new UserHibernate(entityManagerFactory);

    @Test
    public void testPersonCreation(){
        Person person = new Person("person", "person", "person", "person", "person");
        createPerson(person);
        Person personInDatabase = (Person) userHibernate.getUserByLogin("person");
        Assertions.assertEquals(person.getLogin(), personInDatabase.getLogin());
    }

    @Test
    public void testCompanyCreation(){
        Company company = new Company("company", "company", "company", "company");
        createCompany(company);
        Company companyInDatabase = (Company) userHibernate.getUserByLogin("company");
        Assertions.assertEquals(company.getLogin(), companyInDatabase.getLogin());
    }

    @AfterAll
    static void clearData() {
        userHibernate.deleteUser(userHibernate.getUserByLogin("person").getId());
        userHibernate.deleteUser(userHibernate.getUserByLogin("company").getId());
    }
}
