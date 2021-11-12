package courseSystem.ds;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.io.Serializable;
import java.util.List;

@Entity
public class Person extends User implements Serializable {
    private String name;
    private String surname;
    private String email;
    @ManyToMany
    private List<Course> myEnrolledCourses;


    public Person() {
    }

    public Person(String login, String password, String name, String surname, String email) {
        super(login, password);
        this.name = name;
        this.surname = surname;
        this.email = email;
    }

    public Person(String name, String surname, String email) {
        this.name = name;
        this.surname = surname;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Course> getMyEnrolledCourses() {
        return myEnrolledCourses;
    }

    public void setMyEnrolledCourses(List<Course> myEnrolledCourses) {
        this.myEnrolledCourses = myEnrolledCourses;
    }
}
