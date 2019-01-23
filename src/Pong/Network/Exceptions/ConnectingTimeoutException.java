package Pong.Network.Exceptions;

public class ConnectingTimeoutException extends ConnectionException {
    public ConnectingTimeoutException() {
        super();
    }

    public ConnectingTimeoutException(String s) {
        super(s);
    }

    public ConnectingTimeoutException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ConnectingTimeoutException(Throwable throwable) {
        super(throwable);
    }

    protected ConnectingTimeoutException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
