package Pong.Game;

import Pong.Game.Exceptions.BallStateException;
import Pong.Game.Types.Side;
import javafx.geometry.Point2D;

/**
 * The class BallState represent a bal state in a given time point,
 * at the one of the field sides, with the movement direction (angle) and speed.
 */
public class BallState {

    /**
     * The radius of the ball.
     */
    public static final int RADIUS = 25;

    /**
     * The height range where the ball can move (relative to the ball center).
     */
    public static final int MOVEMENT_HEIGHT = Game.HEIGHT - 2 * RADIUS;

    /**
     * The width range where the ball can move (relative to the ball center).
     */
    public static final int MOVEMENT_WIDTH = Game.WIDTH - 2 * RADIUS;

    /**
     * The max angle of the ball hit.
     */
    public static final int ANGLE_MAX = 60;

    /**
     * The min angle of the ball hit.
     */
    public static final int ANGLE_MIN = -60;

    /**
     * The max ball speed.
     */
    public static final int SPEED_MAX = 2000;

    /**
     * The min ball speed.
     */
    public static final int SPEED_MIN = 600;

    /**
     * The default timestamp.
     */
    public static final long DEFAULT_TIMESTAMP = 0;

    /**
     * The default side.
     */
    public static final Side DEFAULT_SIDE = Side.LEFT;

    /**
     * The default position.
     */
    public static final int DEFAULT_POSITION = 0;

    /**
     * The default angle.
     */
    public static final int DEFAULT_ANGLE = 0;

    /**
     * The default speed.
     */
    public static final int DEFAULT_SPEED = 0;


    /**
     * The number of required item to be present in the packet.
     */
    public static final int ITEMS_COUNT = 5;

    private final long timestamp;
    private final Side side;
    private final int position;
    private final int angle;
    private final int speed;

    /**
     * Instantiates a new BallState.
     *
     * @param timestamp the timestamp
     * @param side      the side
     * @param position  the position
     * @param angle     the angle
     * @param speed     the speed
     */
    public BallState(long timestamp, Side side, int position, int angle, int speed) {
        this.timestamp = timestamp;
        this.side = side;
        this.position = position;
        this.angle = angle;
        this.speed = speed;
    }

    /**
     * Instantiates a new BallState with default values.
     */
    public BallState() {
        this(DEFAULT_TIMESTAMP, DEFAULT_SIDE, DEFAULT_POSITION, DEFAULT_ANGLE, DEFAULT_SPEED);
    }

    /**
     * Instantiates a new BallState from the array of strings in the following order:
     * timestamp, side, position, angle, speed.
     *
     * @param items array containing all ball parameters
     * @throws BallStateException is thrown, when there are wrong number of parameters in array
     * or one of the parameters is in bad format and parsing fails
     */
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

    /**
     * Gets the ball x and y coordinates at the given time point
     * relative to this state and to the phase of the game.
     *
     * @param when  time point in milliseconds
     * @param phase the phase of the game
     * @return the coordinates of the ball
     */
    public Point2D getPositionAt(long when, Game.Phase phase) {
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

        if (phase == Game.Phase.START || phase == Game.Phase.WAITING) {
            if (timestamp >= when) {
                return new Point2D(0, position);
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

        return new Point2D(x, y);
    }

    /**
     * Gets the timestamp.
     *
     * @return the timestamp
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Gets the side.
     *
     * @return the side
     */
    public Side getSide() {
        return side;
    }

    /**
     * Gets the position.
     *
     * @return the position
     */
    public int getPosition() {
        return position;
    }

    /**
     * Gets the angle.
     *
     * @return the angle
     */
    public int getAngle() {
        return angle;
    }

    /**
     * Gets the speed.
     *
     * @return the speed
     */
    public int getSpeed() {
        return speed;
    }
}
