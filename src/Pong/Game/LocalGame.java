package Pong.Game;

import Pong.App;
import Pong.Game.Types.Side;
import javafx.beans.property.IntegerProperty;

import java.util.Random;

/**
 * The type Local game.
 */
public class LocalGame extends Game{

    private Random random;
    private Side serviceSide;

    /**
     * Instantiates a new LocalGame.
     *
     * @param app           the app instance
     * @param nicknameLeft  the left players nickname
     * @param nicknameRight the right players nickname
     */
    public LocalGame(App app, String nicknameLeft, String nicknameRight) {
        super(app, nicknameLeft, nicknameRight);
        this.serviceSide = Side.LEFT;
        this.random = new Random(getTime());
    }

    @Override
    public synchronized void startRound() {
        super.startRound();
        getBall().setState(new BallState(getTime(), serviceSide, 0, 0, BallState.SPEED_MIN));
        System.out.println("hello");
    }

    @Override
    protected void gameLoop(long timestamp, Phase phase) {
        super.gameLoop(timestamp, phase);
        checkCollisions(timestamp, phase);
    }

    @Override
    public long getTime() {
        return System.currentTimeMillis();
    }

    private void checkCollisions(long now, Game.Phase phase) {
        if (getPhase() != Game.Phase.PLAYING && getPhase() != Game.Phase.START) {
            return;
        }

        Ball ball = getBall();
        double ballX = ball.getX();
        Side side;

        // check whether the ball is over one of the sides
        if (ballX <= -BallState.MOVEMENT_WIDTH / 2) {
            side = Side.LEFT;
        }
        else if (ballX >= BallState.MOVEMENT_WIDTH / 2) {
            side = Side.RIGHT;
        }
        else {
            return;
        }

        if (ball.getState().getSide() == side) {
            // collision already processed once
            return;
        }

        double playerY = getPlayer(side).getY();
        double ballY = ball.getY();

        // check if the player missed the ball
        if (ballY > playerY + (PlayerState.HEIGHT / 2) || ballY < playerY - (PlayerState.HEIGHT / 2)) {
            serviceSide = side;
            IntegerProperty score = scoreProperty(Side.getOther(side));
            score.set(score.get() + 1);

            newRound();
            return;
        }

        // generate new random ball state
        int angle = random.nextInt(BallState.ANGLE_MAX - BallState.ANGLE_MIN) + BallState.ANGLE_MIN;
        int speed = random.nextInt(BallState.SPEED_MAX - BallState.SPEED_MIN) + BallState.SPEED_MIN;

        ball.setState(new BallState(now, side, (int) ballY, angle, speed));

        setPhase(Game.Phase.PLAYING);
    }
}
