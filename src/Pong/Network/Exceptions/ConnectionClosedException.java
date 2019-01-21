package Pong.Network.Exceptions;

public class ConnectionClosedException extends ConnectionException {
    public ConnectionClosedException() {
        super();
    }

    public ConnectionClosedException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ConnectionClosedException(Throwable throwable) {
        super(throwable);
    }

    protected ConnectionClosedException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }

    public ConnectionClosedException(String s) {
    }
}
