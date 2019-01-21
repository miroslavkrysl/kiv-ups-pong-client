package Pong.Gui.Controllers;

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
//        try {
//            operator.connect();
//        }
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

        Platform.runLater(() -> {
            operator.startLocalGame(leftPlayerNickname, rightPlayerNickname);
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
