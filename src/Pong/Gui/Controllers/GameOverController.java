package Pong.Gui.Controllers;

import Pong.Game.Game;
import Pong.Operator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class GameOverController implements Initializable {

    @FXML
    private Label scoreLeft;

    @FXML
    private Label scoreRight;

    @FXML
    private Button leaveButton;

    @FXML
    void leave(ActionEvent event) {
        operator.requestLeaveGame();
    }

    private Operator operator;
    private Game game;

    public GameOverController(Operator operator, Game game) {
        this.operator = operator;
        this.game = game;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        scoreLeft.setText(Integer.toString(game.getScoreLeft()));
        scoreRight.setText(Integer.toString(game.getScoreRight()));
    }
}
