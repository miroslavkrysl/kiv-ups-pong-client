package Pong.Game.Exceptions;

public class PlayerStateException extends GameException {
    public PlayerStateException() {
        super();
    }

    public PlayerStateException(String s) {
        super(s);
    }

    public PlayerStateException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public PlayerStateException(Throwable throwable) {
        super(throwable);
    }

    protected PlayerStateException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
