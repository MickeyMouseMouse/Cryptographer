import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class CryptographerGUI extends Application {
    public static void main(String[] args) { Application.launch(args); }

    @Override
    public void start(Stage mainStage) {
        PaneRoot paneRoot = new PaneRoot();
        Scene sceneRoot = new Scene(paneRoot);

        PaneEncode paneEncode = new PaneEncode(mainStage, sceneRoot);
        Scene sceneEncode = new Scene(paneEncode);

        PaneDecode paneDecode = new PaneDecode(mainStage, sceneRoot);
        Scene sceneDecode = new Scene(paneDecode);

        paneRoot.buttonEncode.setOnMouseClicked((e) -> mainStage.setScene(sceneEncode));
        paneRoot.buttonDecode.setOnMouseClicked((e) -> mainStage.setScene(sceneDecode));

        mainStage.show();
        mainStage.setTitle("Cryptographer");
        mainStage.getIcons().add(new Image("/icon.png"));
        mainStage.centerOnScreen();
        mainStage.setResizable(false);
        mainStage.setScene(sceneRoot);
    }
}
