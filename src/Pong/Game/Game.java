package Pong.Game;

import Pong.App;
import Pong.Game.Types.GamePhase;
import Pong.Game.Types.Side;
import com.sun.istack.internal.NotNull;
import javafx.animation.AnimationTimer;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.Random;

public class Game {
    public static final int WIDTH = 1920;
    public static final int HEIGHT = 1080;
    public static final long START_DELAY = 3000;

    private App app;
    private Random random;
    private AnimationTimer timer;

    private boolean local;
    private Side side;

    private String nicknameLeft;
    private String nicknameRight;

    private IntegerProperty scoreLeft;
    private IntegerProperty scoreRight;

    private Player playerLeft;
    private Player playerRight;
    private Ball ball;

    private Side serviceSide;

    private ObjectProperty<GamePhase> phase;

    private Game(@NotNull App app) {
        this.app = app;
        this.serviceSide = Side.LEFT;

        long now = getTime();

        playerLeft = new Player(now);
        playerRight = new Player(now);
        ball = new Ball(now);
        random = new Random(now);

        timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                gameLoop();
            }
        };
        timer.start();
    }

    public Game(@NotNull App app, String nicknameLeft, String nicknameRight) {
        this(app);
        this.nicknameLeft = nicknameLeft;
        this.nicknameRight = nicknameRight;
        this.local = true;
        this.side = Side.LEFT;
        this.phase = new SimpleObjectProperty<>(GamePhase.WAITING);
    }

    public Game(@NotNull App app, String nickname, String nicknameOpponent, Side side) {
        this(app);
        this.side = side;
        this.local = false;

        if (side == Side.LEFT) {
            this.nicknameLeft = nickname;
            this.nicknameRight = nicknameOpponent;
        }
        else {
            this.nicknameLeft = nicknameOpponent;
            this.nicknameRight = nickname;
        }
    }

    public void newRound() {
        phase.set(GamePhase.WAITING);
        ball.setState(new BallState(getTime()));
    }

    public void startRound(BallState ballState) {
        phase.set(GamePhase.START);
        ball.setState(ballState);
    }

    public void startRound() {
        phase.set(GamePhase.START);
        ball.setState(new BallState(getTime() + START_DELAY, serviceSide, 0, 0, BallState.SPEED_MIN));
    }

    private void gameLoop() {
        long now = app.getTime();
        GamePhase phase = getPhase();

        playerLeft.update(now);
        playerRight.update(now);
        ball.update(now, phase);

        checkCollisions(now);
    }

    private void checkCollisions(long now) {
        if (!isLocal() || (getPhase() != GamePhase.PLAYING && getPhase() != GamePhase.START)) {
            return;
        }

        Side side;

        if (ball.getX() <= -BallState.MOVEMENT_WIDTH / 2) {
            side = Side.LEFT;
        }
        else if (ball.getX() >= BallState.MOVEMENT_WIDTH / 2) {
            side = Side.RIGHT;
        }
        else {
            return;
        }

        if (ball.getState().getSide() == side) {
            return;
        }

        int playerY = getPlayer(side).getY();
        int ballY = ball.getY();

        if (ballY > playerY + (PlayerState.HEIGHT / 2) || ballY < playerY - (PlayerState.HEIGHT / 2)) {
            switch (side) {
                case LEFT:
                    serviceSide = Side.LEFT;
                    scoreRight.add(1);
                    break;
                case RIGHT:
                    serviceSide = Side.RIGHT;
                    scoreLeft.add(1);
                    break;
            }

            newRound();
            return;
        }

        int angle = random.nextInt(BallState.ANGLE_MAX - BallState.ANGLE_MIN) + BallState.ANGLE_MIN;
        int speed = random.nextInt(BallState.SPEED_MAX - BallState.SPEED_MIN) + BallState.SPEED_MIN;

        ball.setState(new BallState(now, side, ballY, angle, speed));

        phase.set(GamePhase.PLAYING);
    }

    public Player getPlayer(Side side) {
        switch (side) {
            case LEFT:
                return playerLeft;
            case RIGHT:
                return playerRight;
        }

        return null;
    }

    public void setBallState(BallState state) {
        this.ball.setState(state);
    }

    public void setPlayerState(PlayerState state, Side left) {
        switch (side) {
            case LEFT:
                this.playerLeft.setState(state);
                break;
            case RIGHT:
                this.playerRight.setState(state);
                break;
        }
    }

    public long getTime() {
        return app.getTime();
    }

    public boolean isLocal() {
        return local;
    }

    public Side getSide() {
        return side;
    }

    public String getNicknameLeft() {
        return nicknameLeft;
    }

    public String getNicknameRight() {
        return nicknameRight;
    }

    public int getScoreLeft() {
        return scoreLeft.get();
    }

    public IntegerProperty scoreLeftProperty() {
        return scoreLeft;
    }

    public int getScoreRight() {
        return scoreRight.get();
    }

    public IntegerProperty scoreRightProperty() {
        return scoreRight;
    }

    public Player getPlayerLeft() {
        return playerLeft;
    }

    public Player getPlayerRight() {
        return playerRight;
    }

    public Ball getBall() {
        return ball;
    }

    public GamePhase getPhase() {
        return phase.get();
    }

    public ObjectProperty<GamePhase> phaseProperty() {
        return phase;
    }
}
