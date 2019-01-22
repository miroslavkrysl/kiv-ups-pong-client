package Pong.Gui.Controllers;

import Pong.Exceptions.OperatorException;
import Pong.Operator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class UnavailableController {

    @FXML
    private Button disconnectButton;

    @FXML
    void disconnect(ActionEvent event) {
        try {
            operator.requestDisconnect();
        } catch (OperatorException e) {
            System.out.println("Error while disconnecting");
        }
    }

    private Operator operator;

    public UnavailableController(Operator operator) {
        this.operator = operator;
    }
}

