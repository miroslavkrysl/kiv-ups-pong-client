package Pong.Game;

import Pong.Game.Exceptions.GameTypeException;
import Pong.Game.Exceptions.PlayerStateException;
import Pong.Game.Types.Direction;

public class PlayerState {
    public static final int SPEED = 400;
    public static final int HEIGHT = 320;
    public static final int WIDTH = 50;
    public static final int INIT_POSITION = 0;
    public static final Direction INIT_DIRECTION = Direction.STOP;
    public static final int POSITION_MAX = (Game.HEIGHT / 2) - (HEIGHT / 2);
    public static final int POSITION_MIN = -(Game.HEIGHT / 2) + (HEIGHT / 2);

    private long timestamp;
    private int position;
    private Direction direction;

    public PlayerState(long timestamp, int position, Direction direction) {
        this.timestamp = timestamp;
        this.position = position;
        this.direction = direction;
    }

    public PlayerState(long timestamp) {
        this(timestamp, INIT_POSITION, INIT_DIRECTION);
    }

    public PlayerState(String[] items) throws GameTypeException, PlayerStateException {
        if (items.length != 3) {
            throw new PlayerStateException("player must have 3 items");
        }

        try {
            this.timestamp = Long.parseLong(items[0]);
        }
        catch (Exception exception) {
            throw new PlayerStateException("timestamp is in invalid format");
        }

        try {
            this.position = Integer.parseInt(items[1]);
        }
        catch (Exception exception) {
            throw new PlayerStateException("position is in invalid format");
        }

        try {
            this.direction = Direction.fromString(items[2]);
        }
        catch (Exception exception) {
            throw new PlayerStateException("direction is in invalid format");
        }

        if (position > POSITION_MAX || position < POSITION_MIN) {
            throw new PlayerStateException("position is out of game field");
        }
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getPosition() {
        return position;
    }

    public Direction getDirection() {
        return direction;
    }
}
