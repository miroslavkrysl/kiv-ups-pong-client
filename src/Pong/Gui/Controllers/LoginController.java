package Pong.Gui.Controllers;

import Pong.App;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * The type Login controller.
 */
public class LoginController implements Initializable {

    @FXML
    private TextField ipTF;

    @FXML
    private TextField portTF;

    @FXML
    private Button connectButton;

    @FXML
    private TextField nicknameTF;

    @FXML
    private Label errorMessageNetwork;

    @FXML
    private ProgressIndicator progressIndicator;

    @FXML
    private TextField leftPlayerTF;

    @FXML
    private TextField rightPlayerTF;

    @FXML
    private Label errorMessageLocal;

    @FXML
    private Button playButton;

    /**
     * Connect.
     *
     * @param event the event
     */
    @FXML
    void connect(ActionEvent event) {
        String nickname = nicknameTF.getText();
        String ip = ipTF.getText();
        int port = 0;

        try {
            port = Integer.parseUnsignedInt(portTF.getText());
        }
        catch (NumberFormatException e) {
            setErrorMessage("Port is invalid", true);
            return;
        }

        if (!nickname.matches("[a-zA-Z0-9]{3,16}")) {
            setErrorMessage("Nickname is in invalid format", true);
            return;
        }

        setErrorMessage(null, null);

        int finalPort = port;

        connectButton.setDisable(true);
        playButton.setDisable(true);
        progressIndicator.setVisible(true);

        Task connectTask = new Task() {
            @Override
            protected Object call() throws Exception {
                app.connect(ip, finalPort, nickname);

                progressIndicator.setVisible(false);
                connectButton.setDisable(false);
                playButton.setDisable(false);
                return null;
            }
        };

        Thread connectThread = new Thread(connectTask);
        connectThread.start();
    }

    /**
     * Start local.
     *
     * @param event the event
     */
    @FXML
    void startLocal(ActionEvent event) {
        String leftPlayerNickname = leftPlayerTF.getText();
        String rightPlayerNickname = rightPlayerTF.getText();

        if (!leftPlayerNickname.matches("[a-zA-Z0-9]{3,16}")
                || !rightPlayerNickname.matches("[a-zA-Z0-9]{3,16}")) {
            setErrorMessage("Nickname is in invalid format", false);
            return;
        }

        app.startLocalGame(leftPlayerNickname, rightPlayerNickname);
    }

    private App app;

    /**
     * Instantiates a new Login controller.
     *
     * @param app the app
     */
    public LoginController(App app) {
        this.app = app;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        portTF.textProperty().addListener((observable, oldPort, newPort) -> {
            if (!newPort.matches("\\d*")) {
                portTF.setText(oldPort);
            }
        });
    }

    public void setErrorMessage(String message, Boolean isNetworkMessage) {
        Platform.runLater(() -> {
            errorMessageNetwork.setText("");
            errorMessageLocal.setText("");

            if (message == null || message.isEmpty()) {
                return;
            }

            if (isNetworkMessage) {
                errorMessageNetwork.setText(message);
            }
            else {
                errorMessageLocal.setText(message);
            }
        });
    }
}
