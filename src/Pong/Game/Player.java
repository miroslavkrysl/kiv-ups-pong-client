package Pong.Game;

import Pong.Game.Exceptions.GameTypeException;
import Pong.Game.Exceptions.PlayerStateException;
import Pong.Game.Types.Direction;
import javafx.beans.property.*;

public class Player {
    public static final int SPEED = 400;
    public static final int HEIGHT = 320;
    public static final int WIDTH = 50;
    public static final int INIT_POSITION = 0;
    public static final Direction INIT_DIRECTION = Direction.STOP;
    public static final int POSITION_MAX = (Game.HEIGHT / 2) - (HEIGHT / 2);
    public static final int POSITION_MIN = -(Game.HEIGHT / 2) + (HEIGHT / 2);

    private LongProperty timestamp;
    private IntegerProperty position;
    private ObjectProperty<Direction> direction;

    public Player(long timestamp, int position, Direction direction) {
        this.timestamp = new SimpleLongProperty(timestamp);
        this.position = new SimpleIntegerProperty(position);
        this.direction = new SimpleObjectProperty<>(direction);
    }

    public Player(long timestamp) {
        this(timestamp, INIT_POSITION, INIT_DIRECTION);
    }

    public Player(String[] items) throws GameTypeException, PlayerStateException {
        if (items.length != 3) {
            throw new PlayerStateException("player must have 3 items");
        }

        try {
            setTimestamp(Long.parseLong(items[0]));
        }
        catch (Exception exception) {
            throw new PlayerStateException("timestamp is in invalid format");
        }

        try {
            setPosition(Integer.parseInt(items[1]));
        }
        catch (Exception exception) {
            throw new PlayerStateException("position is in invalid format");
        }

        try {
            setDirection(Direction.fromString(items[2]));
        }
        catch (Exception exception) {
            throw new PlayerStateException("direction is in invalid format");
        }
    }

    public void changeState(long timestamp, int position, Direction direction) {
        setTimestamp(timestamp);
        setPosition(position);
        setDirection(direction);
    }

    public int getPosition(long when) {
        double p = (when - getTimestamp())
                * (Player.SPEED / 1000.0)
                * getDirection().getValue();

        int position = (int) p + getPosition();

        if (position > POSITION_MAX) {
            return POSITION_MAX;
        }
        else if (position < POSITION_MIN) {
            return POSITION_MIN;
        }

        return position;
    }

    public long getTimestamp() {
        return timestamp.get();
    }

    public LongProperty timestampProperty() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp.set(timestamp);
    }

    public int getPosition() {
        return position.get();
    }

    public IntegerProperty positionProperty() {
        return position;
    }

    public void setPosition(int position) {
        this.position.set(position);
    }

    public Direction getDirection() {
        return direction.get();
    }

    public ObjectProperty<Direction> directionProperty() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction.set(direction);
    }
}
