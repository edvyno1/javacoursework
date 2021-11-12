package courseSystem.fxControllers;

import courseSystem.ds.Person;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import courseSystem.hibernateControllers.UserHibernate;

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

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("CourseSystem");
    UserHibernate userHibernate = new UserHibernate(entityManagerFactory);

    public void onActionSignupButton(ActionEvent actionEvent)throws IOException, SQLException {
        if (PersonBtn.isSelected()) {
            Person person = new Person(usernameF.getText(), passwordF.getText(), fNameF.getText(), lNameF.getText(), emailF.getText());

            userHibernate.createUser(person);
            //Write to database
            //INSERT Values (person.getLogin(), person.getPsw(), person.getDateCreated()...)
            //projectMngSys.getAllSysUsers().add(person);
        } else {
//            Company company = new Company(loginF.getText(), pswF.getText(), comNameF.getText(), comRepF.getText(), comAddrF.getText(), comPhoneF.getText());
            //Write to db
            //INSERT...values(company.getLogin()...)
//            projectMngSys.getAllSysUsers().add(company);
        }
    }
}