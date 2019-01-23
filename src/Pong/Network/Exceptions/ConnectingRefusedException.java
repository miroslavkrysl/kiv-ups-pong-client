package Pong.Network.Exceptions;

public class ConnectingRefusedException extends ConnectionException {
    public ConnectingRefusedException() {
        super();
    }

    public ConnectingRefusedException(String s) {
        super(s);
    }

    public ConnectingRefusedException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ConnectingRefusedException(Throwable throwable) {
        super(throwable);
    }

    protected ConnectingRefusedException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
