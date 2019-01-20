package Pong.Game.Exceptions;

public class BallStateException extends Exception {
    public BallStateException() {
        super();
    }

    public BallStateException(String s) {
        super(s);
    }

    public BallStateException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public BallStateException(Throwable throwable) {
        super(throwable);
    }

    protected BallStateException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
