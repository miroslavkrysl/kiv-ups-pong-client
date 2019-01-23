package Pong.Network.Exceptions;

public class AlreadyConnectedException extends ConnectionException {
    public AlreadyConnectedException() {
        super();
    }

    public AlreadyConnectedException(String s) {
        super(s);
    }

    public AlreadyConnectedException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public AlreadyConnectedException(Throwable throwable) {
        super(throwable);
    }

    protected AlreadyConnectedException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
