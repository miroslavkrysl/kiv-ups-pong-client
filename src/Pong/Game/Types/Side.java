package Pong.Game.Types;

import Pong.Game.Exceptions.GameTypeException;

/**
 * The enum Side represents one of the game field sides.
 */
public enum Side {
    /**
     * Left side.
     */
    LEFT("left"),
    /**
     * Right side.
     */
    RIGHT("right");

    /**
     * The String representation of the side.
     */
    String string;

    Side(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return string;
    }

    /**
     * Get side from string.
     *
     * @param string the string
     * @return the side
     * @throws GameTypeException is thrown, if the string is not in the side representation format
     */
    static public Side fromString(String string) throws GameTypeException {
        for (Side side : Side.values()) {
            if (side.string.equals(string)) {
                return side;
            }
        }

        throw new GameTypeException("side is in invalid format");
    }

    /**
     * Gets the other side of the field.
     *
     * @param side the side
     * @return the other side
     */
    static public Side getOther(Side side) {
        switch (side) {
            case LEFT:
                return RIGHT;
            case RIGHT:
                return LEFT;
        }

        return null;
    }
}
