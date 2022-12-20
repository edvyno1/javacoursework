package courseSystem.fxControllers;

import courseSystem.Start;
import courseSystem.ds.Course;
import courseSystem.hibernateControllers.CourseHibernate;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;

public class NewCourseForm {
    public TextField courseTitle;
    public TextArea courseDesc;
    public DatePicker courseStart;
    public DatePicker courseEnd;

    private String login;
    private String titleOfEdit;

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("CourseSystem");
    CourseHibernate courseHibernate = new CourseHibernate(entityManagerFactory);

    public void setCourseFormData(String login){
        this.login = login;
    }

    public void enterByEdit(Course course){
        courseTitle.setText(course.getTitle());
        courseDesc.setText(course.getDescription());
        courseStart.setValue(course.getStartDate());
        courseEnd.setValue(course.getEndDate());
        titleOfEdit = course.getTitle();
    }

    public void createCourse(ActionEvent actionEvent) throws IOException {

        Course course = new Course(courseTitle.getText(),courseDesc.getText(),courseStart.getValue(),courseEnd.getValue());
        courseHibernate.createCourse(course);
        courseWindowFxmlLoader();
    }

    private void courseWindowFxmlLoader() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Start.class.getResource("CourseWindow.fxml"));
        Parent root = fxmlLoader.load();

        CourseWindowController courseWindowController = fxmlLoader.getController();
        courseWindowController.setUser(login);

        Scene scene = new Scene(root);
        Stage stage = (Stage) courseTitle.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void editCourse(ActionEvent actionEvent) throws IOException {
        Course course = courseHibernate.getCourseByTitle(titleOfEdit);
        Course newCourse = new Course();
        newCourse.setId(course.getId());
        newCourse.setTitle(courseTitle.getText());
        newCourse.setDescription(courseDesc.getText());
        newCourse.setStartDate(courseStart.getValue());
        newCourse.setEndDate(courseEnd.getValue());
        courseHibernate.editCourse(newCourse);
        courseWindowFxmlLoader();
    }
}