package Pong.Game;

import Pong.Game.Types.Direction;
import javafx.beans.property.*;

/**
 * The class Player represents a game player with a given state in time.
 * The player position in game based on the last known state can be updated via the update() method.
 */
public class Player {

    private ObjectProperty<PlayerState> state;
    private DoubleProperty y;
    private BooleanProperty changedFromServer;

    /**
     * Instantiates a new Player with the default state.
     */
    public Player() {
        this.state = new SimpleObjectProperty<>(new PlayerState());
        this.y = new SimpleDoubleProperty(0);
        this.changedFromServer = new SimpleBooleanProperty(false);
    }

    /**
     * Update the players x property based on the players last known state.
     *
     * @param timestamp the timestamp
     */
    public void update(long timestamp) {
        y.set(getState().getPositionAt(timestamp));
    }

    /**
     * Change direction of the player.
     * Updates the player state.
     *
     * @param timestamp the timestamp
     * @param direction the direction
     */
    public void changeDirection(long timestamp, Direction direction) {
        setState(new PlayerState(timestamp, (int) state.get().getPositionAt(timestamp), direction), false);
    }

    /**
     * Gets state.
     *
     * @return the state
     */
    public PlayerState getState() {
        return state.get();
    }

    /**
     * Sets state of the player.
     *
     * @param state the state
     * @param causedByServer true, if the change was initiated from the server
     */
    public void setState(PlayerState state, boolean causedByServer) {
        this.changedFromServer.set(causedByServer);
        this.state.set(state);
    }

    /**
     * State property.
     *
     * @return the object property
     */
    public ObjectProperty<PlayerState> stateProperty() {
        return state;
    }

    /**
     * Gets y.
     *
     * @return the y
     */
    public double getY() {
        return y.get();
    }

    /**
     * Y property.
     *
     * @return the double property
     */
    public DoubleProperty yProperty() {
        return y;
    }

    /**
     * Check whether the last state change was caused by the server.
     *
     * @return true if the player state was changed from the server, false if locally
     */
    public boolean isChangedFromServer() {
        return changedFromServer.get();
    }

    /**
     * Changed from server property boolean property.
     *
     * @return the boolean property
     */
    public BooleanProperty changedFromServerProperty() {
        return changedFromServer;
    }
}