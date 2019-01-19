package Pong.Game.Types;

import Pong.Game.Exceptions.GameTypeException;

public enum Side {
    LEFT("left"),
    RIGHT("right");

    String string;

    Side(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return string;
    }

    static public Side fromString(String string) throws GameTypeException {
        for (Side side : Side.values()) {
            if (side.string.equals(string)) {
                return side;
            }
        }

        throw new GameTypeException("side is in invalid format");
    }
}
