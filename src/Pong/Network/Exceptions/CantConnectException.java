package Pong.Network.Exceptions;

public class CantConnectException extends ConnectionException {
    public CantConnectException() {
        super();
    }

    public CantConnectException(String s) {
        super(s);
    }

    public CantConnectException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public CantConnectException(Throwable throwable) {
        super(throwable);
    }

    protected CantConnectException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
