package courseSystem.fxControllers;

import courseSystem.ds.Person;
import courseSystem.ds.User;
import courseSystem.hibernateControllers.UserHibernate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
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

//    public TableColumn<UserTableParameters, String> colId;

//    private ObservableList<UserTableParameters> data = FXCollections.observableArrayList();

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("CourseSystem");
    UserHibernate userHibernate = new UserHibernate(entityManagerFactory);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        populateTable();
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
}
