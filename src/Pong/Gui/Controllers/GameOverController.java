package Pong.Gui.Controllers;

import Pong.App;
import Pong.Game.Game;
import Pong.Game.Types.Side;
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
        app.leaveGame();
    }

    private App app;
    private Game game;
    private String message;

    public GameOverController(App app, Game game, String message) {
        this.app = app;
        this.game = game;
        this.message = message;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        scoreLeft.setText(Integer.toString(game.getScore(Side.LEFT)));
        scoreRight.setText(Integer.toString(game.getScore(Side.RIGHT)));
    }
}
