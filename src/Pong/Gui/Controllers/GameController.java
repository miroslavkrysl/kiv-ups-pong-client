package Pong.Gui.Controllers;

import Pong.App;
import Pong.Game.*;
import Pong.Game.Types.Action;
import Pong.Game.Types.Direction;
import Pong.Game.Types.Side;
import Pong.Gui.Controls;
import Pong.Gui.PlayerAction;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * The type Game controller.
 */
public class GameController implements Initializable {

    @FXML
    private Label nicknameLeft;

    @FXML
    private Label nicknameRight;

    @FXML
    private Label scoreRight;

    @FXML
    private Label scoreLeft;

    @FXML
    private Pane field;

    /**
     * Key pressed handler. Process user keyboard actions.
     *
     * @param event the key event
     */
    @FXML
    public void keyPressed(KeyEvent event) {
        KeyCode key = event.getCode();

        if (pressedKeys.contains(key)) {
            return;
        }
        pressedKeys.add(key);

        PlayerAction playerAction = controls.getPlayerAction(key);

        if (playerAction == null) {
            return;
        }


        if (playerAction.action == Action.READY) {
            if (isNetworkGame) {
                ((NetworkGame)game).playerReady(((NetworkGame) game).getLocalPlayerSide());
            }
            else {
                game.startRound();
            }
            return;
        }

        if (isNetworkGame && playerAction.side != ((NetworkGame)game).getLocalPlayerSide()) {
            return;
        }

        Direction direction = Direction.STOP;

        switch (playerAction.action) {
            case UP:
                direction = Direction.UP;
                break;
            case DOWN:
                direction = Direction.DOWN;
                break;
        }

        game.getPlayer(playerAction.side).changeDirection(game.getTime(), direction);
    }

    /**
     * Key released handler. Processed user keyboard actions.
     *
     * @param event the key event
     */
    @FXML
    public void keyReleased(KeyEvent event) {
        KeyCode key = event.getCode();

        pressedKeys.remove(key);

        PlayerAction playerAction = controls.getPlayerAction(key);

        if (playerAction == null) {
            return;
        }

        switch (playerAction.action) {
            case UP:
                if (pressedKeys.contains(controls.getKeyCode(playerAction.side, Action.DOWN))){
                    return;
                }
            case DOWN:
                if (pressedKeys.contains(controls.getKeyCode(playerAction.side, Action.UP))){
                    return;
                }
        }

        game.getPlayer(playerAction.side).changeDirection(game.getTime(), Direction.STOP);
    }

    /**
     * Call apps leave game method.
     *
     * @param event the event
     */
    @FXML
    void leaveGame(ActionEvent event) {
        app.leaveGame();
    }

    private static final int LINE_WIDTH = 10;
    private static final int NET_DASHES_COUNT = 21;

    private App app;
    private Controls controls;
    private Game game;
    private DoubleProperty ratio;

    private boolean isNetworkGame;

    private Set<KeyCode> pressedKeys;
    private Set<Action> activeActions;

    /**
     * Instantiates a new GameController.
     *
     * @param app      the app
     * @param controls the controls
     * @param game     the game
     * @param ratio    the ratio
     */
    public GameController(App app, Controls controls, Game game, double ratio) {
        this.app = app;
        this.controls = controls;
        this.game = game;
        this.ratio = new SimpleDoubleProperty(ratio);
        this.pressedKeys = Collections.synchronizedSet(new HashSet<>());
        this.activeActions = Collections.synchronizedSet(new HashSet<>());

        isNetworkGame = game.getClass() == NetworkGame.class;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // bind texts
        nicknameLeft.textProperty().bind(game.nicknameProperty(Side.LEFT));
        nicknameRight.textProperty().bind(game.nicknameProperty(Side.RIGHT));

        if (isNetworkGame) {
            switch (((NetworkGame)game).getLocalPlayerSide()) {
                case LEFT:
                    nicknameLeft.setTextFill(Color.GREEN);
                    nicknameRight.setTextFill(Color.RED);
                    break;
                case RIGHT:
                    nicknameRight.setTextFill(Color.GREEN);
                    nicknameLeft.setTextFill(Color.RED);
                    break;
            }
        }

        scoreLeft.textProperty().bind(game.scoreProperty(Side.LEFT).asString());
        scoreRight.textProperty().bind(game.scoreProperty(Side.RIGHT).asString());


        // bind game field
        DoubleBinding fieldWidthBinding = ratio.multiply(Game.WIDTH + 2 * PlayerState.WIDTH);
        field.prefWidthProperty().bind(fieldWidthBinding);
        field.minWidthProperty().bind(fieldWidthBinding);
        field.maxWidthProperty().bind(fieldWidthBinding);

        DoubleBinding fieldHeightBinding = ratio.multiply(Game.HEIGHT + 2 * LINE_WIDTH);
        field.prefHeightProperty().bind(fieldHeightBinding);
        field.minHeightProperty().bind(fieldHeightBinding);
        field.maxHeightProperty().bind(fieldHeightBinding);

        // create game objects
        Point2D center = new Point2D(PlayerState.WIDTH, LINE_WIDTH);
        double halfHeight = Game.HEIGHT / 2.0;
        double halfWidth = Game.WIDTH / 2.0;
        double halfPlayerHeight = PlayerState.HEIGHT / 2.0;
        double halfLineWidth = LINE_WIDTH / 2.0;

        Rectangle playerLeft = createPlayerRectangle();
        playerLeft.layoutXProperty().bind(ratio.multiply(center.getX() - PlayerState.WIDTH));
        playerLeft.layoutYProperty().bind(game.getPlayer(Side.LEFT)
                .yProperty()
                .multiply(-1)
                .add(center.getY())
                .add(halfHeight)
                .subtract(halfPlayerHeight)
                .multiply(ratio));

        Rectangle playerRight = createPlayerRectangle();
        playerRight.layoutXProperty().bind(ratio.multiply(center.getX() + Game.WIDTH));
        playerRight.layoutYProperty().bind(game.getPlayer(Side.RIGHT)
                .yProperty()
                .multiply(-1)
                .add(center.getY())
                .add(halfHeight)
                .subtract(halfPlayerHeight)
                .multiply(ratio));

        Circle ball = createBallCircle();
        ball.layoutXProperty().bind(game.getBall()
                .xProperty()
                .add(center.getX())
                .add(halfWidth)
                .multiply(ratio));
        ball.layoutYProperty().bind(game.getBall()
                .yProperty()
                .multiply(-1)
                .add(center.getY())
                .add(halfHeight)
                .multiply(ratio));

        Line borderTop = createBorder();
        borderTop.startXProperty().bind(ratio.multiply(center.getX()));
        borderTop.startYProperty().bind(ratio.multiply(center.getY() - halfLineWidth));
        borderTop.endXProperty().bind(ratio.multiply(center.getX() + Game.WIDTH));
        borderTop.endYProperty().bind(ratio.multiply(center.getY() - halfLineWidth));

        Line borderBotom = createBorder();
        borderBotom.startXProperty().bind(ratio.multiply(center.getX()));
        borderBotom.startYProperty().bind(ratio.multiply(center.getY() + Game.HEIGHT + halfLineWidth));
        borderBotom.endXProperty().bind(ratio.multiply(center.getX() + Game.WIDTH));
        borderBotom.endYProperty().bind(ratio.multiply(center.getY() + Game.HEIGHT + halfLineWidth));

        Line net = createNet();
        net.startXProperty().bind(ratio.multiply(center.getX() + halfWidth));
        net.startYProperty().bind(ratio.multiply(center.getY()));
        net.endXProperty().bind(ratio.multiply(center.getX() + halfWidth));
        net.endYProperty().bind(ratio.multiply(center.getY() + Game.HEIGHT));

        field.getChildren().addAll(
                borderTop,
                borderBotom,
                net,
                ball,
                playerLeft,
                playerRight
        );
    }

    private Rectangle createPlayerRectangle() {
        Rectangle rectangle = new Rectangle();
        rectangle.arcWidthProperty().bind(ratio.multiply(10));
        rectangle.arcHeightProperty().bind(ratio.multiply(10));
        rectangle.widthProperty().bind(ratio.multiply(PlayerState.WIDTH));
        rectangle.heightProperty().bind(ratio.multiply(PlayerState.HEIGHT));
        rectangle.setFill(Color.WHITE);
        rectangle.setStrokeWidth(0);
        return rectangle;
    }

    private Circle createBallCircle() {
        Circle ball = new Circle();
        ball.radiusProperty().bind(ratio.multiply(BallState.RADIUS));
        ball.setFill(Color.WHITE);
        ball.setStrokeWidth(0);
        return ball;
    }

    private Line createBorder() {
        Line border = new Line();
        border.strokeWidthProperty().bind(ratio.multiply(LINE_WIDTH));
        border.setStroke(Color.WHITE);
        border.setFill(Color.TRANSPARENT);
        border.setStrokeLineCap(StrokeLineCap.ROUND);
        return border;
    }

    private Line createNet() {
        Line net = new Line();
        net.strokeWidthProperty().bind(ratio.multiply(LINE_WIDTH));
        net.setStroke(Color.WHITE);
        net.setFill(Color.TRANSPARENT);
        net.getStrokeDashArray().add((double)Game.HEIGHT / NET_DASHES_COUNT * ratio.get());

        ratio.addListener((observable, oldRation, newRatio) -> {
            net.getStrokeDashArray().removeAll();
            net.getStrokeDashArray().add(Game.HEIGHT / NET_DASHES_COUNT * newRatio.doubleValue());
        });
        return net;
    }
}
