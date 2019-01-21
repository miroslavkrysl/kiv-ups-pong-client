package Pong.Gui.Controllers;

import Pong.Network.Connection;
import Pong.Operator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class LobbyController implements Initializable {

    @FXML
    private Label port;

    @FXML
    private Label ip;

    @FXML
    private Button joinButton;

    @FXML
    private Button disconnectButton;

    @FXML
    void join(ActionEvent event) {
        operator.requestJoinGame();
    }

    @FXML
    void disconnect(ActionEvent event) {
        operator.requestDisconnect();
    }

    private Operator operator;

    public LobbyController(Operator operator) {
        this.operator = operator;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Connection connection = operator.getConnection();
        ip.setText(connection.getAddress().getHostString());
        port.setText(Integer.toString(connection.getAddress().getPort()));
    }
}
