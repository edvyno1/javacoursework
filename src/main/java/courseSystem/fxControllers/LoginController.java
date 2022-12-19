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
    protected PasswordField passwordF;
    @FXML
    protected TextField usernameF;

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

        if (isLoginValid(usernameF.getText(), passwordF.getText())) {
            FXMLLoader fxmlLoader = new FXMLLoader(Start.class.getResource("CourseWindow.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }
        else {
            alertController.errorDialog("Login error", "Username or Password is invalid.", "Verify that you entered the credentials correctly.");
        }

    }

    protected boolean isLoginValid(String usernameF, String passwordF) throws IOException{
        if(usernameF.isEmpty() || passwordF.isEmpty()){
            return false;
        }
        try{
            User user = userHibernate.getUserByLogin(usernameF);

            if(!BCrypt.checkpw(passwordF, user.getPassword())){
                return false;
            }
        }
        catch (Exception e){
            return false;
        }

        return true;
    }
}