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

    public void onRegisterButtonClick(ActionEvent actionEvent)throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Start.class.getResource("Signup.fxml"));
//        SignupController signUpForm = fxmlLoader.getController();
//        signUpForm.setProjectMngSys(projectMngSys);
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void onLoginButtonClick(ActionEvent actionEvent)throws IOException {
        User user = userHibernate.getUserByLogin(usernameF);
        if(user.getPassword().equals(passwordF.getText()))
        {
            FXMLLoader fxmlLoader = new FXMLLoader(Start.class.getResource("CourseWindow.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }
    }



}