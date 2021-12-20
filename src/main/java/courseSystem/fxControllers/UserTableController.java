package courseSystem.fxControllers;

import courseSystem.Start;
import courseSystem.ds.Person;
import courseSystem.ds.User;
import courseSystem.ds.UserType;
import courseSystem.hibernateControllers.UserHibernate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.Callback;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class UserTableController implements Initializable {
    @FXML
    public TableView usersTable;
    @FXML
    public TableColumn<User, String> colId;
    @FXML
    public TableColumn<User, String> colDTYPE;
    @FXML
    public TableColumn<User, String> colUserType;
    @FXML
    public TableColumn<User, String> colLogin;
    @FXML
    public TableColumn<User, String> colCreated;
    @FXML
    public TableColumn<User, String> colModified;
    @FXML
    public TableColumn<Person, String> colEmail;
    @FXML
    public TableColumn<User, String> colName;
    @FXML
    public TableColumn<User, String> colSurname;
    @FXML
    public MenuBar menuBar;

//    public TableColumn<UserTableParameters, String> colId;

//    private ObservableList<UserTableParameters> data = FXCollections.observableArrayList();

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("CourseSystem");
    UserHibernate userHibernate = new UserHibernate(entityManagerFactory);

    private User user;

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        populateTable();
    }

    public void createUser(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Start.class.getResource("Signup.fxml"));
        Parent root = fxmlLoader.load();

        SignupController signupController = fxmlLoader.getController();
        signupController.setPath("UserTable.fxml");
        signupController.setUser(user);

        Scene scene = new Scene(root);
        Stage stage = (Stage) usersTable.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void removeUser(){
        User selectedId = (User) usersTable.getSelectionModel().getSelectedItem();
        userHibernate.deleteUser(selectedId.getId());
        populateTable();

    }
    public void populateTable(){
        usersTable.setEditable(true);
        usersTable.getItems().clear();
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDTYPE.setCellValueFactory(new PropertyValueFactory<>("DTYPE"));
        colUserType.setCellValueFactory(new PropertyValueFactory<>("userType"));
        colCreated.setCellValueFactory(new PropertyValueFactory<>("dateCreated"));
        colModified.setCellValueFactory(new PropertyValueFactory<>("dateModified"));

        colLogin.setCellValueFactory(new PropertyValueFactory<>("login"));
        colLogin.setCellFactory(TextFieldTableCell.forTableColumn());
        colLogin.setOnEditCommit(
                t -> {
                    t.getTableView().getItems().get(t.getTablePosition().getRow()).setLogin(t.getNewValue());
                    User user = userHibernate.getUserById(t.getTableView().getItems().get(t.getTablePosition().getRow()).getId());
                    user.setLogin(t.getTableView().getItems().get(t.getTablePosition().getRow()).getLogin());
                    userHibernate.editUser(user);
                }
        );

        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colSurname.setCellValueFactory(new PropertyValueFactory<>("surname"));

        List<User> users = userHibernate.getAllUsers();

        for(User user : users){
            usersTable.getItems().add(user);
        }
    }
    public void returnBack() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Start.class.getResource("CourseWindow.fxml"));
        Parent root = fxmlLoader.load();

        CourseWindowController courseWindowController = fxmlLoader.getController();
        courseWindowController.setUser(user);

        Scene scene = new Scene(root);
        Stage stage = (Stage) usersTable.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void setRoleAdmin(ActionEvent event) {
        User user = (User) usersTable.getSelectionModel().getSelectedItem();
        user = userHibernate.getUserById(user.getId());
        user.setUserType(UserType.ADMIN);
        userHibernate.editUser(user);
        populateTable();
    }

    public void setRoleCreator(ActionEvent event) {
        User user = (User) usersTable.getSelectionModel().getSelectedItem();
        user = userHibernate.getUserById(user.getId());
        user.setUserType(UserType.CREATOR);
        userHibernate.editUser(user);
        populateTable();
    }

    public void setRoleViewer(ActionEvent event) {
        User user = (User) usersTable.getSelectionModel().getSelectedItem();
        user = userHibernate.getUserById(user.getId());
        user.setUserType(UserType.VIEWER);
        userHibernate.editUser(user);
        populateTable();
    }
}
