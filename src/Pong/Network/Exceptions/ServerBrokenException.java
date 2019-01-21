package Pong.Network.Exceptions;

public class ServerBrokenException extends ConnectionException {
    public ServerBrokenException() {
        super();
    }

    public ServerBrokenException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ServerBrokenException(Throwable throwable) {
        super(throwable);
    }

    protected ServerBrokenException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }

    public ServerBrokenException(String server_is_sending_unknown_packets) {
    }
}
