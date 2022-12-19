package courseSystem.fxControllers;

import javafx.scene.control.Alert;

public class AlertController {

    public void errorDialog(String title, String headerText, String contentText){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

}
