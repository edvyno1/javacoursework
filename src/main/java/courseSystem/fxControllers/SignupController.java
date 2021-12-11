package courseSystem.fxControllers;

import courseSystem.Start;
import courseSystem.ds.Company;
import courseSystem.ds.Person;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("CourseSystem");
    UserHibernate userHibernate = new UserHibernate(entityManagerFactory);

    public void onActionSignupButton(ActionEvent actionEvent)throws IOException, SQLException {
        if (PersonBtn.isSelected()) {
            Person person = new Person(usernameF.getText(), passwordF.getText(), fNameF.getText(), lNameF.getText(), emailF.getText());

            userHibernate.createUser(person);
            FXMLLoader fxmlLoader = new FXMLLoader(Start.class.getResource("CourseWindow.fxml"));
            Parent root = fxmlLoader.load();

            CourseWindowController courseWindowController = fxmlLoader.getController();
            Scene scene = new Scene(root);
            Stage stage = (Stage) usernameF.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
            //Write to database
            //INSERT Values (person.getLogin(), person.getPsw(), person.getDateCreated()...)
            //projectMngSys.getAllSysUsers().add(person);
        } else {
            Company company = new Company(usernameF.getText(), passwordF.getText(), cNameF.getText(), pOfContactF.getText());
            userHibernate.createUser(company);
            FXMLLoader fxmlLoader = new FXMLLoader(Start.class.getResource("CourseWindow.fxml"));
            Parent root = fxmlLoader.load();

            CourseWindowController courseWindowController = fxmlLoader.getController();
            Scene scene = new Scene(root);
            Stage stage = (Stage) usernameF.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }
    }
}
