package Pong.Game;

import Pong.Game.Exceptions.PlayerStateException;
import Pong.Game.Types.Direction;

/**
 * The type Player state.
 */
public class PlayerState {

    /**
     * The player movement speed.
     */
    public static final int SPEED = 400;

    /**
     * The player height.
     */
    public static final int HEIGHT = 320;

    /**
     * The player width.
     */
    public static final int WIDTH = 50;

    /**
     * The player max position.
     */
    public static final int POSITION_MAX = (Game.HEIGHT / 2) - (HEIGHT / 2);

    /**
     * The player min position..
     */
    public static final int POSITION_MIN = -(Game.HEIGHT / 2) + (HEIGHT / 2);

    /**
     * The default players timestamp.
     */
    public static final long DEFAULT_TIMESTAMP = 0;

    /**
     * The default players position.
     */
    public static final int DEFAULT_POSITION = 0;

    /**
     * The default players direction.
     */
    public static final Direction DEFAULT_DIRECTION = Direction.STOP;

    /**
     * The number of items required to be present in packet.
     */
    public static final int ITEMS_COUNT = 3;

    private final long timestamp;
    private final int position;
    private final Direction direction;

    /**
     * Instantiates a new Player state.
     *
     * @param timestamp the timestamp
     * @param position  the position
     * @param direction the direction
     */
    public PlayerState(long timestamp, int position, Direction direction) {
        this.timestamp = timestamp;
        this.position = position;
        this.direction = direction;
    }

    /**
     * Instantiates a new Player state with the default parameters.
     */
    public PlayerState() {
        this(DEFAULT_TIMESTAMP, DEFAULT_POSITION, DEFAULT_DIRECTION);
    }

    /**
     * Instantiates a new Player state with the default parameters from the array of strings in the following order:
     * timestamp, position, direction.
     *
     * @param items the items
     * @throws PlayerStateException is thrown, when there are wrong number of parameters in array or one of the parameters is in bad format and parsing fails
     */
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

    /**
     * Gets the player x coordinate at the given time point
     * relative to this state.
     *
     * @param when the time point
     * @return the x coordinate
     */
    public double getPositionAt(long when) {
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

    /**
     * Extract player parameters into the string array in the following order:
     * timestamp, position, direction.
     *
     * @return the string array with parameters
     */
    public String[] itemize() {
        return new String[] {Long.toString(timestamp), Integer.toString(position), direction.toString()};
    }

    /**
     * Gets timestamp.
     *
     * @return the timestamp
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Gets position.
     *
     * @return the position
     */
    public int getPosition() {
        return position;
    }

    /**
     * Gets direction.
     *
     * @return the direction
     */
    public Direction getDirection() {
        return direction;
    }
}
