package Pong.Game;

import Pong.Game.Types.GamePhase;
import javafx.beans.property.*;

public class Ball {
    private ObjectProperty<BallState> state;

    private IntegerProperty x;
    private IntegerProperty y;

    public Ball(long timestamp) {
        this.state = new SimpleObjectProperty<>(new BallState(timestamp));
        this.x = new SimpleIntegerProperty(0);
        this.y = new SimpleIntegerProperty(0);
    }

    public void update(long timestamp, GamePhase phase) {
        int[] pos = getState().getPositionAt(timestamp, phase);
        x.set(pos[0]);
        y.set(pos[1]);
    }

    public BallState getState() {
        return state.get();
    }

    public ObjectProperty<BallState> stateProperty() {
        return state;
    }

    public void setState(BallState state) {
        this.state.set(state);
    }

    public int getX() {
        return x.get();
    }

    public IntegerProperty xProperty() {
        return x;
    }

    public int getY() {
        return y.get();
    }

    public IntegerProperty yProperty() {
        return y;
    }
}
