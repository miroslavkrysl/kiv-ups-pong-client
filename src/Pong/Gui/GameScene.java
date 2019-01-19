package Pong.Gui;

import Pong.Game.Ball;
import Pong.Game.Game;
import Pong.Game.Player;
import Pong.Game.Types.Action;
import Pong.Game.Types.Direction;
import Pong.Game.Types.Side;
import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class GameScene extends Scene{
    public static final int BORDER_WIDTH = 6;
    public static final int NET_WIDTH = 6;
    public static final int PADDING = 50;

    private Game game;
    private Rectangle playerLeftRectangle;
    private Rectangle playerRightRectangle;
    private Circle ballCircle;

    private AnimationTimer animationTimer;

    private Set<KeyCode> pressedKeys;
    private Set<Action> activeActions;

    private double ratio;

    public GameScene(Game game, double ratio) {
        super(new Group());
        this.game = game;
        this.ratio = ratio;

        this.pressedKeys = Collections.synchronizedSet(new HashSet<>());
        this.activeActions = Collections.synchronizedSet(new HashSet<>());

        this.playerLeftRectangle = createPlayerRectangle();
        this.playerLeftRectangle.setX(- Player.WIDTH * ratio);

        this.playerRightRectangle = createPlayerRectangle();
        this.playerRightRectangle.setX(Game.WIDTH * ratio);

        this.ballCircle = createBallCircle();

        this.animationTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                updateGameObjects();
            }
        };

        Polyline topBorder = createBorder();
        topBorder.setTranslateY(- BORDER_WIDTH / 2.0 * ratio);

        Polyline bottomBorder = createBorder();
        bottomBorder.setTranslateY((Game.HEIGHT + BORDER_WIDTH / 2.0) * ratio);

        Polyline net = createNet();

        Group gameObjects = new Group(
                topBorder,
                bottomBorder,
                net,
                ballCircle,
                playerLeftRectangle,
                playerRightRectangle);

        StackPane root = new StackPane(gameObjects);
        root.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
        root.setPadding(new Insets(PADDING * ratio));

        initHandlers();

        this.setRoot(root);
        this.animationTimer.start();
    }

    private Rectangle createPlayerRectangle() {
        Rectangle rectangle = new Rectangle(Player.WIDTH * ratio, Player.HEIGHT * ratio);
        rectangle.setFill(Color.WHITE);
        rectangle.setStrokeWidth(0);
        return rectangle;
    }

    private Circle createBallCircle() {
        Circle ball = new Circle(Ball.RADIUS * ratio);
        ball.setCenterX(Game.WIDTH / 2.0 * ratio);
        ball.setCenterY(Game.HEIGHT / 2.0 * ratio);
        ball.setFill(Color.WHITE);
        ball.setStrokeWidth(0);
        return ball;
    }

    private Polyline createBorder() {
        Polyline border = new Polyline(0, 0, Game.WIDTH * ratio, 0);
        border.setStrokeWidth(BORDER_WIDTH * ratio);
        border.setStroke(Color.WHITE);
        border.setFill(Color.TRANSPARENT);
        border.setStrokeLineCap(StrokeLineCap.ROUND);
        return border;
    }

    private Polyline createNet() {
        double netX = (Game.WIDTH / 2.0) * ratio;
        Polyline net = new Polyline(netX, 0, netX, Game.HEIGHT * ratio);
        net.setStrokeWidth(NET_WIDTH * ratio);
        net.setStroke(Color.WHITE);
        double dash = Game.HEIGHT / 21.0 * ratio;
        net.getStrokeDashArray().addAll(dash);
        net.setStrokeLineCap(StrokeLineCap.ROUND);
        net.setFill(Color.TRANSPARENT);
        return net;
    }

    private void updateGameObjects() {
        long now = game.getApp().getCurrentTime();

        // player left
        Player playerLeft = game.getPlayerLeft();

        double playerLeftY = playerLeft.getPosition(now) * -1
                + (Game.HEIGHT / 2.0) - (Player.HEIGHT / 2.0);


        // player right
        Player playerRight = game.getPlayerRight();

        double playerRightY = playerRight.getPosition(now) * -1
                + (Game.HEIGHT / 2.0) - (Player.HEIGHT / 2.0);


        // ballCircle
        int[] ballPos = game.getBall().getPosition(now, game.getPhase());
        ballPos[0] *= -1;
        ballPos[0] += Game.WIDTH / 2.0;
        ballPos[1] *= -1;
        ballPos[1] += Game.HEIGHT / 2.0;

        playerLeftRectangle.setY(playerLeftY * ratio);
        playerRightRectangle.setY(playerRightY * ratio);
        ballCircle.setCenterX(ballPos[0] * ratio);
        ballCircle.setCenterY(ballPos[1] * ratio);
    }

    private void initHandlers() {
        this.setOnKeyPressed(keyEvent -> {
            KeyCode key = keyEvent.getCode();

            if (pressedKeys.contains(key)) {
                return;
            }
            pressedKeys.add(key);

            PlayerAction playerAction = game.getApp().getControls().getPlayerAction(key);

            if (playerAction == null) {
                return;
            }

            if (game.getType() == Game.Type.NET
                && playerAction.side != game.getPlayerSide()) {
                return;
            }


                long now = game.getApp().getCurrentTime();
            Player player = game.getPlayer(playerAction.side);
            Direction direction = Direction.STOP;

            switch (playerAction.action) {
                case UP:
                    direction = Direction.UP;
                    break;
                case DOWN:
                    direction = Direction.DOWN;
                    break;
            }

            player.changeState(now, player.getPosition(now), direction);
        });


        this.setOnKeyReleased(keyEvent -> {
            KeyCode key = keyEvent.getCode();

            pressedKeys.remove(key);

            Controls controls = game.getApp().getControls();
            PlayerAction playerAction = controls.getPlayerAction(key);

            if (playerAction == null) {
                return;
            }

            long now = game.getApp().getCurrentTime();
            Player player = game.getPlayer(playerAction.side);

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

            player.changeState(now, player.getPosition(now), Direction.STOP);
        });
    }
}
