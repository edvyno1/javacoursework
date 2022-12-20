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
import org.mindrot.jbcrypt.BCrypt;
import org.w3c.dom.Text;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
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
    AlertController alertController = new AlertController();

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("CourseSystem");
    UserHibernate userHibernate = new UserHibernate(entityManagerFactory);
    public void onActionSignupButton()throws IOException, NoSuchAlgorithmException {
        String password = passwordF.getText();
        if(!isPasswordValid(password)){
            alertController.errorDialog("Register error", "Password must be at least 8 characters", "Use a longer password");
            return;
        };
        String hashedPass = hashPassword(password);

        if (PersonBtn.isSelected()) {
            Person person = new Person(usernameF.getText(), hashedPass, fNameF.getText(), lNameF.getText(), emailF.getText());
            createPerson(person);
        } else {
            Company company = new Company(usernameF.getText(), hashedPass, cNameF.getText(), pOfContactF.getText());
            createCompany(company);
        }
        showScene();
    }

    public void createPerson(Person person) {
        userHibernate.createUser(person);
    }

    public void createCompany(Company company) {
        userHibernate.createUser(company);
    }

    public void showScene() throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(Start.class.getResource("CourseWindow.fxml"));
        Parent root = fxmlLoader.load();
        CourseWindowController courseWindowController = fxmlLoader.getController();
        Scene scene = new Scene(root);
        Stage stage = (Stage) usernameF.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public String hashPassword(String unhashedPassword) throws NoSuchAlgorithmException {
        String hashedPassword = BCrypt.hashpw(unhashedPassword, BCrypt.gensalt());
        return hashedPassword;
    }

    public boolean isPasswordValid(String password){
        if(password.length() <= 7){
            return false;
        }
        return true;
    }
}
