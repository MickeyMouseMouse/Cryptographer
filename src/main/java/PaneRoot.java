import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;

public class PaneRoot extends GridPane {
    public final Button buttonEncode = new Button("Encode");
    public final Button buttonDecode = new Button("Decode");
    private final TextArea help = new TextArea("Select an option to view detailed information.");

    PaneRoot() {
        Button buttonGenerateKeyPair = new Button("Generate A Key Pair");
        buttonGenerateKeyPair.setMinWidth(140);
        buttonEncode.setMinWidth(140);
        buttonDecode.setMinWidth(140);
        help.setStyle("-fx-font-size: 16px;");
        help.setWrapText(true);
        help.setEditable(false);
        help.setMaxWidth(350);
        help.setMaxHeight(200);


        VBox vbox = new VBox(buttonGenerateKeyPair, buttonEncode, buttonDecode);
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(10));
        vbox.setAlignment(Pos.CENTER);

        this.add(vbox, 0, 0);
        this.add(help, 1, 0);

        buttonGenerateKeyPair.setOnMouseEntered((e) -> viewHelpOnButtonGenerateKeyPair());
        buttonGenerateKeyPair.setOnMouseExited((e) -> eraseHelpInfo());
        buttonGenerateKeyPair.setOnMouseClicked((e) -> Controller.generateKeyPairRSA());

        buttonEncode.setOnMouseEntered((e) -> viewHelpOnButtonEncode());
        buttonEncode.setOnMouseExited((e) -> eraseHelpInfo());

        buttonDecode.setOnMouseEntered((e) -> viewHelpOnButtonDecode());
        buttonDecode.setOnMouseExited((e) -> eraseHelpInfo());
    }

    public void viewHelpOnButtonGenerateKeyPair() {
        help.setText("Generate a key pair: public and private. Pass the public key to your interlocutor. " +
                "Keep the private key in a safe place.");
    }

    public void viewHelpOnButtonEncode() {
        help.setText("Select the file to encrypt and specify your interlocutor's public key. " +
                "You will get an encrypted file and secret key. Pass them on to your interlocutor." +
                "\n\nTo create a signature, specify your private key in addition.");
    }

    public void viewHelpOnButtonDecode() {
        help.setText("Select the file to decrypt, specify the secret key and your private key. " +
                "You will get the decrypted file." +
                "\n\nTo verify the signature, set it and the interlocutor's public key.");
    }

    public void eraseHelpInfo() { help.setText("Select an option to view detailed information."); }
}
