package Pong.Game.Exceptions;

public class GameException extends Exception {
    public GameException() {
        super();
    }

    public GameException(String s) {
        super(s);
    }

    public GameException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public GameException(Throwable throwable) {
        super(throwable);
    }

    protected GameException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
