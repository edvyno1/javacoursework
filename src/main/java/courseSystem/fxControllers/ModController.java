package courseSystem.fxControllers;

import courseSystem.Start;
import courseSystem.ds.Course;
import courseSystem.ds.User;
import courseSystem.hibernateControllers.CourseHibernate;
import courseSystem.hibernateControllers.UserHibernate;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ModController implements Initializable {
    @FXML
    public TableView usersTable;
    @FXML
    public TableColumn<User, String> colId;
    @FXML
    public TableColumn<User, String> colLogin;
    @FXML
    public TableColumn<User, String> isModCol;

    @FXML
    public MenuBar menuBar;
    private int courseId;

    public void setCourse(int id) {
        this.courseId = id;
    }


    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("CourseSystem");
    UserHibernate userHibernate = new UserHibernate(entityManagerFactory);
    CourseHibernate courseHibernate = new CourseHibernate(entityManagerFactory);
    AlertController alertController = new AlertController();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        populateTable();
    }

    public void addMod(){
        User user = (User) usersTable.getSelectionModel().getSelectedItem();
        for(Course c : user.getMyModeratedCourses()){
            if (c.getId() == courseId){
                alertController.errorDialog("This user is already a moderator","This user is already a moderator","");
                return;
            }
        }
        user = userHibernate.getUserById(user.getId());
        Course addCourse = courseHibernate.getCourseById(courseId);
        user.getMyModeratedCourses().add(addCourse);
        userHibernate.editUser(user);
        populateTable();
    }
    public void goToMain(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Start.class.getResource("CourseWindow.fxml"));
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root);
        Stage stage = (Stage) menuBar.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void populateTable(){
        usersTable.setEditable(false);
        usersTable.getItems().clear();
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colLogin.setCellValueFactory(new PropertyValueFactory<>("login"));
//        isModCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<User, Boolean>, ObservableValue<Boolean>>() {
//            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<User, Boolean> p) {
//                return p.getValue().isModerator(courseId);
//            }
//        });
        List<User> users = userHibernate.getAllUsers();

        for(User user : users){
            usersTable.getItems().add(user);
        }
    }

    public void removeMod(ActionEvent event) {
        User user = (User) usersTable.getSelectionModel().getSelectedItem();
        for(Course c : user.getMyModeratedCourses()){
            if (c.getId() == courseId){
                user.getMyModeratedCourses().remove(c);
                userHibernate.editUser(user);
                populateTable();
                return;
            }
        }
        alertController.errorDialog("This user is not a moderator","This user is not a moderator","");
        return;
    }
}