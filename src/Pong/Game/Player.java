package Pong.Game;

import Pong.Game.Types.Direction;
import javafx.beans.property.*;

public class Player {

    private ObjectProperty<PlayerState> state;
    private BooleanProperty changedFromServer;

    private IntegerProperty y;

    public Player(long timestamp) {
        this.changedFromServer = new SimpleBooleanProperty(false);
        this.state = new SimpleObjectProperty<>(new PlayerState(timestamp));
        this.y = new SimpleIntegerProperty(0);
    }

    public void update(long timestamp) {
        y.set(getState().getPositionAt(timestamp));
    }

    public void changeDirection(long timestamp, Direction direction) {
        state.set(new PlayerState(timestamp, state.get().getPositionAt(timestamp), direction));
    }

    public PlayerState getState() {
        return state.get();
    }

    public void setState(PlayerState state) {
        this.state.set(state);
    }

    public ObjectProperty<PlayerState> stateProperty() {
        return state;
    }

    public int getY() {
        return y.get();
    }

    public IntegerProperty yProperty() {
        return y;
    }

    public boolean isChangedFromServer() {
        return changedFromServer.get();
    }

    public BooleanProperty changedFromServerProperty() {
        return changedFromServer;
    }

    public void setChangedFromServer(boolean changedFromServer) {
        this.changedFromServer.set(changedFromServer);
    }
}