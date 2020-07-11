import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class PaneDecode extends GridPane {
    PaneDecode(Stage mainStage, Scene previousPane) {
        Label label1 = new Label("File:");
        TextField pathToFile = new TextField();
        pathToFile.setEditable(false);
        pathToFile.setMinWidth(350);
        Button buttonSetPathToFile = new Button("...");

        Label label2 = new Label("Secret key:");
        TextField pathToSecretKey = new TextField();
        pathToSecretKey.setEditable(false);
        pathToSecretKey.setMinWidth(350);
        Button buttonSetPathToSecretKey = new Button("...");

        Label label3 = new Label("Your private key:");
        TextField pathToPrivateKey = new TextField();
        pathToPrivateKey.setEditable(false);
        pathToPrivateKey.setMinWidth(350);
        Button buttonSetPathToPrivateKey = new Button("...");

        CheckBox checkBox = new CheckBox("Verify signature");

        Label label4 = new Label("Signature:");
        TextField pathToSignature = new TextField();
        pathToPrivateKey.setEditable(false);
        pathToPrivateKey.setMinWidth(350);
        Button buttonSetPathToSignature = new Button("...");
        buttonSetPathToSignature.setDisable(true);

        Label label5 = new Label("Interlocutor's public key:");
        TextField pathToPublicKey = new TextField();
        pathToPublicKey.setEditable(false);
        pathToPrivateKey.setMinWidth(350);
        Button buttonSetPathToPublicKey = new Button("...");
        buttonSetPathToPublicKey.setDisable(true);

        Button buttonBack = new Button("Back");

        Button buttonDecode = new Button("Decode");

        HBox hbox = new HBox(buttonBack, buttonDecode);
        hbox.setSpacing(5);
        hbox.setPadding(new Insets(5));
        hbox.setAlignment(Pos.CENTER_RIGHT);

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.add(label1, 0, 0);
        grid.add(pathToFile, 1, 0);
        grid.add(buttonSetPathToFile, 2, 0);
        grid.add(label2, 0, 1);
        grid.add(pathToSecretKey, 1, 1);
        grid.add(buttonSetPathToSecretKey, 2, 1);
        grid.add(label3, 0, 2);
        grid.add(pathToPrivateKey, 1, 2);
        grid.add(buttonSetPathToPrivateKey, 2, 2);
        grid.add(checkBox, 0, 3);
        grid.add(label4, 0, 4);
        grid.add(pathToSignature, 1, 4);
        grid.add(buttonSetPathToSignature, 2, 4);
        grid.add(label5, 0, 5);
        grid.add(pathToPublicKey, 1, 5);
        grid.add(buttonSetPathToPublicKey, 2, 5);

        this.add(grid, 0, 0);
        this.add(hbox, 0, 1);

        buttonSetPathToFile.setOnMouseClicked((e) -> setPath(pathToFile));
        buttonSetPathToSecretKey.setOnMouseClicked((e) -> setPath(pathToSecretKey));
        buttonSetPathToPrivateKey.setOnMouseClicked((e) -> setPath(pathToPrivateKey));
        buttonSetPathToSignature.setOnMouseClicked((e) -> setPath(pathToSignature));
        buttonSetPathToPublicKey.setOnMouseClicked((e) -> setPath(pathToPublicKey));
        checkBox.setOnAction((e) -> {
            if (checkBox.isSelected()) {
                buttonSetPathToSignature.setDisable(false);
                buttonSetPathToPublicKey.setDisable(false);
            } else {
                buttonSetPathToSignature.setDisable(true);
                buttonSetPathToPublicKey.setDisable(true);
            }
        });
        buttonDecode.setOnMouseClicked((e) ->
            Controller.decode(pathToFile.getText(), pathToSecretKey.getText(),
                    pathToPrivateKey.getText(), pathToSignature.getText(),
                    pathToPublicKey.getText(), checkBox.isSelected()));

        buttonBack.setOnMouseClicked((e) -> {
            pathToFile.clear();
            pathToSecretKey.clear();
            pathToPrivateKey.clear();
            pathToSignature.clear();
            pathToPublicKey.clear();
            buttonSetPathToSignature.setDisable(true);
            buttonSetPathToPublicKey.setDisable(true);
            checkBox.setSelected(false);

            mainStage.setScene(previousPane);
        });
    }

    private static void setPath(TextField textField) {
        String path = Controller.setFile();
        if (!path.equals("")) textField.setText(path);
    }
}