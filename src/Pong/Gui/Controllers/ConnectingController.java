package Pong.Gui.Controllers;

import Pong.Operator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ConnectingController {

    @FXML
    private Button stopButton;

    @FXML
    void stop(ActionEvent event) {
        operator.requestDisconnect();
    }

    private Operator operator;

    public ConnectingController(Operator operator) {
        this.operator = operator;
    }
}

