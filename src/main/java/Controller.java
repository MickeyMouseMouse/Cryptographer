import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Controller {
    private final static GUIDialogs dialogs = new GUIDialogs(new Image("/icon.png"));
    
    public static void generateKeyPairRSA() {
        if (!dialogs.showConfirmation("The first file will be the public key, " +
                "the second file will be the private key. Continue?"))
            return;

        File filePublicKey = saveFileDialog("Save Public Key", "PublicKey_" + getDateAndTime());
        if (filePublicKey == null) return;

        File filePrivateKey = saveFileDialog("Save Private Key", "PrivateKey_" + getDateAndTime());
        if (filePrivateKey == null) {
            filePublicKey.delete();
            return;
        }

        KeyPair keyPair = RSA.generateKeyPair();
        if (keyPair == null) {
            dialogs.showError("Key pair generation failed");
            return;
        }

        if (!writeToFile(filePublicKey, keyPair.getPublic().getEncoded())) {
            dialogs.showError("Failed to write the public key to the file");
            return;
        }

        if (!writeToFile(filePrivateKey, keyPair.getPrivate().getEncoded())) {
            dialogs.showError("Failed to write the private key to the file");
        }
    }

    // mode = false -> without signature; true -> with signature
    public static void encode(String pathToFile, String pathToPublicKey, String pathToPrivateKey, boolean mode) {
        byte[] data = readFromFile(new File(pathToFile));
        if (data == null) {
            dialogs.showError("Invalid input data");
            return;
        }

        byte[] publicKeyBytes = readFromFile(new File(pathToPublicKey));
        if (publicKeyBytes == null) {
            dialogs.showError("Invalid input data");
            return;
        }
        PublicKey publicKey = getPublicKey(publicKeyBytes);
        if (publicKey == null) {
            dialogs.showError("Public key corrupted");
            return;
        }

        File secretKeyFile = saveFileDialog("Save Secret Key", "SecretKey_" + getDateAndTime());
        if (secretKeyFile == null) return;
        SecretKey secretKey = AES.generateSecretKey();
        if (secretKey == null) return;

        File result = saveFileDialog("Save", "Encode_" + getDateAndTime());
        if (result == null) return;
        if (!writeToFile(result, AES.applyAES(data, secretKey, Cipher.ENCRYPT_MODE))) {
            dialogs.showError("Failed to write the result to the file");
            return;
        }

        if (!writeToFile(secretKeyFile, RSA.encrypt(secretKey.getEncoded(), publicKey))) {
            dialogs.showError("Failed to write the secret key to the file");
            return;
        }

        if (mode) makeSignature(secretKeyFile.getAbsolutePath(), pathToPrivateKey);
    }

    public static void makeSignature(String pathToFile, String pathToPrivateKey) {
        byte[] data = readFromFile(new File(pathToFile));
        if (data == null) return;

        byte[] privateKeyBytes = readFromFile(new File(pathToPrivateKey));
        if (privateKeyBytes == null) return;
        PrivateKey privateKey = getPrivateKey(privateKeyBytes);
        if (privateKey == null) {
            dialogs.showError("Private key corrupted");
            return;
        }

        File sign = saveFileDialog("Save Signature", "Signature_" + getDateAndTime());
        if (sign == null) return;

        if (!writeToFile(sign, RSA.makeSignature(data, privateKey))) {
            dialogs.showError("Failed to write the signature to the file");
        }
    }

    // mode = false -> without signature check; true -> with signature check
    public static void decode(String pathToFile, String pathToEncryptedSecretKey, String pathToPrivateKey,
                              String pathToSignature, String pathToPublicKey, boolean mode) {
        if (mode) {
            if (verifySignature(pathToEncryptedSecretKey, pathToSignature, pathToPublicKey)) {
                dialogs.showInformation("Confirmed");
            } else {
                dialogs.showWarning("Not confirmed");
                return;
            }
        }

        byte[] data = readFromFile(new File(pathToFile));
        if (data == null) {
            dialogs.showError("Invalid input data");
            return;
        }

        byte[] encryptedSecretKey = readFromFile(new File(pathToEncryptedSecretKey));
        if (encryptedSecretKey == null) {
            dialogs.showError("Invalid input data");
            return;
        }

        byte[] privateKeyBytes = readFromFile(new File(pathToPrivateKey));
        if (privateKeyBytes == null) {
            dialogs.showError("Invalid input data");
            return;
        }
        PrivateKey privateKey = getPrivateKey(privateKeyBytes);
        if (privateKey == null) {
            dialogs.showError("Private key corrupted");
            return;
        }

        byte[] secretKeyBytes = RSA.decrypt(encryptedSecretKey, privateKey);
        if (secretKeyBytes == null) {
            dialogs.showError("Failed decrypt secret key");
            return;
        }
        SecretKey secretKey;
        secretKey = new SecretKeySpec(secretKeyBytes, "AES");

        File result = saveFileDialog("Save", "Decode_" + getDateAndTime());
        if (result == null) return;

        if (!writeToFile(result, AES.applyAES(data, secretKey, Cipher.DECRYPT_MODE))) {
            dialogs.showError("Failed to write the result to the file");
        }
    }

    public static boolean verifySignature(String pathToSecretKey, String pathToSignature, String pathToPublicKey) {
        byte[] secretKey = readFromFile(new File(pathToSecretKey));
        if (secretKey == null) return false;

        byte[] sign = readFromFile(new File(pathToSignature));
        if (sign == null) return false;

        byte[] publicKeyBytes = readFromFile(new File(pathToPublicKey));
        if (publicKeyBytes == null) return false;
        PublicKey publicKey = getPublicKey(publicKeyBytes);
        if (publicKey == null) {
            dialogs.showError("Public key corrupted");
            return false;
        }

        return RSA.verifySignature(secretKey, sign, publicKey);
    }

    private static PublicKey getPublicKey(byte[] publicKey) {
        try {
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePublic(new X509EncodedKeySpec(publicKey));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            return null;
        }
    }

    private static PrivateKey getPrivateKey(byte[] privateKey) {
        try {
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePrivate(new PKCS8EncodedKeySpec(privateKey));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            return null;
        }
    }

    // Other
    private static File openFileDialog() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open Text File");
        return chooser.showOpenDialog(new Stage());
    }

    private static File saveFileDialog(String title, String fileName) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle(title);
        chooser.setInitialFileName(fileName);
        return chooser.showSaveDialog(new Stage());
    }

    // returns true (OK); false (WRONG)
    private static boolean writeToFile(File file, byte[] data) {
        try (FileOutputStream output = new FileOutputStream(file)) {
            output.write(data);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private static byte[] readFromFile(File file) {
        if (!file.exists()) return null;

        try {
            byte[] result = new byte[(int) file.length()];
            try (FileInputStream input = new FileInputStream(file)) {
                input.read(result);
                return result;
            } catch (IOException e) { return null; }
        } catch (NegativeArraySizeException e) {
            dialogs.showError("The selected file is too large");
            return null;
        }
    }

    private static String getDateAndTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd_MM_yyyy-HH_mm_ss");
        return formatter.format(Calendar.getInstance().getTime());
    }

    public static String setFile() {
        File file = openFileDialog();
        if (file == null) return "";
        if (file.isDirectory()) {
            dialogs.showError("Choose a file (not directory)");
            return "";
        }
        return file.getAbsolutePath();
    }
}