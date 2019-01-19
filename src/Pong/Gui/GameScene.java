package Pong.Gui;

import Pong.Game.BallState;
import Pong.Game.Game;
import Pong.Game.PlayerState;
import Pong.Game.Types.Side;
import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;

public class GameScene extends Scene{
    public static final int BORDER_WIDTH = 6;
    public static final int NET_WIDTH = 6;
    public static final int PADDING = 50;

    private Game game;
    private Rectangle playerLeft;
    private Rectangle playerRight;
    private Circle ball;

    private AnimationTimer animationTimer;

    private double ratio;

    public GameScene(Game game, double ratio) {
        super(new Group());
        this.game = game;
        this.ratio = ratio;

        this.playerLeft = createPlayerRectangle();
        this.playerLeft.setX(- PlayerState.WIDTH * ratio);

        this.playerRight = createPlayerRectangle();
        this.playerRight.setX(Game.WIDTH * ratio);

        this.ball = createBallCircle();

//        this.animationTimer = new AnimationTimer() {
//            @Override
//            public void handle(long l) {
//                updateGameObjects();
//            }
//        };

        Polyline topBorder = createBorder();
        topBorder.setTranslateY(- BORDER_WIDTH / 2.0 * ratio);

        Polyline bottomBorder = createBorder();
        bottomBorder.setTranslateY((Game.HEIGHT + BORDER_WIDTH / 2.0) * ratio);

        Polyline net = createNet();

        Group gameObjects = new Group(
                topBorder,
                bottomBorder,
                net,
                ball,
                playerLeft,
                playerRight);

        StackPane root = new StackPane(gameObjects);
        root.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
        root.setPadding(new Insets(PADDING * ratio));

        this.setRoot(root);
    }

    private Rectangle createPlayerRectangle() {
        Rectangle rectangle = new Rectangle(PlayerState.WIDTH * ratio, PlayerState.HEIGHT * ratio);
        rectangle.setFill(Color.WHITE);
        rectangle.setStrokeWidth(0);
        return rectangle;
    }

    private Circle createBallCircle() {
        Circle ball = new Circle(BallState.RADIUS * ratio);
        ball.setFill(Color.WHITE);
        ball.setStrokeWidth(0);
        return ball;
    }

    private Polyline createBorder() {
        Polyline border = new Polyline(0, 0, Game.WIDTH * ratio, 0);
        border.setStrokeWidth(BORDER_WIDTH * ratio);
        border.setStroke(Color.WHITE);
        border.setFill(Color.TRANSPARENT);
        return border;
    }

    private Polyline createNet() {
        double netX = (Game.WIDTH / 2.0 - NET_WIDTH / 2.0) * ratio;
        Polyline net = new Polyline(netX, 0, netX, Game.HEIGHT * ratio);
        net.setStrokeWidth(NET_WIDTH * ratio);
        net.setStroke(Color.WHITE);
        double dash = Game.HEIGHT / 21.0;
                net.getStrokeDashArray().addAll(dash, dash);
        net.setFill(Color.TRANSPARENT);
        return net;
    }

    private void updateGameObjects() {
        long now = game.getApp().getCurrentTime();

        // player left
        PlayerState playerStateLeft = game.getPlayerStateLeft();
        double playerLeftY = playerStateLeft.getPosition();

        playerLeftY += (now - playerStateLeft.getTimestamp())
                * (PlayerState.SPEED / 1000.0)
                * playerStateLeft.getDirection().getValue() * -1;
        playerLeftY += (Game.HEIGHT / 2.0) - (PlayerState.HEIGHT / 2.0);
        playerLeftY *= ratio;


        // player right
        PlayerState playerStateRight = game.getPlayerStateRight();
        double playerRightY = playerStateRight.getPosition();

        playerRightY += (now - playerStateRight.getTimestamp())
                * (PlayerState.SPEED / 1000.0)
                * playerStateRight.getDirection().getValue() * -1;
        playerRightY += (Game.HEIGHT / 2.0) - (PlayerState.HEIGHT / 2.0);
        playerRightY *= ratio;


        // ball
        BallState ballState = game.getBallState();

        double hypotenuse = (now - ballState.getTimestamp())
                * (ballState.getSpeed() / 1000.0);

        double legY = Math.sin(ballState.getAngle()) * hypotenuse;
        double legX = Math.cos(ballState.getAngle()) * hypotenuse;

        double ballX = ballState.getSide() == Side.LEFT
                ? legX
                : Game.WIDTH - legX;
        ballX *= ratio;

        double ballY = legY % Game.HEIGHT;
        if ((legY / Game.HEIGHT) % 2 == 0) {
            ballY *= -1;
        }
        ballY += ballState.getPosition();


        playerLeft.setY(playerLeftY);
        playerRight.setY(playerRightY);
        ball.setCenterX(ballX);
        ball.setCenterY(ballY);
    }
}
