import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

public class EncryptDecryptGUI {
    private final JButton encryptButton;
    private final JButton decryptButton;
    private final JButton manualDecryptButton;
    private final JTextArea messageArea;
    private final JTextArea encryptResultArea;
    private final JTextArea decryptResultArea;
    private final JTextArea manualEncryptResultArea;
    private final JTextArea manualDecryptResultArea;
    private final JPasswordField passwordField;
    private final JButton selectPublicKeyButton;
    private final JButton selectPrivateKeyButton;
    private File publicKeyFile;
    private File privateKeyFile;
    private PublicKey publicKey;
    private PrivateKey privateKey;
    private byte[] encryptedMessage;
    private String decryptedMessage;
    private String manualDecryptedMessage;
    

    public EncryptDecryptGUI() {
        encryptButton = new JButton("Encriptar");
        decryptButton = new JButton("Desencriptar");
        manualDecryptButton  = new JButton("Desencriptar manual");
        messageArea = new JTextArea(2, 10);
        encryptResultArea = new JTextArea(2, 20);
        decryptResultArea = new JTextArea(2, 20);
        manualEncryptResultArea = new JTextArea(2, 20);
        manualDecryptResultArea = new JTextArea(2, 20);
        passwordField = new JPasswordField(20);
        selectPublicKeyButton = new JButton("Seleccionar clave pública (.pem)");
        selectPrivateKeyButton = new JButton("Seleccionar clave privada (.p12)");

        selectPublicKeyButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                publicKeyFile = fileChooser.getSelectedFile();
                try {
                    publicKey = EncryptDecryptFunction.getPublicKey(publicKeyFile.getPath());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });

        selectPrivateKeyButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                privateKeyFile = fileChooser.getSelectedFile();
                try {
                    privateKey = EncryptDecryptFunction.getPrivateKey(privateKeyFile.getPath(), new String(passwordField.getPassword()));
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });

        encryptButton.addActionListener(e -> {
            String message = messageArea.getText();
            try {
                encryptedMessage = EncryptDecryptFunction.encrypt(message, publicKey);
                encryptResultArea.setText(Base64.getEncoder().encodeToString(encryptedMessage));
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        decryptButton.addActionListener(e -> {
            try {
                decryptedMessage = EncryptDecryptFunction.decrypt(Base64.getEncoder().encodeToString(encryptedMessage), privateKey);
                decryptResultArea.setText(decryptedMessage);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });
        
        manualDecryptButton.addActionListener(e -> {
            try {
                manualDecryptedMessage = EncryptDecryptFunction.decrypt(
                        manualEncryptResultArea.getText(), privateKey);
                manualDecryptResultArea.setText(manualDecryptedMessage);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("RSAGUI");
        EncryptDecryptGUI rsaGUI = new EncryptDecryptGUI();
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(rsaGUI.selectPublicKeyButton);
        panel.add(new JLabel("Contraseña de la clave privada:"));
        panel.add(rsaGUI.passwordField);
        panel.add(rsaGUI.selectPrivateKeyButton);
        panel.add(new JLabel("Mensaje:"));
        panel.add(new JScrollPane(rsaGUI.messageArea));
        panel.add(rsaGUI.encryptButton);
        panel.add(new JLabel("Texto encriptado:"));
        panel.add(new JScrollPane(rsaGUI.encryptResultArea));
        panel.add(rsaGUI.decryptButton);
        panel.add(new JLabel("Texto desencriptado:"));
        panel.add(new JScrollPane(rsaGUI.decryptResultArea));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel panel2 = new JPanel();
        panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS));
        panel2.add(new JLabel("Texto encriptado manual:"));
        panel2.add(new JScrollPane(rsaGUI.manualEncryptResultArea));
        panel2.add(rsaGUI.manualDecryptButton);
        panel2.add(new JLabel("Texto desencriptado manual:"));
        panel2.add(new JScrollPane(rsaGUI.manualDecryptResultArea));
        panel2.setAlignmentX(Component.RIGHT_ALIGNMENT);

        JPanel panelContenedor = new JPanel();
        panelContenedor.setLayout(new BoxLayout(panelContenedor, BoxLayout.X_AXIS));
        panelContenedor.add(panel);
        panelContenedor.add(panel2);

        frame.setContentPane(panelContenedor);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

}
