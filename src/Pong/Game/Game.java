package Pong.Game;


import Pong.App;
import Pong.Game.Types.GamePhase;
import Pong.Game.Types.Side;
import Pong.Operator;
import com.sun.istack.internal.NotNull;
import javafx.animation.AnimationTimer;
import javafx.beans.property.*;

import java.util.Random;

public class Game {
    public static final int WIDTH = 1920;
    public static final int HEIGHT = 1080;
    public static final long START_DELAY = 3000;

    private Operator operator;
    private Random random;
    private AnimationTimer timer;

    private boolean local;
    private Side side;

    private StringProperty nicknameLeft;
    private StringProperty nicknameRight;

    private IntegerProperty scoreLeft;
    private IntegerProperty scoreRight;

    private Player playerLeft;
    private Player playerRight;
    private Ball ball;

    private Side serviceSide;

    private ObjectProperty<GamePhase> phase;

    private Game(@NotNull Operator operator) {
        this.operator = operator;
        this.serviceSide = Side.LEFT;
        this.phase = new SimpleObjectProperty<>(GamePhase.WAITING);

        long now = getTime();

        playerLeft = new Player(now);
        playerRight = new Player(now);
        ball = new Ball(now);
        random = new Random(now);

        scoreLeft = new SimpleIntegerProperty(0);
        scoreRight = new SimpleIntegerProperty(0);

        timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                gameLoop();
            }
        };
        timer.start();
    }

    public Game(@NotNull Operator operator, String nicknameLeft, String nicknameRight) {
        this(operator);
        this.nicknameLeft = new SimpleStringProperty(nicknameLeft);
        this.nicknameRight = new SimpleStringProperty(nicknameRight);
        this.local = true;
        this.side = Side.LEFT;
    }

    public Game(@NotNull Operator operator, String nickname, String nicknameOpponent, @NotNull Side side) {
        this(operator);
        this.side = side;
        this.local = false;

        if (side == Side.LEFT) {
            this.nicknameLeft = new SimpleStringProperty(nickname);
            this.nicknameRight = new SimpleStringProperty(nicknameOpponent);
        }
        else {
            this.nicknameLeft = new SimpleStringProperty(nicknameOpponent);
            this.nicknameRight = new SimpleStringProperty(nickname);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        this.timer.stop();
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
        long now = operator.getTime();
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
                    scoreRight.set(scoreRight.get() + 1);
                    break;
                case RIGHT:
                    serviceSide = Side.RIGHT;
                    scoreLeft.set(scoreLeft.get() + 1);
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

    public void setPlayerState(PlayerState state, Side side) {
        switch (side) {
            case LEFT:
                this.playerLeft.setState(state);
                break;
            case RIGHT:
                this.playerRight.setState(state);
                break;
        }
    }

    public void setOpponentNickname(String opponent) {
        switch (side) {
            case LEFT:
                setNicknameRight(opponent);
                break;
            case RIGHT:
                setNicknameLeft(opponent);
                break;
        }
    }

    public long getTime() {
        return operator.getTime();
    }

    public boolean isLocal() {
        return local;
    }

    public Side getSide() {
        return side;
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

    public StringProperty nicknameLeftProperty() {
        return nicknameLeft;
    }

    public void setNicknameLeft(String nicknameLeft) {
        this.nicknameLeft.set(nicknameLeft);
    }

    public StringProperty nicknameRightProperty() {
        return nicknameRight;
    }

    public void setNicknameRight(String nicknameRight) {
        this.nicknameRight.set(nicknameRight);
    }

    public String getNicknameLeft() {
        return nicknameLeft.get();
    }

    public String getNicknameRight() {
        return nicknameRight.get();
    }

    public void setScoreLeft(int scoreLeft) {
        this.scoreLeft.set(scoreLeft);
    }

    public void setScoreRight(int scoreRight) {
        this.scoreRight.set(scoreRight);
    }

    public void setPhase(GamePhase phase) {
        this.phase.set(phase);
    }

    public Operator getOperator() {
        return operator;
    }
}
