import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class PaneEncode extends GridPane {
    PaneEncode(Stage mainStage, Scene previousPane) {
        Label label1 = new Label("File:");
        TextField pathToFile = new TextField();
        pathToFile.setEditable(false);
        pathToFile.setMinWidth(350);
        Button buttonSetPathToFile = new Button("...");

        Label label2 = new Label("Interlocutor's public key:");
        TextField pathToPublicKey = new TextField();
        pathToPublicKey.setEditable(false);
        pathToPublicKey.setMinWidth(350);
        Button buttonSetPathToPublicKey = new Button("...");

        CheckBox checkBox = new CheckBox("Make signature");

        Label label3 = new Label("Your private key:");
        TextField pathToPrivateKey = new TextField();
        pathToPrivateKey.setEditable(false);
        pathToPrivateKey.setMinWidth(350);
        Button buttonSetPathToPrivateKey = new Button("...");
        buttonSetPathToPrivateKey.setDisable(true);

        Button buttonBack = new Button("Back");

        Button buttonEncode = new Button("Encode");

        HBox hbox = new HBox(buttonBack, buttonEncode);
        hbox.setSpacing(5);
        hbox.setPadding(new Insets(5));
        hbox.setAlignment(Pos.CENTER_RIGHT);

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.add(label1, 0, 0);
        grid.add(pathToFile, 1, 0);
        grid.add(buttonSetPathToFile, 2, 0);
        grid.add(label2, 0, 1);
        grid.add(pathToPublicKey, 1, 1);
        grid.add(buttonSetPathToPublicKey, 2, 1);
        grid.add(checkBox, 0, 2);
        grid.add(label3, 0, 3);
        grid.add(pathToPrivateKey, 1, 3);
        grid.add(buttonSetPathToPrivateKey, 2, 3);

        this.add(grid, 0, 0);
        this.add(hbox, 0, 1);

        buttonSetPathToFile.setOnMouseClicked((e) -> setPath(pathToFile));
        buttonSetPathToPublicKey.setOnMouseClicked((e) -> setPath(pathToPublicKey));
        buttonSetPathToPrivateKey.setOnMouseClicked((e) -> setPath(pathToPrivateKey));
        checkBox.setOnAction((e) ->
            buttonSetPathToPrivateKey.setDisable(!checkBox.isSelected()));
        buttonEncode.setOnMouseClicked((e) ->
            Controller.encode(pathToFile.getText(), pathToPublicKey.getText(),
                    pathToPrivateKey.getText(), checkBox.isSelected()));
        buttonBack.setOnMouseClicked((e) -> {
            pathToFile.clear();
            pathToPublicKey.clear();
            pathToPrivateKey.clear();
            buttonSetPathToPrivateKey.setDisable(true);
            checkBox.setSelected(false);

            mainStage.setScene(previousPane);
        });
    }

    private static void setPath(TextField textField) {
        String path = Controller.setFile();
        if (!path.equals("")) textField.setText(path);
    }
}