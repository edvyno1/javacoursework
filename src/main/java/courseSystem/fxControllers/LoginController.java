package courseSystem.fxControllers;

import courseSystem.Start;
import courseSystem.ds.User;

import courseSystem.hibernateControllers.UserHibernate;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;

public class LoginController {
    @FXML
    private PasswordField passwordF;
    @FXML
    private TextField usernameF;

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("CourseSystem");
    UserHibernate userHibernate = new UserHibernate(entityManagerFactory);
    AlertController alertController = new AlertController();

    public void onRegisterButtonClick(ActionEvent actionEvent)throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Start.class.getResource("Signup.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void onLoginButtonClick(ActionEvent actionEvent)throws IOException {

        if(isLoginValid()) {
            FXMLLoader fxmlLoader = new FXMLLoader(Start.class.getResource("CourseWindow.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }
    }

    private boolean isLoginValid() throws IOException{
        if(usernameF.getText().isEmpty() || passwordF.getText().isEmpty()){
            alertController.errorDialog("Login error", "Username and/or Password fields mustn't be empty.", "Fill the missing fields and try again.");
            return false;
        }
        try{
            User user = userHibernate.getUserByLogin(usernameF.getText());

            if(!BCrypt.checkpw(passwordF.getText(), user.getPassword())){
                alertController.errorDialog("Login error", "Username or Password is invalid.", "Verify that you entered the credentials correctly.");
                return false;
            }
        }
        catch (Exception e){
            alertController.errorDialog("Login error", "Username or Password is invalid.", "Verify that you entered the credentials correctly.");
            return false;
        }

        return true;
    }
}