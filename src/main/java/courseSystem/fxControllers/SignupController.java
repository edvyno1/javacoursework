package courseSystem.fxControllers;

import courseSystem.Start;
import courseSystem.ds.Company;
import courseSystem.ds.Person;
import courseSystem.ds.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import courseSystem.hibernateControllers.UserHibernate;
import javafx.stage.Stage;
import org.w3c.dom.Text;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.sql.SQLException;

public class SignupController {
    @FXML
    public TextField usernameF;
    @FXML
    public PasswordField passwordF;
    @FXML
    public TextField fNameF;
    @FXML
    public TextField lNameF;
    @FXML
    public TextField emailF;
    @FXML
    public RadioButton PersonBtn;
    @FXML
    public RadioButton OrgBtn;
    @FXML
    public ToggleGroup PerOrOrg;
    @FXML
    public TextField cNameF;
    @FXML
    public TextField pOfContactF;

    private String returnPath;
    private User user;

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("CourseSystem");
    UserHibernate userHibernate = new UserHibernate(entityManagerFactory);

    public void onActionSignupButton(ActionEvent actionEvent)throws IOException, SQLException {
        if (PersonBtn.isSelected()) {
            Person person = new Person(usernameF.getText(), passwordF.getText(), fNameF.getText(), lNameF.getText(), emailF.getText(), null);

            userHibernate.createUser(person);
        } else {
            Company company = new Company(usernameF.getText(), passwordF.getText(), cNameF.getText(), pOfContactF.getText());
            userHibernate.createUser(company);
        }
        returnToPrevious(actionEvent);
    }

    @FXML
    private void returnToPrevious(ActionEvent actionEvent) throws IOException {
        System.out.println(returnPath);
        if(returnPath == null){returnPath = "Login.fxml";}
        FXMLLoader fxmlLoader = new FXMLLoader(Start.class.getResource(returnPath));
        Parent root = fxmlLoader.load();
        if(returnPath!="Login.fxml"){
            UserTableController userTableController = fxmlLoader.getController();
            userTableController.setUser(user);
        }
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    public void setUser(User user) {
        this.user = user;
    }
    public void setPath(String returnPath){
        this.returnPath = returnPath;
    }
}
