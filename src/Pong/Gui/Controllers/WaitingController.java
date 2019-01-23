package Pong.Gui.Controllers;

import Pong.Operator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class WaitingController {

    @FXML
    private Button stopButton;

    @FXML
    void stop(ActionEvent event) {
        operator.requestLeaveGame();
    }

    private Operator operator;

    public WaitingController(Operator operator) {
        this.operator = operator;
    }
}

