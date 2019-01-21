package Pong.Game;

import Pong.Game.Exceptions.PlayerStateException;
import Pong.Game.Types.Direction;
import Pong.Game.Types.Side;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

public class PlayerState {
    public static final int SPEED = 400;
    public static final int HEIGHT = 320;
    public static final int WIDTH = 50;
    public static final int POSITION_MAX = (Game.HEIGHT / 2) - (HEIGHT / 2);
    public static final int POSITION_MIN = -(Game.HEIGHT / 2) + (HEIGHT / 2);
    public static final int POSITION_INIT = 0;
    public static final Direction DIRECTION_INIT = Direction.STOP;

    private final long timestamp;
    private final int position;
    private final Direction direction;

    public PlayerState(long timestamp, int position, Direction direction) {
        this.timestamp = timestamp;
        this.position = position;
        this.direction = direction;
    }

    public PlayerState(long timestamp) {
        this(timestamp, POSITION_INIT, DIRECTION_INIT);
    }

    public PlayerState(String[] items) throws PlayerStateException {
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
    }

    public int getPositionAt(long when) {
        double p = (when - timestamp)
                * (SPEED / 1000.0)
                * direction.getValue();

        int x = (int) p + position;

        if (x > POSITION_MAX) {
            return POSITION_MAX;
        }
        else if (x < POSITION_MIN) {
            return POSITION_MIN;
        }

        return x;
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
