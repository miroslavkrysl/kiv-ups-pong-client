package Pong.Game.Types;

import Pong.Game.Exceptions.GameTypeException;

public enum Direction {
    UP("up", 1),
    STOP("stop", 0),
    DOWN("down", -1);

    String string;
    int value;

    Direction(String string, int value) {
        this.string = string;
        this.value = value;
    }

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

    public int getValue() {
        return value;
    }
}
