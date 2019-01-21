package Pong.Game;

import Pong.Game.Exceptions.BallStateException;
import Pong.Game.Exceptions.GameTypeException;
import Pong.Game.Types.GamePhase;
import Pong.Game.Types.Side;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

public class BallState {
    public static final int RADIUS = 25;
    public static final int MOVEMENT_HEIGHT = Game.HEIGHT - 2 * RADIUS;
    public static final int MOVEMENT_WIDTH = Game.WIDTH - 2 * RADIUS;
    public static final int ANGLE_MAX = 60;
    public static final int ANGLE_MIN = -60;
    public static final int SPEED_MAX = 2000;
    public static final int SPEED_MIN = 600;
    public static final Side SIDE_INIT = Side.LEFT;
    public static final int POSITION_INIT = 0;
    public static final int ANGLE_INIT = 0;
    public static final int SPEED_INIT = 0;

    private final long timestamp;
    private final Side side;
    private final int position;
    private final int angle;
    private final int speed;

    public BallState(long timestamp, Side side, int position, int angle, int speed) {
        this.timestamp = timestamp;
        this.side = side;
        this.position = position;
        this.angle = angle;
        this.speed = speed;
    }

    public BallState(long timestamp) {
        this(timestamp, SIDE_INIT, POSITION_INIT, ANGLE_INIT, SPEED_INIT);
    }

    public BallState(String[] items) throws BallStateException {
        if (items.length != 5) {
            throw new BallStateException("ball must have 5 items");
        }

        try {
            this.timestamp = Long.parseLong(items[0]);
        }
        catch (Exception exception) {
            throw new BallStateException("timestamp is in invalid format");
        }

        try {
            this.side = Side.fromString(items[1]);
        }
        catch (Exception exception) {
            throw new BallStateException("side is in invalid format");
        }

        try {
            this.position = Integer.parseInt(items[2]);
        }
        catch (Exception exception) {
            throw new BallStateException("position is in invalid format");
        }

        try {
            this.angle = Integer.parseInt(items[3]);
        }
        catch (Exception exception) {
            throw new BallStateException("angle is in invalid format");
        }

        try {
            this.speed = Integer.parseUnsignedInt(items[4]);
        }
        catch (Exception exception) {
            throw new BallStateException("speed is in invalid format");
        }
    }

    public int[] getPositionAt(long when, GamePhase phase) {
        double hypotenuse = (when - timestamp)
                * (speed / 1000.0);

        double halfWidth = MOVEMENT_WIDTH / 2.0;
        double halfHeight = MOVEMENT_HEIGHT / 2.0;
        double rad = Math.toRadians(angle);
        double legX = Math.cos(rad) * hypotenuse;
        double legY = Math.sin(rad) * hypotenuse;

        double x = getSide() == Side.LEFT
                ? legX - halfWidth
                : halfWidth - legX;

        if (phase == GamePhase.START || phase == GamePhase.WAITING) {
            if (timestamp >= when) {
                return new int[] {0, position};
            }
            x += getSide() == Side.LEFT
                    ? halfWidth
                    : - halfWidth;
        }

        double y = (rad < 0 ? -1 : 1 ) * (legY + position) + halfHeight;
        double mod = y % (halfHeight + halfHeight);
        int n = (int)(y / (MOVEMENT_HEIGHT)) + (rad < 0 ? 1 : 0);

        switch (n % 2) {
            case 0:
                y = mod;
                break;
            case 1:
                y = MOVEMENT_HEIGHT - mod;
                break;
        }

        y -= halfHeight;

        return new int[] {(int)x, (int)y};
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
