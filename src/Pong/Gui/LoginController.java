package Pong.Gui;

import Pong.Exceptions.OperatorException;
import Pong.Network.Connection;
import Pong.Network.Exceptions.ConnectionException;
import Pong.Operator;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

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

    @FXML
    void connect(ActionEvent event) {
        String nickname = nicknameTF.getText();
        String ip = ipTF.getText();
        int port = 0;

        try {
            port = Integer.parseUnsignedInt(portTF.getText());
        }
        catch (NumberFormatException e) {
            errorMessageNetwork.setVisible(true);
            errorMessageNetwork.setText("port is invalid");
            return;
        }

        if (nickname.length() < Operator.NICKNAME_LENGTH_MIN
                || nickname.length() > Operator.NICKNAME_LENGTH_MAX) {
            errorMessageNetwork.setVisible(true);
            errorMessageNetwork.setText("nickname is in invalid format");
            return;
        }

        errorMessageNetwork.setVisible(false);
        errorMessageNetwork.setText("");
        errorMessageLocal.setVisible(false);
        errorMessageLocal.setText("");

        try {
            operator.requestConnect(ip, port, nickname);
        } catch (ConnectionException e) {
            errorMessageNetwork.setVisible(true);
            errorMessageNetwork.setText("Can't connect to the server");
        }
    }

    @FXML
    void startLocal(ActionEvent event) {
        String leftPlayerNickname = leftPlayerTF.getText();
        String rightPlayerNickname = rightPlayerTF.getText();

        if (leftPlayerNickname.length() < Operator.NICKNAME_LENGTH_MIN
                || leftPlayerNickname.length() > Operator.NICKNAME_LENGTH_MAX
                || rightPlayerNickname.length() < Operator.NICKNAME_LENGTH_MIN
                || rightPlayerNickname.length() > Operator.NICKNAME_LENGTH_MAX) {
            errorMessageLocal.setVisible(true);
            errorMessageLocal.setText("nickname is in invalid format");
            return;
        }

        errorMessageLocal.setVisible(false);
        errorMessageLocal.setText("");
        errorMessageNetwork.setVisible(false);
        errorMessageNetwork.setText("");

        Platform.runLater(() -> {
            try {
                operator.startLocalGame(leftPlayerNickname, rightPlayerNickname);
            } catch (OperatorException e) {
                System.out.println("Error while starting local game " + e.toString());
            }
        });
    }

    private Operator operator;

    public LoginController(Operator operator) {
        this.operator = operator;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        portTF.textProperty().addListener((observable, oldPort, newPort) -> {
            if (!newPort.matches("\\d*")) {
                portTF.setText(oldPort);
            }
        });
    }
}
