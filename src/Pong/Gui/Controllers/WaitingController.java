package Pong.Gui.Controllers;

import Pong.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class WaitingController {

    @FXML
    private Button stopButton;

    @FXML
    void stop(ActionEvent event) {
        app.leaveGame();
    }

    private App app;

    public WaitingController(App app) {
        this.app = app;
    }
}

