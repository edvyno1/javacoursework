package courseSystem.ds;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public abstract class User implements Serializable {
    @Column(insertable = false, updatable = false)
    private String DTYPE;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(length = 64, unique = true)
    private String login;
    private String password;
    private LocalDate dateCreated;
    private LocalDate dateModified;
    @Enumerated(EnumType.ORDINAL)
    private UserType userType;

    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinTable(name = "course_moderator", joinColumns = @JoinColumn(name = "moderator_id"),
            inverseJoinColumns = @JoinColumn(name = "moderatedCourse_id"))
    private List<Course> myModeratedCourses;

    public User(String login, String password) {
        this.login = login;
        this.password = password;
        this.dateCreated = LocalDate.now();
        this.dateModified = LocalDate.now();
        this.myModeratedCourses = new ArrayList<>();
    }


    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    public LocalDate getDateModified() {
        return dateModified;
    }

    public void setDateModified(LocalDate dateModified) {
        this.dateModified = dateModified;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String getDTYPE() {
        return DTYPE;
    }

    public List<Course> getMyModeratedCourses() {
        return myModeratedCourses;
    }

    public void setMyModeratedCourses(List<Course> myModeratedCourses) {
        this.myModeratedCourses = myModeratedCourses;
    }
}
