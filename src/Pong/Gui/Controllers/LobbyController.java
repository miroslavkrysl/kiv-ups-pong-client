package Pong.Gui.Controllers;

import Pong.App;
import Pong.Network.Connection;
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
        app.joinGame();
    }

    @FXML
    void disconnect(ActionEvent event) {
        app.disconnect();
    }

    private App app;
    private Connection connection;

    public LobbyController(App app, Connection connection) {
        this.app = app;
        this.connection = connection;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ip.setText(connection.getAddress().getHostString());
        port.setText(Integer.toString(connection.getAddress().getPort()));
    }
}
