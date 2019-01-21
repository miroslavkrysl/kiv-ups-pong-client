package Pong.Gui.Controllers;

import Pong.Operator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class UnavailableController {

    @FXML
    private Button disconnectButton;

    @FXML
    void disconnect(ActionEvent event) {
        operator.requestDisconnect();
    }

    private Operator operator;

    public UnavailableController(Operator operator) {
        this.operator = operator;
    }
}

