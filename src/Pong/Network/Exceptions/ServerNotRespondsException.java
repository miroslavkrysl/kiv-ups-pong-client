package Pong.Network.Exceptions;

public class ServerNotRespondsException extends ConnectionException {
    public ServerNotRespondsException() {
        super();
    }

    public ServerNotRespondsException(String s) {
        super(s);
    }

    public ServerNotRespondsException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ServerNotRespondsException(Throwable throwable) {
        super(throwable);
    }

    protected ServerNotRespondsException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
