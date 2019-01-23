package Pong.Game.Types;

import Pong.Game.Exceptions.GameTypeException;

/**
 * The enum class of player Directions.
 */
public enum Direction {
    /**
     * Player is moving up.
     */
    UP("up", 1),
    /**
     * Player is not moving.
     */
    STOP("stop", 0),
    /**
     * Player is moving down.
     */
    DOWN("down", -1);

    /**
     * The String representation of the enum.
     */
    String string;
    /**
     * The integer value of the enum.
     */
    int value;

    Direction(String string, int value) {
        this.string = string;
        this.value = value;
    }

    /**
     * Create Direction from string.
     *
     * @param string the string
     * @return the direction
     * @throws GameTypeException is thrown, if the string is not a direction, or is in bad format
     */
    static public Direction fromString(String string) throws GameTypeException {
        for (Direction direction : Direction.values()) {
            if (direction.string.equals(string)) {
                return direction;
            }
        }

        throw new GameTypeException("direction is in invalid format");
    }

    @Override
    public String toString() {
        return string;
    }

    /**
     * Gets integer value.
     *
     * @return the value
     */
    public int getValue() {
        return value;
    }
}
