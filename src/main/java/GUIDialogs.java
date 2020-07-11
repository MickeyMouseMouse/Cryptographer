import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class GUIDialogs {
    private Alert alert = new Alert(null);
    private final Image icon;

    GUIDialogs(Image icon) { this.icon = icon; }

    public void showError(String error) {
        alert = new Alert(Alert.AlertType.ERROR);
        customization(error);
        alert.showAndWait();
    }

    public void showWarning(String warning) {
        alert = new Alert(Alert.AlertType.WARNING);
        customization(warning);
        alert.showAndWait();
    }

    public void showInformation(String info) {
        alert = new Alert(Alert.AlertType.INFORMATION);
        customization(info);
        alert.showAndWait();
    }

    // true = YES; false = NO
    public boolean showConfirmation(String question) {
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        customization(question);

        ButtonType buttonContinue = new ButtonType("Continue", ButtonBar.ButtonData.YES);
        ButtonType buttonCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonContinue, buttonCancel);

        ButtonType result = alert.showAndWait().get();
        return result.equals(buttonContinue);
    }

    private void customization(String text) {
        DialogPane dialogPane = alert.getDialogPane();
        if (icon != null)
            ((Stage) dialogPane.getScene().getWindow()).getIcons().add(icon);
        dialogPane.setStyle("-fx-font-size: 15px;");
        alert.setHeaderText(null);
        alert.setContentText(text);
    }
}