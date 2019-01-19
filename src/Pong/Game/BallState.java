package Pong.Game;

import Pong.Game.Exceptions.GameTypeException;
import Pong.Game.Exceptions.PlayerStateException;
import Pong.Game.Types.Side;

public class BallState {
    public static final int RADIUS = 25;
    public static final int POSITION_MAX = (Game.HEIGHT / 2) - RADIUS;
    public static final int POSITION_MIN = -(Game.HEIGHT / 2) + RADIUS;
    public static final int ANGLE_MAX = 60;
    public static final int ANGLE_MIN = -60;
    public static final int SPEED_MAX = 1920;
    public static final int SPEED_MIN = 640;
    public static final Side INIT_SIDE = Side.LEFT;
    public static final int INIT_POSITION = 0;
    public static final int INIT_ANGLE = 0;
    public static final int INIT_SPEED = 0;

    private long timestamp;
    private Side side;
    private int position;
    private int angle;
    private int speed;

    public BallState(long timestamp, Side side, int position, int angle, int speed) {
        this.timestamp = timestamp;
        this.side = side;
        this.position = position;
        this.angle = angle;
        this.speed = speed;
    }

    public BallState(long timestamp) {
        this(timestamp, INIT_SIDE, INIT_POSITION, INIT_ANGLE, INIT_SPEED);
    }

    public BallState(String[] items) throws GameTypeException, PlayerStateException {
        if (items.length != 5) {
            throw new PlayerStateException("ball must have 5 items");
        }

        try {
            this.timestamp = Long.parseLong(items[0]);
        }
        catch (Exception exception) {
            throw new PlayerStateException("timestamp is in invalid format");
        }

        try {
            this.side = Side.fromString(items[1]);
        }
        catch (Exception exception) {
            throw new PlayerStateException("side is in invalid format");
        }

        try {
            this.position = Integer.parseInt(items[2]);
        }
        catch (Exception exception) {
            throw new PlayerStateException("position is in invalid format");
        }

        try {
            this.angle = Integer.parseInt(items[3]);
        }
        catch (Exception exception) {
            throw new PlayerStateException("angle is in invalid format");
        }

        try {
            this.speed = Integer.parseUnsignedInt(items[4]);
        }
        catch (Exception exception) {
            throw new PlayerStateException("speed is in invalid format");
        }

        if (position > POSITION_MAX || position < POSITION_MIN) {
            throw new PlayerStateException("position is out of game field");
        }
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Side getSide() {
        return side;
    }

    public int getPosition() {
        return position;
    }

    public int getAngle() {
        return angle;
    }

    public int getSpeed() {
        return speed;
    }
}
