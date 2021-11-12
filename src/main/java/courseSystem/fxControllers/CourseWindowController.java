package courseSystem.fxControllers;

import courseSystem.Start;
import courseSystem.ds.Course;
import courseSystem.ds.Folder;
import courseSystem.hibernateControllers.CourseHibernate;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class CourseWindowController implements Initializable {

    @FXML
    public ListView myCourses;
    @FXML
    public TreeView courseFolderTree;
    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("CourseSystem");
    CourseHibernate courseHibernate = new CourseHibernate(entityManagerFactory);

    private String login;

    public void setUser(String login) {
        this.login = login;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        myCourses.getItems().clear();

        List<Course> courses = courseHibernate.getAllCourses(true, -1, -1);
        courses.forEach(c -> myCourses.getItems().add(c.getId() + ":" + c.getDescription()));
    }

    public void populateFolders(MouseEvent mouseEvent) {

        String courseId = myCourses.getSelectionModel().getSelectedItem().toString().split(":")[0];
        Course selectedCourse = courseHibernate.getCourseById(Integer.parseInt(courseId));

        courseFolderTree.setRoot(new TreeItem<String>("Course folders:"));

        for(Folder folder : selectedCourse.getCourseFolders()){
            System.out.println(folder.getTitle());
            addTreeItem(folder, courseFolderTree.getRoot());
        }
    }
    private void addTreeItem(Folder folder, TreeItem parentFolder) {
        TreeItem<Folder> treeItem = new TreeItem<>(folder);
        parentFolder.getChildren().add(treeItem);
        folder.getSubFolders().forEach(sub -> addTreeItem(sub, treeItem));
    }
    public void newProjectForm(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Start.class.getResource("NewCourse.fxml"));
        Parent root = fxmlLoader.load();

        NewCourseForm newCourseForm = fxmlLoader.getController();
        newCourseForm.setCourseFormData(login);

        Scene scene = new Scene(root);

        Stage stage = (Stage) myCourses.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
//
//    public void loadAllUsersForm(ActionEvent actionEvent) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(Start.class.getResource("all-users-form.fxml"));
//        Parent root = fxmlLoader.load();
//
//
//        Scene scene = new Scene(root);
//
//        Stage stage = new Stage();
//        stage.initModality(Modality.APPLICATION_MODAL);
//        stage.setScene(scene);
//        stage.showAndWait();
//    }

    public void editSelected(ActionEvent actionEvent) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(Start.class.getResource("EditCourse.fxml"));
        Parent root = fxmlLoader.load();

        NewCourseForm newCourseForm = fxmlLoader.getController();
        String ID = myCourses.getSelectionModel().getSelectedItem().toString().split(":")[0];
        int id = Integer.parseInt(ID);
        Course course = courseHibernate.getCourseById(id);
        newCourseForm.enterByEdit(course);

        Scene scene = new Scene(root);

        Stage stage = (Stage) myCourses.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void deleteSelected(ActionEvent event) {

        String ID = myCourses.getSelectionModel().getSelectedItem().toString().split(":")[0];
        int id = Integer.parseInt(ID);
        Course course = courseHibernate.getCourseById(id);
        id = myCourses.getSelectionModel().getSelectedIndex();
        myCourses.getItems().remove(id);
        courseHibernate.deleteCourse(course);
    }
}
