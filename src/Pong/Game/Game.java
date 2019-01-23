package Pong.Game;

import Pong.App;
import Pong.Game.Types.Side;
import javafx.beans.property.*;

/**
 * The class Game represent a game in a game phase, with two players, a ball and a score of the two players.
 */
public abstract class Game {

    /**
     * The enum class of game phases.
     */
    public enum Phase {
        /**
         * Players waiting for each other to be ready.
         */
        WAITING,
        /**
         * Ball was released from the center.
         */
        START,
        /**
         * Ball is after the first hit, moving between the players ang game is in progress.
         */
        PLAYING,
    }

    /**
     * The width of the game field.
     */
    public static final int WIDTH = 1920;

    /**
     * The height of the game field.
     */
    public static final int HEIGHT = 1080;

    protected App app;

    private StringProperty nicknameLeft;
    private StringProperty nicknameRight;

    private IntegerProperty scoreLeft;
    private IntegerProperty scoreRight;

    private ObjectProperty<Phase> phase;

    private Player playerLeft;
    private Player playerRight;
    private Ball ball;

    /**
     * Instantiates a new Game.
     *
     * @param app           the app instance
     * @param nicknameLeft  the left players nickname
     * @param nicknameRight the right players nickname
     */
    public Game(App app, String nicknameLeft, String nicknameRight) {
        this.app = app;

        this.phase = new SimpleObjectProperty<>(Phase.WAITING);
        this.scoreLeft = new SimpleIntegerProperty(0);
        this.scoreRight = new SimpleIntegerProperty(0);
        this.nicknameLeft = new SimpleStringProperty(nicknameLeft);
        this.nicknameRight = new SimpleStringProperty(nicknameRight);

        this.playerLeft = new Player();
        this.playerRight = new Player();
        this.ball = new Ball();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    /**
     * Sets ball position to center with no speed and set the game phase to WAITING.
     */
    public void newRound() {
        phase.set(Phase.WAITING);
        ball.setState(new BallState());
    }

    /**
     * Sets game phase to START.
     */
    public void startRound() {
        phase.set(Phase.START);
    }

    /**
     * A game loop, which is called by the timer.
     *
     * @param timestamp the current time
     * @param phase     the current game phase
     */
    protected void gameLoop(long timestamp, Phase phase) {
        playerLeft.update(timestamp);
        playerRight.update(timestamp);
        ball.update(timestamp, phase);
    }

    /**
     * Gets the current game time.
     *
     * @return the time
     */
    public abstract long getTime();

    /**
     * Gets the player from the given side.
     *
     * @param side the side
     * @return the player
     */
    public Player getPlayer(Side side) {
        if (side == Side.LEFT) {
            return playerLeft;
        } else if (side == Side.RIGHT) {
            return playerRight;
        }

        return null;
    }

    /**
     * Gets the ball.
     *
     * @return the ball
     */
    public Ball getBall() {
        return ball;
    }

    /**
     * Score property of the given side.
     *
     * @param side the side
     * @return the integer property
     */
    public IntegerProperty scoreProperty(Side side) {
        if (side == Side.LEFT) {
            return scoreLeft;
        } else if (side == Side.RIGHT) {
            return scoreRight;
        }

        return null;
    }

    /**
     * Gets the score of the given side.
     *
     * @param side the side
     * @return the score
     */
    public int getScore(Side side) {
        return scoreProperty(side).get();
    }

    /**
     * Sets the score of the given side.
     *
     * @param side  the side
     * @param score the score
     */
    public void setScore(Side side, int score) {
        scoreProperty(side).set(score);
    }

    /**
     * Nickname property of the given side.
     *
     * @param side the side
     * @return the string property
     */
    public StringProperty nicknameProperty(Side side) {
        if (side == Side.LEFT) {
            return nicknameLeft;
        } else if (side == Side.RIGHT) {
            return nicknameRight;
        }

        return null;
    }

    /**
     * Gets the nickname of the given side.
     *
     * @param side the side
     * @return the nickname
     */
    public String getNickname(Side side) {
        return nicknameProperty(side).get();
    }

    /**
     * Sets the nickname of the given side.
     *
     * @param side     the side
     * @param nickname the nickname
     */
    public void setNickname(Side side, String nickname) {
        nicknameProperty(side).set(nickname);
    }

    /**
     * Gets the game phase.
     *
     * @return the phase
     */
    public Phase getPhase() {
        return phase.get();
    }

    /**
     * Sets the phase of the game.
     *
     * @param phase the phase
     */
    protected void setPhase(Phase phase) {
        this.phase.set(phase);
    }
}
