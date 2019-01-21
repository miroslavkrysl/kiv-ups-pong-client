package Pong.Game.Exceptions;

public class GameTypeException extends GameException {
    public GameTypeException() {
        super();
    }

    public GameTypeException(String s) {
        super(s);
    }

    public GameTypeException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public GameTypeException(Throwable throwable) {
        super(throwable);
    }

    protected GameTypeException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
