package Pong.Gui.Controllers;

import Pong.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class UnavailableController {

    @FXML
    private Button disconnectButton;

    @FXML
    void disconnect(ActionEvent event) {
        app.disconnect();
    }

    private App app;

    public UnavailableController(App app) {
        this.app = app;
    }
}

