package Pong.Game;

import javafx.beans.property.*;
import javafx.geometry.Point2D;

/**
 * The class Ball represents a game ball with a given state in time.
 * The ball position in game based on the last known state can be updated via the update() method.
 */
public class Ball {

    private ObjectProperty<BallState> state;
    private DoubleProperty x;
    private DoubleProperty y;

    /**
     * Instantiates a new Ball with the default stationary state.
     */
    public Ball() {
        this.state = new SimpleObjectProperty<>(new BallState());
        this.x = new SimpleDoubleProperty(0);
        this.y = new SimpleDoubleProperty(0);
    }

    /**
     * Updates the ball x and y properties based on the ball last known state.
     *
     * @param timestamp the timestamp
     * @param phase     the phase
     */
    public void update(long timestamp, Game.Phase phase) {
        Point2D pos = getState().getPositionAt(timestamp, phase);
        x.set(pos.getX());
        y.set(pos.getY());
    }

    /**
     * Gets state.
     *
     * @return the state
     */
    public BallState getState() {
        return state.get();
    }

    /**
     * State property object property.
     *
     * @return the object property
     */
    public ObjectProperty<BallState> stateProperty() {
        return state;
    }

    /**
     * Sets ball state in timepoint.
     *
     * @param state the state in timepoint
     */
    public void setState(BallState state) {
        this.state.set(state);
    }

    /**
     * Gets the x coordinate of the ball.
     *
     * @return the x coordinate of the ball
     */
    public double getX() {
        return x.get();
    }

    /**
     * X property integer property.
     *
     * @return the x coordinate property of the ball
     */
    public DoubleProperty xProperty() {
        return x;
    }

    /**
     * Gets the y coordinate of the ball.
     *
     * @return the y coordinate of the ball
     */
    public double getY() {
        return y.get();
    }

    /**
     * Y property integer property.
     *
     * @return the y coordinate property of the ball
     */
    public DoubleProperty yProperty() {
        return y;
    }
}
